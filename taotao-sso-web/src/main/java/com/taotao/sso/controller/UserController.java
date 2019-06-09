package com.taotao.sso.controller;

import com.taotao.pojo.TbUser;
import com.taotao.result.TaotaoResult;
import com.taotao.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkData(@PathVariable String param,@PathVariable Integer type){
        TaotaoResult result = userService.checkData(param, type);
        return result;
    }

    //新增
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser tbUser){
        TaotaoResult result = userService.createUser(tbUser);
        return result;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String userName,String passWord){
        TaotaoResult result = userService.loginUser(userName, passWord);
        return result;
    }

    @RequestMapping(value = "/token/{token}",method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult getUserByToken(@PathVariable String token){
        TaotaoResult result = userService.getUserByToken(token);
        return result;
    }

    @RequestMapping("/logout/{token}")
    @ResponseBody
    public TaotaoResult logoutUser(@PathVariable String token){
        TaotaoResult result = userService.loginOutUser(token);
        return result;
    }

}
