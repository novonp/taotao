package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.pojo.TbItemDesc;
import com.taotao.result.EasyUIResult;
import com.taotao.result.JsonUtils;
import com.taotao.result.TaotaoResult;
import com.taotao.service.jedis.JedisClient;
import com.taotao.utils.IDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;


import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

	@Autowired
	private TbItemMapper tbItemMapper;

	@Autowired
	private TbItemDescMapper tbItemDescMapper;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Destination topicDestination;

	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	@Value("${BASE}")
	private String BASE;
	@Value("${Expiry_TIME}")
	private Integer Expiry_TIME;
	@Value("${DESC}")
	private String DESC;
	/**
	 * 缓存
	 * @param itemId 商品id
	 * @return
	 */
	@Override
	public TbItem findTbItemById(Long itemId) {
		/**
		 * 首先判断redis中是否有数据 有则从redis中获取，并且return返回
		 * 如果没有 代码则往下继续执行  查询sql
		 */
		String json = jedisClient.get(ITEM_INFO + ":" + itemId + BASE);
		if (StringUtils.isNotBlank(json)){
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			System.out.println("从缓存中获取商品基本信息");
			//每次加载都需要过期时间
			jedisClient.expire("ITEM_INFO+\":\"+itemId+DESC",Expiry_TIME);
			return tbItem;
		}



		TbItem tbItem = tbItemMapper.findTbItemById(itemId);
		System.out.println("从数据库中获取商品基本信息");
		//加入缓存(把商品信息存入redis中)  (根据key (ITEM_INFO+":"+itemId+BASE) 取value ) JsonUtils.objectToJson(tbItem)  json格式
		jedisClient.set(ITEM_INFO+":"+itemId+BASE, JsonUtils.objectToJson(tbItem));
		//设置缓存过期时间
		jedisClient.expire("ITEM_INFO+\":\"+itemId+BASE",Expiry_TIME);
		return tbItem;
	}

	@Override
	public EasyUIResult getItemList(int page, int rows) {
		//使用分页插件，设置我们的分页信息
		PageHelper.startPage(page,rows);

		List<TbItem> items = tbItemMapper.findTbItems();
		//获取分页信息
		//pageInfo表示:从所有商品集合里面分出**个集合对象(是根据当前页分出来的)
		PageInfo<TbItem> pageInfo = new PageInfo<>(items);

		//这里的意思是说 只要使用了pageInfo去关联了我们的商品集合对象以后，这个集合对象就会变成**(页面显示记录条数)个商品集合对象了
		//EasyUIResult他是一个工具类里面有两个参数total(总记录条数)   rows(每一页显示的记录条数)
		EasyUIResult result = new EasyUIResult(pageInfo.getTotal(),items);

		return result;
	}

	@Override
	public TaotaoResult addItem(TbItem tbItem, String desc) {
		//生成商品id
		final long id = IDUtils.genItemId();
		Date time = new Date();
		//设置id
		tbItem.setId(id);
		//设置商品创建的时间
		tbItem.setCreated(time);
		//设置商品修改的时间
		tbItem.setUpdated(time);
		//设置状态码(1:代表正常状态)
		tbItem.setStatus((byte) 1);

		//调用商品dao的基本信息
		tbItemMapper.insert(tbItem);
		//调用商品描述信息
		TbItemDesc itemDesc = new TbItemDesc();
		//描述信息中的id
		itemDesc.setItemId(id);
		itemDesc.setCreated(time);
		itemDesc.setUpdated(time);
		itemDesc.setItemDesc(desc);
		tbItemDescMapper.insert(itemDesc);


		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				//创建字符串型的消息对象
				TextMessage textMessage = session.createTextMessage();
				textMessage.setText(id+"");
				return textMessage;
			}
		});

		/**
		 * //因为这儿传入的参数只有一个，所以说只能调用一个参数的构造方法p54行
		 *     //调用这个方法状态吗为 200  消息为 ok 数据是传什么就是什么
		 *public static TaotaoResult ok() {
		 *   return new TaotaoResult(null);
		 *}
		 * public TaotaoResult(Object data) {
		 *         this.status = 200;
		 *         this.msg = "OK";
		 *         this.data = data;
		 * }
		 */
		return TaotaoResult.ok();
	}
	//根据商品id查询商品描述信息
	@Override
	public TbItemDesc findItemDescByItemId(Long itemId) {
		/**
		 * 首先判断redis中是否有数据 有则从redis中获取，并且return返回
		 * 如果没有 代码则往下继续执行  查询sql
		 */
		String json = jedisClient.get(ITEM_INFO + ":" + itemId + DESC);
		if (StringUtils.isNotBlank(json)){
			TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
			//每次加载都需要过期时间
			jedisClient.expire("ITEM_INFO+\":\"+itemId+DESC",Expiry_TIME);
			return tbItemDesc;
		}


		TbItemDesc itemDesc = tbItemMapper.findTbItemDescByItemId(itemId);
		//跟上面一样 75
		//加入缓存(把商品信息存入redis中)  (根据key (ITEM_INFO+":"+itemId+BASE) 取value ) JsonUtils.objectToJson(tbItem)  json格式
		jedisClient.set(ITEM_INFO+":"+itemId+DESC,JsonUtils.objectToJson(itemDesc));
		//设置缓存过期时间
		jedisClient.expire("ITEM_INFO+\":\"+itemId+DESC",Expiry_TIME);

		return itemDesc;
	}

}
