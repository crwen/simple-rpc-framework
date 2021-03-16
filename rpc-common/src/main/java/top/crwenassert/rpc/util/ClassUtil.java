package top.crwenassert.rpc.util;

import lombok.extern.slf4j.Slf4j;
import top.crwenassert.rpc.domain.enums.RPCErrorEnum;
import top.crwenassert.rpc.exception.RPCException;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * ClassName: ClassUtil
 * Description:
 * date: 2021/2/21 17:50
 *
 * @author crwen
 * @create 2021-02-21-17:50
 * @since JDK 1.8
 */
@Slf4j
public class ClassUtil {

    private static final String FILE_PROTOCOL = "file";

    public static String getStackTrace() {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        return stack[stack.length - 1].getClassName();
    }

    /**
     * 获取包下类集合
     *
     * @param packageName 包名
     * @return
     */
    public static Set<Class<?>> extractPackageClass(String packageName) {
        ClassLoader classLoader = getClassLoader();
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if (url == null) {
            log.warn("unable to retrieve anything from packaage " + packageName);
            return null;
        }
        // 根据不同的资源类型，采用不同的方式获取资源集合
        Set<Class<?>> classSet = null;
        // 过滤处理文件类型的资源
        if (url.getProtocol().equals(FILE_PROTOCOL)) {
            classSet = new HashSet<Class<?>>();
            File packageDirectory = new File(url.getPath());
            extractClassFile(classSet, packageDirectory, packageName);
        }
        return classSet;
    }

    /**
     * 递归获取目标 package 里的所有 class 文件（包括目录下的 class 文件）
     * @param classSet 存放 class 的集合
     * @param fileSource 文件源
     * @param packageName 包名
     */
    private static void extractClassFile(Set<Class<?>> classSet, File fileSource, String packageName) {
        if (!fileSource.isDirectory()) {
            return ;
        }
        // 如果是文件夹，调用 listFiles 获取文件夹下的文件或文件夹
        File[] files = fileSource.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    String absolutePath = file.getAbsolutePath();
                    if (absolutePath.endsWith(".class")) {
                        addToClassSet(absolutePath);
                    }
                }
                return false;
            }
            // 根据 class 文件的绝对路径，获取并生成 class 对象加入 ClassSet 中
            private void addToClassSet(String absolutePath) {
                absolutePath = absolutePath.replace(File.separator, ".");
                String className = absolutePath.substring(absolutePath.indexOf(packageName));
                className = className.substring(0, className.lastIndexOf("."));
                // 通过反射获取对象加到 classSet 里
                Class<?> targetClass = loadClass(className);
                classSet.add(targetClass);
            }
        });

        if (files != null) {
            for (File file : files) {
                extractClassFile(classSet, file, packageName);
            }
        }
    }

    /**
     * 获取 Class 对象
     *
     * @param className class名（pacakge + 类名）
     */
    private static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("加载类时出错: ", e);
            throw new RPCException(RPCErrorEnum.UNKNOWN_ERROR);
        }
    }

    /**
     * 获取 ClassLoader
     *
     * @return 当前线程的 ClassLoader
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    /**
     * 实例化 class
     * @param clazz Class
     * @param <T> class 的类型
     * @param <T> 是否支持创建出私有 class 对象的实例
     * @return 类的实例化
     */
    public static <T> T newInstance(Class<?> clazz, boolean accessible) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(accessible);
            return (T) constructor.newInstance();
        } catch (Exception e) {
            log.error("创建 " + clazz + " 时发生错误，", e);
            throw new RuntimeException(e);
        }
    }

    public static void setField(Field field, Object obj, Object value) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            log.error("为 " + obj.getClass() + " 设置属性失败, {}",e);
            throw new RuntimeException(e);
        }
    }
}
