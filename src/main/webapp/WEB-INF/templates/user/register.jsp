<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
String contextPath = request.getContextPath();
String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="author" content="TobIAs - Sistema de Educacao">
  <title>Cadastro - TobIAs</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@3.34.0/tabler-icons.min.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/auth.css">
</head>

<body class="auth-page">
  <main class="auth-shell auth-shell-register">
    <section class="auth-hero" aria-label="Apresentacao do TobIAs">
    </section>

    <section class="auth-panel" aria-labelledby="cadastro-title">
      <div class="auth-card">
        <div class="auth-card-header">
          <div>
            <p class="auth-eyebrow">Nova conta</p>
            <h2 id="cadastro-title">Cadastrar usuário</h2>
          </div>
        </div>

        <% if (error != null) { %>
          <div class="alert alert-danger" role="alert">
            <%= error %>
          </div>
        <% } %>

        <%@ include file="../layout/partials/flash.jspf" %>

        <form action="<%= contextPath %>/user/register-save" method="post" class="auth-form">
          <div class="auth-grid">
            <div class="auth-field">
              <div class="auth-input">
                <svg class="auth-svg-icon" viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8Zm0 2c-4.1 0-7 2.1-7 5v1h14v-1c0-2.9-2.9-5-7-5Zm0-4a2 2 0 1 1 0-4 2 2 0 0 1 0 4Zm-4.8 8c.6-1.2 2.4-2 4.8-2s4.2.8 4.8 2H7.2Z"/>
                </svg>
                <input id="nome" name="nome" type="text" placeholder="Seu nome" autocomplete="name" aria-label="Nome completo" required>
              </div>
            </div>

            <div class="auth-field">
              <div class="auth-input">
                <svg class="auth-svg-icon" viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M4 5h16v14H4V5Zm2 2v10h12V7H6Zm2 2h4v4H8V9Zm6 1h3v2h-3v-2Zm0 4h3v2h-3v-2Zm-6 1h4v2H8v-2Z"/>
                </svg>
                <input id="cpf" name="cpf" type="text" placeholder="000.000.000-00" autocomplete="off" inputmode="numeric" maxlength="14" pattern="\d{3}\.\d{3}\.\d{3}-\d{2}" aria-label="CPF" data-cpf-mask required>
              </div>
            </div>
          </div>

          <div class="auth-field">
            <div class="auth-input">
              <svg class="auth-svg-icon" viewBox="0 0 24 24" aria-hidden="true">
                <path d="M4 6h16v12H4V6Zm1.8 1.5 6.2 4.6 6.2-4.6H5.8Zm12.7 9V9.4L12 14.2 5.5 9.4v7.1h13Z"/>
              </svg>
              <input id="email" name="email" type="email" placeholder="seuemail@exemplo.com" autocomplete="email" aria-label="E-mail" required>
            </div>
          </div>

          <div class="auth-grid">
            <div class="auth-field">
              <div class="auth-input">
                <svg class="auth-svg-icon" viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M7 10V8a5 5 0 0 1 10 0v2h2v10H5V10h2Zm2 0h6V8a3 3 0 0 0-6 0v2Zm-2 2v6h10v-6H7Z"/>
                </svg>
                <input id="senha" name="senha" type="password" placeholder="Crie uma senha" autocomplete="new-password" aria-label="Senha" required>
              </div>
            </div>
          </div>

          <button type="submit" class="auth-submit">
            <svg class="auth-button-icon" viewBox="0 0 24 24" aria-hidden="true">
              <path d="m9.2 16.6-4.1-4.1-1.4 1.4 5.5 5.5L21 7.6l-1.4-1.4L9.2 16.6Z"/>
            </svg>
            Criar conta
          </button>
        </form>

        <p class="auth-switch">
          Já tem uma conta?
          <a href="<%= contextPath %>/login">Entrar agora</a>
        </p>
      </div>
    </section>
  </main>
  <script src="<%= contextPath %>/assets/js/cpf-mask.js"></script>
</body>

</html>
