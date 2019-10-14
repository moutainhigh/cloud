package com.smart4y.cloud.core.infrastructure.toolkit.ip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * IP 信息
 *
 * @author Youtao
 *         Created by youtao on 2018/8/28.
 */
@Getter
@ToString
@AllArgsConstructor
public class IpInfo implements Serializable {

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
     * 例如：联通、电信、移动、阿里云
     * </p>
     */
    private final String isp;
}