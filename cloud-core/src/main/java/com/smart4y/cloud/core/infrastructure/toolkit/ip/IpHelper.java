package com.smart4y.cloud.core.infrastructure.toolkit.ip;

import com.smart4y.cloud.core.infrastructure.toolkit.ip.impl.IpInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 本地配置获取 助手类
 *
 * @author Youtao
 *         Created by youtao on 2018/8/27.
 */
public interface IpHelper {

    /**
     * 解析IP信息
     *
     * @param ip IP 地址
     * @return {@link IpInfo}
     */
    IpInfo of(String ip);

    /**
     * 获取 内网 Ip 地址
     * <p>
     * 格式：10.0.3.184
     * </p>
     *
     * @return 内网 Ip 地址
     */
    String localIp();

    /**
     * 获取 外部真实IP
     * <p>
     * 获取客户端的IP地址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。
     * 但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了，如果通过了多级反向代理的话，
     * X-Forwarded-For的值并不止一个，而是一串IP值， 究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * 例如：X-Forwarded-For：192.168.1.110, 192.168.1.120,
     * 192.168.1.130, 192.168.1.100 用户真实IP为： 192.168.1.110
     * </p>
     *
     * @param request 请求信息
     * @return 真实IP
     */
    String realIp(HttpServletRequest request);

    /**
     * 获取 mac 地址
     * <p>
     * {@link String#toLowerCase()}
     * 格式：18:65:90:cc:48:95
     * </p>
     *
     * @return mac 地址
     */
    String localMac();
}