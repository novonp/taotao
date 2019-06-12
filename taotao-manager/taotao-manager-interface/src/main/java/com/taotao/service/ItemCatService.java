package com.taotao.service;

import com.taotao.result.EasyUITreeNode;
import com.taotao.result.ItemCatResult;

import java.util.List;

public interface ItemCatService {

    /**
     * 根据父级分类id查询分类的信息
     * @param parentId 分级分类id
     * @return EasyUITreeNode对象里面有三个参数(id,name,state("closed","open"))
     */
    List<EasyUITreeNode> getCatList(long parentId);

    ItemCatResult findItemCatAll(long parentId);
}
