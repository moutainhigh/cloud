package com.smart4y.cloud.core.infrastructure.toolkit.ip.core;

/**
 * database configuration class
 */
public final class DbConfig {

    /**
     * 8 * 2048
     */
    private static final int DEFAULT_TOTAL_HEADER_SIZE = 16384;
    /**
     * 4 * 2048
     */
    private static final int DEFAULT_INDEX_BLOCK_SIZE = 8192;

    /**
     * total header data block size
     */
    private int totalHeaderSize;

    /**
     * max index data block size
     * u should always choice the fastest read block size
     */
    private int indexBlockSize;

    /**
     * construct method
     */
    private DbConfig(int totalHeaderSize) {
        if ((totalHeaderSize % 8) != 0) {
            throw new IllegalArgumentException("totalHeaderSize must be times of 8");
        }
        this.totalHeaderSize = totalHeaderSize;
        this.indexBlockSize = DEFAULT_INDEX_BLOCK_SIZE;
    }

    public DbConfig() {
        this(DEFAULT_TOTAL_HEADER_SIZE);
    }

    int getTotalHeaderSize() {
        return totalHeaderSize;
    }

    public DbConfig setTotalHeaderSize(int totalHeaderSize) {
        this.totalHeaderSize = totalHeaderSize;
        return this;
    }

    public int getIndexBlockSize() {
        return indexBlockSize;
    }

    public DbConfig setIndexBlockSize(int dataBlockSize) {
        this.indexBlockSize = dataBlockSize;
        return this;
    }
}