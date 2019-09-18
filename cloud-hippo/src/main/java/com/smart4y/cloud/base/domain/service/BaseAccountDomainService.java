package com.smart4y.cloud.base.domain.service;

import com.smart4y.cloud.base.domain.model.BaseAccount;
import com.smart4y.cloud.base.domain.model.BaseAccountLogs;
import com.smart4y.cloud.base.domain.repository.BaseAccountLogsMapper;
import com.smart4y.cloud.core.domain.annotation.DomainService;
import com.smart4y.cloud.core.infrastructure.mapper.BaseDomainService;
import com.smart4y.cloud.core.infrastructure.toolkit.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import tk.mybatis.mapper.weekend.Weekend;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/18.
 */
@Slf4j
@DomainService
public class BaseAccountDomainService extends BaseDomainService<BaseAccount> {

    private final BaseAccountLogsMapper baseAccountLogsMapper;

    @Autowired
    public BaseAccountDomainService(BaseAccountLogsMapper baseAccountLogsMapper) {
        this.baseAccountLogsMapper = baseAccountLogsMapper;
    }

    /**
     * 查询 账号信息
     *
     * @param account     登录账号
     * @param accountType 账号类型
     * @param domain      所属域
     * @return 账号信息
     */
    public BaseAccount selectAccount(String account, String accountType, String domain) {
        Weekend<BaseAccount> weekend = Weekend.of(BaseAccount.class);
        weekend
                .weekendCriteria()
                .andEqualTo(BaseAccount::getAccount, account)
                .andEqualTo(BaseAccount::getAccountType, accountType)
                .andEqualTo(BaseAccount::getDomain, domain);
        return super.getOne(weekend);
    }

    /**
     * 添加登录成功日志
     */
    public void addLoginLog(BaseAccount baseAccount) {
        int count = getLoginCount(baseAccount.getAccountId(), baseAccount.getUserId());
        try {
            HttpServletRequest request = WebUtils.getHttpServletRequest();
            if (request != null) {
                String remoteAddress = WebUtils.getRemoteAddress(request);
                String header = request.getHeader(HttpHeaders.USER_AGENT);
                BaseAccountLogs log = new BaseAccountLogs()
                        .setDomain(baseAccount.getDomain())
                        .setUserId(baseAccount.getUserId())
                        .setAccount(baseAccount.getAccount())
                        .setAccountId(baseAccount.getAccountId())
                        .setAccountType(baseAccount.getAccountType())
                        .setLoginIp(remoteAddress)
                        .setLoginAgent(header)
                        .setLoginTime(LocalDateTime.now())
                        .setLoginNums(count + 1)
                        .setCreatedDate(LocalDateTime.now());
                baseAccountLogsMapper.insertSelective(log);
            }
        } catch (Exception e) {
            log.error("添加登录日志失败：{}", e.getLocalizedMessage(), e);
        }
    }

    /**
     * 账号登录次数
     */
    private int getLoginCount(long accountId, long userId) {
        Weekend<BaseAccountLogs> weekend = Weekend.of(BaseAccountLogs.class);
        weekend
                .weekendCriteria()
                .andEqualTo(BaseAccountLogs::getAccountId, accountId)
                .andEqualTo(BaseAccountLogs::getUserId, userId);
        return baseAccountLogsMapper.selectCountByExample(weekend);
    }
}