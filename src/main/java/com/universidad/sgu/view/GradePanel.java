package com.universidad.sgu.view;

import com.universidad.sgu.model.Enrollment;
import com.universidad.sgu.model.Subject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class GradePanel extends JPanel {

    private final JComboBox<Subject> subjectComboBox = new JComboBox<>();
    private final JTable gradesTable;
    private final DefaultTableModel tableModel;
    private final JButton saveGradesButton = new JButton("Guardar Notas");

    public GradePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for subject selection
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Seleccionar Asignatura"));
        subjectComboBox.setRenderer(new SubjectListCellRenderer());
        topPanel.add(subjectComboBox, BorderLayout.CENTER);

        // Center panel for the grades table
        String[] columnNames = {"ID Estudiante", "Nombre", "Apellido", "Nota"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only the "Nota" column is editable
                return column == 3;
            }
        };
        gradesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gradesTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Estudiantes Matriculados"));

        // Bottom panel for the save button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(saveGradesButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refreshSubjects(List<Subject> subjects) {
        subjectComboBox.removeAllItems();
        subjectComboBox.addItem(null);
        for (Subject subject : subjects) {
            subjectComboBox.addItem(subject);
        }
    }

    public void refreshEnrollmentsTable(List<Enrollment> enrollments) {
        tableModel.setRowCount(0);
        if (enrollments != null) {
            for (Enrollment enrollment : enrollments) {
                Vector<Object> row = new Vector<>();
                row.add(enrollment.getStudent().getId());
                row.add(enrollment.getStudent().getFirstName());
                row.add(enrollment.getStudent().getLastName());
                row.add(enrollment.getGrade());
                tableModel.addRow(row);
            }
        }
    }

    public JComboBox<Subject> getSubjectComboBox() {
        return subjectComboBox;
    }
    public JTable getGradesTable() {
        return gradesTable;
    }
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    public JButton getSaveGradesButton() {
        return saveGradesButton;
    }

    // Custom renderer for Subject JComboBox
    private static class SubjectListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Subject) {
                setText(((Subject) value).getName());
            } else {
                setText("Seleccione una asignatura...");
            }
            return this;
        }
    }
}
