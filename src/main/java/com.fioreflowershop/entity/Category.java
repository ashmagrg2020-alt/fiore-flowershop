package com.fioreflowershop.model;

/**
 * Category - Model class representing a product category.
 * Maps to the 'categories' table in the database.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class Category {

    private int    categoryId;
    private String categoryName;
    private String description;

    public Category() {}

    public Category(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description  = description;
    }

    public int getCategoryId(){
        return categoryId;
    }
    public void setCategoryId(int categoryId){
        this.categoryId = categoryId;
    }

    public String getCategoryName(){
        return categoryName;
    }
    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{id=" + categoryId + ", name='" + categoryName + "'}";
    }
}