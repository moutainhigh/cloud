package com.smart4y.cloud.core.infrastructure.toolkit.ip.core;

import com.smart4y.cloud.core.infrastructure.toolkit.ip.impl.IpInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * data block class
 */
public class DataBlock extends IpInfo {

    /**
     * regionString ptr in the db file
     */
    private Integer dataPtr;
    private Integer cityId;

    /**
     * regionString address
     * <p>
     * 国家|区域|省份|城市|运营商
     * </p>
     */
    private String regionString;

    private DataBlock(String country, String region, String province, String city, String isp, Integer cityId, Integer dataPtr, String regionAll) {
        super(country, region, province, city, isp);
        this.cityId = cityId;
        this.dataPtr = dataPtr;
        this.regionString = regionAll;
    }

    public static DataBlock of(int cityId, String regionAll, int dataPtr) {
        String country = "", region = "", province = "", city = "", isp = "";
        if (StringUtils.isNotBlank(regionAll)) {
            String[] array = regionAll.split("\\|");
            if (5 == array.length) {
                country = convertZore2Empty(array[0]);
                region = convertZore2Empty(array[1]);
                province = convertZore2Empty(array[2]);
                city = convertZore2Empty(array[3]);
                isp = convertZore2Empty(array[4]);
            }
        }
        return new DataBlock(country, region, province, city, isp, cityId, dataPtr, regionAll);
    }

    private static String convertZore2Empty(String value) {
        if (StringUtils.isBlank(value) || "0".equals(value)) {
            return "";
        }
        return value;
    }

    public Integer getDataPtr() {
        return dataPtr;
    }

    public void setDataPtr(Integer dataPtr) {
        this.dataPtr = dataPtr;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getRegionString() {
        return regionString;
    }
}