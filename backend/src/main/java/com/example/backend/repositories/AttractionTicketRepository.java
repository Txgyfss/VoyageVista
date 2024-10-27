package com.example.backend.repositories;

import com.example.backend.models.AttractionTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttractionTicketRepository extends JpaRepository<AttractionTicket, Long> {

    // 根据景点ID查找所有票型
    List<AttractionTicket> findByAttraction_AttractionId(Long attractionId);
}
