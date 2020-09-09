package com.smart4y.cloud.hippo.interfaces.dtos;

import com.smart4y.cloud.core.message.page.BaseQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 网关路由分页查询
 *
 * @author Youtao
 *         Created by youtao on 2019/10/14.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "GatewayRouteQuery", description = "网关路由分页查询")
public class GatewayRouteQuery extends BaseQuery {
}