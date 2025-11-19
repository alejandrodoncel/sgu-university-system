package com.universidad.sgu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    private Student student;
    private Subject subject;
    private Date enrollmentDate;
    private Double grade;
}
