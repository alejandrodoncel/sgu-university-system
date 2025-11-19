package com.universidad.sgu.controller;

import com.universidad.sgu.model.Enrollment;
import com.universidad.sgu.model.EnrollmentDAO;
import com.universidad.sgu.model.Subject;
import com.universidad.sgu.model.SubjectDAO;
import com.universidad.sgu.view.GradePanel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.List;

public class GradeController {

    private final GradePanel view;
    private final EnrollmentDAO enrollmentDAO;
    private final SubjectDAO subjectDAO;

    public GradeController(GradePanel view, EnrollmentDAO enrollmentDAO, SubjectDAO subjectDAO) {
        this.view = view;
        this.enrollmentDAO = enrollmentDAO;
        this.subjectDAO = subjectDAO;
        initController();
    }

    private void initController() {
        view.getSubjectComboBox().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loadEnrolledStudents();
            }
        });
        view.getSaveGradesButton().addActionListener(e -> saveGrades());
        loadInitialData();
    }

    private void loadInitialData() {
        try {
            view.refreshSubjects(subjectDAO.getAllSubjects());
        } catch (SQLException e) {
            showError("Error al cargar la lista de asignaturas.", e);
        }
    }

    private void loadEnrolledStudents() {
        Subject selectedSubject = (Subject) view.getSubjectComboBox().getSelectedItem();
        if (selectedSubject == null) {
            view.refreshEnrollmentsTable(null);
            return;
        }

        try {
            List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsBySubject(selectedSubject.getId());
            view.refreshEnrollmentsTable(enrollments);
        } catch (SQLException e) {
            showError("Error al cargar los estudiantes matriculados.", e);
        }
    }

    private void saveGrades() {
        Subject selectedSubject = (Subject) view.getSubjectComboBox().getSelectedItem();
        if (selectedSubject == null) {
            showMessage("Por favor, seleccione una asignatura primero.");
            return;
        }

        TableModel model = view.getTableModel();
        int rowCount = model.getRowCount();
        try {
            for (int i = 0; i < rowCount; i++) {
                int studentId = (int) model.getValueAt(i, 0);
                Object gradeValue = model.getValueAt(i, 3);
                Double grade = null;
                if (gradeValue != null && !gradeValue.toString().trim().isEmpty()) {
                    grade = Double.parseDouble(gradeValue.toString());
                    if (grade < 0 || grade > 10) {
                        showError("La nota para el estudiante con ID " + studentId + " debe estar entre 0 y 10.", null);
                        return; // Stop saving if one grade is invalid
                    }
                }
                enrollmentDAO.updateGrade(studentId, selectedSubject.getId(), grade);
            }
            showMessage("Notas guardadas correctamente.");
            loadEnrolledStudents(); // Refresh the view
        } catch (NumberFormatException e) {
            showError("Una o más notas no son un número válido.", e);
        } catch (SQLException e) {
            showError("Error al guardar las notas en la base de datos.", e);
        }
    }

    private void showError(String message, Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(view, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
