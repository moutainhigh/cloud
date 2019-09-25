package com.smart4y.cloud.base.application.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseAppService;
import com.smart4y.cloud.base.application.BaseAuthorityService;
import com.smart4y.cloud.base.domain.model.BaseApp;
import com.smart4y.cloud.base.domain.repository.BaseAppMapper;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.Page;
import com.smart4y.cloud.core.domain.PageParams;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import com.smart4y.cloud.core.infrastructure.exception.OpenAlertException;
import com.smart4y.cloud.core.infrastructure.security.OpenClientDetails;
import com.smart4y.cloud.core.infrastructure.toolkit.BeanConvertUtils;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtils;
import com.smart4y.cloud.core.infrastructure.toolkit.random.RandomValueUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/12 16:26
 * @description:
 */
@Slf4j
@ApplicationService
public class BaseAppServiceImpl implements BaseAppService {

    /**
     * token有效期，默认12小时
     */
    public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12;
    /**
     * token有效期，默认7天
     */
    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 7;

    @Autowired
    private BaseAppMapper baseAppMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;

    @Override
    public IPage<BaseApp> findListPage(PageParams pageParams) {
        BaseApp query = pageParams.mapToObject(BaseApp.class);
        Weekend<BaseApp> wrapper = Weekend.of(BaseApp.class);
        WeekendCriteria<BaseApp, Object> criteria = wrapper.weekendCriteria();
        if (null != query.getDeveloperId()) {
            criteria.andEqualTo(BaseApp::getDeveloperId, query.getDeveloperId());
        }
        if (StringUtils.isNotBlank(query.getAppType())) {
            criteria.andEqualTo(BaseApp::getAppType, query.getAppType());
        }
        Object aid = pageParams.getRequestMap().get("aid");
        if (null != aid) {
            criteria.andEqualTo(BaseApp::getAppId, aid.toString());
        }
        if (StringUtils.isNotBlank(query.getAppName())) {
            criteria.andLike(BaseApp::getAppName, query.getAppName() + "%");
        }
        if (StringUtils.isNotBlank(query.getAppNameEn())) {
            criteria.andLike(BaseApp::getAppNameEn, query.getAppNameEn() + "%");
        }
        wrapper.orderBy("createdDate").desc();

        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), Boolean.TRUE);
        List<BaseApp> list = baseAppMapper.selectByExample(wrapper);
        PageInfo<BaseApp> pageInfo = new PageInfo<>(list);
        IPage<BaseApp> page = new Page<>();
        page.setRecords(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        return page;
    }

    @Cacheable(value = "apps", key = "#appId")
    @Override
    public BaseApp getAppInfo(String appId) {
        return baseAppMapper.selectByPrimaryKey(appId);
    }

    @Override
    @Cacheable(value = "apps", key = "'client:'+#clientId")
    public OpenClientDetails getAppClientInfo(String clientId) {
        BaseClientDetails baseClientDetails = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(clientId);
        if (baseClientDetails == null) {
            return null;
        }
        String appId = baseClientDetails.getAdditionalInformation().get("appId").toString();
        OpenClientDetails openClient = new OpenClientDetails();
        BeanUtils.copyProperties(baseClientDetails, openClient);
        openClient.setAuthorities(baseAuthorityService.findAuthorityByApp(appId));
        return openClient;
    }

    @CacheEvict(value = {"apps"}, key = "'client:'+#client.clientId")
    @Override
    public void updateAppClientInfo(OpenClientDetails client) {
        jdbcClientDetailsService.updateClientDetails(client);
    }

    @CachePut(value = "apps", key = "#app.appId")
    @Override
    public BaseApp addAppInfo(BaseApp app) {
        String appId = String.valueOf(System.currentTimeMillis());
        String apiKey = RandomValueUtils.randomAlphanumeric(24);
        String secretKey = RandomValueUtils.randomAlphanumeric(32);
        app.setAppId(appId);
        app.setApiKey(apiKey);
        app.setSecretKey(secretKey);
        app.setCreatedDate(LocalDateTime.now());
        app.setLastModifiedDate(LocalDateTime.now());
        if (app.getIsPersist() == null) {
            app.setIsPersist(0);
        }
        baseAppMapper.insert(app);
        Map info = BeanConvertUtils.objectToMap(app);
        // 功能授权
        BaseClientDetails client = new BaseClientDetails();
        client.setClientId(app.getApiKey());
        client.setClientSecret(app.getSecretKey());
        client.setAdditionalInformation(info);
        client.setAuthorizedGrantTypes(Arrays.asList("authorization_code", "client_credentials", "implicit", "refresh_token"));
        client.setAccessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS);
        client.setRefreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
        jdbcClientDetailsService.addClientDetails(client);
        return app;
    }

    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#app.appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#app.appId")
    })
    @Override
    public BaseApp updateInfo(BaseApp app) {
        app.setLastModifiedDate(LocalDateTime.now());
        baseAppMapper.updateByPrimaryKeySelective(app);
        // 修改客户端附加信息
        BaseApp appInfo = getAppInfo(app.getAppId());
        Map info = BeanConvertUtils.objectToMap(appInfo);
        BaseClientDetails client = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(appInfo.getApiKey());
        client.setAdditionalInformation(info);
        jdbcClientDetailsService.updateClientDetails(client);
        return app;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    })
    public String restSecret(String appId) {
        BaseApp appInfo = getAppInfo(appId);
        if (appInfo == null) {
            throw new OpenAlertException(appId + "应用不存在!");
        }
        if (appInfo.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许修改"));
        }
        // 生成新的密钥
        String secretKey = RandomValueUtils.randomAlphanumeric(32);
        appInfo.setSecretKey(secretKey);
        appInfo.setLastModifiedDate(LocalDateTime.now());
        baseAppMapper.updateByPrimaryKeySelective(appInfo);
        jdbcClientDetailsService.updateClientSecret(appInfo.getApiKey(), secretKey);
        return secretKey;
    }

    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    })
    @Override
    public void removeApp(String appId) {
        BaseApp appInfo = getAppInfo(appId);
        if (appInfo == null) {
            throw new OpenAlertException(appId + "应用不存在!");
        }
        if (appInfo.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许删除"));
        }
        // 移除应用权限
        baseAuthorityService.removeAuthorityApp(appId);
        baseAppMapper.deleteByPrimaryKey(appInfo.getAppId());
        jdbcClientDetailsService.removeClientDetails(appInfo.getApiKey());
    }

    //public static void main(String[] args) {
    //    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    //    String apiKey = String.valueOf(RandomValueUtils.randomAlphanumeric(24));
    //    String secretKey = String.valueOf(RandomValueUtils.randomAlphanumeric(32));
    //    System.out.println("apiKey=" + apiKey);
    //    System.out.println("secretKey=" + secretKey);
    //    System.out.println("encodeSecretKey=" + encoder.encode(secretKey));
    //}
}