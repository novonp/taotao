package com.taotao.service;

import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.result.EasyUIResult;
import com.taotao.result.TaotaoResult;

public interface ItemService {

	/**
	 * 根据指定id查询指定商品信息。(第一次查询从数据库里查看,第二次从缓存里面拿数据)
	 * @param itemId 商品id
	 * @return 返回指定商品id的商品信息
	 */
	TbItem findTbItemById(Long itemId);

	/**
	 * 查询页面所有记录条数
	 * @param page 当前页面
	 * @param rows 页面的总记录条数
	 * @return EasyUIResult里面有total(总记录条数)和rows(每一页现实的记录条数)
	 */
	 EasyUIResult getItemList(int page,int rows);

	/**
	 * 添加一个商品信息到数据库中(商品基本数据类型对象，商品描述信息类型对象,商品规格类型对象)
	 * @param tbItem 商品基本数据类型对象
	 * @param desc 商品描述信息类型对象
	 * @param itemParams 商品规格类型对象 json
	 * @return TaotaoResult对象(里面有四个属性MAPPER jackson对象、status状态码、msg响应消息、data数据)
	 */
	 TaotaoResult addItem(TbItem tbItem,String desc,String itemParams);

	/**
	 * 根据商品id查询商品的描述信息
	 * @param itemId 商品id
	 * @return 指定商品id下的描述信息
	 */
	 TbItemDesc findItemDescByItemId(Long itemId);

	/**
	 * 根据商品id查询商品的规格参数 json
	 * @param itemId
	 * @return 指定商品的规格参数信息
	 */
    String findItemParamByItemId(Long itemId);

	/**
	 * 查询规格参数表的所有记录
	 * @param page 当前页面
	 * @param rows 总记录条数
	 * @return EasyUIResult里面有total(总记录条数)和rows(每一页现实的记录条数)
	 */
    EasyUIResult getItemParamList(Integer page, Integer rows);
}
