/**
 * Copyright (c) 2017, China Mobile IOT All Rights Reserved.
 */
package util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


/**   
 * 此类描述的是：   
 * @author: wangjian  
 * @date: 2017年11月13日 下午2:21:54    
 */
public final class SFTPUtils {
    private static final Logger logger = LoggerFactory.getLogger(SFTPUtils.class);

    private Channel channel = null;
    private Session session = null;
    private ChannelSftp sftp = null;

    private String ftphost;
    private String ftpuser;
    private String ftppass;
    private int ftpport;

    public static final String FILE_SEPARATOR = "file.separator";

    /**
     * 
     * 创建一个新的实例 SFTPUtil.
     * 
     * @param ftphost
     * @param ftpuser
     * @param ftppass
     * @param ftpport
     */
    public SFTPUtils(String ftphost, String ftpuser, String ftppass, int ftpport) {
        this.ftphost = ftphost;
        this.ftpuser = ftpuser;
        this.ftppass = ftppass;
        this.ftpport = ftpport;
    }


    /**
     * 创建FTP连接信息
     *
     * @return
     * @throws Exception
     */
    public void connect() throws JSchException {
        try {
            JSch jsch = new JSch();
            this.session = jsch.getSession(this.ftpuser, this.ftphost, this.ftpport);
            session.setPassword(this.ftppass);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.connect();
            this.channel = session.openChannel("sftp");
            channel.connect();
            this.sftp = (ChannelSftp) channel;
            logger.info("connected to ftp server success");
        } catch (JSchException e) {
            logger.error("Connected failed. cause by error is ：", e);
            throw new JSchException("Connected failed. cause by error is :" + e.getMessage());
        }
    }

    /**
     * 关闭ftp
     */
    public void closeFtp() {
        try {

            if (channel.isConnected()) {
                channel.disconnect();
            }
            if (session.isConnected()) {
                session.disconnect();
            }
            if (this.sftp.isConnected()) {
                this.sftp.disconnect();
            }
        } catch (Exception e) {
            logger.error(" close ftp connection failed:", e);
        }
        logger.info(" close ftp connection success");
    }


    /**
     * 下载目录下全部文件
     *
     * @param directory 下载目录
     * @param saveDirectory 存在本地的路径
     * @throws Exception
     */
    public void downloadByDirectory(String directory, String saveDirectory) throws SftpException {
        String downloadFile;
        List<String> downloadFileList = listFiles(directory);
        Iterator<String> it = downloadFileList.iterator();

        while (it.hasNext()) {
            downloadFile = it.next();
            if (downloadFile.indexOf('.') < 0) {
                continue;
            }
            download(directory, downloadFile, saveDirectory);
        }
    }


    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return list 文件名列表
     * @throws Exception
     */
    public List<String> listFiles(String directory) throws SftpException {

        Vector<?> fileList;
        List<String> fileNameList = new ArrayList<>();

        fileList = this.sftp.ls(directory);
        Iterator<?> it = fileList.iterator();

        while (it.hasNext()) {
            String fileName = ((ChannelSftp.LsEntry) it.next()).getFilename();
            if (".".equals(fileName) || "..".equals(fileName)) {
                continue;
            }
            fileNameList.add(fileName);

        }

        return fileNameList;
    }

    /**
     * 下载指定文件
     *
     * @param dir 相对于ftp用户根目录的路径
     * @param downloadFile 需要下载的文件
     * @param saveFile 保存文件路径
     */
    public void download(String dir, String downloadFile, String saveFile) {
        try {
            boolean isCd = false;
            if (StringUtils.isNotBlank(dir)) {
                this.sftp.cd(dir);
                isCd = true;
            }
            this.sftp.get(downloadFile, saveFile);
            if (isCd) {
                this.sftp.cd("..");
            }
            logger.info(" file download success. file name :{}", downloadFile);
        } catch (Exception e) {
            logger.error(" file download failed. cause by error:", e);
        }
    }

    /**
     * 上传指定文件到服务上
     *
     * @param inputStream
     * @param fileName
     * @param remoteDir
     * @throws SftpException
     */
    public void upload(FileInputStream inputStream, String fileName, String remoteDir) throws SftpException {
        try {
            sftp.cd(remoteDir);
            this.sftp.put(inputStream, fileName);
            logger.info("upload file success, file: {}", fileName);
        } catch (SftpException e) {
            logger.error(fileName + " upload failed. cause by error:", e);
            throw new SftpException(ftpport, fileName + " upload failed, dir is:" + remoteDir, e);

        }
    }

    /**
     * /** 删除文件
     *
     * @param directory 要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) {
        try {
            this.sftp.cd(directory);
            this.sftp.rm(deleteFile);
            logger.info(" file delete success. file name :{}", deleteFile);
        } catch (Exception e) {
            logger.error(" file delete error. file name :{},caused by :{}", deleteFile, e);
        }
    }

    /**
     * 下载目录下指定文件
     *
     * @param directory 下载目录
     * @param saveDirectory 存在本地的路径
     * @throws Exception
     */
    public void downloadByFileName(String directory, String saveDirectory, String fileNamePre) throws SftpException {
        String downloadFile = "";
        List<String> downloadFileList = listFiles(directory);
        Iterator<String> it = downloadFileList.iterator();

        while (it.hasNext()) {
            downloadFile = it.next();
            if (downloadFile.indexOf('.') < 0 || !downloadFile.startsWith(fileNamePre)) {
                continue;
            }
            download(directory, downloadFile, saveDirectory);
        }
    }
}
