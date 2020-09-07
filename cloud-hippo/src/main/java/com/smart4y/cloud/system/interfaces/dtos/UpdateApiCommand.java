package com.smart4y.cloud.system.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/23 10:04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "UpdateApiCommand", description = "更新Api")
public class UpdateApiCommand implements Serializable {

    @NotNull(message = "接口ID必填")
    @ApiModelProperty(value = "接口ID", required = true)
    private Long apiId;

    @NotBlank(message = "接口编码必填")
    @ApiModelProperty(value = "接口编码编码", required = true)
    private String apiCode;

    @NotBlank(message = "接口名称必填")
    @ApiModelProperty(value = "接口名称", required = true)
    private String apiName;

    @NotBlank(message = "接口分类必填")
    @ApiModelProperty(value = "接口分类", required = true)
    private String apiCategory;

    @NotBlank(message = "服务ID必填")
    @ApiModelProperty(value = "服务ID", required = true)
    private String serviceId;

    @ApiModelProperty(value = "请求路径")
    private String path;

    @ApiModelProperty(value = "是否启用")
    private Integer status = 1;

    @ApiModelProperty("排序（优先级越小越靠前）")
    private Integer isAuth = 1;

    @ApiModelProperty("排序（优先级越小越靠前）")
    private Integer isOpen = 0;

    @ApiModelProperty("排序（优先级越小越靠前）")
    private Integer priority = 0;

    @ApiModelProperty("描述")
    private String apiDesc;
}