<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    Integer disciplineId = (Integer) request.getAttribute("disciplineId");
%>

<div class="custom-container activity-page">
    <div class="activity-header">
        <div>
            <h2 class="activity-title">Novo Material</h2>
            <p class="activity-subtitle">Publique um conteúdo de apoio para os alunos da disciplina.</p>
        </div>
        <a href="${pageContext.request.contextPath}/Disciplines?action=view&id=<%= disciplineId %>" class="btn btn-action btn-action-back">
            <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                <path d="M10.8 5.4 4.2 12l6.6 6.6 1.4-1.4L8 13h12v-2H8l4.2-4.2-1.4-1.4Z"/>
            </svg>
            Voltar
        </a>
    </div>

    <div class="activity-panel">
        <form action="${pageContext.request.contextPath}/Material" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="new">
            <input type="hidden" name="disciplineId" value="<%= disciplineId %>">

            <div class="activity-panel-header">
                <h3 class="activity-panel-title">Dados do material</h3>
            </div>

            <div class="row activity-form-grid">
                <div class="col-md-8">
                    <label class="form-label">Título</label>
                    <input type="text" class="form-control" name="title" placeholder="Ex: Apostila de revisão" required>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Arquivo</label>
                    <input type="file" class="form-control" name="file">
                </div>
                <div class="col-12">
                    <label class="form-label">Conteúdo</label>
                    <textarea class="form-control" name="content" rows="6" placeholder="Descreva o material ou cole orientações para estudo"></textarea>
                </div>
            </div>

            <div class="activity-footer-actions">
                <button type="submit" class="btn btn-action btn-action-save">
                    <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                        <path d="m9.2 16.6-4.1-4.1 1.4-1.4 2.7 2.7 8.3-8.3 1.4 1.4-9.7 9.7Z"/>
                    </svg>
                    Salvar Material
                </button>
            </div>
        </form>
    </div>
</div>
