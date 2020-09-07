package com.smart4y.cloud.access.interfaces.dtos.user;

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
@ApiModel(value = "ModifyUserCommand", description = "用户:修改")
public class ModifyUserCommand implements Serializable {
}