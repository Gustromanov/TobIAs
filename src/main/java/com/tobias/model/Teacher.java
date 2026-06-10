package com.tobias.model;

public class Teacher extends User{
    private int teacherId;
    private String specialty;
    private String siapeRegistration;

    public Teacher() {}

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getSiapeRegistration() { return siapeRegistration; }
    public void setSiapeRegistration(String siapeRegistration) { this.siapeRegistration = siapeRegistration; }

    @Override
    public String getRoleName() { return "PROFESSOR"; }

    @Override
    public boolean canSubmitActivity() { return false; }

    @Override
    public boolean canManageDiscipline() { return true; }
}
