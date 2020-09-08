package com.smart4y.cloud.hippo.access.interfaces.dtos.log;

import com.smart4y.cloud.core.message.page.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Youtao on 2019/10/14.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "LogPageQuery", description = "日志:分页:查询")
public class LogPageQuery extends BaseQuery {

    @ApiModelProperty(value = "访问路径")
    private String logPath;

    @ApiModelProperty(value = "请求IP")
    private String logIp;

    @ApiModelProperty(value = "服务名")
    private String logServiceId;
}