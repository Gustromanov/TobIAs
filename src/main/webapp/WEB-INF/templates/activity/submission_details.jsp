<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tobias.model.Activity" %>
<%@ page import="com.tobias.model.ActivitySubmission" %>
<%@ page import="com.tobias.model.SubmissionAnswer" %>

<%
    Activity activity = (Activity) request.getAttribute("activity");
    ActivitySubmission submission = (ActivitySubmission) request.getAttribute("submission");
    List<SubmissionAnswer> answers = (List<SubmissionAnswer>) request.getAttribute("answers");
%>

<div class="custom-container">
    <% if (activity != null && submission != null) { %>
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="h4 mb-0"><%= activity.getTitle() %></h2>
                <p class="text-muted mb-0">
                    Respostas de <%= submission.getStudentName() %>
                    <% if (submission.getSubmittedAt() != null) { %>
                        · Enviado em <%= submission.getSubmittedAt() %>
                    <% } %>
                </p>
            </div>
            <a href="${pageContext.request.contextPath}/Activity?action=view&id=<%= activity.getId() %>" class="btn btn-outline-secondary">
                Voltar
            </a>
        </div>

        <div class="card border-0 shadow-sm p-4">
            <h5 class="mb-3">Respostas</h5>

            <% if (answers != null && !answers.isEmpty()) { %>
                <div class="list-group list-group-flush">
                    <% for (SubmissionAnswer answer : answers) { %>
                        <div class="list-group-item px-0">
                            <p class="mb-1"><%= answer.getQuestionText() %></p>
                            <small class="text-muted">Peso <%= answer.getQuestionWeight() %></small>
                            <div class="border rounded p-3 mt-2 bg-light">
                                <%= answer.getAnswerText() %>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } else { %>
                <p class="text-muted text-center py-4 mb-0">Esta entrega não tem respostas salvas.</p>
            <% } %>
        </div>
    <% } else { %>
        <div class="card border-0 shadow-sm p-4">
            <p class="text-muted text-center mb-0">Entrega não encontrada.</p>
        </div>
    <% } %>
</div>
