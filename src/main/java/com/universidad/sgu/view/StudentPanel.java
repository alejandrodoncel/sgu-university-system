package com.universidad.sgu.view;

import com.universidad.sgu.model.Student;
import com.universidad.sgu.view.components.PlaceholderTextField;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class StudentPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable studentTable;
    private final PlaceholderTextField idField = new PlaceholderTextField("ID (auto)");
    private final PlaceholderTextField dniField = new PlaceholderTextField("DNI/Pasaporte");
    private final PlaceholderTextField firstNameField = new PlaceholderTextField("Nombres");
    private final PlaceholderTextField lastNameField = new PlaceholderTextField("Apellidos");
    private final PlaceholderTextField birthDateField = new PlaceholderTextField("Fecha Nacimiento (yyyy-MM-dd)");
    private final PlaceholderTextField emailField = new PlaceholderTextField("Correo electrónico");
    private final PlaceholderTextField addressField = new PlaceholderTextField("Dirección");
    private final JButton saveButton = new JButton("Guardar");
    private final JButton deleteButton = new JButton("Eliminar");
    private final JButton newButton = new JButton("Nuevo");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public StudentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Estudiante"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        idField.setEnabled(false);
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(idField, gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(dniField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(firstNameField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(lastNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(birthDateField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; formPanel.add(addressField, gbc);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(newButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(deleteButton);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; formPanel.add(buttonsPanel, gbc);

        // Table Panel
        String[] columnNames = {"ID", "DNI", "Nombres", "Apellidos", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Estudiantes"));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            Vector<Object> row = new Vector<>();
            row.add(student.getId());
            row.add(student.getDni());
            row.add(student.getFirstName());
            row.add(student.getLastName());
            row.add(student.getEmail());
            tableModel.addRow(row);
        }
    }

    public void populateForm(Student student) {
        idField.setText(String.valueOf(student.getId()));
        dniField.setText(student.getDni());
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        birthDateField.setText(dateFormat.format(student.getBirthDate()));
        emailField.setText(student.getEmail());
        addressField.setText(student.getAddress());
    }

    public void clearForm() {
        idField.setText("");
        dniField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        birthDateField.setText("");
        emailField.setText("");
        addressField.setText("");
        studentTable.clearSelection();
    }

    public Student getStudentFromForm() throws ParseException {
        Date birthDate = birthDateField.getText().isEmpty() ? null : dateFormat.parse(birthDateField.getText());
        int id = idField.getText().isEmpty() ? 0 : Integer.parseInt(idField.getText());
        return new Student(
                id,
                dniField.getText(),
                firstNameField.getText(),
                lastNameField.getText(),
                birthDate,
                emailField.getText(),
                addressField.getText()
        );
    }

    public int getSelectedStudentId() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) tableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public JTable getStudentTable() {
        return studentTable;
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
