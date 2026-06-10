package com.tobias.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;  

import com.tobias.model.Teacher;

public class TeacherDAO extends BaseDAO{

    public int getOrCreateTeacher(int idUser){
        int teacherId = -1;

        String get = "SELECT id FROM professor WHERE usuario_id = ?";
        try{
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(get);
            pst.setInt(1,idUser);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return rs.getInt("id");
            }
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }

        String insert = "INSERT INTO professor (usuario_id) VALUES (?)";
        try{
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,idUser);
            pst.executeUpdate();

            ResultSet rsKeys = pst.getGeneratedKeys();
            if(rsKeys.next()){
                teacherId = rsKeys.getInt(1);
            }
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
        return teacherId;
    }


    public Teacher getTeacherByUserId(int idUser){
        String sql = "SELECT p.id AS professor_id, p.especialidade, p.matricula_siape, " +
        "u.id AS usuario_id, u.nome, u.cpf, u.email, u.senha " +
        "FROM professor p " +
        "INNER JOIN usuario u ON p.usuario_id = u.id " +
        "WHERE u.id = ?";

        try{
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1,idUser);
            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                Teacher teacher = new Teacher();
                teacher.setTeacherId(rs.getInt("professor_id"));
                teacher.setSpecialty(rs.getString("especialidade"));
                teacher.setSiapeRegistration(rs.getString("matricula_siape"));
                teacher.setId(rs.getInt("usuario_id"));
                teacher.setName(rs.getString("nome"));
                teacher.setCpf(rs.getString("cpf"));
                teacher.setEmail(rs.getString("email"));
                teacher.setPassword(rs.getString("senha"));
                con.close();
                return teacher;
            }
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
}