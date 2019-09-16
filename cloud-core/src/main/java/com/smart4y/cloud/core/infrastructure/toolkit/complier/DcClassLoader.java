package com.smart4y.cloud.core.infrastructure.toolkit.complier;

import javax.tools.JavaFileObject;

/**
 * @author Youtao
 *         Created by youtao on 2018/8/3.
 */
public class DcClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        JavaFileObject fileObject = ClassBuilder.fileObjects.get(name);
        if (fileObject != null) {
            byte[] bytes = ((DcJavaSourceFromCodeString) fileObject).getCompiledBytes();
            return defineClass(name, bytes, 0, bytes.length);
        }
        try {
            return ClassLoader.getSystemClassLoader().loadClass(name);
        } catch (Exception e) {
            return super.findClass(name);
        }
    }
}