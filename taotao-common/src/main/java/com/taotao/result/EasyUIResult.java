package com.taotao.result;

import java.io.Serializable;
import java.util.List;

/**
 * easyUIDataGrid对象返回值
 * Serializable:标记接口
 * java对象想要在网络上传输必须要实现Serializable接口
 */
public class EasyUIResult implements Serializable {

	private Integer total;
	//   ? 代表任意  在java里面 ? 代表object
	private List<?> rows;
	
	public EasyUIResult(Integer total, List<?> rows) {
		this.total = total;
		this.rows = rows;
	}
	
	public EasyUIResult(long total, List<?> rows) {
		this.total = (int) total;
		this.rows = rows;
	}

    public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
	
}
