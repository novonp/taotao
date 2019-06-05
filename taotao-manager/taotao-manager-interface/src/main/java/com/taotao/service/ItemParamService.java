package com.taotao.service;

import com.taotao.pojo.TbItemParam;
import com.taotao.result.TaotaoResult;

public interface ItemParamService {
    /**
     * 根据商品分类id查询指定模板
     * @param itemCatId 商品分类id
     * @return
     */
    TaotaoResult getItemParamByCid(Long itemCatId);

    /**
     * 添加规格模板
     * @param tbItemParam
     * @return
     */
    TaotaoResult addItemParam(TbItemParam tbItemParam);
}
