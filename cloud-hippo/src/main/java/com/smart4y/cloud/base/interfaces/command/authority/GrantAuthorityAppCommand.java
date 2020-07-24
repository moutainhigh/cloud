package com.smart4y.cloud.base.interfaces.command.authority;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Youtao
 * on 2020/7/23 10:04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "GrantAuthorityAppCommand", description = "分配应用权限")
public class GrantAuthorityAppCommand implements Serializable {

    @NotBlank(message = "应用Id必填")
    @ApiModelProperty(value = "应用Id", required = true)
    private String appId;

    @ApiModelProperty(value = "过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    @ApiModelProperty("权限ID（多个以,隔开）")
    private String authorityIds;
}