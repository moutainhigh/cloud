package com.smart4y.cloud.base.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统权限-功能操作关联表
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "BaseAuthorityActionVO", description = "系统权限（功能操作关联表）")
public class BaseAuthorityActionVO implements Serializable {

    /**
     * 操作ID
     */
    @ApiModelProperty(value = "操作ID")
    private Long actionId;

    /**
     * API
     */
    @ApiModelProperty(value = "API")
    private Long authorityId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间（格式：yyyy-MM-dd HH:mm:ss）")
    private String createdDate;

    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间（格式：yyyy-MM-dd HH:mm:ss）")
    private String lastModifiedDate;
}