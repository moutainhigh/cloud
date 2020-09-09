package com.smart4y.cloud.hippo.interfaces.dtos.route;

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
@ApiModel(value = "RoutePageQuery", description = "路由:分页:查询")
public class RoutePageQuery extends BaseQuery {

    /**
     * 路由名称
     */
    @ApiModelProperty(value = "路由名称")
    private String routeName;

    /**
     * 路由前缀
     */
    @ApiModelProperty(value = "路由前缀")
    private String routePath;
}