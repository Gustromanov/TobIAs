<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tobias.model.Activity" %>
<%@ page import="com.tobias.model.ActivityReportRow" %>

<%!
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String formatInlineMarkdown(String value) {
        return value.replaceAll("\\*\\*(.+?)\\*\\*", "<strong>$1</strong>");
    }

    private String formatAiReport(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        StringBuilder html = new StringBuilder();
        boolean listOpen = false;

        for (String line : escapeHtml(value).split("\\R")) {
            String trimmed = line.trim();

            if (trimmed.isEmpty()) {
                continue;
            }

            if (trimmed.startsWith("* ")) {
                if (!listOpen) {
                    html.append("<ul>");
                    listOpen = true;
                }
                html.append("<li>")
                        .append(formatInlineMarkdown(trimmed.substring(2).trim()))
                        .append("</li>");
                continue;
            }

            if (listOpen) {
                html.append("</ul>");
                listOpen = false;
            }

            html.append("<p>")
                    .append(formatInlineMarkdown(trimmed))
                    .append("</p>");
        }

        if (listOpen) {
            html.append("</ul>");
        }

        return html.toString();
    }
%>

<%
    Activity activity = (Activity) request.getAttribute("activity");
    List<ActivityReportRow> reportRows = (List<ActivityReportRow>) request.getAttribute("reportRows");
    String aiReport = (String) request.getAttribute("aiReport");
%>

<div class="custom-container activity-page">
    <% if (activity != null) { %>
        <div class="activity-header">
            <div>
                <h2 class="activity-title">Relatório: <%= escapeHtml(activity.getTitle()) %></h2>
                <p class="activity-subtitle">Alunos que fizeram a atividade e quantidade de acertos.</p>
            </div>
            <a href="${pageContext.request.contextPath}/Activity?action=view&id=<%= activity.getId() %>" class="btn btn-action btn-action-back">
                Voltar
            </a>
        </div>

        <div class="activity-panel">
            <div class="activity-panel-header">
                <h3 class="activity-panel-title">Resumo gerado com IA</h3>
            </div>
            <div class="ai-report-content">
                <%= formatAiReport(aiReport) %>
            </div>
        </div>

        <div class="activity-panel">
            <div class="activity-panel-header">
                <h3 class="activity-panel-title">Resultado por aluno</h3>
            </div>

            <% if (reportRows != null && !reportRows.isEmpty()) { %>
                <div class="question-list">
                    <% for (ActivityReportRow row : reportRows) { %>
                        <div class="question-list-item">
                            <div>
                                <p class="question-text"><%= row.getStudentName() %></p>
                                <span class="activity-meta-item">
                                    <%= row.getStudentEmail() %>
                                    <% if (row.getSubmittedAt() != null) { %>
                                        · Enviado em <%= row.getSubmittedAt() %>
                                    <% } %>
                                </span>
                            </div>
                            <div class="activity-meta-item">
                                <strong><%= row.getCorrectAnswers() %></strong> de <strong><%= row.getTotalQuestions() %></strong> questões
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } else { %>
                <div class="empty-state">Nenhum aluno fez esta atividade ainda.</div>
            <% } %>
        </div>
    <% } else { %>
        <div class="empty-state">Atividade não encontrada.</div>
    <% } %>
</div>
