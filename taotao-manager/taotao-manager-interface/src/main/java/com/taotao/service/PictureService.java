package com.taotao.service;

import com.taotao.result.PictureResult;

public interface PictureService {
    /**
     * 上传图片方法
     * @param bytes 图片的byte数组 表示图片
     * @param name 图片的名称
     * @return PictureResult对象里面有三个属性
     *                                      error(0 表示成功，1 表示失败)
     *                                      url(上传图片的地址  用作web程序员来显示图片)
     *                                      message (200或者500、404)发给web程序员让他根据状态码来判断是否上传成功
     */
    PictureResult uploadFile(byte[] bytes, String name);
}
