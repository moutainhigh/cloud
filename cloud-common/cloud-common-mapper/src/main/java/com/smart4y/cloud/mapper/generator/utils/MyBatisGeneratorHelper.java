package com.smart4y.cloud.mapper.generator.utils;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Youtao
 *         Created by youtao on 2018/12/27.
 */
@Slf4j
@SuppressWarnings("all")
public class MyBatisGeneratorHelper {

    private static Configuration configuration;
    private static Context context;

    /**
     * 运行 MyBatisGenerator 插件
     *
     * @param warnings 错误收集器
     * @return 错误收集器（若不为空则说明存在错误）
     */
    public static List<String> generateMybatis(Set<String> fullyQualifiedTables, List<String> warnings) {
        try {
            Configuration myBatisGeneratorConfig = MyBatisGeneratorHelper.getMyBatisGeneratorConfig();
            DefaultShellCallback shellCallback = new DefaultShellCallback(Boolean.TRUE);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(myBatisGeneratorConfig, shellCallback, warnings);
            ProgressCallback progressCallback = new VerboseProgressCallback();
            Set<String> contextIds = new HashSet<>();
            myBatisGenerator.generate(progressCallback, contextIds, fullyQualifiedTables);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("运行MyBatisGenerator插件错误");
        }
        return warnings;
    }

    /**
     * 获取 MyBatisGenerator配置
     * <p>
     * 即：MyBatisGeneratorConfig.xml 配置文件的<generatorConfiguration></generatorConfiguration>
     * </p>
     *
     * @return {@link Configuration}
     */
    public static Configuration getMyBatisGeneratorConfig() {
        if (null == configuration) {
            loadMyBatisGeneratorConfig();
        }
        return configuration;
    }

    /**
     * 获取 MyBatisGenerator配置上下文
     * <p>
     * 即：MyBatis Generator Config 配置文件的 <context></context>
     * </p>
     *
     * @return {@link Context}
     */
    public static Context getMyBatisGeneratorConfigContexts() {
        if (null == context) {
            loadMyBatisGeneratorConfigContexts();
        }
        return context;
    }

    /**
     * 获取 MyBatisGenerator配置上下文
     * <p>
     * 即：MyBatis Generator Config 配置文件的 <context></context>
     * </p>
     */
    private static void loadMyBatisGeneratorConfigContexts() {
        Configuration myBatisGeneratorConfig = MyBatisGeneratorHelper.getMyBatisGeneratorConfig();
        context = myBatisGeneratorConfig.getContexts().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("获取MyBatisGenerator配置上下文错误"));
    }

    /**
     * 获取 MyBatisGenerator配置
     * <p>
     * 即：MyBatisGeneratorConfig.xml 配置文件的<generatorConfiguration></generatorConfiguration>
     * </p>
     */
    private static void loadMyBatisGeneratorConfig() {
        try {
            File classPathFile = MyBatisGeneratorHelper.getClassPathDirectory();
            Predicate<String> tPredicate = x ->
                    x.endsWith(".xml") && x.contains("generatorConfig.");
            String filePath = MyBatisGeneratorHelper
                    .getFiles(classPathFile, tPredicate).stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("获取MyBatisGenerator配置错误"));
            File configFile = new File(filePath);
            ConfigurationParser cp = new ConfigurationParser(null);
            configuration = cp.parseConfiguration(configFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取MyBatisGenerator配置错误");
        }
    }

    /**
     * 获取 classpath 根路径
     *
     * @return classpath 根路径目录
     * @throws URISyntaxException 异常
     */
    @SuppressWarnings("all")
    public static File getClassPathDirectory() {
        try {
            // /Users/youtao/workspace/kernel/kernel-wxmp/src/main/resources
            String classPath = System.getProperty("user.dir") + "/src/main/resources";
            return new File(classPath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取classpath根路径错误");
        }
    }

    /**
     * 获取 文件全路径集合
     *
     * @param directory 目录
     * @param predicate 文件路径名过滤条件
     * @return 文件全路径集合
     */
    public static List<String> getFiles(File directory, Predicate<String> predicate) {
        return getFiles(directory).stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * 获取 文件全路径集合
     *
     * @param directory 目录
     * @return 文件全路径集合
     */
    public static List<String> getFiles(File directory) {
        List<String> collect = new ArrayList<>();
        ergodic(directory, collect);
        return collect;
    }

    /**
     * 获取 路径下所有文件和文件夹
     *
     * @param file    文件或目录
     * @param collect 收集文件的集合
     */
    private static void ergodic(File file, List<String> collect) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                // 判断是否文件夹
                if (f.isDirectory()) {
                    collect.add(f.getPath());
                    ergodic(f, collect);
                } else
                    collect.add(f.getPath());
            }
        }
    }
}