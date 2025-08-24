package com.CeyBazaar.backend.dto;

import com.CeyBazaar.backend.entity.Product;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class ProductCatDTO {

    private int id;
    private String categoryName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
