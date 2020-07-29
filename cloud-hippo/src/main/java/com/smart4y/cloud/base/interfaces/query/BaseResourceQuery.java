package com.smart4y.cloud.base.interfaces.query;

import com.smart4y.cloud.core.message.page.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 接口分页查询
 *
 * @author Youtao
 *         Created by youtao on 2019/10/14.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BaseApiQuery", description = "接口分页查询")
public class BaseResourceQuery extends BaseQuery {

    @ApiModelProperty(value = "接口编码")
    private String apiCode;

    @ApiModelProperty(value = "接口名称")
    private String apiName;

    @ApiModelProperty(value = "服务ID")
    private String serviceId;

    @ApiModelProperty(value = "请求路径")
    private String path;

    @ApiModelProperty(value = "状态（0-无效 1-有效）")
    private Integer status;

    @ApiModelProperty(value = "是否需要认证（0-无认证 1-身份认证 默认:1）")
    private Integer isAuth;
}