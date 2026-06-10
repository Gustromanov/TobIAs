package com.tobias.dto;

import java.util.Map;

public class GeneratedQuestion {
    private String type; 
    private String statement;
    private String expectedAnswer; 
    private Map<String, String> alternatives; 
    private String correctOption;

    public GeneratedQuestion() {}

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatement() { return statement; }
    public void setStatement(String statement) { this.statement = statement; }

    public String getExpectedAnswer() { return expectedAnswer; }
    public void setExpectedAnswer(String expectedAnswer) { this.expectedAnswer = expectedAnswer; }

    public Map<String, String> getAlternatives() { return alternatives; }
    public void setAlternatives(Map<String, String> alternatives) { this.alternatives = alternatives; }

    public String getCorrectOption() { return correctOption; }
    public void setCorrectOption(String correctOption) { this.correctOption = correctOption; }
}