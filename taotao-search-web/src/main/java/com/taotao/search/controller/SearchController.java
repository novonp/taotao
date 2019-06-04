package com.taotao.search.controller;

import com.taotao.result.SearchResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SearchController {

    @Value("${ITEM_ROWS}")
    private Integer ITEM_ROWS;

    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String search(@RequestParam String q, @RequestParam(defaultValue = "1")int page, Model model) throws Exception {
        //这里的请求是get请求 所以不能解决post请求的中文乱码问题
        byte[] bytes = q.getBytes("ISO-8859-1");
        String queryString = new String(bytes, "UTF-8");

        SearchResult result = searchService.search(queryString, page, ITEM_ROWS);
        //传递给页面
        model.addAttribute("query", queryString);
        model.addAttribute("totalPages", result.getPageCount());
        model.addAttribute("itemList", result.getItemList());
        model.addAttribute("page", page);
        return "search";
    }
}
