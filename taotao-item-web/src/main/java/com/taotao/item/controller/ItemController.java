package com.taotao.item.controller;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/item")
public class ItemController {
//详情页面
    @Autowired
    private ItemService itemService;
//基本信息
    @RequestMapping("/{itemId}")
    public String showItem(@PathVariable Long itemId, Model model){
        //Tbitem对象没有getImages方法
        TbItem tbItem = itemService.findTbItemById(itemId);
        Item item = new Item(tbItem);
        model.addAttribute("item",item);
        return "item";
    }
//描述信息
    @RequestMapping("/desc/{itemId}")
    @ResponseBody //如果返回一个对象，则通过jacson的jar包直接把对象变成json格式的数据，如果是一个字符串，则把数据输出到http的body里面去
    public String showItemDesc(@PathVariable Long itemId){
        TbItemDesc itemDesc = itemService.findItemDescByItemId(itemId);
        return itemDesc.getItemDesc();
    }
//规格参数
    @RequestMapping("/param/{itemId}")
    @ResponseBody
    public String showItemParam(@PathVariable Long itemId){
        String itemParamByItemId = itemService.findItemParamByItemId(itemId);
        return itemParamByItemId;
}

}
