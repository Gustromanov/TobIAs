package com.tobias.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tobias.model.User;

public class UserDAO extends BaseDAO {

    public void inserirUsuario(User user) throws SQLException {
        String sql = "INSERT INTO usuario (nome, cpf, email, senha) VALUES (?, ?, ?, ?)";

        try (
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getCpf());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());

            stmt.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String update = "UPDATE usuario SET nome =?,cpf=?,email=?,senha=?,foto=? WHERE id =?";
        try (
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(update)
        ) {
            pst.setString(1,user.getName());
            pst.setString(2,user.getCpf());
            pst.setString(3,user.getEmail());
            pst.setString(4,user.getPassword());
            if(user.getPhoto()!= null){
                pst.setString(5,user.getPhoto());
            }else{
                pst.setString(5, "default.png");
            }
            pst.setInt(6,user.getId());

            pst.executeUpdate();
        } 
    }

    public void deleteUser(int id) throws SQLException{
        String delete = "DELETE FROM usuario WHERE id = ?";
        try{
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(delete);
            pst.setInt(1,id);
            pst.executeUpdate();
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
