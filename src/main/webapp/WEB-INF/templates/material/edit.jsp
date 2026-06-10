<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.tobias.model.Material" %>

<%
    Material material = (Material) request.getAttribute("material");
%>

<div class="custom-container activity-page">
    <% if (material != null) { %>
        <div class="activity-header">
            <div>
                <h2 class="activity-title">Editar Material</h2>
                <p class="activity-subtitle">Atualize o conteúdo ou substitua o arquivo publicado.</p>
            </div>
            <a href="${pageContext.request.contextPath}/Disciplines?action=view&id=<%= material.getDisciplineId() %>" class="btn btn-action btn-action-back">
                <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                    <path d="M10.8 5.4 4.2 12l6.6 6.6 1.4-1.4L8 13h12v-2H8l4.2-4.2-1.4-1.4Z"/>
                </svg>
                Voltar
            </a>
        </div>

        <div class="activity-panel">
            <form action="${pageContext.request.contextPath}/Material" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="materialId" value="<%= material.getId() %>">

                <div class="activity-panel-header">
                    <h3 class="activity-panel-title">Informações do material</h3>
                </div>

                <div class="row activity-form-grid">
                    <div class="col-md-8">
                        <label class="form-label">Título</label>
                        <input type="text" class="form-control" name="title" value="<%= material.getTitle() != null ? material.getTitle() : "" %>" required>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Substituir arquivo</label>
                        <input type="file" class="form-control" name="file">
                        <% if (material.hasFile()) { %>
                            <small class="text-muted">Atual: <%= material.getOriginalFileName() != null ? material.getOriginalFileName() : "arquivo anexado" %></small>
                        <% } %>
                    </div>
                    <div class="col-12">
                        <label class="form-label">Conteúdo</label>
                        <textarea class="form-control" name="content" rows="6"><%= material.getContent() != null ? material.getContent() : "" %></textarea>
                    </div>
                </div>

                <div class="activity-footer-actions">
                    <button type="submit" class="btn btn-action btn-action-save">
                        <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                            <path d="m9.2 16.6-4.1-4.1 1.4-1.4 2.7 2.7 8.3-8.3 1.4 1.4-9.7 9.7Z"/>
                        </svg>
                        Salvar Alterações
                    </button>
                </div>
            </form>
        </div>
    <% } else { %>
        <div class="empty-state">Material não encontrado.</div>
    <% } %>
</div>
