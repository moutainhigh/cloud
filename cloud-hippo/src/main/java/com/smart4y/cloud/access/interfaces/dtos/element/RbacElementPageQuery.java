package com.smart4y.cloud.access.interfaces.dtos.element;

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
@ApiModel(value = "RbacElementPageQuery", description = "元素:分页:查询")
public class RbacElementPageQuery extends BaseQuery {

    /**
     * 页面元素名
     */
    @ApiModelProperty(value = "元素名")
    private String elementName;

    /**
     * 页面元素编码
     */
    @ApiModelProperty(value = "元素编码")
    private String elementCode;
}