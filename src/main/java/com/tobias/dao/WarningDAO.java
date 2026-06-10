package com.tobias.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.tobias.model.Warning;
import com.tobias.model.Comment;

public class WarningDAO extends BaseDAO {

    public void createWarning(Warning warning) {
        String sql = "INSERT INTO aviso (disciplina_id, usuario_id, conteudo) VALUES (?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, warning.getDisciplineId());
            pst.setInt(2, warning.getUserId());
            pst.setString(3, warning.getContent());
            pst.executeUpdate();
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addComment(Comment comment) {
        String sql = "INSERT INTO comentario_aviso (aviso_id, usuario_id, conteudo) VALUES (?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, comment.getWarningId()); 
            pst.setInt(2, comment.getUserId());
            pst.setString(3, comment.getContent());
            pst.executeUpdate();
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Warning> listWarningsByDiscipline(int disciplineId) {
        List<Warning> warnings = new ArrayList<>();
        String sql = "SELECT a.id, a.disciplina_id, a.usuario_id, a.conteudo, a.data_publicacao, u.nome, u.foto " +
                     "FROM aviso a " +
                     "INNER JOIN usuario u ON a.usuario_id = u.id " +
                     "WHERE a.disciplina_id = ? ORDER BY a.data_publicacao DESC";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, disciplineId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Warning warning = new Warning();
                    warning.setId(rs.getInt("id"));
                    warning.setDisciplineId(rs.getInt("disciplina_id"));
                    warning.setUserId(rs.getInt("usuario_id"));
                    warning.setContent(rs.getString("conteudo"));
                    
                    Timestamp data = rs.getTimestamp("data_publicacao");
                    if (data != null) warning.setPublishedAt(data.toLocalDateTime());
                    
                    warning.setAuthorName(rs.getString("nome"));
                    warning.setAuthorPhoto(rs.getString("foto"));

                    warning.setComments(listComments(con, warning.getId()));
                    warnings.add(warning);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return warnings;
    }

    private List<Comment> listComments(Connection con, int warningId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.id, c.aviso_id, c.usuario_id, c.conteudo, c.data_comentario, u.nome, u.foto " +
                     "FROM comentario_aviso c " +
                     "INNER JOIN usuario u ON c.usuario_id = u.id " +
                     "WHERE c.aviso_id = ? ORDER BY c.data_comentario ASC";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, warningId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Comment c = new Comment();
                    c.setId(rs.getInt("id"));
                    c.setWarningId(rs.getInt("aviso_id"));
                    c.setUserId(rs.getInt("usuario_id"));
                    c.setContent(rs.getString("conteudo"));
                    
                    Timestamp data = rs.getTimestamp("data_comentario");
                    if (data != null) c.setDateComment(data.toLocalDateTime());
                    
                    c.setAuthorName(rs.getString("nome"));
                    c.setAuthorPhoto(rs.getString("foto"));
                    comments.add(c);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return comments;
    }
}