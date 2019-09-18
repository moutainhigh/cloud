package com.smart4y.cloud.base.application;

import com.smart4y.cloud.core.application.dto.UserAccount;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/18.
 */
public interface BaseDeveloperService {

    UserAccount login(String username);
}