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
  <title>404 - TobIAs</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@3.34.0/tabler-icons.min.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/error.css">
</head>

<body class="error-page">
  <main class="error-shell">
    <section class="error-main" aria-labelledby="error-title">
      <div class="error-copy">
        <h1 id="error-title" class="error-title">
          <small>Erro</small>
          <span>404</span>
          pagina nao encontrada
        </h1>
        <p class="error-text">
          O endereco acessado nao existe por aqui. Volte para um ponto seguro e continue navegando pelo sistema.
        </p>

        <div class="error-actions">
          <a href="<%= contextPath %>/dashboard" class="error-button error-button-primary">
            <i class="ti ti-layout-dashboard" aria-hidden="true"></i>
            Ir para o painel
          </a>
          <a href="<%= contextPath %>/login" class="error-button error-button-secondary">
            <i class="ti ti-login-2" aria-hidden="true"></i>
            Voltar ao login
          </a>
        </div>
      </div>

      <div class="error-visual" aria-hidden="true">
        <img src="<%= contextPath %>/assets/images/tobias404.png" alt="">
      </div>
    </section>
  </main>
</body>

</html>
