package com.taotao.mapper;


import com.taotao.pojo.TbContent;

import java.util.List;

public interface TbContentMapper {

    /**
     * 根据内容分类id查询所有分类信息
     * @param categoryId 内容分类id
     * @return 指定内容分类id下的所有分类信息
     */
    List<TbContent> findContentByCategoryId(long categoryId);

    void insertContent(TbContent tbContent);
 
}