package com.taotao.order.service.impl;

import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.order.service.jedis.JedisClient;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.result.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;

    @Value("${ORDER_ID_BEGIN}")
    private String ORDER_ID_BEGIN;

    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {

        //判断

        if (jedisClient.exists(ORDER_GEN_KEY)){
            jedisClient.set(ORDER_GEN_KEY,ORDER_ID_BEGIN);
        }

        //通过redis的incr方法来生成我们的id (递增)
        String orderId = jedisClient.incr(ORDER_GEN_KEY).toString();
        //设置订单号
        orderInfo.setOrderId(orderId);
        //订单状态  未支付
        orderInfo.setStatus(1);
        //时间
        Date date = new Date();
        orderInfo.setCreateTime(date);
        orderInfo.setUpdateTime(date);

        //插入订单到订单表中
        orderMapper.insertOrder(orderInfo);

        //从OrderInfo对象里面得到订单明细集合对象
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem:orderItems){
            //通过redis生成订单id
            String orderItemId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY).toString();
            //设置订单号
            orderItem.setId(orderItemId);
            //设置订单详情表与订单表之间的关系
            orderItem.setOrderId(orderId);
            //插入订单详情信息
            tbOrderItemMapper.insertOrderItem(orderItem);
        }

        //从orderinfo对象里面得到地址对象
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        //建立订单与地址之间的关系
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);

        //插入地址
        tbOrderShippingMapper.insertOrderShipping(orderShipping);

        return TaotaoResult.ok(orderId);
    }
}
