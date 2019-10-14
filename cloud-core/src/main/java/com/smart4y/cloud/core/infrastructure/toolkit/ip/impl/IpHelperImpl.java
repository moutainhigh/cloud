package com.smart4y.cloud.core.infrastructure.toolkit.ip.impl;

import com.smart4y.cloud.core.infrastructure.toolkit.ip.IpHelper;
import com.smart4y.cloud.core.infrastructure.toolkit.ip.IpInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

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
            try {
                String dbPath = getDbFilePath();
                searcher = new DbSearcher(new DbConfig(), dbPath);
            } catch (DbMakerConfigException | IOException e) {
                log.error("初始化IP地址转换器错误：{}", e.getLocalizedMessage(), e);
            }
        }
        return searcher;
    }

    private static String getDbFilePath() throws IOException {
        String dbPath = null;

        String resourcePath = "ip/ip2region.db";
        URL resource = IpInfo.class.getResource("/" + resourcePath);
        if (null != resource) {
            dbPath = resource.getPath();
            File file = new File(dbPath);
            if (!file.exists()) {
                InputStream asStream = IpInfo.class.getClassLoader()
                        .getResourceAsStream(resourcePath);
                if (null != asStream) {
                    String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
                    dbPath = tmpDir + "ip2region.db";
                    file = new File(dbPath);
                    FileUtils.copyInputStreamToFile(asStream, file);
                }
            }
        }
        return dbPath;
    }

    @Override
    public IpInfo of(String ip) {
        try {
            DbSearcher searcher = getSearcher();
            if (null != searcher) {
                DataBlock dataBlock = searcher.memorySearch(ip);
                if (null != dataBlock) {
                    // 中国|0|浙江省|杭州市|阿里云
                    String region = dataBlock.getRegion();
                    String[] split = region.split("\\|");
                    return new IpInfo(split[0], split[2], split[3], split[4]);
                }
            }
        } catch (Exception e) {
            log.warn("获取IP地址所在区域异常：{}", e.getLocalizedMessage(), e);
        }
        String unknown = "未知";
        return new IpInfo(unknown, unknown, unknown, unknown);
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