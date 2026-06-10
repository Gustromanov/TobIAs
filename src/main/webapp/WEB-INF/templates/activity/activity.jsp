<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="custom-container activity-page">
    <div class="activity-header">
        <div>
            <h2 class="activity-title">Atividades</h2>
            <p class="activity-subtitle">Gerencie entregas, pesos e questões da disciplina.</p>
        </div>
        <a href="${pageContext.request.contextPath}/Activity?action=new&disciplineId=${param.disciplineId}" class="btn btn-action btn-action-add">
            <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                <path d="M11 5h2v6h6v2h-6v6h-2v-6H5v-2h6V5Z"/>
            </svg>
            Criar Atividade
        </a>
    </div>

    <div class="activity-panel">
        <div class="empty-state">Nenhuma atividade cadastrada ainda.</div>
    </div>
</div>
