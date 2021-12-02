package com.example.foodyshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("topic_id")
    @Expose
    private int topicId;
    @SerializedName("name")
    @Expose
    private String name;

    public CategoryModel() {
    }

    public CategoryModel(int id, int topicId, String name) {
        this.id = id;
        this.topicId = topicId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
