package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "attraction_tickets")
public class AttractionTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;  // 票型唯一标识

    @ManyToOne
    @JoinColumn(name = "attraction_id", nullable = false)
    @JsonIgnore
    private Attraction attraction;  // 所属景点

    @Column(nullable = false, length = 100)
    private String ticketType;  // 票型名称

    @Column(nullable = false)
    private BigDecimal price;  // 票价

    @Column(length = 255)
    private String description;  // 票型描述

    // getters and setters
    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
