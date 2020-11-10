package com.xiaoaifreamwork.util.base;

/**
 * 平台工具类
 * @author edison
 * @version 1.0.0
 */
public class PlatformUtil {

    /**
     * 获取系统名称
     */
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    private PlatformUtil() {
        throw new AssertionError("utility class must not be instantiated");
    }
    
    public static boolean isLinux() {
        return OS_NAME.startsWith("linux");
    }

    public static boolean isMac() {
        return OS_NAME.startsWith("mac");
    }

    public static boolean isWindows() {
        return OS_NAME.startsWith("windows");
    }



}

