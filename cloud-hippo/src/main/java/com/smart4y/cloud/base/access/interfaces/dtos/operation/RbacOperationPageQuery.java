package com.smart4y.cloud.base.access.interfaces.dtos.operation;

import com.smart4y.cloud.core.message.page.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Youtao
 * on 2020/7/31 10:43
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "RbacOperationPageQuery", description = "操作:分页:查询")
public class RbacOperationPageQuery extends BaseQuery {

    /**
     * 操作名
     */
    @ApiModelProperty(value = "操作名")
    private String operationName;

    /**
     * 操作描述
     */
    @ApiModelProperty(value = "操作描述")
    private String operationDesc;

    /**
     * 操作URL
     */
    @ApiModelProperty(value = "操作URL")
    private String operationPath;
}