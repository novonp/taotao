package com.taotao.order.interceptor;

import com.taotao.pojo.TbUser;
import com.taotao.result.TaotaoResult;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Value("${TT_TOKEN}")
    private String TT_TOKEN;

    @Value("${SSO_LOGIN_URL}")
    private String SSO_LOGIN_URL;
    /**
     *执行controller之前调用 （判断用户是否登录逻辑） return :放行   false :拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        /**
         * 从cookie中取token(json格式用户信息不包含password)
         *          取到：
         *              用token去请求sso单点登录系统，根据token查询用户是否存在
         *                  找到用户：
         *                      直接显示用户并放行
         *                   找不到用户：
         *                      redis中过期了(重新登录)
         *          娶不到：
         *              跳转到登录页面  (把当前url发过去) 回调  在那个页面登录就返回到那个页面
         */

        String cookieValue = CookieUtils.getCookieValue(request, TT_TOKEN, true);
        //判断是空的意思  token为null  就为true
        if (StringUtils.isBlank(cookieValue)){
            //取当前地址的url
            String url = request.getRequestURL().toString();
            //没有token就跳转到登录页面取
            response.sendRedirect(SSO_LOGIN_URL + "?url=" + url);
            //拦截
            return false;
        }

        //判断TaotaoResult的data数据里面是否为false
        TaotaoResult result = userService.getUserByToken(cookieValue);
        if (result.getStatus() != 200) {
            //http://localhost:8091/order/order-cart.html
            String url = request.getRequestURL().toString();
            //http://localhost:8088/page/login
            response.sendRedirect(SSO_LOGIN_URL + "?url=" + url);
            //拦截
            return false;
        }

        //因为走到这里 就表示 cookie里面有token token没有过期 那么 result自然是status为200 msg为ok data为用户的json
        TbUser user = (TbUser) result.getData();
        //在吧这个东西存入域里面去 那么订单任何地方想要 获取 都可以找到
        request.setAttribute("user",user);

        //走到了这里 就是 浏览器上面有cookie根据cookie里面取到了 token，然后根据token查询redis 得到了数据

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
