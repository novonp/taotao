package com.taotao.mapper;


import com.taotao.pojo.TbContentCategory;

import java.util.List;

public interface TbContentCategoryMapper {

    /**
     * 根据内容分类id查询所有分类信息
     * @param parentId 内容分类id
     * @return 返回所有指定内容分类id下的所有分类信息
     */
    List<TbContentCategory> findTbContentCategoryById(long parentId);

    /**
     * 添加一个分类到数据库中
     * @param tbContentCategory 分类对象
     */
    void insert(TbContentCategory tbContentCategory);

    /**
     * 根据父节点查询当前内容分类
     * @param parentId 父级id
     * @return 返回该父节点指定内容分类信息
     */
    TbContentCategory findContentCategoryByParentId(long parentId);

    /**
     * 需改内容的分类信息
     * @param tbContentCategory 需要修改的内容分类
     */
    void updateCategoryisparentId(TbContentCategory tbContentCategory);

}