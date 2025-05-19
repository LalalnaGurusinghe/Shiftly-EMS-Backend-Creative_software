package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "available_leaves")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableLeaveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private int count;
    private String hexColor;
}
