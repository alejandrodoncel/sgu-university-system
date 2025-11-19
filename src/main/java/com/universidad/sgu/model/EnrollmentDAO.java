package com.universidad.sgu.model;

import com.universidad.sgu.config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    public void enrollStudentInSubject(int studentId, int subjectId) throws SQLException {
        String sql = "INSERT INTO enrollments (student_id, subject_id, enrollment_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            pstmt.setDate(3, new java.sql.Date(new java.util.Date().getTime())); // Current date
            pstmt.executeUpdate();
        }
    }

    public List<Enrollment> getEnrollmentsByStudent(int studentId) throws SQLException {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT s.id as student_id, s.dni, s.first_name, s.last_name, s.birth_date, s.email, s.address, " +
                     "sub.id as subject_id, sub.name, sub.credits, " +
                     "e.enrollment_date, e.grade " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.id " +
                     "JOIN subjects sub ON e.subject_id = sub.id " +
                     "WHERE e.student_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                            rs.getInt("student_id"),
                            rs.getString("dni"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getDate("birth_date"),
                            rs.getString("email"),
                            rs.getString("address")
                    );
                    Subject subject = new Subject(
                            rs.getInt("subject_id"),
                            rs.getString("name"),
                            rs.getInt("credits"),
                            null // Professor details not needed here
                    );
                    Double grade = rs.getObject("grade") != null ? rs.getDouble("grade") : null;
                    enrollments.add(new Enrollment(student, subject, rs.getDate("enrollment_date"), grade));
                }
            }
        }
        return enrollments;
    }

    public List<Enrollment> getEnrollmentsBySubject(int subjectId) throws SQLException {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT s.id as student_id, s.dni, s.first_name, s.last_name, s.birth_date, s.email, s.address, " +
                     "sub.id as subject_id, sub.name, sub.credits, " +
                     "e.enrollment_date, e.grade " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.id " +
                     "JOIN subjects sub ON e.subject_id = sub.id " +
                     "WHERE e.subject_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subjectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                            rs.getInt("student_id"),
                            rs.getString("dni"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getDate("birth_date"),
                            rs.getString("email"),
                            rs.getString("address")
                    );
                    Subject subject = new Subject(
                            rs.getInt("subject_id"),
                            rs.getString("name"),
                            rs.getInt("credits"),
                            null // Professor details not needed here for simplicity
                    );
                    Double grade = rs.getObject("grade") != null ? rs.getDouble("grade") : null;
                    enrollments.add(new Enrollment(student, subject, rs.getDate("enrollment_date"), grade));
                }
            }
        }
        return enrollments;
    }

    public void updateGrade(int studentId, int subjectId, Double grade) throws SQLException {
        String sql = "UPDATE enrollments SET grade = ? WHERE student_id = ? AND subject_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (grade != null) {
                pstmt.setDouble(1, grade);
            } else {
                pstmt.setNull(1, Types.DECIMAL);
            }
            pstmt.setInt(2, studentId);
            pstmt.setInt(3, subjectId);
            pstmt.executeUpdate();
        }
    }

    public void unenrollStudentFromSubject(int studentId, int subjectId) throws SQLException {
        String sql = "DELETE FROM enrollments WHERE student_id = ? AND subject_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            pstmt.executeUpdate();
        }
    }
}