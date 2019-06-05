package com.taotao.content.service.impl;

import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.result.EasyUIResult;
import com.taotao.result.JsonUtils;
import com.taotao.result.TaotaoResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    //缓存
    @Autowired
    private JedisClient jedisClient;
    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;

    @Value("${Expiry_TIME}")
    private Integer Expiry_TIME;

    @Autowired
    private TbContentMapper tbContentMapper;

    @Override
    public EasyUIResult findContentAll(long categoryId) {
        List<TbContent> contents = tbContentMapper.findContentByCategoryId(categoryId);
        EasyUIResult result = new EasyUIResult(contents.size(),contents);
        return result;
    }

    @Override
    public TaotaoResult addContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        tbContentMapper.insertContent(tbContent);
        //缓存同步
        jedisClient.hdel(CONTENT_KEY,tbContent.getCategoryId().toString());
        String hget = jedisClient.hget(CONTENT_KEY, tbContent.getCategoryId().toString());
        System.out.println(hget);
        //缓存过期时间
        jedisClient.expire(CONTENT_KEY,Expiry_TIME);
        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> findContentByCategoryId(long categoryId) {
        /**
         * 首先判断redis中是否有数据 有则从redis中获取，而且return返回
         * 如果没有 代码则往下继续执行  查询sql
         */
        String json = jedisClient.hget(CONTENT_KEY,categoryId+"");
        //缓存过期时间
        jedisClient.expire(CONTENT_KEY,Expiry_TIME);
        System.out.println(json);
        //isnoneblank
        if (StringUtils.isNotBlank(json)){
            List<TbContent> tbContent =  JsonUtils.jsonToList(json,TbContent.class);
            System.out.println("从缓存中拿到的数据");

            return tbContent;
        }
        List<TbContent> result = tbContentMapper.findContentByCategoryId(categoryId);
        System.out.println("从数据库中拿到的数据");
        /**
         * 存入一个散列数据到redis中
         * key叫做CONTENT_KEY
         * value是一个map集合
         *          map集合为 分类id，value为：内容集合json格式String字符串
         */
        /*jedisClient.hset(CONTENT_KEY,categoryId+"",JsonUtils.objectToJson(result));*/
        jedisClient.hset(CONTENT_KEY,categoryId+"",JsonUtils.objectToJson(result));
        /**
         * 把从数据库中得到的数据存放在redis中 return返回
          */
        //缓存过期时间
        jedisClient.expire(CONTENT_KEY,Expiry_TIME);
    return result;
    }
}
