package com.taotao.content.service;

import com.taotao.result.EasyUITreeNode;
import com.taotao.result.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {
    /**
     * 查询所有内容分类信息
     * @param parentId 父类id
     * @return EasyUITreeNode对象 里面有三个参数(id ，text ，state)
     */
    List<EasyUITreeNode> getContentCategoryList(long parentId);

    /**
     * 添加分类信息，需要自己组装id,status,sortOrder,判断isparent,create,update
     *
     * @param parentId 父类id
     * @param name 名称
     * @return 添加一个内容分类
     */
    TaotaoResult addContentCategory(long parentId,String name);
}
