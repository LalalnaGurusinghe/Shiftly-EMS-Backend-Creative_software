package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "enable_date", nullable = false)
    private LocalDate enableDate;

    @Column(name = "expire_date", nullable = false)
    private LocalDate expireDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EventStatus status = EventStatus.PENDING;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}