package com.taotao.search.service;

import com.taotao.result.SearchItem;
import com.taotao.result.TaotaoResult;


public interface SearchItemService {
    TaotaoResult importAllItems() throws Exception;

    SearchItem findItemById(Long id);
}
