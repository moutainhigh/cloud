package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseAccountService;
import com.smart4y.cloud.base.application.BaseDeveloperService;
import com.smart4y.cloud.base.domain.model.BaseAccount;
import com.smart4y.cloud.base.domain.model.BaseAccountLogs;
import com.smart4y.cloud.base.domain.model.BaseDeveloper;
import com.smart4y.cloud.base.domain.repository.BaseDeveloperMapper;
import com.smart4y.cloud.base.interfaces.command.AddDeveloperUserCommand;
import com.smart4y.cloud.base.interfaces.command.RegisterDeveloperThirdPartyCommand;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import com.smart4y.cloud.core.application.dto.UserAccount;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.Page;
import com.smart4y.cloud.core.domain.PageParams;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.exception.OpenAlertException;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtils;
import com.smart4y.cloud.core.infrastructure.toolkit.WebUtils;
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

/**
 * @author: liuyadu
 * @date: 2018/10/24 16:33
 * @description:
 */
@Slf4j
@ApplicationService
public class BaseDeveloperServiceImpl implements BaseDeveloperService {

    private final String ACCOUNT_DOMAIN = BaseConstants.ACCOUNT_DOMAIN_PORTAL;

    @Autowired
    private BaseDeveloperMapper baseDeveloperMapper;
    @Autowired
    private BaseAccountService baseAccountService;

    @Override
    public IPage<BaseDeveloper> findListPage(PageParams pageParams) {
        BaseDeveloper query = pageParams.mapToObject(BaseDeveloper.class);

        Weekend<BaseDeveloper> queryWrapper = Weekend.of(BaseDeveloper.class);
        WeekendCriteria<BaseDeveloper, Object> criteria = queryWrapper.weekendCriteria();
        if (null != query.getUserId()) {
            criteria.andEqualTo(BaseDeveloper::getUserId, query.getUserId());
        }
        if (StringUtils.isNotBlank(query.getUserType())) {
            criteria.andEqualTo(BaseDeveloper::getUserType, query.getUserType());
        }
        if (StringUtils.isNotBlank(query.getUserName())) {
            criteria.andEqualTo(BaseDeveloper::getUserName, query.getUserName());
        }
        if (StringUtils.isNotBlank(query.getMobile())) {
            criteria.andEqualTo(BaseDeveloper::getMobile, query.getMobile());
        }
        queryWrapper.orderBy("createdDate").desc();

        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), Boolean.TRUE);
        List<BaseDeveloper> list = baseDeveloperMapper.selectByExample(queryWrapper);
        PageInfo<BaseDeveloper> pageInfo = new PageInfo<>(list);
        IPage<BaseDeveloper> page = new Page<>();
        page.setRecords(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        return page;
    }

    @Override
    public void addUser(AddDeveloperUserCommand command) {
        if (getUserByUsername(command.getUserName()) != null) {
            throw new OpenAlertException("用户名:" + command.getUserName() + "已存在!");
        }
        // 保存用户信息
        BaseDeveloper developer = new BaseDeveloper()
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
        baseDeveloperMapper.insert(developer);
        long userId = developer.getUserId();

        // 默认注册用户名账户
        baseAccountService.register(userId, command.getUserName(), command.getPassword(), BaseConstants.ACCOUNT_TYPE_USERNAME, command.getStatus(), ACCOUNT_DOMAIN, null);
        if (StringUtils.matchEmail(command.getEmail())) {
            // 注册email账号登陆
            baseAccountService.register(userId, command.getEmail(), command.getPassword(), BaseConstants.ACCOUNT_TYPE_EMAIL, command.getStatus(), ACCOUNT_DOMAIN, null);
        }
        if (StringUtils.matchMobile(command.getMobile())) {
            // 注册手机号账号登陆
            baseAccountService.register(userId, command.getMobile(), command.getPassword(), BaseConstants.ACCOUNT_TYPE_MOBILE, command.getStatus(), ACCOUNT_DOMAIN, null);
        }
    }

    @Override
    public void updateUser(BaseDeveloper baseDeveloper) {
        if (baseDeveloper == null || baseDeveloper.getUserId() == null) {
            return;
        }
        if (baseDeveloper.getStatus() != null) {
            baseAccountService.updateStatusByUserId(baseDeveloper.getUserId(), ACCOUNT_DOMAIN, baseDeveloper.getStatus());
        }
        baseDeveloperMapper.updateByPrimaryKeySelective(baseDeveloper);
    }

    @Override
    public void addUserThirdParty(RegisterDeveloperThirdPartyCommand command, String accountType) {
        if (!baseAccountService.isExist(command.getUserName(), accountType, ACCOUNT_DOMAIN)) {
            // 保存系统用户信息
            BaseDeveloper developer = new BaseDeveloper()
                    .setUserName(command.getUserName())
                    .setNickName(command.getNickName())
                    .setAvatar(command.getAvatar())
                    .setUserType(BaseConstants.USER_TYPE_ADMIN)
                    .setCreatedDate(LocalDateTime.now())
                    .setLastModifiedDate(LocalDateTime.now());
            baseDeveloperMapper.insert(developer);
            long userId = developer.getUserId();

            // 注册账号信息
            baseAccountService.register(userId, command.getUserName(), command.getPassword(), accountType, BaseConstants.ACCOUNT_STATUS_NORMAL, ACCOUNT_DOMAIN, null);
        }
    }

    @Override
    public void updatePassword(Long userId, String password) {
        baseAccountService.updatePasswordByUserId(userId, ACCOUNT_DOMAIN, password);
    }

    @Override
    public List<BaseDeveloper> findAllList() {
        return baseDeveloperMapper.selectAll();
    }

    @Override
    public BaseDeveloper getUserById(long userId) {
        return baseDeveloperMapper.selectByPrimaryKey(userId);
    }

    @Override
    public BaseDeveloper getUserByUsername(String username) {
        Weekend<BaseDeveloper> queryWrapper = Weekend.of(BaseDeveloper.class);
        queryWrapper.weekendCriteria()
                .andEqualTo(BaseDeveloper::getUserName, username);
        return baseDeveloperMapper.selectOneByExample(queryWrapper);
    }

    @Override
    public UserAccount login(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        Map<String, String> parameterMap = WebUtils.getParameterMap(WebUtils.getHttpServletRequest());
        // 第三方登录标识
        String loginType = parameterMap.get("login_type");
        BaseAccount baseAccount;
        if (StringUtils.isNotBlank(loginType)) {
            baseAccount = baseAccountService.getAccount(account, loginType, ACCOUNT_DOMAIN);
        } else {
            // 非第三方登录

            //用户名登录
            baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_USERNAME, ACCOUNT_DOMAIN);

            // 手机号登陆
            if (StringUtils.matchMobile(account)) {
                baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_MOBILE, ACCOUNT_DOMAIN);
            }
            // 邮箱登陆
            if (StringUtils.matchEmail(account)) {
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
                log.error("添加登录日志失败；{}", e.getLocalizedMessage(), e);
            }
            // 用户权限信息
            // 复制账号信息
            UserAccount userAccount = new UserAccount();
            BeanUtils.copyProperties(userAccount, baseAccount);
            return userAccount;
        }
        return null;
    }
}