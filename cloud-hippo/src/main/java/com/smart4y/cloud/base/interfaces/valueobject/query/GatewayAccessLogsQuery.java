package com.smart4y.cloud.base.interfaces.valueobject.query;

import com.smart4y.cloud.core.domain.page.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 网关日志分页查询
 *
 * @author Youtao
 *         Created by youtao on 2019/10/14.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "GatewayAccessLogsQuery", description = "网关日志分页查询")
public class GatewayAccessLogsQuery extends BaseQuery {

    @ApiModelProperty(value = "访问路径")
    private String path;

    @ApiModelProperty(value = "请求IP")
    private String ip;

    @ApiModelProperty(value = "服务名")
    private String serviceId;
}