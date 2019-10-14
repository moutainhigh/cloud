package com.smart4y.cloud.base.interfaces.valueobject.query;

import com.smart4y.cloud.core.domain.page.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 网关流量访问控制分页查询
 *
 * @author Youtao
 *         Created by youtao on 2019/10/14.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "RateLimitQuery", description = "网关流量访问控制分页查询")
public class RateLimitQuery extends BaseQuery {

    @ApiModelProperty(value = "策略名称")
    private String policyName;

    @ApiModelProperty(value = "限流规则类型（url, origin, user）")
    private Integer policyType;
}