package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "guide_tags")
public class GuideTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;  // 标签唯一标识

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)

    private GuideTagCategory category;  // 所属标签分类

    @Column(nullable = false, unique = true, length = 50)
    private String name;  // 标签名称

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)

    private Set<GuideTagMap> guideTagMaps;  // 攻略标签映射

    // getters and setters
    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public GuideTagCategory getCategory() {
        return category;
    }

    public void setCategory(GuideTagCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GuideTagMap> getGuideTagMaps() {
        return guideTagMaps;
    }

    public void setGuideTagMaps(Set<GuideTagMap> guideTagMaps) {
        this.guideTagMaps = guideTagMaps;
    }
}
