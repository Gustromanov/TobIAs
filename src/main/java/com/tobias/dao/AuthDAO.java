package com.tobias.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tobias.model.User;

public class AuthDAO extends BaseDAO{

    public User buscarPorEmail(String email) {
    String sql = "SELECT * FROM usuario WHERE email = ?";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        
        try (ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("nome"));
                user.setEmail(rs.getString("email"));
                user.setCpf(rs.getString("cpf"));
                user.setPassword(rs.getString("senha")); // hash ou texto
                user.setPhoto(rs.getString("foto"));
                return user;
            }
        }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
