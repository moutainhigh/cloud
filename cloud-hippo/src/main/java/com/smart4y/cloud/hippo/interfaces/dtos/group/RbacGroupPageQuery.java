package com.smart4y.cloud.hippo.interfaces.dtos.group;

import com.smart4y.cloud.core.message.page.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author Youtao
 * on 2020/7/31 10:43
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "RbacGroupPageQuery", description = "组织:分页:查询")
public class RbacGroupPageQuery extends BaseQuery {

    @NotNull(message = "组织父级ID 必填")
    @ApiModelProperty(value = "组织父级ID", required = true)
    private Long groupParentId;

    @ApiModelProperty(value = "组织类型", allowableValues = "g,c,d,t,p")
    private String groupType;

    @ApiModelProperty(value = "组织状态", allowableValues = "10,20,30")
    private String groupState;
}