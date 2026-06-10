<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.tobias.model.Activity" %>

<%
    Activity activity = (Activity) request.getAttribute("activity");
%>

<div class="custom-container activity-page">
    <% if (activity != null) { %>
        <div class="activity-header">
            <div>
                <h2 class="activity-title">Editar Atividade</h2>
                <p class="activity-subtitle">Atualize titulo, peso e datas dessa atividade.</p>
            </div>
            <a href="${pageContext.request.contextPath}/Activity?action=view&id=<%= activity.getId() %>" class="btn btn-action btn-action-back">
                <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                    <path d="M10.8 5.4 4.2 12l6.6 6.6 1.4-1.4L8 13h12v-2H8l4.2-4.2-1.4-1.4Z"/>
                </svg>
                Voltar
            </a>
        </div>

        <div class="activity-panel">
            <form action="${pageContext.request.contextPath}/Activity" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="activityId" value="<%= activity.getId() %>">
                <input type="hidden" name="disciplineId" value="<%= activity.getIdDiscipline() %>">

                <div class="activity-panel-header">
                    <h3 class="activity-panel-title">Informações da atividade</h3>
                </div>

                <div class="row activity-form-grid">
                    <div class="col-md-8">
                        <label class="form-label">Título</label>
                        <input type="text" class="form-control" name="title" value="<%= activity.getTitle() %>" required>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Peso da atividade</label>
                        <input type="number" class="form-control" name="weight" min="0" step="0.1" value="<%= activity.getPeso() %>">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Data de atribuição</label>
                        <input type="date" class="form-control" name="submitDate" value="<%= activity.getSubmitDate() %>" required>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Data de entrega</label>
                        <input type="date" class="form-control" name="deliveryDate" value="<%= activity.getDeliveryDate() != null ? activity.getDeliveryDate() : "" %>">
                    </div>
                </div>

                <div class="activity-panel-header mt-4">
                    <h3 class="activity-panel-title">Adicionar novas questões</h3>
                    <button type="button" class="btn btn-sm btn-primary me-2" data-bs-toggle="modal" data-bs-target="#aiModal" style="background-color: #6f42c1; border-color: #6f42c1;">
                         Gerar com IA
                    </button>
                    <button type="button" class="btn btn-sm btn-action btn-action-add" id="add-question">
                        Adicionar questão manualmente
                    </button>
                </div>
                <div id="questions-list" class="question-list"></div>

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
        <div class="empty-state">
            Atividade não encontrada.
        </div>
    <% } %>
    <div class="modal fade" id="aiModal" tabindex="-1" aria-labelledby="aiModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="aiModalLabel">✨ Gerador de Questões com IA</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Material de Apoio</label>
                        <textarea class="form-control mb-2" id="aiMaterial" rows="3" placeholder="Cole o texto..."></textarea>
                        
                        <label class="form-label text-muted small">Ou envie um arquivo (Apenas PDF):</label>
                        <input type="file" class="form-control form-control-sm" id="aiFile" accept="application/pdf">
                    </div>
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Tipo de Questão</label>
                            <select class="form-control" id="aiType">
                                <option value="ABERTA">Aberta (Discursiva)</option>
                                <option value="FECHADA">Fechada (Múltipla Escolha)</option>
                            </select>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Quantidade</label>
                            <input type="number" class="form-control" id="aiQuantity" value="3" min="1" max="10">
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Dificuldade</label>
                            <select class="form-control" id="aiDifficulty">
                                <option value="FACIL">Fácil</option>
                                <option value="MEDIO" selected>Médio</option>
                                <option value="DIFICIL">Difícil</option>
                            </select>
                        </div>
                    </div>
                    <div id="aiLoading" class="alert alert-info d-none mt-2">
                        A IA está a processar o material e a gerar as questões. Por favor, aguarde...
                    </div>
                    <div id="aiError" class="alert alert-danger d-none mt-2"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary" id="btnGenerateAi" style="background-color: #6f42c1; border-color: #6f42c1;" onclick="generateQuestionsAI()">Gerar Questões</button>
                </div>
            </div>
        </div>
    </div>
    </div>
</div>
