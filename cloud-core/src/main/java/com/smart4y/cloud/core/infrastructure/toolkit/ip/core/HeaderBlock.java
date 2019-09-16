package com.smart4y.cloud.core.infrastructure.toolkit.ip.core;

/**
 * header block class
 */
public class HeaderBlock {
    /**
     * index block start ip address
     */
    private long indexStartIp;

    /**
     * ip address
     */
    private int indexPtr;

    public HeaderBlock(long indexStartIp, int indexPtr) {
        this.indexStartIp = indexStartIp;
        this.indexPtr = indexPtr;
    }

    public long getIndexStartIp() {
        return indexStartIp;
    }

    public HeaderBlock setIndexStartIp(long indexStartIp) {
        this.indexStartIp = indexStartIp;
        return this;
    }

    public int getIndexPtr() {
        return indexPtr;
    }

    public HeaderBlock setIndexPtr(int indexPtr) {
        this.indexPtr = indexPtr;
        return this;
    }

    /**
     * +------------+-----------+
     * | 4bytes        | 4bytes    |
     * +------------+-----------+
     * start ip      index ptr
     * get the bytes for db storage
     *
     * @return byte[]
     */
    public byte[] getBytes() {
        byte[] b = new byte[8];
        Util.writeIntLong(b, 0, indexStartIp);
        Util.writeIntLong(b, 4, indexPtr);
        return b;
    }
}