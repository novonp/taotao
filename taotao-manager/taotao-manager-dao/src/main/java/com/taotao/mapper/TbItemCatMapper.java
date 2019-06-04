package com.taotao.mapper;

import java.util.List;

import com.taotao.pojo.TbItemCat;
import org.apache.ibatis.annotations.Select;

public interface TbItemCatMapper {

    /**
     * 根据分类id查询该分类
     * @param parentId 类目的父级id
     * @return 父级类目id的所有信息
     */
    List<TbItemCat> findTbTtemCatParentId(long parentId);
}