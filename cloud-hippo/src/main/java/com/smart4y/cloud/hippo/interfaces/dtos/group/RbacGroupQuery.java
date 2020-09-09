package com.smart4y.cloud.hippo.interfaces.dtos.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/31 10:43
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "RbacGroupQuery", description = "组织:查询")
public class RbacGroupQuery implements Serializable {

    @NotNull(message = "组织ID 必填")
    @ApiModelProperty(value = "组织ID", required = true)
    private Long groupId;
}