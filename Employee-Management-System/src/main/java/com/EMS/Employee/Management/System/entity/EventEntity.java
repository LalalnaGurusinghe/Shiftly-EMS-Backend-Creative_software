package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "form_url")
    private String formUrl;

    @Column(name = "response_url")
    private String responseUrl;

    @Column(name = "audience")
    private String audience;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "projects")
    private String projects;

    @Column(name = "enable_date_time")
    private LocalDateTime enableDateTime;

    @Column(name = "expire_date_time")
    private LocalDateTime expireDateTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "event_banner", columnDefinition = "LONGBLOB")
    @Lob
    private byte[] photo;
}