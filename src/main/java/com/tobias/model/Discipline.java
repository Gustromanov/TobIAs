package com.tobias.model;

public class Discipline {
    private int id;
    private String name;
    private String code;
    private String description;
    // Id da tabela professor que identifica o dono/professor da disciplina.
    private int idProfessor;
    private String professorName;
    // Papel do usuario logado nesta disciplina, usado para escolher a visao correta.
    private String userRole;

    public Discipline(){}

    public Discipline(int id, String name, String code, String description, int idProfessor){
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.idProfessor = idProfessor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public boolean isProfessor() {
        // Ajuda views antigas que ainda verificam o papel como texto.
        return "PROFESSOR".equals(userRole);
    }

    public boolean isStudent() {
        // Ajuda views antigas que ainda verificam o papel como texto.
        return "ALUNO".equals(userRole);
    }
}
