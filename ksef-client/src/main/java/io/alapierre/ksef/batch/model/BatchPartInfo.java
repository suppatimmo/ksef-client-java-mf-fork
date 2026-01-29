package io.alapierre.ksef.batch.model;

import java.nio.file.Path;
import java.util.Objects;

/**
 * @author Adrian Lapierre {@literal al@alapierre.io}
 * Copyrights by original author 26.11.2025
 */
public class BatchPartInfo {
    private final int index;
    private final Path cipherPath;
    private final long cipherSize;
    private final String cipherHash;

    public BatchPartInfo(int index, Path cipherPath, long cipherSize, String cipherHash) {
        this.index = index;
        this.cipherPath = cipherPath;
        this.cipherSize = cipherSize;
        this.cipherHash = cipherHash;
    }

    public int index() {
        return index;
    }

    public Path cipherPath() {
        return cipherPath;
    }

    public long cipherSize() {
        return cipherSize;
    }

    public String cipherHash() {
        return cipherHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatchPartInfo that = (BatchPartInfo) o;
        return index == that.index &&
                cipherSize == that.cipherSize &&
                Objects.equals(cipherPath, that.cipherPath) &&
                Objects.equals(cipherHash, that.cipherHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, cipherPath, cipherSize, cipherHash);
    }

    @Override
    public String toString() {
        return "BatchPartInfo[" +
                "index=" + index + ", " +
                "cipherPath=" + cipherPath + ", " +
                "cipherSize=" + cipherSize + ", " +
                "cipherHash=" + cipherHash + ']';
    }
}
