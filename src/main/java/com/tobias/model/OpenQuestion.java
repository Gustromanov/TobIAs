package com.tobias.model;

public class OpenQuestion extends Question{
    private String expectedAnswer;

    public OpenQuestion() {}

    public OpenQuestion(int id, float weight, String statement, int activityId, String expectedAnswer) {
        super(id, weight, statement, activityId);
        this.expectedAnswer = expectedAnswer;
    }

    public String getExpectedAnswer() { return expectedAnswer; }
    public void setExpectedAnswer(String expectedAnswer) { this.expectedAnswer = expectedAnswer; }

    @Override
    public String getType() { return "ABERTA"; }
}
