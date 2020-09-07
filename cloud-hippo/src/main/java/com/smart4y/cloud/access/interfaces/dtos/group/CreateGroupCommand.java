package com.smart4y.cloud.access.interfaces.dtos.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/31 10:44
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CreateGroupCommand", description = "组织:添加")
public class CreateGroupCommand implements Serializable {

    @NotNull(message = "父级ID 必填")
    @ApiModelProperty(value = "父级ID", required = true)
    private Long groupParentId;

    @NotBlank(message = "组织名称 必填")
    @ApiModelProperty(value = "组织名称", required = true)
    private String groupName;

    @NotBlank(message = "组织状态 必填")
    @ApiModelProperty(value = "组织状态（10-启用，20-禁用，30-锁定）", allowableValues = "10,20,30", required = true)
    private String groupState;

    @NotBlank(message = "组织类型 必填")
    @ApiModelProperty(value = "组织类型（g-集团，c-公司，d-部门，t-小组，p-岗位，u-人员）", allowableValues = "10,20,30", required = true)
    private String groupType;
}