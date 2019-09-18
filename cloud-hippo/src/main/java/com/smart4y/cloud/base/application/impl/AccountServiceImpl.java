package com.smart4y.cloud.base.application.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.smart4y.cloud.base.application.AccountService;
import com.smart4y.cloud.base.domain.model.BaseAccount;
import com.smart4y.cloud.base.domain.model.BaseRole;
import com.smart4y.cloud.base.domain.model.BaseUser;
import com.smart4y.cloud.base.domain.service.BaseAccountDomainService;
import com.smart4y.cloud.base.domain.service.BaseAuthorityDomainService;
import com.smart4y.cloud.base.domain.service.BaseRoleDomainService;
import com.smart4y.cloud.base.domain.service.BaseUserDomainService;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import com.smart4y.cloud.core.application.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.application.dto.UserAccount;
import com.smart4y.cloud.core.domain.model.OpenAuthority;
import com.smart4y.cloud.core.infrastructure.constants.CommonConstants;
import com.smart4y.cloud.core.infrastructure.security.OpenSecurityConstants;
import com.smart4y.cloud.core.infrastructure.toolkit.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.smart4y.cloud.core.infrastructure.constants.BaseConstants.*;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/18.
 */
@Slf4j
@ApplicationService
public class AccountServiceImpl implements AccountService {

    private final BaseAccountDomainService baseAccountDomainService;
    private final BaseUserDomainService baseUserDomainService;
    private final BaseRoleDomainService baseRoleDomainService;
    private final BaseAuthorityDomainService baseAuthorityDomainService;

    @Autowired
    public AccountServiceImpl(BaseUserDomainService baseUserDomainService, BaseRoleDomainService baseRoleDomainService, BaseAuthorityDomainService baseAuthorityDomainService, BaseAccountDomainService baseAccountDomainService) {
        this.baseUserDomainService = baseUserDomainService;
        this.baseRoleDomainService = baseRoleDomainService;
        this.baseAuthorityDomainService = baseAuthorityDomainService;
        this.baseAccountDomainService = baseAccountDomainService;
    }

    /**
     * 查询 账号信息
     *
     * @param account     登录账号
     * @param accountType 账号类型
     * @param domain      所属域
     * @return 账号信息
     */
    private BaseAccount getAccount(String account, String accountType, String domain) {
        return baseAccountDomainService.selectAccount(account, accountType, domain);
    }

    @Override
    public UserAccount login(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        BaseAccount baseAccount;

        // 第三方登录标识
        Map<String, String> parameterMap = WebUtils.getParameterMap(Objects.requireNonNull(WebUtils.getHttpServletRequest()));
        String loginType = parameterMap.get("login_type");
        if (StringUtils.isNotBlank(loginType)) {
            baseAccount = this.getAccount(account, loginType, ACCOUNT_DOMAIN_ADMIN);
        } else {
            // 非第三方登录

            // 用户名登录
            baseAccount = this.getAccount(account, ACCOUNT_TYPE_USERNAME, ACCOUNT_DOMAIN_ADMIN);
            // 手机号登录
            if (com.smart4y.cloud.core.infrastructure.toolkit.StringUtils.matchMobile(account)) {
                baseAccount = this.getAccount(account, ACCOUNT_TYPE_MOBILE, ACCOUNT_DOMAIN_ADMIN);
            }
            // 邮箱登录
            if (com.smart4y.cloud.core.infrastructure.toolkit.StringUtils.matchEmail(account)) {
                baseAccount = this.getAccount(account, ACCOUNT_TYPE_EMAIL, ACCOUNT_DOMAIN_ADMIN);
            }
        }
        UserAccount userAccount = null;
        // 获取用户详细信息
        if (null != baseAccount) {
            userAccount = getUserAccount(baseAccount.getUserId());

            BeanUtils.copyProperties(baseAccount, userAccount);
            // 添加登录日志
            baseAccountDomainService.addLoginLog(baseAccount);
        }
        return userAccount;
    }

    @Override
    public void modifyPassword(long userId, String password) {
        // TODO 修改密码
    }

    @Override
    public void modifyUser(BaseUser user) {
        // TODO 修改用户基本信息
    }

    @Override
    public List<AuthorityMenuDTO> findAuthorityMenuByUser(long userId, boolean isAdmin) {
        // TODO 获取 用户菜单权限
        return Collections.emptyList();
    }

    private UserAccount getUserAccount(long userId) {
        // admin 域用户需要查权限 平台域用户不用查
        UserAccount userAccount = new UserAccount();
        BaseUser baseUser = baseUserDomainService.getById(userId);
        if (null != baseUser) {
            // 昵称、头像
            userAccount
                    .setNickName(baseUser.getNickName())
                    .setAvatar(baseUser.getAvatar());

            // 用户权限列表
            List<OpenAuthority> authorities = Lists.newArrayList();

            // 用户角色列表
            List<Map<String, Object>> roles = Lists.newArrayList();
            List<BaseRole> userRoles = baseRoleDomainService.getUserRoles(userId);
            for (BaseRole role : userRoles) {
                Map<String, Object> roleMap = Maps.newHashMap();
                roleMap.put("roleId", role.getRoleId());
                roleMap.put("roleName", role.getRoleName());
                roleMap.put("roleCode", role.getRoleCode());
                roles.add(roleMap);

                // 角色标识加入权限集合
                OpenAuthority authority = new OpenAuthority(role.getRoleId().toString(),
                        OpenSecurityConstants.AUTHORITY_PREFIX_ROLE + role.getRoleCode(), null, "role");
                authorities.add(authority);
            }
            userAccount.setRoles(roles);

            // 加入用户权限
            boolean isAdmin = CommonConstants.ROOT.equals(baseUser.getUserName());
            List<OpenAuthority> userAuthorities = baseAuthorityDomainService.getUserAuthorities(userId, isAdmin);
            if (CollectionUtils.isNotEmpty(userAuthorities)) {
                authorities.addAll(userAuthorities);
            }
            userAccount.setAuthorities(authorities);
        }
        return userAccount;
    }
}