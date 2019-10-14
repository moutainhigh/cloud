package com.smart4y.cloud.base.interfaces.valueobject.query;

import com.smart4y.cloud.core.domain.page.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 应用分页查询
 *
 * @author Youtao
 *         Created by youtao on 2019/10/14.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BaseAppQuery", description = "应用分页查询")
public class BaseAppQuery extends BaseQuery {

    /**
     * 客户端ID
     */
    @ApiModelProperty(value = "客户端ID")
    private String aid;

    /**
     * app名称
     */
    @ApiModelProperty(value = "app名称")
    private String appName;

    /**
     * app英文名称
     */
    @ApiModelProperty(value = "app英文名称")
    private String appNameEn;

    /**
     * app类型（server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用）
     */
    @ApiModelProperty(value = "APP类型（server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用）")
    private String appType;

    /**
     * 开发者ID（默认为0）
     */
    @ApiModelProperty(value = "开发者ID（默认为0）")
    private Long developerId;
}