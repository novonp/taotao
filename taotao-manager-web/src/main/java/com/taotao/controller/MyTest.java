package com.taotao.controller;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 文件上传
 */
public class MyTest {
    @Test
    public void show() throws IOException {
        //创建ftp客户端对象
        FTPClient client = new FTPClient();
        //ftp客户端连接ftp服务器
        client.connect("192.168.89.128");
        //登录
        client.login("ftpuser","ftpuser");
        //解决上传0KB问题
        client.enterLocalPassiveMode();
        //上传图片
            //1.上传的类型(为2进制   0  和  1)
            client.setFileType(FTP.BINARY_FILE_TYPE);
            //创建上传图片路径
        FileInputStream fis = new FileInputStream(new File("E:\\GET\\Git\\yby.jpg"));
            //2.需要一个流对象(上传后的路径)
        client.storeFile("/home/ftpuser/www/images /non.jpg", fis);
        //关闭ftp服务器
        client.logout();

    }
}
