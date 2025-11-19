package com.universidad.sgu.view;

import com.universidad.sgu.model.Professor;
import com.universidad.sgu.view.components.PlaceholderTextField;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class ProfessorPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable professorTable;
    private final PlaceholderTextField idField = new PlaceholderTextField("ID (auto)");
    private final PlaceholderTextField dniField = new PlaceholderTextField("DNI/Pasaporte");
    private final PlaceholderTextField firstNameField = new PlaceholderTextField("Nombres");
    private final PlaceholderTextField lastNameField = new PlaceholderTextField("Apellidos");
    private final PlaceholderTextField specialtyField = new PlaceholderTextField("Especialidad");
    private final JButton saveButton = new JButton("Guardar");
    private final JButton deleteButton = new JButton("Eliminar");
    private final JButton newButton = new JButton("Nuevo");

    public ProfessorPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Profesor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        idField.setEnabled(false);
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(idField, gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(dniField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(firstNameField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(lastNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; formPanel.add(specialtyField, gbc);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(newButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(deleteButton);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; formPanel.add(buttonsPanel, gbc);

        // Table Panel
        String[] columnNames = {"ID", "DNI", "Nombres", "Apellidos", "Especialidad"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        professorTable = new JTable(tableModel);
        professorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(professorTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Profesores"));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshTable(List<Professor> professors) {
        tableModel.setRowCount(0);
        for (Professor professor : professors) {
            Vector<Object> row = new Vector<>();
            row.add(professor.getId());
            row.add(professor.getDni());
            row.add(professor.getFirstName());
            row.add(professor.getLastName());
            row.add(professor.getSpecialty());
            tableModel.addRow(row);
        }
    }

    public void populateForm(Professor professor) {
        idField.setText(String.valueOf(professor.getId()));
        dniField.setText(professor.getDni());
        firstNameField.setText(professor.getFirstName());
        lastNameField.setText(professor.getLastName());
        specialtyField.setText(professor.getSpecialty());
    }

    public void clearForm() {
        idField.setText("");
        dniField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        specialtyField.setText("");
        professorTable.clearSelection();
    }

    public Professor getProfessorFromForm() {
        int id = idField.getText().isEmpty() ? 0 : Integer.parseInt(idField.getText());
        return new Professor(
                id,
                dniField.getText(),
                firstNameField.getText(),
                lastNameField.getText(),
                specialtyField.getText()
        );
    }

    public int getSelectedProfessorId() {
        int selectedRow = professorTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) tableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public JTable getProfessorTable() {
        return professorTable;
    }
    public JButton getSaveButton() {
        return saveButton;
    }
    public JButton getDeleteButton() {
        return deleteButton;
    }
    public JButton getNewButton() {
        return newButton;
    }
}
