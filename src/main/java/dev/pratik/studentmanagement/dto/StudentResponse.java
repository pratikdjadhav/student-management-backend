package dev.pratik.studentmanagement.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Double paidFees;
    private Double courseFees;
    private Double pendingFees;
    private LocalDate enrollmentDate;
    private String status;
    private String courseName;
    private Long courseId;
}