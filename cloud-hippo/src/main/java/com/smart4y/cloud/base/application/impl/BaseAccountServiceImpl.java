package com.smart4y.cloud.base.application.impl;

import com.smart4y.cloud.base.application.BaseAccountService;
import com.smart4y.cloud.base.domain.model.BaseAccount;
import com.smart4y.cloud.base.domain.model.BaseAccountLogs;
import com.smart4y.cloud.base.domain.repository.BaseAccountLogsMapper;
import com.smart4y.cloud.base.domain.repository.BaseAccountMapper;
import com.smart4y.cloud.core.application.ApplicationService;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.exception.OpenAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import tk.mybatis.mapper.weekend.Weekend;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 通用账号
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
@ApplicationService
public class BaseAccountServiceImpl implements BaseAccountService {

    @Autowired
    private BaseAccountMapper baseAccountMapper;
    @Autowired
    private BaseAccountLogsMapper baseAccountLogsMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public BaseAccount getAccountById(long accountId) {
        return baseAccountMapper.selectByPrimaryKey(accountId);
    }

    @Override
    public BaseAccount getAccount(String account, String accountType, String domain) {
        Weekend<BaseAccount> queryWrapper = Weekend.of(BaseAccount.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseAccount::getAccount, account)
                .andEqualTo(BaseAccount::getAccountType, accountType)
                .andEqualTo(BaseAccount::getDomain, domain);
        return baseAccountMapper.selectOneByExample(queryWrapper);
    }

    @Override
    public BaseAccount register(Long userId, String account, String password, String accountType, Integer status, String domain, String registerIp) {
        if (isExist(account, accountType, domain)) {
            // 账号已被注册
            throw new OpenAlertException(String.format("account=[%s],domain=[%s]", account, domain));
        }
        //加密
        String encodePassword = passwordEncoder.encode(password);
        BaseAccount baseAccount = new BaseAccount()
                .setUserId(userId)
                .setAccount(account)
                .setPassword(encodePassword)
                .setAccountType(accountType)
                .setDomain(domain)
                .setRegisterIp(registerIp)
                .setCreatedDate(LocalDateTime.now())
                .setLastModifiedDate(LocalDateTime.now())
                .setStatus(status);
        baseAccountMapper.insert(baseAccount);
        return baseAccount;
    }

    @Override
    public boolean isExist(String account, String accountType, String domain) {
        Weekend<BaseAccount> queryWrapper = Weekend.of(BaseAccount.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseAccount::getAccount, account)
                .andEqualTo(BaseAccount::getAccountType, accountType)
                .andEqualTo(BaseAccount::getDomain, domain);
        int count = baseAccountMapper.selectCountByExample(queryWrapper);
        return count > 0;
    }

    @Override
    public int removeAccount(long accountId) {
        return baseAccountMapper.deleteByPrimaryKey(accountId);
    }

    @Override
    public int updateStatus(long accountId, int status) {
        BaseAccount baseAccount = new BaseAccount();
        baseAccount.setAccountId(accountId);
        baseAccount.setLastModifiedDate(LocalDateTime.now());
        baseAccount.setStatus(status);
        return baseAccountMapper.updateByPrimaryKeySelective(baseAccount);
    }

    @Override
    public int updateStatusByUserId(long userId, String domain, int status) {
        BaseAccount baseAccount = new BaseAccount();
        baseAccount.setLastModifiedDate(LocalDateTime.now());
        baseAccount.setStatus(status);
        Weekend<BaseAccount> wrapper = Weekend.of(BaseAccount.class);
        wrapper.weekendCriteria()
                .andEqualTo(BaseAccount::getDomain, domain)
                .andEqualTo(BaseAccount::getUserId, userId);
        return baseAccountMapper.updateByExampleSelective(baseAccount, wrapper);
    }

    @Override
    public int updatePasswordByUserId(Long userId, String domain, String password) {
        BaseAccount baseAccount = new BaseAccount();
        baseAccount.setLastModifiedDate(LocalDateTime.now());
        baseAccount.setPassword(passwordEncoder.encode(password));
        Weekend<BaseAccount> wrapper = Weekend.of(BaseAccount.class);
        wrapper.weekendCriteria()
                .andIn(BaseAccount::getAccountType, Arrays.asList(BaseConstants.ACCOUNT_TYPE_USERNAME, BaseConstants.ACCOUNT_TYPE_EMAIL, BaseConstants.ACCOUNT_TYPE_MOBILE))
                .andEqualTo(BaseAccount::getUserId, userId)
                .andEqualTo(BaseAccount::getDomain, domain);
        return baseAccountMapper.updateByExampleSelective(baseAccount, wrapper);
    }

    @Override
    public int removeAccountByUserId(Long userId, String domain) {
        Weekend<BaseAccount> wrapper = Weekend.of(BaseAccount.class);
        wrapper.weekendCriteria()
                .andEqualTo(BaseAccount::getUserId, userId)
                .andEqualTo(BaseAccount::getDomain, domain);
        return baseAccountMapper.deleteByExample(wrapper);
    }

    @Override
    public void addLoginLog(BaseAccountLogs log) {
        Weekend<BaseAccountLogs> queryWrapper = Weekend.of(BaseAccountLogs.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseAccountLogs::getAccountId, log.getDomain())
                .andEqualTo(BaseAccountLogs::getUserId, log.getUserId());
        int count = baseAccountLogsMapper.selectCountByExample(queryWrapper);
        log.setLoginTime(LocalDateTime.now());
        log.setLoginNums(count + 1);
        baseAccountLogsMapper.insert(log);
    }
}