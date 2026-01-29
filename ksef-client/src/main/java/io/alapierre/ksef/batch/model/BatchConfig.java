package io.alapierre.ksef.batch.model;

import java.nio.file.Path;
import java.util.Objects;

/**
 * @author Adrian Lapierre {@literal al@alapierre.io}
 * Copyrights by original author 26.11.2025
 */
public class BatchConfig {
    private final Path outputDir;
    private final int maxPartSize;
    private final boolean cleanup;

    public BatchConfig(Path outputDir, int maxPartSize, boolean cleanup) {
        this.outputDir = outputDir;
        this.maxPartSize = maxPartSize;
        this.cleanup = cleanup;
    }

    public Path outputDir() {
        return outputDir;
    }

    public int maxPartSize() {
        return maxPartSize;
    }

    public boolean cleanup() {
        return cleanup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatchConfig that = (BatchConfig) o;
        return maxPartSize == that.maxPartSize &&
                cleanup == that.cleanup &&
                Objects.equals(outputDir, that.outputDir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outputDir, maxPartSize, cleanup);
    }

    @Override
    public String toString() {
        return "BatchConfig[" +
                "outputDir=" + outputDir + ", " +
                "maxPartSize=" + maxPartSize + ", " +
                "cleanup=" + cleanup + ']';
    }
}
