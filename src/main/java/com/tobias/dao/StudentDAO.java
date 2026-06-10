package com.tobias.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement; 

import com.tobias.model.Student;

public class StudentDAO extends BaseDAO{

    public int getOrCreateStudent(int id){
        int studentId = -1;
        String get = "SELECT id FROM aluno WHERE usuario_id = ?";
        try{
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(get);
            pst.setInt(1,id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return rs.getInt("id");
            }
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
        String insert = "INSERT INTO aluno (usuario_id) VALUES (?)";
        try{
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,id);
            pst.executeUpdate();
            ResultSet rsKey = pst.getGeneratedKeys();
            if(rsKey.next()){
                studentId = rsKey.getInt(1);
            }
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
        return studentId;
    }


    public Student getStudentByUserId(int idUser){
        String sql = "SELECT a.id AS aluno_id, a.matricula, " +
        "u.id AS usuario_id, u.nome, u.cpf, u.email, u.senha " +
        "FROM aluno a " +
        "INNER JOIN usuario u ON a.usuario_id = u.id " +
        "WHERE u.id = ?";

        try{
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1,idUser);
            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                Student student = new Student();
                student.setStudentId(rs.getInt("aluno_id"));
                student.setRegistration(rs.getString("matricula"));
                student.setId(rs.getInt("usuario_id"));
                student.setName(rs.getString("nome"));
                student.setCpf(rs.getString("cpf"));
                student.setEmail(rs.getString("email"));
                student.setPassword(rs.getString("senha"));
                con.close();
                return student;
            }
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
}