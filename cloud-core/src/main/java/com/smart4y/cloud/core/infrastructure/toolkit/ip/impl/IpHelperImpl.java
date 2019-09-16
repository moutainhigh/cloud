package com.smart4y.cloud.core.infrastructure.toolkit.ip.impl;

import com.smart4y.cloud.core.infrastructure.toolkit.ip.core.DbSearcher;
import com.smart4y.cloud.core.infrastructure.toolkit.ip.IpHelper;
import com.smart4y.cloud.core.infrastructure.toolkit.ip.core.DataBlock;
import com.smart4y.cloud.core.infrastructure.toolkit.ip.core.DbConfig;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author Youtao
 *         Created by youtao on 2018/8/27.
 */
@Slf4j
public enum IpHelperImpl implements IpHelper {

    INSTANCE;

    private static DbSearcher searcher = null;

    private static DbSearcher getSearcher() {
        if (null == searcher) {
            searcher = new DbSearcher(new DbConfig());
        }
        return searcher;
    }

    @Override
    public IpInfo of(String ip) {
        try {
            DataBlock dataBlock = getSearcher().memorySearch(ip);
            if (null != dataBlock) {
                return new IpInfo(dataBlock.country(), dataBlock.region(), dataBlock.province(), dataBlock.city(), dataBlock.isp());
            }
        } catch (Exception ignored) {
        }
        String unknown = "未知";
        return new IpInfo(unknown, unknown, unknown, unknown, unknown);
    }

    @Override
    public String localIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            if (null != inetAddress) {
                return inetAddress.getHostAddress();
            }
        } catch (UnknownHostException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public String realIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                try {
                    // 根据网卡取本机配置的IP
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ip = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    @Override
    public String localMac() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    builder.append(":");
                }
                // 字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    builder.append("0").append(str);
                } else {
                    builder.append(str);
                }
            }
            return builder.toString().toLowerCase();
        } catch (UnknownHostException | SocketException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}