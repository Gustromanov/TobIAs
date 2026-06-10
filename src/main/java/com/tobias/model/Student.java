package com.tobias.model;

public class Student extends User{
    private int studentId;
    private String registration;

    public Student() {}

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getRegistration() { return registration; }
    public void setRegistration(String registration) { this.registration = registration; }

    @Override
    public String getRoleName() { return "ALUNO"; }

    @Override
    public boolean canSubmitActivity() { return true; }

    @Override
    public boolean canManageDiscipline() { return false; }
}
