package com.tobias.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tobias.model.Material;

public class MaterialDAO extends BaseDAO {

    public Integer create(Material material) {
        String sql = "INSERT INTO material(disciplina_id, titulo, conteudo, caminho_arquivo, nome_arquivo, tipo_arquivo, tamanho_arquivo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
                PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            fillStatement(pst, material);
            pst.executeUpdate();

            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public List<Material> listByDiscipline(int disciplineId) {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT id, disciplina_id, titulo, conteudo, caminho_arquivo, nome_arquivo, tipo_arquivo, tamanho_arquivo, data_upload "
                + "FROM material WHERE disciplina_id = ? ORDER BY data_upload DESC, id DESC";

        try (Connection con = getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, disciplineId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    materials.add(buildMaterial(rs));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return materials;
    }

    public Material getById(int id) {
        String sql = "SELECT id, disciplina_id, titulo, conteudo, caminho_arquivo, nome_arquivo, tipo_arquivo, tamanho_arquivo, data_upload "
                + "FROM material WHERE id = ?";

        try (Connection con = getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return buildMaterial(rs);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public void update(Material material) {
        String sql = "UPDATE material SET titulo = ?, conteudo = ?, caminho_arquivo = ?, nome_arquivo = ?, tipo_arquivo = ?, tamanho_arquivo = ? "
                + "WHERE id = ?";

        try (Connection con = getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, material.getTitle());
            pst.setString(2, material.getContent());
            pst.setString(3, material.getFilePath());
            pst.setString(4, material.getOriginalFileName());
            pst.setString(5, material.getContentType());
            if (material.getFileSize() != null) {
                pst.setLong(6, material.getFileSize());
            } else {
                pst.setNull(6, Types.BIGINT);
            }
            pst.setInt(7, material.getId());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM material WHERE id = ?";

        try (Connection con = getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void fillStatement(PreparedStatement pst, Material material) throws Exception {
        pst.setInt(1, material.getDisciplineId());
        pst.setString(2, material.getTitle());
        pst.setString(3, material.getContent());
        pst.setString(4, material.getFilePath());
        pst.setString(5, material.getOriginalFileName());
        pst.setString(6, material.getContentType());
        if (material.getFileSize() != null) {
            pst.setLong(7, material.getFileSize());
        } else {
            pst.setNull(7, Types.BIGINT);
        }
    }

    private Material buildMaterial(ResultSet rs) throws Exception {
        Timestamp uploadedAt = rs.getTimestamp("data_upload");
        Long fileSize = rs.getObject("tamanho_arquivo") != null ? rs.getLong("tamanho_arquivo") : null;

        return new Material(
                rs.getInt("id"),
                rs.getInt("disciplina_id"),
                rs.getString("titulo"),
                rs.getString("conteudo"),
                rs.getString("caminho_arquivo"),
                rs.getString("nome_arquivo"),
                rs.getString("tipo_arquivo"),
                fileSize,
                uploadedAt != null ? uploadedAt.toLocalDateTime() : null);
    }
}
