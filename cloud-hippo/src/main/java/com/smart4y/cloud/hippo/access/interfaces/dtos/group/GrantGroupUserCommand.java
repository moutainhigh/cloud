package com.smart4y.cloud.hippo.access.interfaces.dtos.group;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/7/31 10:44
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "GrantGroupUserCommand", description = "组织:用户:分配")
public class GrantGroupUserCommand implements Serializable {
}