package com.universidad.sgu.controller;

import com.universidad.sgu.model.*;
import com.universidad.sgu.view.EnrollmentPanel;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class EnrollmentController {

    private final EnrollmentPanel view;
    private final EnrollmentDAO enrollmentDAO;
    private final StudentDAO studentDAO;
    private final SubjectDAO subjectDAO;

    public EnrollmentController(EnrollmentPanel view, EnrollmentDAO enrollmentDAO, StudentDAO studentDAO,
            SubjectDAO subjectDAO) {
        this.view = view;
        this.enrollmentDAO = enrollmentDAO;
        this.studentDAO = studentDAO;
        this.subjectDAO = subjectDAO;
        initController();
    }

    private void initController() {
        view.getStudentComboBox().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loadSubjectLists();
            }
        });
        view.getEnrollButton().addActionListener(e -> enrollSelectedSubjects());
        view.getUnenrollButton().addActionListener(e -> unenrollSelectedSubjects());
        loadInitialData();
    }

    private void loadInitialData() {
        try {
            view.refreshStudents(studentDAO.getAllStudents());
        } catch (SQLException e) {
            showError("Error al cargar la lista de estudiantes.", e);
        }
    }

    private void loadSubjectLists() {
        Student selectedStudent = (Student) view.getStudentComboBox().getSelectedItem();
        if (selectedStudent == null) {
            view.updateSubjectLists(null, null);
            return;
        }

        try {
            List<Subject> allSubjects = subjectDAO.getAllSubjects();
            List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByStudent(selectedStudent.getId());
            List<Subject> enrolledSubjects = enrollments.stream()
                    .map(Enrollment::getSubject)
                    .collect(Collectors.toList());

            List<Integer> enrolledIds = enrolledSubjects.stream().map(Subject::getId).collect(Collectors.toList());
            List<Subject> availableSubjects = allSubjects.stream()
                    .filter(s -> !enrolledIds.contains(s.getId()))
                    .collect(Collectors.toList());

            view.updateSubjectLists(availableSubjects, enrolledSubjects);

        } catch (SQLException e) {
            showError("Error al cargar las listas de asignaturas.", e);
        }
    }

    private void enrollSelectedSubjects() {
        Student selectedStudent = (Student) view.getStudentComboBox().getSelectedItem();
        List<Subject> selectedSubjects = view.getAvailableSubjectsList().getSelectedValuesList();

        if (selectedStudent == null || selectedSubjects.isEmpty()) {
            showMessage("Seleccione un estudiante y una o más asignaturas disponibles para matricular.");
            return;
        }

        try {
            for (Subject subject : selectedSubjects) {
                enrollmentDAO.enrollStudentInSubject(selectedStudent.getId(), subject.getId());
            }
            loadSubjectLists(); // Refresh lists
        } catch (SQLException e) {
            showError("Error al realizar la matrícula.", e);
        }
    }

    private void unenrollSelectedSubjects() {
        Student selectedStudent = (Student) view.getStudentComboBox().getSelectedItem();
        List<Subject> selectedSubjects = view.getEnrolledSubjectsList().getSelectedValuesList();

        if (selectedStudent == null || selectedSubjects.isEmpty()) {
            showMessage("Seleccione un estudiante y una o más asignaturas matriculadas para anular la matrícula.");
            return;
        }

        try {
            for (Subject subject : selectedSubjects) {
                enrollmentDAO.unenrollStudentFromSubject(selectedStudent.getId(), subject.getId());
            }
            loadSubjectLists(); // Refresh lists
        } catch (SQLException e) {
            showError("Error al anular la matrícula.", e);
        }
    }

    private void showError(String message, Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(view, message + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(view, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
