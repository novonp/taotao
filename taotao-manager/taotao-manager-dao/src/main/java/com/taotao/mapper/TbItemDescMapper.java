package com.taotao.mapper;

import com.taotao.pojo.TbItemDesc;

public interface TbItemDescMapper {

    /**
     *添加商品描述细信息到数据库中
     * @param itemDesc 商品描述细信息
     */
    void insert(TbItemDesc itemDesc);
}