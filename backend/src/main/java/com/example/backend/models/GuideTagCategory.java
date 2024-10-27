package com.example.backend.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "guide_tag_categories")
public class GuideTagCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;  // 标签分类唯一标识

    @Column(nullable = false, unique = true, length = 50)
    private String name;  // 标签分类名称

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<GuideTag> tags;  // 标签
    // getters and setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GuideTag> getTags() {
        return tags;
    }

    public void setTags(Set<GuideTag> tags) {
        this.tags = tags;
    }

}
