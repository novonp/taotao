package com.taotao.controller;

import com.taotao.result.EasyUIResult;
import com.taotao.result.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		TbItem tbItem = itemService.findTbItemById(itemId);
		return tbItem;
	}

	@RequestMapping("/list")
	@ResponseBody
	//返回EasyUIResult
	public EasyUIResult getItemList(Integer page,Integer rows){
		EasyUIResult list = itemService.getItemList(page, rows);
		return list;
	}

	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult saveItem(TbItem tbItem,String desc){
		TaotaoResult result = itemService.addItem(tbItem, desc);
		return result;
	}
}
