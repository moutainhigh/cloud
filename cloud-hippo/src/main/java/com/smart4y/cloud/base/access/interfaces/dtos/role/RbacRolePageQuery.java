package com.smart4y.cloud.base.access.interfaces.dtos.role;

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
@ApiModel(value = "RbacRolePageQuery", description = "角色:分页:查询")
public class RbacRolePageQuery extends BaseQuery {

    @ApiModelProperty(value = "角色名")
    private String roleName;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;
}