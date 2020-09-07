package com.smart4y.cloud.access.interfaces.dtos.privilege;

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
@ApiModel(value = "RbacPrivilegePageQuery", description = "权限:分页:查询")
public class RbacPrivilegePageQuery extends BaseQuery {

    /**
     * 权限ID
     */
    @ApiModelProperty(value = "权限ID")
    private Long privilegeId;

    /**
     * 权限
     */
    @ApiModelProperty(value = "权限")
    private String privilege;

    /**
     * 权限类别
     */
    @ApiModelProperty(value = "权限类别")
    private String privilegeType;
}