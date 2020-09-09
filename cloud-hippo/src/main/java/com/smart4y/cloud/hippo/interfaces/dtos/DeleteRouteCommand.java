package com.smart4y.cloud.hippo.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/23 10:04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "DeleteRouteCommand", description = "删除路由")
public class DeleteRouteCommand implements Serializable {

    @NotNull(message = "路由ID必填")
    @ApiModelProperty(value = "路由ID", required = true)
    private Long routeId;
}