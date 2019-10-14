package com.smart4y.cloud.base.interfaces.valueobject.query;

import com.smart4y.cloud.core.domain.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 网关IP访问控制分页查询
 *
 * @author Youtao
 *         Created by youtao on 2019/10/14.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "IpLimitQuery", description = "网关IP访问控制分页查询")
public class IpLimitQuery extends BaseQuery {

    @ApiModelProperty(value = "策略名称")
    private String policyName;

    @ApiModelProperty(value = "策略类型:0-拒绝/黑名单 1-允许/白名单")
    private Integer policyType;
}