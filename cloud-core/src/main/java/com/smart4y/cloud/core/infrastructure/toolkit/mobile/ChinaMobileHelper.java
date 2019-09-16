package com.smart4y.cloud.core.infrastructure.toolkit.mobile;

import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
import com.smart4y.cloud.core.infrastructure.toolkit.Kit;
import com.smart4y.cloud.core.infrastructure.toolkit.random.RandomHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 中国（排除西藏的大陆地区）手机号 助手类
 *
 * @author Youtao
 *         Created by youtao on 2019-07-15.
 */
public final class ChinaMobileHelper {

    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();
    private static PhoneNumberOfflineGeocoder offlineGeoCoder = PhoneNumberOfflineGeocoder.getInstance();
    private static RandomHelper randomHelper = Kit.help().random();
    /**
     * 省级行政列表
     * <p>
     * 北京市、浙江省、新疆、内蒙古、广西
     * </p>
     */
    private static List<String> SUPPORT_PROVINCES = new ArrayList<>();
    /**
     * 市级行政列表
     * <p>
     * 杭州市、上饶市
     * </p>
     */
    private static List<String> SUPPORT_CITIES = new ArrayList<>();
    /**
     * 归属地前缀表
     * <p>
     * 数据结构：
     * {
     * "北京市" = {"北京市" = [1349, 1451, 1851]},
     * "江西省" = {"南昌市" = [130779, 131778], "上饶市" = [134793, 135763, 137553]},
     * "新疆" = {"乌鲁木齐市" = [138998, 138999]}
     * }
     * </p>
     */
    private static Map<String, Map<String, Set<String>>> SUPPORT_REGION_NUMBER_PREFIX = new TreeMap<>();
    /**
     * 运营商列表
     * <p>
     * 联通、移动、电信
     * </p>
     */
    private static List<String> SUPPORT_CARRIERS = new ArrayList<>();
    /**
     * 运营商前缀表
     * <p>
     * {
     * "联通" = {1349, 1451, 1851},
     * "移动" = {1307, 131778},
     * "电信" = {135763, 137}
     * }
     * </p>
     */
    private static Map<String, Set<String>> SUPPORT_CARRIER_NUMBER_PREFIX = new HashMap<>();

    /*
     * 初始化数据
     */
    static {
        // **************************************** 省级行政列表 ******************************************
        SUPPORT_PROVINCES.add("北京市");
        SUPPORT_PROVINCES.add("上海市");
        SUPPORT_PROVINCES.add("天津市");
        SUPPORT_PROVINCES.add("重庆市");
        SUPPORT_PROVINCES.add("河北省");
        SUPPORT_PROVINCES.add("山西省");
        SUPPORT_PROVINCES.add("辽宁省");
        SUPPORT_PROVINCES.add("吉林省");
        SUPPORT_PROVINCES.add("黑龙江省");
        SUPPORT_PROVINCES.add("江苏省");
        SUPPORT_PROVINCES.add("浙江省");
        SUPPORT_PROVINCES.add("安徽省");
        SUPPORT_PROVINCES.add("福建省");
        SUPPORT_PROVINCES.add("江西省");
        SUPPORT_PROVINCES.add("山东省");
        SUPPORT_PROVINCES.add("河南省");
        SUPPORT_PROVINCES.add("湖北省");
        SUPPORT_PROVINCES.add("湖南省");
        SUPPORT_PROVINCES.add("广东省");
        SUPPORT_PROVINCES.add("海南省");
        SUPPORT_PROVINCES.add("四川省");
        SUPPORT_PROVINCES.add("贵州省");
        SUPPORT_PROVINCES.add("云南省");
        SUPPORT_PROVINCES.add("陕西省");
        SUPPORT_PROVINCES.add("甘肃省");
        SUPPORT_PROVINCES.add("青海省");
        SUPPORT_PROVINCES.add("内蒙古");
        SUPPORT_PROVINCES.add("新疆");
        SUPPORT_PROVINCES.add("广西");
        SUPPORT_PROVINCES.add("宁夏");
        // **************************************** 归属地前缀表 ******************************************
        Path path = Paths.get(getMobileDatPath());
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new IllegalArgumentException("");
        }
        List<String> finalLines = lines;
        SUPPORT_PROVINCES.forEach(province -> {
            // 筛选出所有包含指定省份的数据行
            Map<String, Set<String>> cityCodesMap = new ConcurrentHashMap<>();
            finalLines.stream()
                    .filter(x -> x.contains(province))
                    .forEach(line -> {
                        String first = line.replaceFirst(province, "");
                        String[] split = first.split("\\|");
                        String code = split[0];
                        String city = split.length == 2 ? split[1] : province;
                        // 省、自治区、直辖市
                        if (cityCodesMap.containsKey(city)) {
                            Set<String> codes = cityCodesMap.get(city);
                            codes.add(code);
                            cityCodesMap.put(city, codes);
                        } else {
                            Set<String> hashSet = new HashSet<>();
                            hashSet.add(code);
                            cityCodesMap.put(city, hashSet);
                        }
                    });
            SUPPORT_REGION_NUMBER_PREFIX.put(province, cityCodesMap);
        });
        // **************************************** 市级行政列表 ******************************************
        List<Set<String>> collect = SUPPORT_REGION_NUMBER_PREFIX.values().stream()
                .map(Map::keySet)
                .collect(Collectors.toList());

