package com.taotao.service.impl;

import com.taotao.result.PictureResult;
import com.taotao.service.PictureService;
import com.taotao.utils.FtpUtil;
import com.taotao.utils.IDUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class PictureServiceImpl implements PictureService {
    /**
     * 这里的意思是说  通过${FTP_ADDRESS}去取配置文件resource.properties的值，再把取到的值赋值给private String FTP_ADDRESS;
     */
    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;
    @Value("${FTP_PORT}")
    private Integer FTP_PORT;
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;
    @Value("${FILI_UPLOAD_PATH}")
    private String FILI_UPLOAD_PATH;
    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;

    @Override
    public PictureResult uploadFile(byte[] bytes, String name) {
        /**
         * 1.调用上传图片的工具类 (用于图片上传)
         * 2.把byte数组变回图片
         *      因为FTPClient.storeFile()他需要一个inputSteam对象，把byte数组变成inputsteam
         * 3.需要修改图片名称不然传同一张图片会被覆盖
         */

        PictureResult result = new PictureResult();
        try {
            //把byte数组变成inputsteam的子类    ctrl + h 查看继承树
            ByteArrayInputStream pis = new ByteArrayInputStream(bytes);
            //name表示图片名称 得到图片的  后缀名。通过id生成器(当前时间 + 三位随机数 生成的).后缀为新的名字
            String newName = IDUtils.genImageName() + name.substring(name.lastIndexOf("."));
            //当前年月日生成的字符串
            String filePath = new DateTime().toString("yyyy/MM/dd");
            FtpUtil.uploadFile(FTP_ADDRESS,FTP_PORT,FTP_USERNAME,FTP_PASSWORD,FILI_UPLOAD_PATH,filePath,newName,pis);
            result.setError(0);
            //http://192.168.89.128/images/20190522/***.jpg
            result.setUrl(IMAGE_BASE_URL+"/"+filePath+"/"+newName);
            result.setMessage("上传成功");
        }catch (Exception ex){
            result.setError(1);
            result.setMessage("上传失败");
        }

        return result;
    }

    /**返回json数据格式
     * //成功时
     * {
     *         "error" : 0,
     *         "url" : "http://www.example.com/path/to/file.ext"
     * }
     * //失败时
     * {
     *         "error" : 1,
     *         "message" : "错误信息"
     * }
     */
}
