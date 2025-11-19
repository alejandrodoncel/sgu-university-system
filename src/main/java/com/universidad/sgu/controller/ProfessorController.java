package com.universidad.sgu.controller;

import com.universidad.sgu.model.Professor;
import com.universidad.sgu.model.ProfessorDAO;
import com.universidad.sgu.view.ProfessorPanel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class ProfessorController {

    private final ProfessorPanel view;
    private final ProfessorDAO dao;

    public ProfessorController(ProfessorPanel view, ProfessorDAO dao) {
        this.view = view;
        this.dao = dao;
        initController();
    }

    private void initController() {
        view.getSaveButton().addActionListener(e -> saveProfessor());
        view.getDeleteButton().addActionListener(e -> deleteProfessor());
        view.getNewButton().addActionListener(e -> view.clearForm());
        view.getProfessorTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromTable();
            }
        });
        loadProfessors();
    }

    private void loadProfessors() {
        try {
            List<Professor> professors = dao.getAllProfessors();
            view.refreshTable(professors);
        } catch (SQLException e) {
            showError("Error al cargar los profesores desde la base de datos.", e);
        }
    }

    private void saveProfessor() {
        try {
            Professor professor = view.getProfessorFromForm();
            if (professor.getDni().isEmpty() || professor.getFirstName().isEmpty() || professor.getLastName().isEmpty() || professor.getSpecialty().isEmpty()) {
                showMessage("Todos los campos son obligatorios, excepto el ID.");
                return;
            }

            if (professor.getId() == 0) { // New professor
                dao.addProfessor(professor);
                showMessage("Profesor añadido correctamente.");
            } else { // Update existing professor
                dao.updateProfessor(professor);
                showMessage("Profesor actualizado correctamente.");
            }
            view.clearForm();
            loadProfessors();
        } catch (SQLException e) {
            showError("Error al guardar el profesor en la base de datos.", e);
        }
    }

    private void deleteProfessor() {
        int professorId = view.getSelectedProfessorId();
        if (professorId == -1) {
            showMessage("Por favor, seleccione un profesor para eliminar.");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(view,
                "¿Está seguro de que desea eliminar al profesor seleccionado?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                dao.deleteProfessor(professorId);
                showMessage("Profesor eliminado correctamente.");
                view.clearForm();
                loadProfessors();
            } catch (SQLException e) {
                showError("Error al eliminar el profesor de la base de datos.", e);
            }
        }
    }

    private void populateFormFromTable() {
        int professorId = view.getSelectedProfessorId();
        if (professorId != -1) {
            try {
                dao.getProfessorById(professorId).ifPresent(view::populateForm);
            } catch (SQLException e) {
                showError("Error al recuperar los datos del profesor.", e);
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
