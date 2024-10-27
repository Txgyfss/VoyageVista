package com.example.backend.repositories;

import com.example.backend.models.GuideTagMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideTagMapRepository extends JpaRepository<GuideTagMap, Long> {
    void deleteByGuide_GuideId(Long id);
}
