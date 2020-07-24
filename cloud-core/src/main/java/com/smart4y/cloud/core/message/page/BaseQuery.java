package com.smart4y.cloud.core.message.page;

import com.smart4y.cloud.core.constant.CommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Youtao
 *         Created by youtao on 2019/10/14.
 */
@ApiModel(value = "BaseQuery", description = "分页基础查询条件")
public class BaseQuery implements Serializable {

    @ApiModelProperty(value = "页码")
    private Integer page = CommonConstants.DEFAULT_PAGE;

    @ApiModelProperty(value = "每页显示条数")
    private Integer limit = CommonConstants.DEFAULT_LIMIT;

    public Integer getPage() {
        if (page <= CommonConstants.MIN_PAGE) {
            page = 1;
        }
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        if (limit > CommonConstants.MAX_LIMIT) {
            limit = CommonConstants.MAX_LIMIT;
        }
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}