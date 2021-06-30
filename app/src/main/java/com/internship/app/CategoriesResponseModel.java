package com.internship.app;

public class CategoriesResponseModel {
    String cat_name, cat_images, sub_cat_name,sub_cat_images;
    int cat_id, sub_cat_id;

    public CategoriesResponseModel(String cat_name , String cat_images , String sub_cat_name , String sub_cat_images , int cat_id , int sub_cat_id) {
        this.cat_name = cat_name;
        this.cat_images = cat_images;
        this.sub_cat_name = sub_cat_name;
        this.sub_cat_images = sub_cat_images;
        this.cat_id = cat_id;
        this.sub_cat_id = sub_cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_images() {
        return cat_images;
    }

    public void setCat_images(String cat_images) {
        this.cat_images = cat_images;
    }

    public String getSub_cat_name() {
        return sub_cat_name;
    }

    public void setSub_cat_name(String sub_cat_name) {
        this.sub_cat_name = sub_cat_name;
    }

    public String getSub_cat_images() {
        return sub_cat_images;
    }

    public void setSub_cat_images(String sub_cat_images) {
        this.sub_cat_images = sub_cat_images;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(int sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }
}
