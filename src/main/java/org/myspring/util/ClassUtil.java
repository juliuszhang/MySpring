package org.myspring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public class ClassUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ClassUtil.class);

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className, boolean isInitialized) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOG.error("load class failure", e);
            throw new RuntimeException(e);
        }
    }

    private static final String RESOURCE_TYPE_FILE = "file";
    private static final String RESOURCE_TYPE_JAR = "jar";


    public static Set<Class<?>> loadClasses(String basePackage) {
        String basePackagePath;
        if (basePackage.contains(".")) {
            basePackagePath = basePackage.replace(".", "/");
        } else {
            basePackagePath = basePackage;
        }

        try {
            Enumeration<URL> resources = getClassLoader().getResources(basePackagePath);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (RESOURCE_TYPE_FILE.equals(protocol)) {
                        return loadSourceFile(url, basePackage);
                    } else if (RESOURCE_TYPE_JAR.equals(protocol)) {
                        return loadJarFile(url);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("load class failure.", e);
        }
        return Collections.emptySet();
    }

    private static Set<Class<?>> loadJarFile(URL url) {
        Set<Class<?>> classSet = new HashSet<>();
        try {
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            JarFile jarFile = jarURLConnection.getJarFile();
            if (jarFile == null) {
                LOG.error("can not find jar with url {}", url.getHost() + ":" + url.getPort());
                return Collections.emptySet();
            }
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String jarEntryName = jarEntry.getName();
                if (jarEntryName.endsWith(".class")) {
                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll(".", "/");
                    classSet.add(loadClass(className, false));
                }
            }
        } catch (IOException e) {
            LOG.error("load jar file failure.", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    private static Set<Class<?>> loadSourceFile(URL fileUrl, String packageName) {
        Set<Class<?>> classSet = new HashSet<>();
        //replace %20 to space char
        String packagePath = fileUrl.getPath().replaceAll("%20", " ");
        addClass(classSet, packagePath, packageName);
        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(file -> file.isFile() && file.getName().endsWith(".class"));
        for (File file : files) {
            String fileName = file.getName();
            String className = fileName.substring(0, fileName.lastIndexOf("."));
            String fullClassName = packageName + "." + className;
            classSet.add(loadClass(fullClassName, false));
        }
        File[] directories = new File(packagePath).listFiles(file -> file.isDirectory());
        for (File file : directories) {
            String subPackageName = packageName + "." + file.getName();
            String subPackagePath = packagePath + "/" + file.getName();
            addClass(classSet, subPackagePath, subPackageName);
        }
    }


}
