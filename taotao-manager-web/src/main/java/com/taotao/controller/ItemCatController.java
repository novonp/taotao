package com.taotao.controller;

import com.taotao.result.EasyUITreeNode;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;
    @RequestMapping("/list")
    @ResponseBody
    //网页传递过来的参数 名字叫做id，默认有一个初始值叫做  0  ，然后把值赋值给 parentId
    //@RequestParam：传递参数
    public List<EasyUITreeNode> getItemCatList(@RequestParam(value = "id",defaultValue = "0")long parentId){
        List<EasyUITreeNode> catList = itemCatService.getCatList(parentId);
        return catList;
    }
}
