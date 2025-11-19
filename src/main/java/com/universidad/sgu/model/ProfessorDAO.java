package com.universidad.sgu.model;

import com.universidad.sgu.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfessorDAO {

    public void addProfessor(Professor professor) throws SQLException {
        String sql = "INSERT INTO professors (dni, first_name, last_name, specialty) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, professor.getDni());
            pstmt.setString(2, professor.getFirstName());
            pstmt.setString(3, professor.getLastName());
            pstmt.setString(4, professor.getSpecialty());
            pstmt.executeUpdate();
        }
    }

    public List<Professor> getAllProfessors() throws SQLException {
        List<Professor> professors = new ArrayList<>();
        String sql = "SELECT * FROM professors ORDER BY last_name, first_name";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                professors.add(mapResultSetToProfessor(rs));
            }
        }
        return professors;
    }

    public Optional<Professor> getProfessorById(int id) throws SQLException {
        String sql = "SELECT * FROM professors WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProfessor(rs));
                }
            }
        }
        return Optional.empty();
    }

    public void updateProfessor(Professor professor) throws SQLException {
        String sql = "UPDATE professors SET dni = ?, first_name = ?, last_name = ?, specialty = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, professor.getDni());
            pstmt.setString(2, professor.getFirstName());
            pstmt.setString(3, professor.getLastName());
            pstmt.setString(4, professor.getSpecialty());
            pstmt.setInt(5, professor.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteProfessor(int id) throws SQLException {
        String sql = "DELETE FROM professors WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Professor mapResultSetToProfessor(ResultSet rs) throws SQLException {
        return new Professor(
                rs.getInt("id"),
                rs.getString("dni"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("specialty")
        );
    }
}
