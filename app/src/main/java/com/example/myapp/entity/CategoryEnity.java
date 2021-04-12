package com.example.myapp.entity;

import java.io.Serializable;

/**
 * @ClassName CategoryEnity
 * @Description TODO
 * @Author 29481
 * @Date 2021/3/16 18:07
 * @Version 1.0
 */
public class CategoryEnity implements Serializable {
    /**
     * categoryId : 1
     * categoryName : 游戏
     */
    private int categoryId;
    private String categoryName;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
