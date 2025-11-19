package com.universidad.sgu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * POJO que representa la entidad Estudiante.
 * Se utilizan anotaciones de Lombok para generar automáticamente:
 * - Getters y Setters para todos los campos (@Data)
 * - Constructor sin argumentos (@NoArgsConstructor)
 * - Constructor con todos los argumentos (@AllArgsConstructor)
 * - Métodos equals(), hashCode() y toString() (@Data)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private int id;
    private String dni;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;
    private String address;
}
