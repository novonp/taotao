package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/")
    public String showIndex(){
        return "index";
    }

    //定义接口(请求参数：page=***)  请求地址必需为：http://localhost:8081?page=***   配置了前后缀在springmvc
    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page){
        return page;
    }
}
