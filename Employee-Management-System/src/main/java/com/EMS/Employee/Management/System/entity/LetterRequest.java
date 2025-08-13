package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.EMS.Employee.Management.System.entity.LetterRequestStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "letter_requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LetterRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "letter_type")
    private String letterType;

    @Column(name = "fields_json", columnDefinition = "TEXT")
    private String fieldsJson;


    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LetterRequestStatus status; // UNREAD, READ

    @Column(name = "generated_letter_html", columnDefinition = "TEXT")
    private String generatedLetterHtml;


}
