package com.taotao.controller;

import com.taotao.result.TaotaoResult;
import com.taotao.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchItemController {
    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/index/importall")
    @ResponseBody
    public TaotaoResult importAllItems() throws Exception {
        TaotaoResult result = searchItemService.importAllItems();
        return result;
    }
}
