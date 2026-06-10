<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tobias.model.Activity" %>
<%@ page import="com.tobias.model.Question" %>
<%@ page import="com.tobias.model.OpenQuestion" %>
<%@ page import="com.tobias.model.ClosedQuestion" %>
<%@ page import="com.tobias.model.ActivitySubmission" %>

<%
    Activity activity = (Activity) request.getAttribute("activity");
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    List<ActivitySubmission> submissions = (List<ActivitySubmission>) request.getAttribute("submissions");
    String userRole = (String) request.getAttribute("userRole");
    boolean isProfessor = "PROFESSOR".equals(userRole);
    boolean isStudent = "ALUNO".equals(userRole);
    boolean hasSubmission = Boolean.TRUE.equals(request.getAttribute("hasSubmission"));
%>

<div class="custom-container activity-page">
    <% if (activity != null) { %>
        <div class="activity-header">
            <div>
                <h2 class="activity-title"><%= activity.getTitle() %></h2>
                <p class="activity-subtitle">
                    Atribuída em <%= activity.getSubmitDate() %>
                    <% if (activity.getDeliveryDate() != null) { %>
                        · Entrega em <%= activity.getDeliveryDate() %>
                    <% } %>
                    · Peso <%= activity.getPeso() %>
                </p>
            </div>
            <a href="${pageContext.request.contextPath}/Disciplines?action=view&id=<%= activity.getIdDiscipline() %>&tab=atividades" class="btn btn-action btn-action-back">
                <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                    <path d="M10.8 5.4 4.2 12l6.6 6.6 1.4-1.4L8 13h12v-2H8l4.2-4.2-1.4-1.4Z"/>
                </svg>
                Voltar
            </a>
        </div>

        <% if (isProfessor) { %>
        <div class="activity-panel">
            <div class="activity-panel-header">
                <h3 class="activity-panel-title">Ações da atividade</h3>
            </div>
            <div class="activity-actions">
                <a href="${pageContext.request.contextPath}/Activity?action=edit&id=<%= activity.getId() %>" class="btn btn-action btn-action-edit">
                    <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                        <path d="M17.7 3.3a1 1 0 0 1 1.4 0l1.6 1.6a1 1 0 0 1 0 1.4L8.9 18.1 4 19.5l1.4-4.9L17.7 3.3Zm-10.5 12-.5 1.9 1.9-.5L16.6 8.7l-1.4-1.4-8 8ZM17.9 7.3 18.6 6 18 5.4l-1.3.7 1.2 1.2Z"/>
                    </svg>
                    Editar Atividade
                </a>
                <a href="${pageContext.request.contextPath}/Activity?action=report-ai&id=<%= activity.getId() %>" class="btn btn-action btn-action-edit">
                    Relatório com IA
                </a>
                <form action="${pageContext.request.contextPath}/Activity" method="post">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="activityId" value="<%= activity.getId() %>">
                    <input type="hidden" name="disciplineId" value="<%= activity.getIdDiscipline() %>">
                    <button type="submit" class="btn btn-action btn-action-delete">
                        <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                            <path d="M9 3h6l1 2h4v2H4V5h4l1-2Zm-2 6h10l-.7 12H7.7L7 9Zm2.1 2 .5 8h1.8l-.4-8H9.1Zm3.9 0v8h2v-8h-2Z"/>
                        </svg>
                        Excluir Atividade
                    </button>
                </form>
            </div>
        </div>
        <% } %>

        <% if (isProfessor) { %>
        <div class="activity-panel">
            <div class="activity-panel-header">
                <h3 class="activity-panel-title">Entregas dos alunos</h3>
            </div>
            <% if (submissions != null && !submissions.isEmpty()) { %>
                <div class="question-list">
                    <% for (ActivitySubmission submission : submissions) { %>
                        <div class="question-list-item">
                            <div>
                                <p class="question-text"><%= submission.getStudentName() %></p>
                                <span class="activity-meta-item">
                                    <%= submission.getStudentEmail() %>
                                    <% if (submission.getSubmittedAt() != null) { %>
                                        · Enviado em <%= submission.getSubmittedAt() %>
                                    <% } %>
                                </span>
                            </div>
                            <a href="${pageContext.request.contextPath}/Submission?action=view&id=<%= submission.getId() %>" class="btn btn-sm btn-action btn-action-edit">
                                Ver respostas
                            </a>
                        </div>
                    <% } %>
                </div>
            <% } else { %>
                <div class="empty-state">Nenhum aluno enviou esta atividade ainda.</div>
            <% } %>
        </div>
        <% } %>

        <div class="activity-panel">
            <div class="activity-panel-header">
                <h3 class="activity-panel-title">Questões cadastradas</h3>
            </div>

            <% if (questions != null && !questions.isEmpty()) { %>
                <% if (isStudent && !hasSubmission) { %>
                    <form action="${pageContext.request.contextPath}/Submission" method="post" class="student-answer-form">
                        <input type="hidden" name="action" value="submit">
                        <input type="hidden" name="activityId" value="<%= activity.getId() %>">
                        <div class="question-list student-question-list">
                            <% int questionNumber = 1; %>
                            <% for (Question question : questions) { %>
                                <div class="student-question-card">
                                    <div class="student-question-header">
                                        <span class="student-question-number">Questão <%= questionNumber++ %></span>
                                        <span class="student-question-weight">Peso <strong><%= question.getWeight() %></strong></span>
                                    </div>
                                    <p class="student-question-text"><%= question.getStatement() %></p>
                                    <div class="student-answer-area">
                                        <% if (question instanceof ClosedQuestion) {
                                            ClosedQuestion closedQuestion = (ClosedQuestion) question;
                                        %>
                                            <div class="student-option-list">
                                                <label class="student-option">
                                                    <input type="radio" name="answer_<%= question.getId() %>" value="A">
                                                    <span class="student-option-letter">A</span>
                                                    <span class="student-option-text"><%= closedQuestion.getOptionA() %></span>
                                                </label>
                                                <label class="student-option">
                                                    <input type="radio" name="answer_<%= question.getId() %>" value="B">
                                                    <span class="student-option-letter">B</span>
                                                    <span class="student-option-text"><%= closedQuestion.getOptionB() %></span>
                                                </label>
                                                <label class="student-option">
                                                    <input type="radio" name="answer_<%= question.getId() %>" value="C">
                                                    <span class="student-option-letter">C</span>
                                                    <span class="student-option-text"><%= closedQuestion.getOptionC() %></span>
                                                </label>
                                                <label class="student-option">
                                                    <input type="radio" name="answer_<%= question.getId() %>" value="D">
                                                    <span class="student-option-letter">D</span>
                                                    <span class="student-option-text"><%= closedQuestion.getOptionD() %></span>
                                                </label>
                                            </div>
                                        <% } else { %>
                                            <textarea class="form-control student-text-answer" name="answer_<%= question.getId() %>" rows="5" placeholder="Digite sua resposta"></textarea>
                                        <% } %>
                                    </div>
                                </div>
                            <% } %>
                        </div>
                        <div class="student-submit-bar">
                            <button type="submit" class="btn btn-action btn-action-save">Enviar Atividade</button>
                        </div>
                    </form>
                <% } else { %>
                    <% if (isStudent && hasSubmission) { %>
                        <div class="alert alert-success">Você já enviou essa atividade.</div>
                    <% } %>
                    <div class="question-list">
                        <% for (Question question : questions) { %>
                            <div class="question-list-item">
                                <div>
                                    <p class="question-text"><%= question.getStatement() %></p>
                                    <span class="activity-meta-item">Peso <strong><%= question.getWeight() %></strong> · <%= question.getType() %></span>
                                    
                                    <% if (isProfessor && question instanceof OpenQuestion) {
                                        OpenQuestion openQuestion = (OpenQuestion) question;
                                    %>
                                        <p class="activity-meta-item mt-2">Resposta esperada: <strong><%= openQuestion.getExpectedAnswer() == null ? "" : openQuestion.getExpectedAnswer() %></strong></p>
                                    <% } %>
                                    
                                    <% if (question instanceof ClosedQuestion) {
                                        ClosedQuestion closedQuestion = (ClosedQuestion) question;
                                    %>
                                        <div class="activity-meta-item mt-2">
                                            <div>A) <%= closedQuestion.getOptionA() %></div>
                                            <div>B) <%= closedQuestion.getOptionB() %></div>
                                            <div>C) <%= closedQuestion.getOptionC() %></div>
                                            <div>D) <%= closedQuestion.getOptionD() %></div>
                                            <% if (isProfessor) { %>
                                                <strong>Correta: <%= closedQuestion.getCorrectOption() %></strong>
                                            <% } %>
                                        </div>
                                    <% } %>
                                </div>
                                <% if (isProfessor) { %>
                                <div class="activity-actions">
                                    <a href="${pageContext.request.contextPath}/Question?action=edit&id=<%= question.getId() %>" class="btn btn-sm btn-action btn-action-edit">
                                        <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                                            <path d="M17.7 3.3a1 1 0 0 1 1.4 0l1.6 1.6a1 1 0 0 1 0 1.4L8.9 18.1 4 19.5l1.4-4.9L17.7 3.3Zm-10.5 12-.5 1.9 1.9-.5L16.6 8.7l-1.4-1.4-8 8ZM17.9 7.3 18.6 6 18 5.4l-1.3.7 1.2 1.2Z"/>
                                        </svg>
                                        Editar
                                    </a>
                                    <form action="${pageContext.request.contextPath}/Question" method="post">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="questionId" value="<%= question.getId() %>">
                                        <input type="hidden" name="activityId" value="<%= activity.getId() %>">
                                        <button type="submit" class="btn btn-sm btn-action btn-action-delete">
                                            <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                                                <path d="M9 3h6l1 2h4v2H4V5h4l1-2Zm-2 6h10l-.7 12H7.7L7 9Zm2.1 2 .5 8h1.8l-.4-8H9.1Zm3.9 0v8h2v-8h-2Z"/>
                                            </svg>
                                            Excluir
                                        </button>
                                    </form>
                                </div>
                                <% } %>
                            </div>
                        <% } %>
                    </div>
                <% } %>
            <% } else { %>
                <div class="empty-state">Nenhuma questão cadastrada ainda.</div>
            <% } %>
        </div>
    <% } else { %>
        <div class="empty-state">
            Atividade não encontrada.
        </div>
    <% } %>
</div>