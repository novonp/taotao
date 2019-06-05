package com.taotao.controller;

import com.taotao.pojo.TbItemParam;
import com.taotao.result.TaotaoResult;
import com.taotao.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/item/param")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    @RequestMapping("/query/itemcatid/{itemCatId}")
    @ResponseBody
    public TaotaoResult getItemParamByCid(@PathVariable Long itemCatId){
        TaotaoResult result = itemParamService.getItemParamByCid(itemCatId);
        return result;
    }

    //添加模板
    @RequestMapping("/save/{cid}")
    //item-param-add.jsp  97行     Long cid,String paramData :传递参数
    public TaotaoResult insertItemParam(@PathVariable Long cid,String paramData){
        TbItemParam tbItemParam = new TbItemParam();
        tbItemParam.setItemCatId(cid);
        tbItemParam.setParamData(paramData);
        TaotaoResult result = itemParamService.addItemParam(tbItemParam);
        return result;
    }
}
