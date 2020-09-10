package com.smart4y.cloud.hippo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Youtao
 *         Created by youtao on 2019/10/14.
 */
@Slf4j
@Component
public class IpHelper {

    private DbSearcher searcher = null;

    @PostConstruct
    public void init() {
        try {
            // 因为无法读取jar包中的文件，所以复制创建临时文件
            String distFilePath = "/tmp/ip2region.db";
            Path distPath = Paths.get(distFilePath);
            log.info(">>>>>> Init ip region db path [{}]", distFilePath);
            //Files.deleteIfExists(distPath);
            if (!Files.exists(distPath)) {
                String property = System.getProperty("user.dir")
                        + "/cloud-hippo/src/main/resources/ip/ip2region.db";
                Path sourcePath = Paths.get(property);
                Files.copy(sourcePath, distPath);
            }
            DbConfig config = new DbConfig();
            searcher = new DbSearcher(config, distFilePath);
            log.info("Bean IpHelper DbConfig[{}] DbSearch[{}]", config, searcher);
        } catch (Exception e) {
            log.error("初始化IP地址转换器错误：{}", e.getLocalizedMessage(), e);
        }
    }

    public IpInfo of(String ip) {
        try {
            DataBlock dataBlock = searcher.memorySearch(ip);
            if (null != dataBlock) {
                // 中国|0|浙江省|杭州市|阿里云
                String region = dataBlock.getRegion();
                String[] split = region.split("\\|");
                String country = split[0], province = split[2], city = split[3], isp = split[4];
                String detail = String.format("%s|%s|%s|%s", country, province, city, isp);
                return new IpInfo(country, province, city, isp, detail);
            }
        } catch (Exception e) {
            log.warn("获取IP地址所在区域异常：{}", e.getLocalizedMessage(), e);
        }
        String unknown = "未知";
        return new IpInfo(unknown, unknown, unknown, unknown, unknown);
    }

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

    /**
     * IP 信息
     *
     * @author Youtao
     *         Created by youtao on 2018/8/28.
     */
    @Getter
    @ToString
    @AllArgsConstructor
    public static class IpInfo implements Serializable {

        /**
         * 国家名称
         * <p>
         * 例如：中国、美国
         * </p>
         */
        private final String country;

        /**
         * 省份名称
         * <p>
         * 例如：浙江省、江西省、加利福尼亚
         * </p>
         */
        private final String province;

        /**
         * 城市名称
         * <p>
         * 例如：杭州市、上饶市、洛杉矶
         * </p>
         */
        private final String city;

        /**
         * ISP 名称
         * <p>
         * 例如：联通、电信、移动、阿里云、华数
         * </p>
         */
        private final String isp;

        /**
         * 详细信息
         * <p>
         * 例如：中国|浙江省|杭州市|阿里云
         * </p>
         */
        private final String detail;
    }
}