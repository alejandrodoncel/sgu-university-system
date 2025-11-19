package com.universidad.sgu.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final JTabbedPane tabbedPane;
    private final StudentPanel studentPanel;
    private final ProfessorPanel professorPanel;
    private final SubjectPanel subjectPanel;
    private final EnrollmentPanel enrollmentPanel;
    private final GradePanel gradePanel;

    public MainFrame() {
        setTitle("Sistema de Gestión Universitaria (SGU)");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Menu Bar for Theme Switching ---
        JMenuBar menuBar = new JMenuBar();
        JMenu themeMenu = new JMenu("Tema");
        JMenuItem lightThemeItem = new JMenuItem("Light Mode");
        JMenuItem darkThemeItem = new JMenuItem("Dark Mode");

        lightThemeItem.addActionListener(e -> switchTheme(new FlatLightLaf()));
        darkThemeItem.addActionListener(e -> switchTheme(new FlatDarkLaf()));

        themeMenu.add(lightThemeItem);
        themeMenu.add(darkThemeItem);
        menuBar.add(themeMenu);
        setJMenuBar(menuBar);

        // --- Tabbed Pane for Modules ---
        tabbedPane = new JTabbedPane();
        
        studentPanel = new StudentPanel();
        tabbedPane.addTab("Gestión de Estudiantes", studentPanel);

        professorPanel = new ProfessorPanel();
        tabbedPane.addTab("Gestión de Profesores", professorPanel);

        subjectPanel = new SubjectPanel();
        tabbedPane.addTab("Gestión de Asignaturas", subjectPanel);

        enrollmentPanel = new EnrollmentPanel();
        tabbedPane.addTab("Gestión de Matrículas", enrollmentPanel);

        gradePanel = new GradePanel();
        tabbedPane.addTab("Gestión de Notas", gradePanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void switchTheme(LookAndFeel laf) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Error switching theme.");
            e.printStackTrace();
        }
    }

    public StudentPanel getStudentPanel() {
        return studentPanel;
    }

    public ProfessorPanel getProfessorPanel() {
        return professorPanel;
    }

    public SubjectPanel getSubjectPanel() {
        return subjectPanel;
    }

    public EnrollmentPanel getEnrollmentPanel() {
        return enrollmentPanel;
    }

    public GradePanel getGradePanel() {
        return gradePanel;
    }
}
