package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "Events")
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @Column(name = "enable_date")
    private LocalDate enableDate;

    @Column(name = "expire_date")
    private LocalDate expireDate;


}
