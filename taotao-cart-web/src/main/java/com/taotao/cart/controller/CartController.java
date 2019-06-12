package com.taotao.cart.controller;

import com.taotao.pojo.TbItem;
import com.taotao.result.JsonUtils;
import com.taotao.result.TaotaoResult;
import com.taotao.service.ItemService;
import com.taotao.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ItemService itemService;

    @Value("${TT_CART}")
    private String TT_CART;

    @Value("${CART_EXPIRE}")
    private Integer CART_EXPIRE;


    @RequestMapping("/add/{itemId}")
    public String addCartItem(@PathVariable Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
        //cookie的key定义为 TT_CART   value定义为:json格式的商品信息
        List<TbItem> cartList = getCartList(request);
        boolean falg = false;

        for (TbItem tbItem: cartList) {
            //判断页面传递过来的itemId是否包含在集合里面，如果有则在原来的基础上 + 1 ， 如果没有则 + 1
            if (tbItem.getId()==itemId){
                //如果有则在原来的基础上 + 1 ， 如果没有则 + 1
                tbItem.setNum(tbItem.getNum()+num);

                falg = true;
            }
        }

        //如果 falg = false则表示新添加在购物车的商品在cookie购物车里找不到
        if (!falg){
            //使用商品id查询商品信息，把商品信息变成json格式 存入到cookie里面
            TbItem item = itemService.findTbItemById(itemId);
            //设置商品数量
            item.setNum(num);
            //设置商品在购物车里面显示图片  只显示1张
            String image = item.getImage();
            if (StringUtils.isNotBlank(image)){
                String[] split = image.split(",");
                //根据下标来取  显示第一张图片
                item.setImage(split[0]);
            }
            //如果新添加的商品在购物车里面找不到 ，这个商品就要jiarucookie中
            //这个集合可以是空集和也可以是商品信息集合
            cartList.add(item);
        }
        //代码走到这里说明cookie里面一定有商品  ， 要么在原来基础上 + 1， 要么直 + 1          注释：调用工具类cookieutils方法 具体可以去工具类里面看common
        CookieUtils.setCookie(request,response,TT_CART,JsonUtils.objectToJson(cartList),CART_EXPIRE,true);




        return "cartSuccess";
    }

    //根据cookie的key取cookie的value(json格式下的商品信息)
    private List<TbItem> getCartList(HttpServletRequest request){
        //调用cookie的工具类 根据key取value 默认编码格式为utf-8
        String json = CookieUtils.getCookieValue(request, TT_CART, true);
        //判断集合对象是否为null
        if (StringUtils.isNotBlank(json)){
            //调用jsonutils方法把json字符串变成tbitem集合对象
            List<TbItem> tbItems = JsonUtils.jsonToList(json, TbItem.class);

            return tbItems;
        }

        return new ArrayList<TbItem>();
    }

    //购物车页面
    @RequestMapping("/cart")
    public String showCartList(HttpServletRequest request, Model model){
        //代码走到这里说明cookie里面一定有商品信息
        List<TbItem> cartList = getCartList(request);
        /*model.addAttribute("carts",carts);*/
        model.addAttribute("cartList",cartList);

        return "cart";
    }

    /**
     * 购物车页面商品数量  + - 数量自己写入
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateNum(@PathVariable Long itemId,@PathVariable Integer num,HttpServletRequest request,HttpServletResponse response){
        //如果能够点击 + 和 - 说明cookie里面一定有商品
        List<TbItem> cartList = getCartList(request);
        for (TbItem tbItem:cartList) {
            if (tbItem.getId()==itemId.longValue()){
                //把页面改变的num设置到商品里面取
                tbItem.setNum(num);
                break;
            }
        }
        //把商品改变的数据加入到cookie里面
        CookieUtils.setCookie(request,response,TT_CART,JsonUtils.objectToJson(cartList),CART_EXPIRE,true);

        return TaotaoResult.ok();
    }

    /**
     * 删除购物车商品
     * 删除cookie里面的数据：因为cookie里面的数据是json格式的，所有我们必需把它转换成集合对象，for循环遍历这个集合对象，根据id去匹配，然后delete
     */
    @RequestMapping("/delete/{itemId}")
    public String deleteCatItemId(@PathVariable Long itemId,HttpServletResponse response,HttpServletRequest request){
        //取到cookie里面商品集合对象
        List<TbItem> cartList = getCartList(request);
        //遍历集合对象
        for (int i = 0; i < cartList.size(); i++) {
            //匹配id
            if (cartList.get(i).getId()==itemId.longValue()){
                //根据下标删除指定商品信息
                cartList.remove(i);
            }
        }
        //再把商品改变的数据加入到cookie里面
        CookieUtils.setCookie(request,response,TT_CART,JsonUtils.objectToJson(cartList),CART_EXPIRE,true);

        //重定向到cart方法里面去
        return "redirect:/cart/cart.html";
    }
}
