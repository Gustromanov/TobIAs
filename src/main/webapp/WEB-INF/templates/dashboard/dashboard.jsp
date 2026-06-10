<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tobias.model.Discipline" %>
<%@ page import="com.tobias.model.User" %>

<%
  User loggedUser = (User) session.getAttribute("usuarioLogado");
  List<Discipline> disciplines = (List<Discipline>) request.getAttribute("dashboardDisciplines");
  int disciplineCount = request.getAttribute("dashboardDisciplineCount") != null
      ? (Integer) request.getAttribute("dashboardDisciplineCount")
      : 0;
  long teacherDisciplineCount = request.getAttribute("dashboardTeacherDisciplineCount") != null
      ? (Long) request.getAttribute("dashboardTeacherDisciplineCount")
      : 0;
  long studentDisciplineCount = request.getAttribute("dashboardStudentDisciplineCount") != null
      ? (Long) request.getAttribute("dashboardStudentDisciplineCount")
      : 0;
  String firstName = loggedUser != null && loggedUser.getName() != null && !loggedUser.getName().isBlank()
      ? loggedUser.getName().split(" ")[0]
      : "usuário";
%>

<div class="custom-container">
  <div class="dashboard-shell">
    <section class="dashboard-welcome">
      <div>
        <span class="dashboard-kicker">TobIAs</span>
        <h1>Olá, <%= firstName %>.</h1>
        <p>Continue suas disciplinas, publique materiais, acompanhe atividades e gere relatórios em um só lugar.</p>
      </div>
      <div class="dashboard-actions">
        <a href="${pageContext.request.contextPath}/Disciplines" class="btn btn-action btn-action-save">
          <svg class="dashboard-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
            <path d="M5 4.5A2.5 2.5 0 0 1 7.5 2H20v17H7.5A2.5 2.5 0 0 0 5 21.5v-17ZM7.5 4A.5.5 0 0 0 7 4.5v12.55c.16-.03.33-.05.5-.05H18V4H7.5ZM4 4h1v17H4V4Z"/>
          </svg>
          Ver disciplinas
        </a>
        <a href="${pageContext.request.contextPath}/user/update-form" class="btn btn-action btn-action-back">
          <svg class="dashboard-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
            <path d="M12 12a5 5 0 1 1 0-10 5 5 0 0 1 0 10Zm0-2a3 3 0 1 0 0-6 3 3 0 0 0 0 6Zm-8 9.5C4 15.9 7.1 14 11 14h2.2l-2 2H11c-2.9 0-4.8 1.2-5 3.5V20h7.2l-.4 2H4v-2.5Zm12.8-4.6 2.3 2.3-4.4 4.4-2.6.5.5-2.6 4.2-4.6Zm3.1-.9.7.7a1 1 0 0 1 0 1.4l-.4.4-2.3-2.3.4-.4a1 1 0 0 1 1.6.2Z"/>
          </svg>
          Meu perfil
        </a>
      </div>
    </section>

    <section class="dashboard-stats">
      <article class="dashboard-stat-card">
        <span class="dashboard-stat-icon">
          <svg class="dashboard-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
            <path d="M4 3h6a3 3 0 0 1 3 3v14a3 3 0 0 0-3-2H4V3Zm2 2v11h4c.35 0 .69.06 1 .17V6a1 1 0 0 0-1-1H6Zm8 1a3 3 0 0 1 3-3h3v15h-3a3 3 0 0 0-3 2V6Zm3-1a1 1 0 0 0-1 1v10.17c.31-.11.65-.17 1-.17h1V5h-1Z"/>
          </svg>
        </span>
        <strong><%= disciplineCount %></strong>
        <span>Disciplinas vinculadas</span>
      </article>
      <article class="dashboard-stat-card">
        <span class="dashboard-stat-icon">
          <svg class="dashboard-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
            <path d="M3 4h18v12H3V4Zm2 2v8h14V6H5Zm3 12h8v2H8v-2Zm3-2h2v4h-2v-4Z"/>
          </svg>
        </span>
        <strong><%= teacherDisciplineCount %></strong>
        <span>Como professor</span>
      </article>
      <article class="dashboard-stat-card">
        <span class="dashboard-stat-icon">
          <svg class="dashboard-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
            <path d="M12 3 2 8l10 5 8-4v6h2V8L12 3Zm0 2.24L17.53 8 12 10.76 6.47 8 12 5.24ZM6 11.2v4.1c0 1.9 2.7 3.7 6 3.7s6-1.8 6-3.7v-4.1l-2 1v3.1c0 .6-1.5 1.7-4 1.7s-4-1.1-4-1.7v-3.1l-2-1Z"/>
          </svg>
        </span>
        <strong><%= studentDisciplineCount %></strong>
        <span>Como aluno</span>
      </article>
    </section>

    <section class="dashboard-grid">
      <article class="dashboard-panel">
        <div class="dashboard-panel-header">
          <div>
            <span class="dashboard-kicker">Acesso rápido</span>
            <h2>Atalhos do sistema</h2>
          </div>
        </div>

        <div class="dashboard-shortcuts">
          <a href="${pageContext.request.contextPath}/Disciplines?action=new" class="dashboard-shortcut">
            <svg class="dashboard-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
              <path d="M11 5h2v6h6v2h-6v6h-2v-6H5v-2h6V5Z"/>
            </svg>
            <span>Nova disciplina</span>
          </a>
          <a href="${pageContext.request.contextPath}/Disciplines?action=enter" class="dashboard-shortcut">
            <svg class="dashboard-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
              <path d="M10.6 6.4 16.2 12l-5.6 5.6-1.4-1.4 3.2-3.2H3v-2h9.4L9.2 7.8l1.4-1.4ZM19 4h-5V2h7v20h-7v-2h5V4Z"/>
            </svg>
            <span>Entrar em disciplina</span>
          </a>
          <a href="${pageContext.request.contextPath}/Disciplines" class="dashboard-shortcut">
            <svg class="dashboard-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
              <path d="M4 5h3v3H4V5Zm5 0h11v2H9V5ZM4 10.5h3v3H4v-3Zm5 .5h11v2H9v-2ZM4 16h3v3H4v-3Zm5 .5h11v2H9v-2Z"/>
            </svg>
            <span>Minhas disciplinas</span>
          </a>
          <a href="${pageContext.request.contextPath}/user/update-form" class="dashboard-shortcut">
            <svg class="dashboard-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
              <path d="M10 11a4 4 0 1 1 0-8 4 4 0 0 1 0 8Zm0-2a2 2 0 1 0 0-4 2 2 0 0 0 0 4Zm-7 10c0-3.3 2.9-6 7-6 .8 0 1.6.1 2.3.3l-.7 1.9c-.5-.1-1-.2-1.6-.2-3 0-5 1.8-5 4H3Zm14.5-7 1.1.6.9-.5 1 1.8-.9.5v1.2l.9.5-1 1.8-.9-.5-1.1.6v1h-2v-1l-1.1-.6-.9.5-1-1.8.9-.5v-1.2l-.9-.5 1-1.8.9.5 1.1-.6v-1h2v1Zm-1 4.2a1.2 1.2 0 1 0 0-2.4 1.2 1.2 0 0 0 0 2.4Z"/>
            </svg>
            <span>Atualizar perfil</span>
          </a>
        </div>
      </article>

      <article class="dashboard-panel">
        <div class="dashboard-panel-header">
          <div>
            <span class="dashboard-kicker">Continuidade</span>
            <h2>Disciplinas recentes</h2>
          </div>
          <a href="${pageContext.request.contextPath}/Disciplines" class="dashboard-link">Ver todas</a>
        </div>

        <% if (disciplines != null && !disciplines.isEmpty()) { %>
          <div class="dashboard-discipline-list">
            <%
              int limit = Math.min(4, disciplines.size());
              for (int i = 0; i < limit; i++) {
                Discipline discipline = disciplines.get(i);
            %>
              <a href="${pageContext.request.contextPath}/Disciplines?action=view&id=<%= discipline.getId() %>" class="dashboard-discipline-item">
                <div>
                  <strong><%= discipline.getName() %></strong>
                  <span>
                    Código <%= discipline.getCode() %>
                    <% if (discipline.getProfessorName() != null) { %>
                      · Prof. <%= discipline.getProfessorName() %>
                    <% } %>
                  </span>
                </div>
                <small><%= discipline.isProfessor() ? "Professor" : "Aluno" %></small>
              </a>
            <% } %>
          </div>
        <% } else { %>
          <div class="dashboard-empty">
            Você ainda não participa de nenhuma disciplina.
          </div>
        <% } %>
      </article>
    </section>
  </div>
</div>
