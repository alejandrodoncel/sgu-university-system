package com.universidad.sgu;

import com.formdev.flatlaf.FlatDarkLaf;
import com.universidad.sgu.controller.*;
import com.universidad.sgu.model.*;
import com.universidad.sgu.view.MainFrame;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // 1. Configurar el tema moderno antes de crear cualquier componente Swing
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Error al iniciar FlatLaf. Se usará el tema por defecto.");
            ex.printStackTrace();
        }

        // 2. Ejecutar la GUI en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // 3. Instanciar las capas MVC
            // Vista
            MainFrame mainFrame = new MainFrame();

            // Modelo
            StudentDAO studentDAO = new StudentDAO();
            ProfessorDAO professorDAO = new ProfessorDAO();
            SubjectDAO subjectDAO = new SubjectDAO();
            EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

            // Controlador
            // El controlador se encarga de unir la vista con el modelo
            new StudentController(mainFrame.getStudentPanel(), studentDAO);
            new ProfessorController(mainFrame.getProfessorPanel(), professorDAO);
            new SubjectController(mainFrame.getSubjectPanel(), subjectDAO, professorDAO);
            new EnrollmentController(mainFrame.getEnrollmentPanel(), enrollmentDAO, studentDAO, subjectDAO);
            new GradeController(mainFrame.getGradePanel(), enrollmentDAO, subjectDAO);

            // 4. Hacer visible la ventana principal
            mainFrame.setVisible(true);
        });
    }
}
