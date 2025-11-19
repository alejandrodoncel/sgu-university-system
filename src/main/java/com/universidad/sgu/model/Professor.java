package com.universidad.sgu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Professor {
    private int id;
    private String dni;
    private String firstName;
    private String lastName;
    private String specialty;
}
