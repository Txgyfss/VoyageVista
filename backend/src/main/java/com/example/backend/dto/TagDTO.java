package com.example.backend.dto;
public class TagDTO {
    private Long tagId;
    private String name;

    // 构造方法
    public TagDTO(Long tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }

    // getters 和 setters
    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
