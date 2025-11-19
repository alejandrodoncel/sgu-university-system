package com.universidad.sgu.controller;

import com.universidad.sgu.model.Student;
import com.universidad.sgu.model.StudentDAO;
import com.universidad.sgu.view.StudentPanel;

import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class StudentController {

    private final StudentPanel view;
    private final StudentDAO dao;

    public StudentController(StudentPanel view, StudentDAO dao) {
        this.view = view;
        this.dao = dao;
        initController();
    }

    private void initController() {
        view.getSaveButton().addActionListener(e -> saveStudent());
        view.getDeleteButton().addActionListener(e -> deleteStudent());
        view.getNewButton().addActionListener(e -> view.clearForm());
        view.getStudentTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromTable();
            }
        });
        loadStudents();
    }

    private void loadStudents() {
        try {
            List<Student> students = dao.getAllStudents();
            view.refreshTable(students);
        } catch (SQLException e) {
            showError("Error al cargar los estudiantes desde la base de datos.", e);
        }
    }

    private void saveStudent() {
        try {
            Student student = view.getStudentFromForm();
            // Simple validation
            if (student.getDni().isEmpty() || student.getFirstName().isEmpty() || student.getLastName().isEmpty()) {
                showMessage("DNI, Nombres y Apellidos son campos obligatorios.");
                return;
            }

            if (student.getId() == 0) { // New student
                dao.addStudent(student);
                showMessage("Estudiante añadido correctamente.");
            } else { // Update existing student
                dao.updateStudent(student);
                showMessage("Estudiante actualizado correctamente.");
            }
            view.clearForm();
            loadStudents(); // Refresh table
        } catch (ParseException e) {
            showError("El formato de la fecha es incorrecto. Use yyyy-MM-dd.", e);
        } catch (SQLException e) {
            showError("Error al guardar el estudiante en la base de datos.", e);
        }
    }

    private void deleteStudent() {
        int studentId = view.getSelectedStudentId();
        if (studentId == -1) {
            showMessage("Por favor, seleccione un estudiante para eliminar.");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(view,
                "¿Está seguro de que desea eliminar al estudiante seleccionado?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                dao.deleteStudent(studentId);
                showMessage("Estudiante eliminado correctamente.");
                view.clearForm();
                loadStudents(); // Refresh table
            } catch (SQLException e) {
                showError("Error al eliminar el estudiante de la base de datos.", e);
            }
        }
    }

    private void populateFormFromTable() {
        int studentId = view.getSelectedStudentId();
        if (studentId != -1) {
            try {
                dao.getStudentById(studentId).ifPresent(view::populateForm);
            } catch (SQLException e) {
                showError("Error al recuperar los datos del estudiante.", e);
            }
        }
    }

    private void showError(String message, Exception e) {
        e.printStackTrace(); // Log the full error
        JOptionPane.showMessageDialog(view, message + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(view, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
