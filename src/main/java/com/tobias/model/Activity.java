package com.tobias.model;

import java.time.LocalDate;

public class Activity {
    
    private String title;
    private LocalDate submitDate;
    private LocalDate deliveryDate;
    private float peso;
    private int id;
    private int idDiscipline;
    

    public Activity(String title, LocalDate submitDate, LocalDate deliveryDate, float peso, int id, int idDiscipline) {
        this.title = title;
        this.submitDate = submitDate;
        this.deliveryDate = deliveryDate;
        this.peso = peso;
        this.id = id;
        this.idDiscipline = idDiscipline;
    }

    public String getTitle(){
        return title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public LocalDate getSubmitDate(){
        return submitDate;
    }
    
    public void setSubmitDate(LocalDate submitDate){
        this.submitDate = submitDate;
    }
    
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDiscipline() {
        return idDiscipline;
    }

    public void setIdDiscipline(int idDiscipline) {
        this.idDiscipline = idDiscipline;
    }

    


    
}
