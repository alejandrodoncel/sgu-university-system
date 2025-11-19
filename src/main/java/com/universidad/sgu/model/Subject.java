package com.universidad.sgu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    private int id;
    private String name;
    private int credits;
    private Professor professor; // Foreign key relationship
}
