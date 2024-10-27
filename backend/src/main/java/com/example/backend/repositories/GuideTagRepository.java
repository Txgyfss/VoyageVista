package com.example.backend.repositories;

import com.example.backend.models.GuideTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GuideTagRepository extends JpaRepository<GuideTag, Long> {
    List<GuideTag> findByCategoryName(String category);

}