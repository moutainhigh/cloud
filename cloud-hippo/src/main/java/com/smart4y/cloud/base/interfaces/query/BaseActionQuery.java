package com.smart4y.cloud.base.interfaces.query;

import com.smart4y.cloud.core.domain.page.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 功能按钮分页查询
 *
 * @author Youtao
 *         Created by youtao on 2019/10/14.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BaseActionQuery", description = "功能按钮分页查询")
public class BaseActionQuery extends BaseQuery {

    @ApiModelProperty(value = "资源编码")
    private String actionCode;

    @ApiModelProperty(value = "资源名称")
    private String actionName;
}