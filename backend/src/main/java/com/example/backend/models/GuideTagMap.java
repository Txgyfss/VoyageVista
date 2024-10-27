package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "guide_tag_map")
@IdClass(GuideTagMapId.class)
public class GuideTagMap implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "guide_id", nullable = false)
    @JsonBackReference  // 被管理的部分
    private Guide guide;  // 所属攻略

    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private GuideTag tag;  // 所属标签

    // getters and setters
    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    public GuideTag getTag() {
        return tag;
    }

    public void setTag(GuideTag tag) {
        this.tag = tag;
    }


}
