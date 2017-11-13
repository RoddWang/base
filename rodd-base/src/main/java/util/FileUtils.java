/**
 * Copyright (c) 2017, China Mobile IOT All Rights Reserved.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.ApiRuntimeException;


/**
 * 此类描述的是：
 * 
 * @author: wangjian
 * @date: 2017年11月13日 下午2:01:36
 */
public final class FileUtils {
    // BUFFER的大小
    public static final int BUFFER_SIZE = 4 * 1024;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 
     * 私有化工具类，禁止实例化。 FileUtils.
     *
     */
    private FileUtils() {
        throw new ApiRuntimeException("禁止实例化工具类 FileUtils");
    }

    /**
     * 使用该方法上传文件到指定目录
     * 
     * @param upFile 上传的文件
     * @param dir 目标绝对路径
     * @param sourceFileName 原文件名称
     * @return fileName 转化后的文件名称
     */
    public static String uploadFile(File upFile, String dir, String sourceFileName) {
        String suffix = sourceFileName.substring(sourceFileName.lastIndexOf('.')); // 获得文件后缀
        String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis()); // 根据当前时间获得文件名称
        StringBuilder fileNameBuffer = new StringBuilder(fileName).append(suffix); // 连接文件名和后缀
        fileName = fileNameBuffer.toString();
        File filePath = new File(dir);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File uploadFile = new File(dir, fileName);
        copyFile(upFile, uploadFile.getAbsolutePath());
        return fileName;
    }

    /**
     * 复制文件
     * 
     * @param file 待备份的文件
     * @param filePath 备份路径（以‘/’结尾）
     */
    public static void copyFile(File file, String filePath) {
        try (FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(filePath + file.getName());) {
            byte[] buff = new byte[BUFFER_SIZE];
            int fileLen;
            while ((fileLen = fis.read(buff)) != -1) { // NOSONAR
                fos.write(buff, 0, fileLen);
            }
        } catch (IOException e) {
            LOGGER.error("copy file error", e);
        }
    }

    /**
     * 
     * moveFile
     * 
     * @param srcFile
     * @param destPath
     */
    public static boolean moveFile(File srcFile, String destPath) {

        boolean flagSuccess;
        File dir = new File(destPath);
        File destFile = new File(dir, srcFile.getName());
        boolean existsFile = destFile.exists();
        // 如果备份文件已经存在，则删除备份文件，重新备份
        if (existsFile && destFile.delete()) {
            LOGGER.debug("delete file successed,fileName={}", destFile.getName());
        }
        flagSuccess = srcFile.renameTo(destFile);

        return flagSuccess;
    }

}
