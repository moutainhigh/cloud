package com.smart4y.cloud.core.infrastructure.toolkit.complier;

import lombok.extern.slf4j.Slf4j;

import javax.tools.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Youtao
 *         Created by youtao on 2018/8/2.
 */
@Slf4j
public class ClassBuilder {

    static Map<String, JavaFileObject> fileObjects = new ConcurrentHashMap<>();
    private static Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s*");
    private static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static List<String> compilerOptions = Arrays.asList("-target", "1.8");

    /**
     * 动态编译 Java 代码
     *
     * @param javaCode 完整的 .java 文件字符串
     * @return Class
     */
    public static Class<?> buildClass(String javaCode) {
        // 获取类名
        String className = getClassName(javaCode);

        return buildClass(className, javaCode);
    }

    /**
     * 动态编译 Java 代码
     *
     * @param className Java 类名（例如：Apple.java 中的 Apple）
     * @param javaCode  完整的 .java 文件字符串
     * @return Class
     */
    public static Class<?> buildClass(String className, String javaCode) {
        // 是否编译成功
        boolean isCompile = compileJavaCode(className, javaCode);

        return isCompile ? loadClass(className) : null;
    }

    /**
     * 编译后加载指定类
     * <p>
     * 在 {@link ClassBuilder#compileJavaCode(String, String)}之后调用
     * </p>
     *
     * @param className Java 类名（例如：Apple.java 中的 Apple）
     * @return Class
     */
    public static Class<?> loadClass(String className) {
        // 我特此请求使用我的权限完成此方法，即使我是由没有它们的方法调用的
        ClassLoader classLoader = AccessController
                .doPrivileged((PrivilegedAction<ClassLoader>) DcClassLoader::new);

        Class<?> clazz = null;
        try {
            clazz = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            log.error("{}", e.getLocalizedMessage(), e);
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * 编译 Java 代码为字节码
     *
     * @param className Java 类名（例如：Apple.java 中的 Apple）
     * @param javaCode  完整的 .java 文件字符串
     * @return 是否编译成功
     */
    public static boolean compileJavaCode(String className, String javaCode) {
        JavaFileObject javaFileObject = new DcJavaSourceFromCodeString(className, javaCode);
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileManager fileManager = new DcJavaFileManager(compiler.getStandardFileManager(collector, Locale.getDefault(), DEFAULT_CHARSET));
        // execute the compiler
        return compiler
                .getTask(null, fileManager, null, compilerOptions, null,
                        Collections.singletonList(javaFileObject))
                .call();
    }

    /**
     * 获取 Java 类名
     *
     * @param javaCode 完成 .java 文件字符串
     * @return Java 类名（例如：Apple.java 中的 Apple）
     */
    public static String getClassName(String javaCode) {
        Matcher matcher = CLASS_PATTERN.matcher(javaCode);
        String name;
        if (matcher.find()) {
            name = matcher.group(1);
        } else {
            throw new IllegalArgumentException("No such class name in " + javaCode);
        }
        return name;
    }
}