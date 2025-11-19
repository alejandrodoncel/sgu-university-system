package com.universidad.sgu.model;

import com.universidad.sgu.config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubjectDAO {

    public void addSubject(Subject subject) throws SQLException {
        String sql = "INSERT INTO subjects (name, credits, professor_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subject.getName());
            pstmt.setInt(2, subject.getCredits());
            if (subject.getProfessor() != null) {
                pstmt.setInt(3, subject.getProfessor().getId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.executeUpdate();
        }
    }

    public List<Subject> getAllSubjects() throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        // JOIN query to fetch professor details along with the subject
        String sql = "SELECT s.id, s.name, s.credits, p.id as professor_id, p.first_name, p.last_name, p.dni, p.specialty " +
                     "FROM subjects s " +
                     "LEFT JOIN professors p ON s.professor_id = p.id " +
                     "ORDER BY s.name";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                subjects.add(mapResultSetToSubject(rs));
            }
        }
        return subjects;
    }

    public Optional<Subject> getSubjectById(int id) throws SQLException {
        String sql = "SELECT s.id, s.name, s.credits, p.id as professor_id, p.first_name, p.last_name, p.dni, p.specialty " +
                     "FROM subjects s " +
                     "LEFT JOIN professors p ON s.professor_id = p.id " +
                     "WHERE s.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSubject(rs));
                }
            }
        }
        return Optional.empty();
    }

    public void updateSubject(Subject subject) throws SQLException {
        String sql = "UPDATE subjects SET name = ?, credits = ?, professor_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subject.getName());
            pstmt.setInt(2, subject.getCredits());
            if (subject.getProfessor() != null) {
                pstmt.setInt(3, subject.getProfessor().getId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setInt(4, subject.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteSubject(int id) throws SQLException {
        String sql = "DELETE FROM subjects WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Subject mapResultSetToSubject(ResultSet rs) throws SQLException {
        Professor professor = null;
        int professorId = rs.getInt("professor_id");
        if (!rs.wasNull()) {
            professor = new Professor(
                professorId,
                rs.getString("dni"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("specialty")
            );
        }
        return new Subject(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("credits"),
                professor
        );
    }
}
