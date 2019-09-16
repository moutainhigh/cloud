package com.smart4y.cloud.core.infrastructure.toolkit.complier;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author Youtao
 *         Created by youtao on 2018/8/3.
 */
public class DcJavaSourceFromCodeString extends SimpleJavaFileObject {

    private final String javaCode;

    private ByteArrayOutputStream outputStream;

    /**
     * 该构造器用来输入源代码
     *
     * @param className 类名（例如：Apple.java 中的 Apple）
     * @param javaCode  源代码（例如：public class Apple {public void hello(){System.out.println("hello world");}};）
     */
    DcJavaSourceFromCodeString(String className, String javaCode) {
        // 1、先初始化父类，由于该URI是通过类名来完成的，必须以.java结尾。
        // 2、如果是一个真实的路径，比如是 file:///test/demo/Hello.java 则不需要特别加.java
        // 3、这里加的 String:/// 并不是一个真正的URL的 schema, 只是为了区分来源
        super(URI.create("String:///" + className + Kind.SOURCE.extension), Kind.SOURCE);
        this.javaCode = javaCode;
    }

    /**
     * 该构造器用来输出字节码
     *
     * @param className 类名（例如：Apple.java 中的 Apple）
     * @param kind      {@link Kind}
     */
    DcJavaSourceFromCodeString(String className, Kind kind) {
        super(URI.create("String:///" + className + kind.extension), kind);
        this.javaCode = null;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return this.javaCode;
    }

    @Override
    public OutputStream openOutputStream() {
        this.outputStream = new ByteArrayOutputStream();
        return outputStream;
    }

    /**
     * 获取编译成功的字节码byte[]
     */
    public byte[] getCompiledBytes() {
        return outputStream.toByteArray();
    }
}