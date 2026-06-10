<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
String pageSection = (String) request.getAttribute("pageSection");
String pageHeading = (String) request.getAttribute("pageHeading");
String contentPage = (String) request.getAttribute("contentPage");
String pageCss = (String) request.getAttribute("pageCss");
String pageJs = (String) request.getAttribute("pageJs");
String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta content="TobIAs - Sistema de Educação" name="author">
  <title>TobIAs</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@3.34.0/tabler-icons.min.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/theme.css">
  <% if (pageCss != null && !pageCss.isBlank()) { %>
  <link rel="stylesheet" href="<%= contextPath + pageCss %>">
  <% } %>
</head>

<body>
  <%@ include file="partials/sidebar.jspf" %>

  <div id="content" class="position-relative h-100">
    <%@ include file="partials/navbar.jspf" %>

    <%@ include file="partials/flash.jspf" %>

    <jsp:include page="<%= contentPage %>" />
    <%@ include file="partials/footer.jspf" %>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script src="<%= contextPath %>/assets/js/theme.js"></script>
  <% if (pageJs != null && !pageJs.isBlank()) { %>
  <script src="<%= contextPath + pageJs %>"></script>
  <% } %>
</body>

</html>
