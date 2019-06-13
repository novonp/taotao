package com.taotao.order.service;

import com.taotao.order.pojo.OrderInfo;
import com.taotao.result.TaotaoResult;

public interface OrderService {
    /**
     * 生成订单，订单明细，地址  (订单id用redis的incr方法生成，订单明细里面要去关联订单表和商品id，地址要去关联订单id)
     * @param orderInfo 订单信息，订单明细，地址信息
     * @return
     */
    TaotaoResult createOrder(OrderInfo orderInfo);
}
