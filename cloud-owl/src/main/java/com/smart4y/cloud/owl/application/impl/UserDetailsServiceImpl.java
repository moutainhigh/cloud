package com.smart4y.cloud.owl.application.impl;

import com.smart4y.cloud.core.dto.UserAccountVO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.constant.BaseConstants;
import com.smart4y.cloud.core.security.OpenUserDetails;
import com.smart4y.cloud.core.security.oauth2.client.OpenOAuth2ClientProperties;
import com.smart4y.cloud.owl.infrastructure.feign.BaseUserFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Security用户信息获取实现类
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
@Service("userDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private OpenOAuth2ClientProperties clientProperties;
    @Autowired
    private BaseUserFeign baseUserFeign;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResultMessage<UserAccountVO> resp = baseUserFeign.userLogin(username);
        UserAccountVO account = resp.getData();
        if (account == null || account.getAccountId() == null) {
            throw new UsernameNotFoundException("系统用户 " + username + " 不存在!");
        }
        String domain = account.getDomain();
        Long accountId = account.getAccountId();
        Long userId = account.getUserId();
        String password = account.getPassword();
        String nickName = account.getNickName();
        String avatar = account.getAvatar();
        String accountType = account.getAccountType();
        boolean accountNonLocked = account.getStatus() != BaseConstants.ACCOUNT_STATUS_LOCKED;
        boolean credentialsNonExpired = true;
        boolean enabled = account.getStatus() == BaseConstants.ACCOUNT_STATUS_NORMAL;
        boolean accountNonExpired = true;
        OpenUserDetails userDetails = new OpenUserDetails();
        userDetails.setDomain(domain);
        userDetails.setAccountId(accountId);
        userDetails.setUserId(userId);
        userDetails.setUsername(username);
        userDetails.setPassword(password);
        userDetails.setNickName(nickName);
        userDetails.setAuthorities(account.getAuthorities());
        userDetails.setAvatar(avatar);
        userDetails.setAccountId(accountId);
        userDetails.setAccountNonLocked(accountNonLocked);
        userDetails.setAccountNonExpired(accountNonExpired);
        userDetails.setAccountType(accountType);
        userDetails.setCredentialsNonExpired(credentialsNonExpired);
        userDetails.setEnabled(enabled);
        userDetails.setClientId(clientProperties.getOauth2().get("portal").getClientId());
        return userDetails;
    }
}