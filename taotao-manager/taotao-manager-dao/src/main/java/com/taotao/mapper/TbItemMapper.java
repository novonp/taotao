package com.taotao.mapper;

import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import org.apache.ibatis.annotations.Select;

import java.security.acl.LastOwnerException;
import java.util.List;

public interface TbItemMapper {
	
	/**
	 * 根据商品id查询你指定商品信息
	 * @param itemId 商品id
	 * @return 返回指定商品id的商品信息
	 */
	TbItem findTbItemById(Long itemId);

	/**
	 * 查询所有的商品信息
	 * @return 返回所有商品的集合对象
	 */
	/*@Select("SELECT * FROM tbitem")*/
	//查询状态为1的
	@Select("SELECT * FROM tbitem WHERE status = 1")
	List<TbItem> findTbItems();

	/**
	 * 添加商品基本信息
	 * @param tbItem 商品基本信息
	 */
    void insert(TbItem tbItem);

	/**
	 * 根据商品id查询数据库中商品描述信息
	 * @param itemId 商品id
	 * @return 指定id下的商品描述信息
	 */
	TbItemDesc findTbItemDescByItemId(Long itemId);

	/**
	 * 查询所有规格信息
	 * @return
	 */
	@Select("SELECT * FROM tbitemparamitem")
	List<TbItemParamItem> findTbItemParam();
}