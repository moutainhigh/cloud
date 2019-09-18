package com.smart4y.cloud.base.application.impl;

import com.smart4y.cloud.base.application.AccountService;
import com.smart4y.cloud.base.domain.model.BaseAccount;
import com.smart4y.cloud.base.domain.service.BaseAccountDomainService;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/18.
 */
@Slf4j
@ApplicationService
public class AccountServiceImpl implements AccountService {

    private final BaseAccountDomainService baseAccountDomainService;

    @Autowired
    public AccountServiceImpl(BaseAccountDomainService baseAccountDomainService) {
        this.baseAccountDomainService = baseAccountDomainService;
    }

    @Override
    public BaseAccount getAccount(String account, String accountType, String domain) {
        return baseAccountDomainService.selectAccount(account, accountType, domain);
    }

    @Override
    public void loginLog(BaseAccount baseAccount) {
        baseAccountDomainService.addLoginLog(baseAccount);
    }
}