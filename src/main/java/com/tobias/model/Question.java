package com.tobias.model;

public abstract class Question {
    private int id;
    private float weight;
    private String statement;
    private int activityId;

    public Question() {}

    public Question(int id, float weight, String statement, int activityId) {
        this.id = id;
        this.weight = weight;
        this.statement = statement;
        this.activityId = activityId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }

    public String getStatement() { return statement; }
    public void setStatement(String statement) { this.statement = statement; }

    public int getActivityId() { return activityId; }
    public void setActivityId(int activityId) { this.activityId = activityId; }

    public abstract String getType();
}
