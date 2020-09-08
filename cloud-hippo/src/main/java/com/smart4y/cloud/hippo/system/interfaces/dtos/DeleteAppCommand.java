package com.smart4y.cloud.hippo.system.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/23 10:04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "DeleteAppCommand", description = "删除应用")
public class DeleteAppCommand implements Serializable {

    @NotBlank(message = "应用ID必填")
    @ApiModelProperty(value = "应用ID", required = true)
    private String appId;
}