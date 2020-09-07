package com.smart4y.cloud.system.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.smart4y.cloud.access.application.UserApplicationService;
import com.smart4y.cloud.access.domain.entity.RbacPrivilege;
import com.smart4y.cloud.access.domain.entity.RbacRole;
import com.smart4y.cloud.system.application.BaseAccountService;
import com.smart4y.cloud.system.application.BaseUserService;
import com.smart4y.cloud.system.domain.model.BaseAccount;
import com.smart4y.cloud.system.domain.model.BaseAccountLogs;
import com.smart4y.cloud.system.domain.model.BaseUser;
import com.smart4y.cloud.system.infrastructure.mapper.BaseUserMapper;
import com.smart4y.cloud.system.interfaces.dtos.AddUserCommand;
import com.smart4y.cloud.system.interfaces.dtos.AddUserThirdPartyCommand;
import com.smart4y.cloud.system.interfaces.dtos.BaseUserQuery;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.constant.BaseConstants;
import com.smart4y.cloud.core.dto.OpenAuthority;
import com.smart4y.cloud.core.dto.UserAccountVO;
import com.smart4y.cloud.core.exception.OpenAlertException;
import com.smart4y.cloud.core.toolkit.base.StringHelper;
import com.smart4y.cloud.core.toolkit.web.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
@ApplicationService
public class BaseUserServiceImpl implements BaseUserService {

    private final String ACCOUNT_DOMAIN = BaseConstants.ACCOUNT_DOMAIN_ADMIN;

    @Autowired
    private BaseUserMapper baseUserMapper;
    @Autowired
    private BaseAccountService baseAccountService;
    @Autowired
    private UserApplicationService userApplicationService;

    @Override
    public long addUser(AddUserCommand command) {
        if (getUserByUsername(command.getUserName()) != null) {
            throw new OpenAlertException("用户名:" + command.getUserName() + "已存在!");
        }
        // 保存系统用户信息
        BaseUser user = new BaseUser()
                .setUserName(command.getUserName())
                .setNickName(command.getNickName())
                .setUserType(command.getUserType())
                .setEmail(command.getEmail())
                .setMobile(command.getMobile())
                .setUserDesc(command.getUserDesc())
                .setAvatar(command.getAvatar())
                .setStatus(command.getStatus())
                .setCreatedDate(LocalDateTime.now())
                .setLastModifiedDate(LocalDateTime.now());
        baseUserMapper.insertSelective(user);
        long userId = user.getUserId();

        // 默认注册用户名账户
        baseAccountService.register(userId, command.getUserName(), command.getPassword(), BaseConstants.ACCOUNT_TYPE_USERNAME, command.getStatus(), ACCOUNT_DOMAIN, null);
        if (StringHelper.matchEmail(command.getEmail())) {
            // 注册email账号登陆
            baseAccountService.register(userId, command.getEmail(), command.getPassword(), BaseConstants.ACCOUNT_TYPE_EMAIL, command.getStatus(), ACCOUNT_DOMAIN, null);
        }
        if (StringHelper.matchMobile(command.getMobile())) {
            // 注册手机号账号登陆
            baseAccountService.register(userId, command.getMobile(), command.getPassword(), BaseConstants.ACCOUNT_TYPE_MOBILE, command.getStatus(), ACCOUNT_DOMAIN, null);
        }
        return userId;
    }

    @Override
    public void updateUser(BaseUser baseUser) {
        if (baseUser == null || baseUser.getUserId() == null) {
            return;
        }
        if (baseUser.getStatus() != null) {
            baseAccountService.updateStatusByUserId(baseUser.getUserId(), ACCOUNT_DOMAIN, baseUser.getStatus());
        }
        baseUserMapper.updateByPrimaryKeySelective(baseUser);
    }

    @Override
    public void addUserThirdParty(AddUserThirdPartyCommand command) {
        String accountType = command.getAccountType();
        if (!baseAccountService.isExist(command.getUserName(), accountType, ACCOUNT_DOMAIN)) {
            //保存系统用户信息
            BaseUser user = new BaseUser()
                    .setUserName(command.getUserName())
                    .setNickName(command.getNickName())
                    .setAvatar(command.getAvatar())
                    .setUserType(BaseConstants.USER_TYPE_ADMIN)
                    .setCreatedDate(LocalDateTime.now())
                    .setLastModifiedDate(LocalDateTime.now());
            baseUserMapper.insert(user);
            long userId = user.getUserId();

            // 注册账号信息
            baseAccountService.register(userId, command.getUserName(), command.getPassword(), accountType, BaseConstants.ACCOUNT_STATUS_NORMAL, ACCOUNT_DOMAIN, null);
        }
    }

    @Override
    public void updatePassword(long userId, String password) {
        baseAccountService.updatePasswordByUserId(userId, ACCOUNT_DOMAIN, password);
    }

