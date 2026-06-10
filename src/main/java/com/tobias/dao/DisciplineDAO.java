package com.tobias.dao;

import com.tobias.model.Discipline;
import com.tobias.model.User;
import com.tobias.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DisciplineDAO extends BaseDAO {

    public Integer save(Discipline discipline) {
        String add = "INSERT INTO disciplina(nome, codigo, descricao, professor_id) VALUES (?,?,?,?)";
        try {
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(add, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, discipline.getName());
            pst.setString(2, discipline.getCode());
            pst.setString(3, discipline.getDescription());
            pst.setInt(4, discipline.getIdProfessor());
            pst.executeUpdate();
            ResultSet keys = pst.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                con.close();
                return id;
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Discipline> listDisciplines(int idUser) {
        List<Discipline> lista = new ArrayList<>();
        String read = "SELECT d.id,d.nome,d.codigo,d.descricao, u_prof.nome AS professor_nome, " +
                "CASE WHEN p.usuario_id = ? THEN 'PROFESSOR' ELSE 'ALUNO' END AS user_role " +
                "FROM disciplina d " +
                "LEFT JOIN professor p ON d.professor_id = p.id " +
                "LEFT JOIN usuario u_prof ON p.usuario_id = u_prof.id " +
                "WHERE p.usuario_id = ? " +
                "OR d.id IN (SELECT m.disciplina_id FROM matricula m " +
                "INNER JOIN aluno a ON m.aluno_id = a.id WHERE a.usuario_id = ?) " +
                "ORDER BY d.id DESC";
        try {
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(read);
            pst.setInt(1, idUser);
            pst.setInt(2, idUser);
            pst.setInt(3, idUser);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Discipline d = new Discipline();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("nome"));
                d.setCode(rs.getString("codigo"));
                d.setDescription(rs.getString("descricao"));

                String teacherName = rs.getString("professor_nome");
                d.setProfessorName(teacherName);
                d.setUserRole(rs.getString("user_role"));
                lista.add(d);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public Discipline getById(int idDiscipline, int idUser) {
        String sql = "SELECT d.id, d.nome, d.codigo, d.descricao, d.professor_id, u_prof.nome AS professor_nome, " +
                "CASE " +
                "WHEN p.usuario_id = ? THEN 'PROFESSOR' " +
                "WHEN EXISTS (SELECT 1 FROM matricula m INNER JOIN aluno a ON m.aluno_id = a.id WHERE m.disciplina_id = d.id AND a.usuario_id = ?) THEN 'ALUNO' " +
                "ELSE NULL END AS user_role " +
                "FROM disciplina d " +
                "LEFT JOIN professor p ON d.professor_id = p.id " +
                "LEFT JOIN usuario u_prof ON p.usuario_id = u_prof.id " +
                "WHERE d.id = ?";
        try {
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, idUser);
            pst.setInt(2, idUser);
            pst.setInt(3, idDiscipline);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Discipline d = new Discipline();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("nome"));
                d.setCode(rs.getString("codigo"));
                d.setDescription(rs.getString("descricao"));
                d.setIdProfessor(rs.getInt("professor_id"));
                d.setProfessorName(rs.getString("professor_nome"));
                d.setUserRole(rs.getString("user_role"));
                con.close();
                return d;
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<User> listStudentsByDiscipline(int idDiscipline) {
        List<User> students = new ArrayList<>();
        String sql = "SELECT a.id AS aluno_id, a.matricula, u.id, u.nome, u.cpf, u.email " +
                "FROM matricula m " +
                "INNER JOIN aluno a ON m.aluno_id = a.id " +
                "INNER JOIN usuario u ON a.usuario_id = u.id " +
                "WHERE m.disciplina_id = ? " +
                "ORDER BY u.nome";
        try {
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, idDiscipline);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getInt("aluno_id"));
                student.setRegistration(rs.getString("matricula"));
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("nome"));
                student.setCpf(rs.getString("cpf"));
                student.setEmail(rs.getString("email"));
                students.add(student);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return students;
    }

    public int getByCode(String code) {
        String search = "SELECT id FROM disciplina WHERE codigo = ?";
        try {
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(search);
            pst.setString(1, code);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }

    public void enrollStudent(int idStudent, int idDiscipline) {
        if (isStudentEnrolled(idStudent, idDiscipline)) {
            return;
        }
        String insert = "INSERT INTO matricula(aluno_id,disciplina_id) VALUES(?,?)";
        try {
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(insert);
            pst.setInt(1, idStudent);
            pst.setInt(2, idDiscipline);
            pst.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean isStudentEnrolled(int idStudent, int idDiscipline) {
        String sql = "SELECT 1 FROM matricula WHERE aluno_id = ? AND disciplina_id = ?";
        try {
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, idStudent);
            pst.setInt(2, idDiscipline);
            ResultSet rs = pst.executeQuery();
            boolean exists = rs.next();
            con.close();
            return exists;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean isProfessor(int idDiscipline, int idUser) {
        return "PROFESSOR".equals(getUserRole(idDiscipline, idUser));
    }

    public boolean isStudent(int idDiscipline, int idUser) {
        return "ALUNO".equals(getUserRole(idDiscipline, idUser));
    }

    public String getUserRole(int idDiscipline, int idUser) {
        Discipline discipline = getById(idDiscipline, idUser);
        return discipline != null ? discipline.getUserRole() : null;
    }
}