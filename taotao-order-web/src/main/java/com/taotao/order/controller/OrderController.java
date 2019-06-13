package com.taotao.order.controller;

import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.result.JsonUtils;
import com.taotao.result.TaotaoResult;
import com.taotao.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Value("${TT_CART}")
    private String TT_CART;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/order-cart")
    public String showOrderCat(HttpServletRequest request){
        //根据cookie里面的 key 取 value   这个集合不可能为空
        List<TbItem> cartList = getCartList(request);
        //传递给页面
        request.setAttribute("cartList",cartList);
        //返回逻辑视图
        return "order-cart";
    }
    //(封装的一个方法) 根据cookie的 key 取 value
    private List<TbItem> getCartList(HttpServletRequest request) {
        //取购物车的列表
        String cookieValue = CookieUtils.getCookieValue(request, TT_CART, true);
        //判断cookieValue是否为null
        if (StringUtils.isNotBlank(cookieValue)){
            //把json转换成商品列表返回
            List<TbItem> tbItems = JsonUtils.jsonToList(cookieValue, TbItem.class);

            return tbItems;
        }
        return new ArrayList<>();
    }


    @RequestMapping("/create")
    @ResponseBody
    public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
        TbUser user = (TbUser) request.getAttribute("user");
        //接收表单提交的数据orederinfo
        //补全用户信息
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUserName());
        //调用service创建订单
        TaotaoResult result = orderService.createOrder(orderInfo);
        //发送数据给页面，显示添加信息成功
        //取消订单
        String orderId = result.getData().toString();
        //需要service返回订单号
        request.setAttribute("orderId",orderId);
        request.setAttribute("payment",orderInfo.getPayment());
        //当前日期加三天
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(3);
        request.setAttribute("date",dateTime.toString("yyyy-MM-dd"));

        //传递给页面展示是否添加成功
        return "success";
    }

}
