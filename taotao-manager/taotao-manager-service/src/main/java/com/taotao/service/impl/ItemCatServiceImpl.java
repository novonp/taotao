package com.taotao.service.impl;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.result.EasyUITreeNode;
import com.taotao.service.ItemCatService;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EasyUITreeNode> getCatList(long parentId) {
        List<TbItemCat> catList = tbItemCatMapper.findTbTtemCatParentId(parentId);
        List<EasyUITreeNode> result = new ArrayList<EasyUITreeNode>();
        for (TbItemCat itemCat:catList) {
            EasyUITreeNode node = new EasyUITreeNode();
            //设置id
            node.setId(itemCat.getId());
            //设置名称
            node.setText(itemCat.getName());
            //设置状态
            node.setState(itemCat.getIsParent()?"closed":"open");
            result.add(node);
        }
        return result;
    }
}
