package com.universidad.sgu.view;

import com.universidad.sgu.model.Student;
import com.universidad.sgu.model.Subject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EnrollmentPanel extends JPanel {

    private final JComboBox<Student> studentComboBox = new JComboBox<>();
    private final JList<Subject> availableSubjectsList = new JList<>();
    private final JList<Subject> enrolledSubjectsList = new JList<>();
    private final JButton enrollButton = new JButton(">>");
    private final JButton unenrollButton = new JButton("<<");
    private final DefaultListModel<Subject> availableSubjectsModel = new DefaultListModel<>();
    private final DefaultListModel<Subject> enrolledSubjectsModel = new DefaultListModel<>();

    public EnrollmentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for student selection
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Seleccionar Estudiante"));
        studentComboBox.setRenderer(new StudentListCellRenderer());
        topPanel.add(studentComboBox, BorderLayout.CENTER);

        // Center panel for subject lists
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Gestionar Matrículas"));
        GridBagConstraints gbc = new GridBagConstraints();

        availableSubjectsList.setModel(availableSubjectsModel);
        availableSubjectsList.setCellRenderer(new SubjectListCellRenderer());
        enrolledSubjectsList.setModel(enrolledSubjectsModel);
        enrolledSubjectsList.setCellRenderer(new SubjectListCellRenderer());

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.45;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JScrollPane(availableSubjectsList), gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.add(enrollButton);
        buttonPanel.add(unenrollButton);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.1;
        gbc.gridx = 1;
        centerPanel.add(buttonPanel, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.45;
        gbc.gridx = 2;
        centerPanel.add(new JScrollPane(enrolledSubjectsList), gbc);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    public void refreshStudents(List<Student> students) {
        studentComboBox.removeAllItems();
        studentComboBox.addItem(null);
        for (Student student : students) {
            studentComboBox.addItem(student);
        }
    }

    public void updateSubjectLists(List<Subject> available, List<Subject> enrolled) {
        availableSubjectsModel.clear();
        enrolledSubjectsModel.clear();
        if (available != null) {
            available.forEach(availableSubjectsModel::addElement);
        }
        if (enrolled != null) {
            enrolled.forEach(enrolledSubjectsModel::addElement);
        }
    }

    public JComboBox<Student> getStudentComboBox() {
        return studentComboBox;
    }
    public JList<Subject> getAvailableSubjectsList() {
        return availableSubjectsList;
    }
    public JList<Subject> getEnrolledSubjectsList() {
        return enrolledSubjectsList;
    }
    public JButton getEnrollButton() {
        return enrollButton;
    }
    public JButton getUnenrollButton() {
        return unenrollButton;
    }

    // Custom renderers
    private static class StudentListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Student) {
                Student student = (Student) value;
                setText(student.getFirstName() + " " + student.getLastName());
            } else {
                setText("Seleccione un estudiante...");
            }
            return this;
        }
    }

    private static class SubjectListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Subject) {
                Subject subject = (Subject) value;
                setText(subject.getName());
            }
            return this;
        }
    }
}
