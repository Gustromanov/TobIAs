<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="author" content="TobIAs - Sistema de Educacao">
  <title>Login - TobIAs</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@3.34.0/tabler-icons.min.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/auth.css">
</head>

<body class="auth-page">
  <main class="auth-shell">
    <section class="auth-hero" aria-label="Apresentacao do TobIAs">
    </section>

    <section class="auth-panel" aria-labelledby="login-title">
      <div class="auth-card">
        <div class="auth-card-header">
          <div>
            <p class="auth-eyebrow">Bem-vindo de volta</p>
            <h2 id="login-title">Acessar conta</h2>
          </div>
        </div>
        
        <%@ include file="../layout/partials/flash.jspf" %>

        <form action="<%= contextPath %>/login" method="post" class="auth-form">
          <div class="auth-field">
            <div class="auth-input">
              <svg class="auth-svg-icon" viewBox="0 0 24 24" aria-hidden="true">
                <path d="M4 6h16v12H4V6Zm1.8 1.5 6.2 4.6 6.2-4.6H5.8Zm12.7 9V9.4L12 14.2 5.5 9.4v7.1h13Z"/>
              </svg>
              <input id="email" name="email" type="email" placeholder="seuemail@exemplo.com" autocomplete="email" aria-label="E-mail" required>
            </div>
          </div>

          <div class="auth-field">
            <div class="auth-input">
              <svg class="auth-svg-icon" viewBox="0 0 24 24" aria-hidden="true">
                <path d="M7 10V8a5 5 0 0 1 10 0v2h2v10H5V10h2Zm2 0h6V8a3 3 0 0 0-6 0v2Zm-2 2v6h10v-6H7Z"/>
              </svg>
              <input id="senha" name="senha" type="password" placeholder="Digite sua senha" autocomplete="current-password" aria-label="Senha" required>
            </div>
          </div>

          <div class="auth-options">
            <label class="auth-check">
              <input type="checkbox" name="lembrar">
              <span>Lembrar acesso</span>
            </label>
            <a href="#">Esqueci a senha</a>
          </div>

          <button type="submit" class="auth-submit">
            <svg class="auth-button-icon" viewBox="0 0 24 24" aria-hidden="true">
              <path d="m13.2 5.3 5.7 5.7H4v2h14.9l-5.7 5.7 1.4 1.4 8.1-8.1-8.1-8.1-1.4 1.4Z"/>
            </svg>
            Entrar
          </button>
        </form>

        <p class="auth-switch">
          Ainda nao tem conta?
          <a href="<%= contextPath %>/user/register-form">Criar cadastro</a>
        </p>
      </div>
    </section>
  </main>
</body>

</html>
