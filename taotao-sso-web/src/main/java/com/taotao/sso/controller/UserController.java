package com.taotao.sso.controller;

import com.taotao.pojo.TbUser;
import com.taotao.result.JsonUtils;
import com.taotao.result.TaotaoResult;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${TT_TOKEN}")
    private String TT_TOKEN;

    @RequestMapping(value = "/check/{param}/{type}",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String checkData(@PathVariable String param,@PathVariable Integer type,String callback){
        TaotaoResult result = userService.checkData(param, type);
        //首页分类显示
        if (StringUtils.isNotBlank(callback)){
            //判断有没有callback这个参数，如果有就把json改为jsonp响应
            String jsonp = "callback("+ JsonUtils.objectToJson(result)+")";
            return jsonp;
        }
        return JsonUtils.objectToJson(result);
    }

    //注册
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser tbUser){
        TaotaoResult result = userService.createUser(tbUser);
        return result;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String userName, String passWord, HttpServletRequest request, HttpServletResponse response){
        TaotaoResult result = userService.loginUser(userName, passWord);

        //把用户的信息存进cookie里面
        //账号密码验证成功了才能把用户信息存入到cookie里面
        if (result.getStatus()==200){
            //result里面有三个参数  state  msg   data(token随机生成的字符串)
            //验证成功存入cookie
            CookieUtils.setCookie(request,response,TT_TOKEN,result.getData().toString());
        }
        return result;
    }

    @RequestMapping(value = "/token/{token}",produces = MediaType.APPLICATION_ATOM_XML_VALUE + ";charset=utf-8")
    @ResponseBody
    //校验用户是否登录， taotao   search   item  这里是跨域请求
    public String getUserByToken(@PathVariable String token, String callback){
        TaotaoResult result = userService.getUserByToken(token);

        //这里是跨域请求
        if (StringUtils.isNotBlank(callback)){
            //跨域请求jsonp  (就是一个js方法里面包含了json语句)
            String json = callback + "(" + JsonUtils.objectToJson(result) + ");";

            return json;
        }

        //这里是当前工程
        return JsonUtils.objectToJson(result);
    }

    @RequestMapping("/logout/{token}")
    @ResponseBody
    public TaotaoResult logoutUser(@PathVariable String token){
        TaotaoResult result = userService.loginOutUser(token);
        return result;
    }


}