        List<String> tempCities = new ArrayList<>();
        collect.forEach(tempCities::addAll);
        SUPPORT_CITIES.addAll(tempCities.stream()
                .filter(x -> !Arrays.asList("北京市", "上海市", "天津市", "重庆市").contains(x))
                .collect(Collectors.toList()));
        // **************************************** 运营商的列表 ******************************************
        SUPPORT_CARRIERS.addAll(Arrays.asList("联通", "移动", "电信"));
        // **************************************** 运营商前缀表 ******************************************
        Path carrierNumberPath = Paths.get(getCarrierDatPath());
        List<String> carrierNumberLines;
        try {
            carrierNumberLines = Files.readAllLines(carrierNumberPath);
        } catch (IOException e) {
            throw new IllegalArgumentException("");
        }
        carrierNumberLines.forEach(x -> {
            String[] split = x.split("\\|");
            String code = split[0];
            String carrierEnName = split[1];
            if ("China Mobile".equals(carrierEnName)) {
                String tempKey = "移动";
                if (SUPPORT_CARRIER_NUMBER_PREFIX.containsKey(tempKey)) {
                    Set<String> codes = SUPPORT_CARRIER_NUMBER_PREFIX.get(tempKey);
                    codes.add(code);
                    SUPPORT_CARRIER_NUMBER_PREFIX.put(tempKey, codes);
                } else {
                    Set<String> hashSet = new HashSet<>();
                    hashSet.add(code);
                    SUPPORT_CARRIER_NUMBER_PREFIX.put(tempKey, hashSet);
                }
            } else if ("China Unicom".equals(carrierEnName)) {
                String tempKey = "联通";
                if (SUPPORT_CARRIER_NUMBER_PREFIX.containsKey(tempKey)) {
                    Set<String> codes = SUPPORT_CARRIER_NUMBER_PREFIX.get(tempKey);
                    codes.add(code);
                    SUPPORT_CARRIER_NUMBER_PREFIX.put(tempKey, codes);
                } else {
                    Set<String> hashSet = new HashSet<>();
                    hashSet.add(code);
                    SUPPORT_CARRIER_NUMBER_PREFIX.put(tempKey, hashSet);
                }
            } else if ("China Telecom".equals(carrierEnName)) {
                String tempKey = "电信";
                if (SUPPORT_CARRIER_NUMBER_PREFIX.containsKey(tempKey)) {
                    Set<String> codes = SUPPORT_CARRIER_NUMBER_PREFIX.get(tempKey);
                    codes.add(code);
                    SUPPORT_CARRIER_NUMBER_PREFIX.put(tempKey, codes);
                } else {
                    Set<String> hashSet = new HashSet<>();
                    hashSet.add(code);
                    SUPPORT_CARRIER_NUMBER_PREFIX.put(tempKey, hashSet);
                }
            }
        });
    }

    /**
     * 根据条件生成手机号
     *
     * @return 手机号
     */
    public static String generateMobile() {
        return generateMobile(Collections.emptyList());
    }

    /**
     * 根据条件生成手机号
     *
     * @param provinces 省级行政区划名称（包含直辖市） {@link ChinaMobileHelper#SUPPORT_PROVINCES}
     * @return 手机号
     */
    public static String generateMobile(final List<String> provinces) {
        return generateMobile(provinces, Collections.emptyList());
    }

    /**
     * 根据条件生成手机号
     *
     * @param provinces 省级行政区划名称（包含直辖市）  {@link ChinaMobileHelper#SUPPORT_PROVINCES}
     * @param cities    市级行政区划名称 {@link ChinaMobileHelper#SUPPORT_CITIES}
     * @return 手机号
     */
    public static String generateMobile(final List<String> provinces, final List<String> cities) {
        return generateMobile(provinces, cities, Collections.emptyList());
    }

    /**
     * 根据条件生成手机号
     *
     * @param provinces 省级行政区划名称（包含直辖市） {@link ChinaMobileHelper#SUPPORT_PROVINCES}
     * @param cities    市级行政区划名称 {@link ChinaMobileHelper#SUPPORT_CITIES}
     * @param carriers  运营商名称 {@link ChinaMobileHelper#SUPPORT_CARRIERS}
     * @return 手机号
     */
    public static String generateMobile(final List<String> provinces, final List<String> cities, final List<String> carriers) {
        List<String> mobiles = generateMobile(provinces, Collections.emptyList(), cities, Collections.emptyList(), carriers, 1);
        return mobiles.stream().findFirst().orElse(null);
    }

    /**
     * 根据条件生成手机号
     *
     * @param provinces        省级行政区划名称（包含直辖市） {@link ChinaMobileHelper#SUPPORT_PROVINCES}
     * @param excludeProvinces 要排除的省级行政区划（排除省级行政区划则会将省下的市级区划也排除）
     * @param cities           市级行政区划名称 {@link ChinaMobileHelper#SUPPORT_CITIES}
     * @param excludeCities    要排除的市级行政区划
     * @param carriers         运营商名称 {@link ChinaMobileHelper#SUPPORT_CARRIERS}
     * @param nums             生成的手机号个数（必须大于等于1）
     * @return 手机号
     */
    public static List<String> generateMobile(final List<String> provinces, final List<String> excludeProvinces, final List<String> cities, final List<String> excludeCities, final List<String> carriers, final int nums) {
        // ******************************** 参数预处理及校验 ********************************
        if (nums < 1) {
            throw new IllegalArgumentException("生成的手机号个数必须大于等于1");
        }
        if (CollectionUtils.isNotEmpty(provinces)) {
            Set<String> provinceNames = SUPPORT_REGION_NUMBER_PREFIX.keySet();
            boolean provinceAllMatch = provinceNames.containsAll(provinces);
            if (!provinceAllMatch) {
                throw new IllegalArgumentException("省级行政区划错误");
            }
        }
        if (CollectionUtils.isNotEmpty(cities)) {
            boolean cityAllMatch = SUPPORT_CITIES.containsAll(cities);
            if (!cityAllMatch) {
                throw new IllegalArgumentException("市级行政区划错误");
            }
        }
        if (CollectionUtils.isNotEmpty(carriers)) {
            boolean carrierAllMatch = SUPPORT_CARRIERS.containsAll(carriers);
            if (!carrierAllMatch) {
                throw new IllegalArgumentException("运营商错误");
            }
        }
        // 最终需要排除的省、市
        final List<String> finalExcludeProvinces = CollectionUtils.isNotEmpty(excludeProvinces) ? excludeProvinces : Collections.emptyList();
        final List<String> finalExcludeCities = CollectionUtils.isNotEmpty(excludeCities) ? excludeCities : Collections.emptyList();
        // 最终需要保留的省、市
        final List<String> tempProvinces = CollectionUtils.isNotEmpty(provinces) ?
                provinces.stream().filter(province -> !finalExcludeProvinces.contains(province)).collect(Collectors.toList()) :
                getSupportProvinces().stream().filter(province -> !finalExcludeProvinces.contains(province)).collect(Collectors.toList());
        final List<String> finalProvinces = SUPPORT_REGION_NUMBER_PREFIX.keySet().stream()
                .filter(tempProvinces::contains)
                .collect(Collectors.toList());
        final List<String> tempCities = CollectionUtils.isNotEmpty(cities) ?
                cities.stream().filter(city -> !finalExcludeCities.contains(city)).collect(Collectors.toList()) :
                getSupportCities().stream().filter(city -> !finalExcludeCities.contains(city)).collect(Collectors.toList());
        // 指定运营商
        final List<String> finalCarrier = CollectionUtils.isNotEmpty(carriers) ? carriers : getSupportCarriers();
        // 满足条件的运营商号段
        Set<String> finalNumberSet = new HashSet<>();
        SUPPORT_CARRIER_NUMBER_PREFIX.forEach((k, v) -> {
            if (finalCarrier.contains(k)) {
                finalNumberSet.addAll(v);
            }
        });

        // 手机号从这里面取
        Map<String, Map<String, Set<String>>> repository = new ConcurrentHashMap<>();
        SUPPORT_REGION_NUMBER_PREFIX.forEach((province, cityMap) -> {
            if (finalProvinces.contains(province)) {
                Map<String, Set<String>> tempCityMap = new ConcurrentHashMap<>();
                cityMap.forEach((city, codeSet) -> {
                    if (tempCities.contains(city)) {
                        tempCityMap.put(city, codeSet);
                    }
                });
                repository.put(province, tempCityMap);
            }
        });
        Set<String> repositoryNumberPrefixSet = new HashSet<>();
        repository.forEach((province, cityNumberPrefixMap) ->
                cityNumberPrefixMap.forEach((city, numberPrefixSet) -> {
                    Set<String> collect = numberPrefixSet.stream()
                            .filter(x -> finalNumberSet.stream().anyMatch(x::startsWith))
                            .collect(Collectors.toSet());
                    repositoryNumberPrefixSet.addAll(collect);
                }));


        //if (availablePhoneNumberTotal < nums) {
        //    throw new IllegalArgumentException(String.format(
        //            "指定条件可生成的手机号总数为：%s，预期生成的数量（%s）超过了该值。", availablePhoneNumberTotal, nums));
        //}

        return randPhoneNumber(repositoryNumberPrefixSet, nums);
    }

    /**
     * 随机获取 手机号列表
     *
     * @param repositoryNumberPrefixSet 满足条件的号段前缀列表
     * @param nums                      生成总数
     * @return 手机号列表
     */
    private static List<String> randPhoneNumber(final Set<String> repositoryNumberPrefixSet, final int nums) {
        // 每个号段的个数
        long divide = nums / repositoryNumberPrefixSet.size();

        Map<String, Long> numberPrefixMap = new ConcurrentHashMap<>();
        for (String numberPrefix : repositoryNumberPrefixSet) {
            numberPrefixMap.put(numberPrefix, divide);
        }

        int iteratorCount = 0;
        Set<String> collect = new HashSet<>();
        while (collect.size() < nums) {
            numberPrefixMap.forEach((numberPrefix, subNums) -> {
                Set<String> subSet = new HashSet<>();
                while (subSet.size() < subNums) {
                    subSet.add(buildPhoneNumber(numberPrefix));
                }
                collect.addAll(subSet);
                numberPrefixMap.remove(numberPrefix);

                System.out.println(String.format("Index[%s][%s]", nums, collect.size()));
            });
            if (collect.size() < nums) {
                long subtract = nums - collect.size();
                long lastDivide = subtract / repositoryNumberPrefixSet.size() + 1;
                for (String numberPrefix : repositoryNumberPrefixSet) {
                    numberPrefixMap.put(numberPrefix, lastDivide);
                }
            }
            iteratorCount++;
            System.out.println(String.format("Iterator Index: %s, Count: %s", iteratorCount, collect.size()));
        }

        List<String> result = new ArrayList<>();
        Iterator<String> iterator = collect.iterator();
        while (iterator.hasNext() && result.size() < nums) {
            result.add(iterator.next());
        }

        return result;
    }

    /**
     * 根据号段位数，补足手机号剩余位数，并返回手机号
     *
     * @param numberPrefix 手机号前缀
     * @return 手机号
     */
    private static String buildPhoneNumber(String numberPrefix) {
        // 需要补足的位数
        int charCount = 11 - numberPrefix.length();
        String numberSuffix = randomHelper.randNum(charCount);
        return numberPrefix + numberSuffix;
    }

    /**
     * 判断 手机号是否有效
     *
     * @param phoneNumber 手机号
     * @return true有效 false 无效
     */
    public static boolean checkPhoneNumber(String phoneNumber) {
        PhoneNumber number = getPhoneNumber(phoneNumber);

        return phoneNumberUtil.isValidNumber(number);
    }

    /**
     * 解析 手机号运营商
     *
     * @param phoneNumber 手机号
     * @return 手机号运营商（例如：联通、移动、电信、其它）
     */
    public static String parseCarrier(String phoneNumber) {
        PhoneNumber number = getPhoneNumber(phoneNumber);

        // 返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(number, Locale.ENGLISH);
        String carrierZh;
        switch (carrierEn) {
            case "China Mobile":
                carrierZh = "移动";
                break;
            case "China Unicom":
                carrierZh = "联通";
                break;
            case "China Telecom":
                carrierZh = "电信";
                break;
            default:
                carrierZh = "其它";
                break;
        }
        return carrierZh;
    }

    /**
     * 解析 手机号归属地
     *
     * @param phoneNumber 手机号
     * @return 手机号归属地（例如：浙江省杭州市、新疆乌鲁木齐市）
     */
    public static String parseGeo(String phoneNumber) {
        PhoneNumber number = getPhoneNumber(phoneNumber);

        return offlineGeoCoder.getDescriptionForNumber(number, Locale.CHINESE);
    }

    /**
     * 解析 归属地运营商信息
     *
     * @param phoneNumber 手机号
     * @return 归属地运营商信息（例如：浙江省杭州市|联通、新疆乌鲁木齐市|移动、浙江省杭州市|其它）
     */
    public static String parseGeoCarrier(String phoneNumber) {
        PhoneNumber number = getPhoneNumber(phoneNumber);

        String geo = offlineGeoCoder.getDescriptionForNumber(number, Locale.CHINESE);
        String carrierEn = carrierMapper.getNameForNumber(number, Locale.ENGLISH);
        switch (carrierEn) {
            case "China Mobile":
                geo += "|移动";
                break;
            case "China Unicom":
                geo += "|联通";
                break;
            case "China Telecom":
                geo += "|电信";
                break;
            default:
                geo += "|其它";
                break;
        }
        return geo;
    }

    /**
     * 获取手机号信息
     *
     * @param phoneNumber 手机号
     * @return 手机号信息
     */
    private static PhoneNumber getPhoneNumber(String phoneNumber) {
        int countryCode = 86;
        long phone = NumberUtils.toLong(phoneNumber, 0L);
        PhoneNumber number = new PhoneNumber();
        number.setCountryCode(countryCode);
        number.setNationalNumber(phone);
        return number;
    }

    /**
     * 省级行政列表
     * <p>
     * 北京市、浙江省、新疆、内蒙古、广西
     * </p>
     */
    public static List<String> getSupportProvinces() {
        return SUPPORT_PROVINCES;
    }

    /**
     * 市级行政列表
     * <p>
     * 杭州市、上饶市
     * </p>
     */
    public static List<String> getSupportCities() {
        return SUPPORT_CITIES;
    }

    /**
     * 运营商列表
     * <p>
     * 联通、移动、电信
     * </p>
     */
    public static List<String> getSupportCarriers() {
        return SUPPORT_CARRIERS;
    }

    /**
     * 获取 手机号归属地数据文件全路径
     *
     * @return 手机号归属地数据文件全路径
     */
    public static String getMobileDatPath() {
        String fileName = "mobile.dat";
        return getResourceFilePath(fileName);
    }

    /**
     * 获取 电话号码归属地数据文件全路径
     *
     * @return 电话号码归属地数据文件全路径
     */
    public static String getTelPhonePath() {
        String fileName = "telPhone.dat";
        return getResourceFilePath(fileName);
    }

    /**
     * 获取 电话号码运营商数据文件全路径
     *
     * @return 电话号码运营商数据文件全路径
     */
    public static String getCarrierDatPath() {
        String fileName = "carrier.dat";
        return getResourceFilePath(fileName);
    }

    /**
     * 获取 归属地数据文件全路径
     *
     * @param fileName 文件名（mobile.dat/telPhone.dat）
     * @return 归属地数据文件全路径
     */
    private static String getResourceFilePath(String fileName) {
        String dbPath = ChinaMobileHelper.class.getResource("/libphonenumber/" + fileName).getPath();
        File file = new File(dbPath);
        if (!file.exists()) {
            InputStream asStream = ChinaMobileHelper.class.getClassLoader()
                    .getResourceAsStream(fileName);
            dbPath = System.getProperties().getProperty("java.io.tmpdir") + fileName;
            file = new File(dbPath);
            if (asStream != null) {
                try {
                    FileUtils.copyInputStreamToFile(asStream, file);
                } catch (IOException e) {
                    throw new IllegalArgumentException("解析数据文件错误");
                }
            }
        }
        return dbPath;
    }

    //private static void getCarrierData() {
    //    InputStream source = PrefixFileReader.class.getResourceAsStream(
    //            "/com/google/i18n/phonenumbers/carrier/data/86_en");
    //    ObjectInputStream in = null;
    //    try {
    //        in = new ObjectInputStream(source);
    //        PhonePrefixMap map = new PhonePrefixMap();
    //        map.readExternal(in);
    //        System.out.println("...");
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    } finally {
    //        if (in != null) {
    //            try {
    //                in.close();
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //    }
    //}
}