package com.universidad.sgu.view;

import com.universidad.sgu.model.Professor;
import com.universidad.sgu.model.Subject;
import com.universidad.sgu.view.components.PlaceholderTextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class SubjectPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable subjectTable;
    private final PlaceholderTextField idField = new PlaceholderTextField("ID (auto)");
    private final PlaceholderTextField nameField = new PlaceholderTextField("Nombre de la asignatura");
    private final PlaceholderTextField creditsField = new PlaceholderTextField("Créditos");
    private final JComboBox<Professor> professorComboBox = new JComboBox<>();
    private final JButton saveButton = new JButton("Guardar");
    private final JButton deleteButton = new JButton("Eliminar");
    private final JButton newButton = new JButton("Nuevo");

    public SubjectPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos de la Asignatura"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        idField.setEnabled(false);
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(idField, gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(creditsField, gbc);

        // Professor ComboBox
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(createProfessorComboBoxPanel(), gbc);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(newButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(deleteButton);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; formPanel.add(buttonsPanel, gbc);

        // Table Panel
        String[] columnNames = {"ID", "Nombre", "Créditos", "Profesor"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        subjectTable = new JTable(tableModel);
        subjectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(subjectTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Asignaturas"));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createProfessorComboBoxPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Profesor:"), BorderLayout.WEST);
        professorComboBox.setRenderer(new ProfessorListCellRenderer());
        panel.add(professorComboBox, BorderLayout.CENTER);
        return panel;
    }

    public void refreshProfessors(List<Professor> professors) {
        professorComboBox.removeAllItems();
        professorComboBox.addItem(null); // For vacant subjects
        for (Professor professor : professors) {
            professorComboBox.addItem(professor);
        }
    }

    public void refreshTable(List<Subject> subjects) {
        tableModel.setRowCount(0);
        for (Subject subject : subjects) {
            Vector<Object> row = new Vector<>();
            row.add(subject.getId());
            row.add(subject.getName());
            row.add(subject.getCredits());
            row.add(subject.getProfessor() != null ? subject.getProfessor().getFirstName() + " " + subject.getProfessor().getLastName() : "Vacante");
            tableModel.addRow(row);
        }
    }

    public void populateForm(Subject subject) {
        idField.setText(String.valueOf(subject.getId()));
        nameField.setText(subject.getName());
        creditsField.setText(String.valueOf(subject.getCredits()));
        professorComboBox.setSelectedItem(subject.getProfessor());
    }

    public void clearForm() {
        idField.setText("");
        nameField.setText("");
        creditsField.setText("");
        professorComboBox.setSelectedItem(null);
        subjectTable.clearSelection();
    }

    public Subject getSubjectFromForm() {
        int id = idField.getText().isEmpty() ? 0 : Integer.parseInt(idField.getText());
        int credits = creditsField.getText().isEmpty() ? 0 : Integer.parseInt(creditsField.getText());
        return new Subject(
                id,
                nameField.getText(),
                credits,
                (Professor) professorComboBox.getSelectedItem()
        );
    }

    public int getSelectedSubjectId() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) tableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public JTable getSubjectTable() {
        return subjectTable;
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

    // Custom renderer to display Professor's full name in JComboBox
    private static class ProfessorListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Professor) {
                Professor professor = (Professor) value;
                setText(professor.getFirstName() + " " + professor.getLastName());
            } else {
                setText("Asignar profesor...");
            }
            return this;
        }
    }
}
