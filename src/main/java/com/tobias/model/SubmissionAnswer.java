package com.tobias.model;

public class SubmissionAnswer {
    private int questionId;

    // Dados da pergunta respondida.
    private String questionText;
    private float questionWeight;

    // Texto da resposta enviada pelo aluno para essa pergunta.
    private String answerText;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public float getQuestionWeight() {
        return questionWeight;
    }

    public void setQuestionWeight(float questionWeight) {
        this.questionWeight = questionWeight;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
