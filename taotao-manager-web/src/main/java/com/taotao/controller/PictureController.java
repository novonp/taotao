package com.taotao.controller;

import com.taotao.result.PictureResult;
import com.taotao.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/pic")
public class PictureController {
    @Autowired
    private PictureService pictureService;

    @RequestMapping("/upload")
    @ResponseBody
    public PictureResult uploda(MultipartFile uploadFile) throws IOException {
        //把图片对象变成byte数组二进制新式表现
        byte[] bytes = uploadFile.getBytes();
        //得到图片名称
        String name = uploadFile.getOriginalFilename();
        PictureResult result = pictureService.uploadFile(bytes, name);

        return result;
    }
}
