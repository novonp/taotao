package com.taotao.search.service.dao;

import com.taotao.result.SearchItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SearchItemMapper {
    /**
     * 查询数据库得到所有的商品信息，并且把它转换成lucene结构
     * @return
     */
    @Select("SELECT a.id,a.title,a.price,a.image,a.sellPoint,b.name categoryName, c.itemDesc FROM tbitem a LEFT JOIN tbitemcat b ON a.cid=b.id LEFT JOIN tbitemdesc c ON a.id=c.itemId where a.status=1")
    List<SearchItem> getItemList();

    /*@Select("SELECT a.id,a.title,a.price,a.image,a.sellPoint,b.name categoryName, c.itemDesc FROM tbitem a LEFT JOIN tbitemcat b ON a.cid=b.id LEFT JOIN tbitemdesc c ON a.id=c.itemId where a.status=1 where a.id=#{a.id}")*/
    @Select("SELECT\n" +
            "  a.id,\n" +
            "  a.title,\n" +
            "  a.sellPoint,\n" +
            "  a.price,\n" +
            "  a.image,\n" +
            "  b.name categoryName,\n" +
            "  c.itemDesc\n" +
            "FROM tbitem a INNER JOIN tbitemcat b ON a.cid = b.id\n" +
            "  INNER JOIN tbitemdesc c ON a.id = c.itemId\n" +
            "WHERE a.id = #{a.id};")
    SearchItem getItemById(Long id);

}
