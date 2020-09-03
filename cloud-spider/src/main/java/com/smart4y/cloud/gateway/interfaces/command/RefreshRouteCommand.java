package com.smart4y.cloud.gateway.interfaces.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 刷新路由
 *
 * @author Youtao
 * on 2020/7/23 14:42
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "RefreshRouteCommand", description = "刷新路由（支持灰度发布，示例：/actuator/open/refresh?destination = customers：**）")
public class RefreshRouteCommand implements Serializable {

    @ApiModelProperty(value = "目标路由")
    private String destination;
}