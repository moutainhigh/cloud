package com.smart4y.cloud.core.message.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 简单分页模型
 *
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
@ApiModel(value = "Page", description = "分页模型")
public class Page<T> implements Serializable {

    @ApiModelProperty(value = "当前页（默认1）")
    private long current = 1;

    @ApiModelProperty(value = "每页条数（默认10）")
    private long size = 10;

    @ApiModelProperty(value = "总数")
    private long total = 0;

    @ApiModelProperty(value = "数据列表")
    private List<T> records = Collections.emptyList();

    public Page() {
    }

    public Page(long current, long size) {
        this(current, size, 0);
    }

    public Page(long current, long size, long total) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
    }

    /**
     * 是否存在上一页
     */
    public boolean hasPrevious() {
        return this.current > 1;
    }

    /**
     * 是否存在下一页
     */
    public boolean hasNext() {
        return this.current < this.getPages();
    }

    /**
     * 计算当前分页偏移量
     */
    private long offset() {
        return getCurrent() > 0 ? (getCurrent() - 1) * getSize() : 0;
    }

    /**
     * 当前分页总页数
     */
    private long getPages() {
        if (getSize() == 0) {
            return 0L;
        }
        long pages = getTotal() / getSize();
        if (getTotal() % getSize() != 0) {
            pages++;
        }
        return pages;
    }

    public List<T> getRecords() {
        return this.records;
    }

    public Page<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public long getTotal() {
        return this.total;
    }

    public Page<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    public long getSize() {
        return this.size;
    }

    public Page<T> setSize(long size) {
        this.size = size;
        return this;
    }

    public long getCurrent() {
        return this.current;
    }

    public Page<T> setCurrent(long current) {
        this.current = current;
        return this;
    }
}