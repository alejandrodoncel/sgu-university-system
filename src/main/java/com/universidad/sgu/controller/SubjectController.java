package com.universidad.sgu.controller;

import com.universidad.sgu.model.ProfessorDAO;
import com.universidad.sgu.model.Subject;
import com.universidad.sgu.model.SubjectDAO;
import com.universidad.sgu.view.SubjectPanel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class SubjectController {

    private final SubjectPanel view;
    private final SubjectDAO subjectDAO;
    private final ProfessorDAO professorDAO;

    public SubjectController(SubjectPanel view, SubjectDAO subjectDAO, ProfessorDAO professorDAO) {
        this.view = view;
        this.subjectDAO = subjectDAO;
        this.professorDAO = professorDAO;
        initController();
    }

    private void initController() {
        view.getSaveButton().addActionListener(e -> saveSubject());
        view.getDeleteButton().addActionListener(e -> deleteSubject());
        view.getNewButton().addActionListener(e -> view.clearForm());
        view.getSubjectTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromTable();
            }
        });
        loadInitialData();
    }

    private void loadInitialData() {
        try {
            // Load professors into ComboBox
            view.refreshProfessors(professorDAO.getAllProfessors());
            // Load subjects into table
            List<Subject> subjects = subjectDAO.getAllSubjects();
            view.refreshTable(subjects);
        } catch (SQLException e) {
            showError("Error al cargar los datos iniciales.", e);
        }
    }

    private void saveSubject() {
        try {
            Subject subject = view.getSubjectFromForm();
            if (subject.getName().isEmpty() || subject.getCredits() <= 0) {
                showMessage("El nombre de la asignatura es obligatorio y los créditos deben ser un número positivo.");
                return;
            }

            if (subject.getId() == 0) { // New subject
                subjectDAO.addSubject(subject);
                showMessage("Asignatura añadida correctamente.");
            } else { // Update existing subject
                subjectDAO.updateSubject(subject);
                showMessage("Asignatura actualizada correctamente.");
            }
            view.clearForm();
            loadInitialData(); // Refresh both table and combo box if needed
        } catch (NumberFormatException e) {
            showError("Los créditos deben ser un número válido.", e);
        } catch (SQLException e) {
            showError("Error al guardar la asignatura en la base de datos.", e);
        }
    }

    private void deleteSubject() {
        int subjectId = view.getSelectedSubjectId();
        if (subjectId == -1) {
            showMessage("Por favor, seleccione una asignatura para eliminar.");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(view,
                "¿Está seguro de que desea eliminar la asignatura seleccionada?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                subjectDAO.deleteSubject(subjectId);
                showMessage("Asignatura eliminada correctamente.");
                view.clearForm();
                loadInitialData();
            } catch (SQLException e) {
                showError("Error al eliminar la asignatura de la base de datos.", e);
            }
        }
    }

    private void populateFormFromTable() {
        int subjectId = view.getSelectedSubjectId();
        if (subjectId != -1) {
            try {
                subjectDAO.getSubjectById(subjectId).ifPresent(view::populateForm);
            } catch (SQLException e) {
                showError("Error al recuperar los datos de la asignatura.", e);
            }
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
