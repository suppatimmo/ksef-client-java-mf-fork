package io.alapierre.ksef.batch.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Adrian Lapierre {@literal al@alapierre.io}
 * Copyrights by original author 26.11.2025
 */
public class InvoiceItem {
    private final String id;
    private final String fileName;
    private final byte[] content;
    private final String hash;

    public InvoiceItem(String id, String fileName, byte[] content, String hash) {
        this.id = id;
        this.fileName = fileName;
        this.content = content;
        this.hash = hash;
    }

    public String id() {
        return id;
    }

    public String fileName() {
        return fileName;
    }

    public byte[] content() {
        return content;
    }

    public String hash() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceItem that = (InvoiceItem) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(fileName, that.fileName) &&
                Arrays.equals(content, that.content) &&
                Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, fileName, hash);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }

    @Override
    public String toString() {
        return "InvoiceItem[" +
                "id=" + id + ", " +
                "fileName=" + fileName + ", " +
                "content=" + Arrays.toString(content) + ", " +
                "hash=" + hash + ']';
    }
}
