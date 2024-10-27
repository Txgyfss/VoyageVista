package com.example.backend.models;

import java.io.Serializable;
import java.util.Objects;

public class GuideTagMapId implements Serializable {

    private Long guide;  // 攻略ID
    private Long tag;  // 标签ID

    public GuideTagMapId() {}

    public GuideTagMapId(Long guide, Long tag) {
        this.guide = guide;
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuideTagMapId that = (GuideTagMapId) o;
        return Objects.equals(guide, that.guide) &&
                Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guide, tag);
    }

    // getters and setters
    public Long getGuide() {
        return guide;
    }

    public void setGuide(Long guide) {
        this.guide = guide;
    }

    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }
}
