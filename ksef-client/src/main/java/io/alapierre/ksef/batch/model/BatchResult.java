package io.alapierre.ksef.batch.model;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 * @author Adrian Lapierre {@literal al@alapierre.io}
 * Copyrights by original author 26.11.2025
 */
public class BatchResult {
    private final Path zipPath;
    private final long zipSize;
    private final String zipHash;
    private final List<BatchPartInfo> parts;
    private final List<InvoiceHash> invoiceHashes;
    private final byte[] iv;
    private final String encryptedCipherKey;

    public BatchResult(Path zipPath, long zipSize, String zipHash, 
                      List<BatchPartInfo> parts, List<InvoiceHash> invoiceHashes,
                      byte[] iv, String encryptedCipherKey) {
        this.zipPath = zipPath;
        this.zipSize = zipSize;
        this.zipHash = zipHash;
        this.parts = parts;
        this.invoiceHashes = invoiceHashes;
        this.iv = iv;
        this.encryptedCipherKey = encryptedCipherKey;
    }

    public Path zipPath() {
        return zipPath;
    }

    public long zipSize() {
        return zipSize;
    }

    public String zipHash() {
        return zipHash;
    }

    public List<BatchPartInfo> parts() {
        return parts;
    }

    public List<InvoiceHash> invoiceHashes() {
        return invoiceHashes;
    }

    public byte[] iv() {
        return iv;
    }

    public String encryptedCipherKey() {
        return encryptedCipherKey;
    }

    public String encodedIv() {
        return Base64.getEncoder().encodeToString(iv);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatchResult that = (BatchResult) o;
        return zipSize == that.zipSize &&
                Objects.equals(zipPath, that.zipPath) &&
                Objects.equals(zipHash, that.zipHash) &&
                Objects.equals(parts, that.parts) &&
                Objects.equals(invoiceHashes, that.invoiceHashes) &&
                Arrays.equals(iv, that.iv) &&
                Objects.equals(encryptedCipherKey, that.encryptedCipherKey);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(zipPath, zipSize, zipHash, parts, invoiceHashes, encryptedCipherKey);
        result = 31 * result + Arrays.hashCode(iv);
        return result;
    }

    @Override
    public String toString() {
        return "BatchResult[" +
                "zipPath=" + zipPath + ", " +
                "zipSize=" + zipSize + ", " +
                "zipHash=" + zipHash + ", " +
                "parts=" + parts + ", " +
                "invoiceHashes=" + invoiceHashes + ", " +
                "iv=" + Arrays.toString(iv) + ", " +
                "encryptedCipherKey=" + encryptedCipherKey + ']';
    }
}
