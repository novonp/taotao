package com.taotao.result;

import java.io.Serializable;

public class SearchItem implements Serializable {
    private String id;
    private String title;
    private String sell_point;
    private Long price;
    private String image;
    private String category_name;
    private String item_desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSell_point() {
        return sell_point;
    }

    public void setSell_point(String sell_point) {
        this.sell_point = sell_point;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    //用于页面显示图片，因为页面通过el表达式来取土图片信息
    //因为我们再添加图片的时候是添加的多张图片
    public String[] getImages(){
        if (image!=null&&!"".equals(image)){
            String[] strings = image.split(",");
            return strings;
        }
        return null;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    @Override
    public String toString() {
        return "SearchItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", sell_point='" + sell_point + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", category_name='" + category_name + '\'' +
                ", item_desc='" + item_desc + '\'' +
                '}';
    }
}