    @Override
    public PageInfo<BaseUser> findListPage(BaseUserQuery query) {
        Weekend<BaseUser> queryWrapper = Weekend.of(BaseUser.class);
        WeekendCriteria<BaseUser, Object> criteria = queryWrapper.weekendCriteria();
        if (null != query.getUserId()) {
            criteria.andEqualTo(BaseUser::getUserId, query.getUserId());
        }
        if (StringHelper.isNotBlank(query.getUserType())) {
            criteria.andEqualTo(BaseUser::getUserType, query.getUserType());
        }
        if (StringHelper.isNotBlank(query.getUserName())) {
            criteria.andEqualTo(BaseUser::getUserName, query.getUserName());
        }
        if (StringHelper.isNotBlank(query.getMobile())) {
            criteria.andEqualTo(BaseUser::getMobile, query.getMobile());
        }
        queryWrapper.orderBy("createdDate").desc();

        PageHelper.startPage(query.getPage(), query.getLimit(), Boolean.TRUE);
        List<BaseUser> list = baseUserMapper.selectByExample(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    public List<BaseUser> findAllList() {
        return baseUserMapper.selectAll();
    }

    @Override
    public BaseUser getUserById(long userId) {
        return baseUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public UserAccountVO getUserAccount(long userId) {
        // 用户权限列表
        List<OpenAuthority> authorities = Lists.newArrayList();
        // 用户角色列表
        List<Map<String, Object>> roles = Lists.newArrayList();
        List<RbacRole> allRoles = userApplicationService.getRoles(userId);
        if (allRoles != null) {
            for (RbacRole role : allRoles) {
                Map<String, Object> roleMap = Maps.newHashMap();
                roleMap.put("roleId", role.getRoleId());
                roleMap.put("roleCode", role.getRoleCode());
                roleMap.put("roleName", role.getRoleName());
                // 用户角色详情
                roles.add(roleMap);
                // 加入角色标识
                OpenAuthority authority = new OpenAuthority(role.getRoleId().toString(), role.getRoleCode(), null, "role");
                authorities.add(authority);
            }
        }

        // 查询系统用户资料
        BaseUser baseUser = getUserById(userId);

        // 加入用户权限
        List<RbacPrivilege> allPrivileges = userApplicationService.getPrivileges(userId);
        if (allPrivileges != null && allPrivileges.size() > 0) {
            List<OpenAuthority> openAuthorities = allPrivileges.stream()
                    .map(x -> new OpenAuthority(x.getPrivilegeId().toString(), x.getPrivilege(), null, "role"))
                    .collect(Collectors.toList());
            authorities.addAll(openAuthorities);
        }

        UserAccountVO userAccount = new UserAccountVO();
        // 昵称
        userAccount.setNickName(baseUser.getNickName());
        // 头像
        userAccount.setAvatar(baseUser.getAvatar());
        // 权限信息
        userAccount.setAuthorities(authorities);
        userAccount.setRoles(roles);
        return userAccount;
    }

    @Override
    public BaseUser getUserByUsername(String username) {
        Weekend<BaseUser> queryWrapper = Weekend.of(BaseUser.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseUser::getUserName, username);
        return baseUserMapper.selectOneByExample(queryWrapper);
    }

    @Override
    public UserAccountVO login(String account) {
        if (StringHelper.isBlank(account)) {
            return null;
        }
        Map<String, String> parameterMap = WebUtils.getParameterMap(WebUtils.getHttpServletRequest());
        // 第三方登录标识
        String loginType = parameterMap.get("login_type");
        BaseAccount baseAccount;
        if (StringHelper.isNotBlank(loginType)) {
            baseAccount = baseAccountService.getAccount(account, loginType, ACCOUNT_DOMAIN);
        } else {
            // 非第三方登录

            //用户名登录
            baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_USERNAME, ACCOUNT_DOMAIN);

            // 手机号登陆
            if (StringHelper.matchMobile(account)) {
                baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_MOBILE, ACCOUNT_DOMAIN);
            }
            // 邮箱登陆
            if (StringHelper.matchEmail(account)) {
                baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_EMAIL, ACCOUNT_DOMAIN);
            }
        }

        // 获取用户详细信息
        if (baseAccount != null) {
            //添加登录日志
            try {
                HttpServletRequest request = WebUtils.getHttpServletRequest();
                BaseAccountLogs log = new BaseAccountLogs();
                log.setDomain(ACCOUNT_DOMAIN);
                log.setUserId(baseAccount.getUserId());
                log.setAccount(baseAccount.getAccount());
                log.setAccountId(baseAccount.getAccountId());
                log.setAccountType(baseAccount.getAccountType());
                log.setLoginIp(WebUtils.getRemoteAddress(request));
                log.setLoginAgent(request.getHeader(HttpHeaders.USER_AGENT));
                baseAccountService.addLoginLog(log);
            } catch (Exception e) {
                log.error("添加登录日志失败：{}", e.getLocalizedMessage(), e);
            }
            // 用户权限信息
            UserAccountVO userAccount = getUserAccount(baseAccount.getUserId());
            // 复制账号信息
            BeanUtils.copyProperties(baseAccount, userAccount);
            return userAccount;
        }
        return null;
    }
}