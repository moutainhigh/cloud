package com.smart4y.cloud.core.toolkit.complier;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

/**
 * @author Youtao
 *         Created by youtao on 2018/8/3.
 */
public class DcJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     */
    protected DcJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
        JavaFileObject javaFileObject = ClassBuilder.fileObjects.get(className);
        if (javaFileObject == null) {
            super.getJavaFileForInput(location, className, kind);
        }
        return javaFileObject;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        JavaFileObject javaFileObject = new DcJavaSourceFromCodeString(className, kind);
        ClassBuilder.fileObjects.put(className, javaFileObject);
        return javaFileObject;
    }
}