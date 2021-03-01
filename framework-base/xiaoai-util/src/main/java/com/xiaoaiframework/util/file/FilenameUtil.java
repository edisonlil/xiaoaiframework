package com.xiaoaiframework.util.file;

import com.xiaoaiframework.util.base.StrUtil;

import java.io.File;

/**
 * 文件名操作
 * @author edison
 */
public class FilenameUtil {



    /**
     * The extension separator character.
     * @since 1.4
     */
    public static final char EXTENSION_SEPARATOR = '.';

    /**
     * The extension separator String.
     * @since 1.4
     */
    public static final String EXTENSION_SEPARATOR_STR = Character.toString(EXTENSION_SEPARATOR);

    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * The system separator character.
     */
    private static final char SYSTEM_SEPARATOR = File.separatorChar;


    public static String getSuffix(String fileName){

        String ext =getExtension(fileName);
        if (StrUtil.isEmpty(ext)) {
            return null;
        }
        return EXTENSION_SEPARATOR+ext;

    }


    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        } else {
            int index = indexOfExtension(filename);
            return index == -1 ? "" : filename.substring(index + 1);
        }
    }

    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int extensionPos = filename.lastIndexOf(46);
            int lastSeparator = indexOfLastSeparator(filename);
            return lastSeparator > extensionPos ? -1 : extensionPos;
        }
    }

    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int lastUnixPos = filename.lastIndexOf(47);
            int lastWindowsPos = filename.lastIndexOf(92);
            return Math.max(lastUnixPos, lastWindowsPos);
        }
    }

    public static String getBaseName(String filename) {
        return removeExtension(getName(filename));
    }

    /**
     * 获取文件名
     * @param filename
     * @return
     */
    public static String getName(String filename) {
        if (filename == null) {
            return null;
        } else {
            int index = indexOfLastSeparator(filename);
            return filename.substring(index + 1);
        }
    }

    /**
     * 获取文件上一层目录
     * @param path
     * @return
     */
    public static String getPreviousPath(String path) {
        if (path == null) {
            return null;
        } else {
            int index = indexOfLastSeparator(path);
            return path.substring(0,index+1);
        }
    }


    public static String removeExtension(String filename) {
        if (filename == null) {
            return null;
        } else {
            int index = indexOfExtension(filename);
            return index == -1 ? filename : filename.substring(0, index);
        }
    }
}
