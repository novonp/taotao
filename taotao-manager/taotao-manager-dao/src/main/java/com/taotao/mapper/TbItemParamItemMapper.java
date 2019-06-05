package com.taotao.mapper;


import com.taotao.pojo.TbItemParamItem;

public interface TbItemParamItemMapper {

    /**
     * 插入商品规格参数到数据库中
     * @param tbItemParamItem
     */
    void insert(TbItemParamItem tbItemParamItem);

    /**
     * 根据商品id查询数据库商品规格参数表
     * @param itemId 商品id
     * @return 返回指定规格参数对象
     */
    TbItemParamItem findItemParamByItemId(Long itemId);
}