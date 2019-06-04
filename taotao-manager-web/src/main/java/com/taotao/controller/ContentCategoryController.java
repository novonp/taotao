package com.taotao.controller;

import com.taotao.content.service.ContentCategoryService;
import com.taotao.result.EasyUITreeNode;
import com.taotao.result.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/list")
    @ResponseBody
    public List<EasyUITreeNode> setContentCatList(@RequestParam(value = "id",defaultValue = "0")long parentId){
        List<EasyUITreeNode> result = contentCategoryService.getContentCategoryList(parentId);
        return result;
    }

    @RequestMapping("/create")
    @ResponseBody
    public TaotaoResult createCategory(long parentId,String name){
        TaotaoResult result = contentCategoryService.addContentCategory(parentId, name);
        return result;
    }
}
