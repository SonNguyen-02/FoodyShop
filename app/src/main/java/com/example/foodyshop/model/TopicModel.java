package com.example.foodyshop.model;

import static com.example.foodyshop.config.Config.IMG_TOPIC_DIR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class TopicModel implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("categories")
    @Expose
    private List<CategoryModel> categories = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return String.valueOf(name.charAt(0)).toUpperCase().concat(name.substring(1).toLowerCase());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return IMG_TOPIC_DIR + img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }

}
