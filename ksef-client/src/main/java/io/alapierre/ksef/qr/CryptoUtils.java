package io.alapierre.ksef.qr;

import io.alapierre.ksef.qr.exceptions.VerificationLinkGenerationException;
import io.alapierre.ksef.qr.exceptions.VerificationLinkSiningException;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Arrays;
import java.util.Base64;


/**
 * @author Karol Bryzgiel {@literal kb@itrust.dev}
 * Copyrights by original author 25.09.2025
 */
@Slf4j
public final class CryptoUtils {

    private static final String SHA_256 = "SHA-256";
    private static final String SHA_256_WITH_RSA = "SHA256withRSA";
    private static final String SHA_256_WITH_ECDSA = "SHA256withECDSA";
    private static final String BC = "BC";

    /**
     * Computes SHA-256 over invoice XML and encodes it as Base64URL without padding.
     */
    public static String computeInvoiceHashBase64Url(byte[] invoiceXml) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            byte[] sha = digest.digest(invoiceXml);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(sha);
        } catch (NoSuchAlgorithmException e) {
            throw new VerificationLinkGenerationException("SHA-256 algorithm not available", e);
        }
    }

    public static String computeUrlEncodedSignedHash(String pathToSign, PrivateKey privateKey) {
        try {
            byte[] data = pathToSign.getBytes(StandardCharsets.UTF_8);

            Signature signature;
            if (privateKey instanceof RSAPrivateKey) {
                signature = Signature.getInstance("RSASSA-PSS");
                PSSParameterSpec pssSpec = new PSSParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 32, 1);
                signature.setParameter(pssSpec);
            } else if (privateKey instanceof ECPrivateKey) {
                signature = Signature.getInstance(SHA_256_WITH_ECDSA);
            } else {
                throw new VerificationLinkGenerationException("Certificate not support RSA or ECDsa.", null);
            }

            signature.initSign(privateKey);
            signature.update(data);
            byte[] signedBytes = signature.sign();
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signedBytes);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new VerificationLinkGenerationException("Cannot compute signature", e);
        }
    }

    public static String extractCertSerial(X509Certificate cert) {
        byte[] bytes = cert.getSerialNumber().toByteArray();

        if (bytes.length > 1 && bytes[0] == 0) {
            bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
        }

        return bytesToHex(bytes).toUpperCase();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static X509Certificate loadCertificate(InputStream source) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(source);
    }

    public static PrivateKey loadPrivateKey(InputStream input, char[] password) throws IOException {

        if (input == null) {
            throw new IllegalArgumentException("InputStream with key PEM must not be null");
        }

        try (Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
             PEMParser pemParser = new PEMParser(reader)) {

            Object obj = pemParser.readObject();
            if (obj == null) {
                throw new IllegalArgumentException("Empty PEM content");
            }

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BC);

            if (obj instanceof PKCS8EncryptedPrivateKeyInfo) {
                PKCS8EncryptedPrivateKeyInfo encPkcs8 = (PKCS8EncryptedPrivateKeyInfo) obj;
                requirePassword(password, "PKCS#8 encrypted private key");
                InputDecryptorProvider decryptor =
                        new JceOpenSSLPKCS8DecryptorProviderBuilder()
                                .setProvider(BC)
                                .build(password);
                PrivateKeyInfo pkInfo = encPkcs8.decryptPrivateKeyInfo(decryptor);
                return converter.getPrivateKey(pkInfo);
            } else if (obj instanceof PEMEncryptedKeyPair) {
                PEMEncryptedKeyPair encKeyPair = (PEMEncryptedKeyPair) obj;
                requirePassword(password, "PEM encrypted key pair");
                PEMDecryptorProvider decProv =
                        new JcePEMDecryptorProviderBuilder()
                                .setProvider(BC)
                                .build(password);
                PEMKeyPair keyPair = encKeyPair.decryptKeyPair(decProv);
                return converter.getKeyPair(keyPair).getPrivate();
            } else if (obj instanceof PEMKeyPair) {
                PEMKeyPair keyPair = (PEMKeyPair) obj;
                return converter.getKeyPair(keyPair).getPrivate();
            } else if (obj instanceof PrivateKeyInfo) {
                PrivateKeyInfo pkInfo = (PrivateKeyInfo) obj;
                return converter.getPrivateKey(pkInfo);
            } else {
                throw new IllegalArgumentException(
                        "Unsupported key format: " + obj.getClass().getName()
                );
            }
        } catch (OperatorCreationException | PKCSException e) {
            throw new VerificationLinkSiningException(e);
        }
    }

    private static void requirePassword(char[] password, String what) {
        if (password == null || password.length == 0) {
            throw new IllegalArgumentException(what + " requires non-empty password");
        }
    }

}
