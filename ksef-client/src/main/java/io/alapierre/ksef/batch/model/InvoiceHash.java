package io.alapierre.ksef.batch.model;

import java.util.Objects;

/**
 * @author Adrian Lapierre {@literal al@alapierre.io}
 * Copyrights by original author 26.11.2025
 */
public class InvoiceHash {
    private final String fileName;
    private final String hash;

    public InvoiceHash(String fileName, String hash) {
        this.fileName = fileName;
        this.hash = hash;
    }

    public String fileName() {
        return fileName;
    }

    public String hash() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceHash that = (InvoiceHash) o;
        return Objects.equals(fileName, that.fileName) &&
                Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, hash);
    }

    @Override
    public String toString() {
        return "InvoiceHash[" +
                "fileName=" + fileName + ", " +
                "hash=" + hash + ']';
    }
}
