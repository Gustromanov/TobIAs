<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="custom-container activity-page">
    <div class="activity-header">
        <div>
            <h2 class="activity-title">Nova Atividade</h2>
            <p class="activity-subtitle">Crie a atividade, defina prazos e monte as questões em uma só etapa.</p>
        </div>
        <a href="${pageContext.request.contextPath}/Disciplines?action=view&id=${param.disciplineId}" class="btn btn-action btn-action-back">
            <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                <path d="M10.8 5.4 4.2 12l6.6 6.6 1.4-1.4L8 13h12v-2H8l4.2-4.2-1.4-1.4Z"/>
            </svg>
            Voltar
        </a>
    </div>

    <div class="activity-panel">
        <form action="${pageContext.request.contextPath}/Activity" method="post" onsubmit="return validateActivityWeightLimit(this)">
            <input type="hidden" name="action" value="new">
            <input type="hidden" name="disciplineId" value="${param.disciplineId}">

            <div class="activity-panel-header">
                <h3 class="activity-panel-title">Dados da atividade</h3>
            </div>

            <div class="row activity-form-grid">
                <div class="col-md-8">
                    <label class="form-label">Título</label>
                    <input type="text" class="form-control" name="title" placeholder="Ex: Lista de exercícios 1" required>
                </div>

                <div class="col-md-4">
                    <label class="form-label">Peso da atividade</label>
                    <input type="number" class="form-control" name="weight" min="0" step="0.1" placeholder="Ex: 2.0">
                </div>

                <div class="col-md-4">
                    <label class="form-label">Data de atribuição</label>
                    <input type="date" class="form-control" name="submitDate" required>
                </div>

                <div class="col-md-4">
                    <label class="form-label">Data de entrega</label>
                    <input type="date" class="form-control" name="deliveryDate">
                </div>
            </div>

            <div class="activity-panel-header mt-4">
                <h3 class="activity-panel-title">Questões da atividade</h3>
                <button type="button" class="btn btn-sm btn-primary me-2" data-bs-toggle="modal" data-bs-target="#aiModal" style="background-color: #6f42c1; border-color: #6f42c1;">
                     Gerar com IA
                </button>
                <button type="button" class="btn btn-sm btn-action btn-action-add" id="add-question">
                    <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                        <path d="M11 5h2v6h6v2h-6v6h-2v-6H5v-2h6V5Z"/>
                    </svg>
                    Adicionar questão
                </button>
            </div>

            <div id="questions-list" class="question-list">
                <div class="question-item">
                    <div class="question-item-header">
                        <strong class="question-number" data-number="1">Questão 1</strong>
                        <button type="button" class="btn btn-sm btn-action btn-action-delete remove-question">
                            <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                                <path d="M9 3h6l1 2h4v2H4V5h4l1-2Zm-2 6h10l-.7 12H7.7L7 9Zm2.1 2 .5 8h1.8l-.4-8H9.1Zm3.9 0v8h2v-8h-2Z"/>
                            </svg>
                            Remover
                        </button>
                    </div>
                    <div class="row g-3">
                        <div class="col-md-3">
                            <label class="form-label">Tipo</label>
                            <select class="form-control question-type" name="questionType">
                                <option value="ABERTA">Aberta</option>
                                <option value="FECHADA">Fechada</option>
                            </select>
                        </div>
                        <div class="col-md-9">
                            <label class="form-label">Enunciado</label>
                            <textarea class="form-control" name="questionText" rows="3" placeholder="Digite o enunciado da questão" required></textarea>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Peso</label>
                            <input type="number" class="form-control" name="questionWeight" min="0" step="0.1" placeholder="Ex: 1.0">
                        </div>
                        <div class="col-md-9 open-question-fields">
                            <label class="form-label">Resposta esperada</label>
                            <textarea class="form-control" name="expectedAnswer" rows="2" placeholder="Resposta que ficará salva apenas para o professor"></textarea>
                        </div>
                        <div class="col-12 closed-question-fields d-none">
                            <div class="row g-3">
                                <div class="col-md-3">
                                    <label class="form-label">Alternativa A</label>
                                    <input type="text" class="form-control" name="optionA">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Alternativa B</label>
                                    <input type="text" class="form-control" name="optionB">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Alternativa C</label>
                                    <input type="text" class="form-control" name="optionC">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Alternativa D</label>
                                    <input type="text" class="form-control" name="optionD">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Letra correta</label>
                                    <select class="form-control" name="correctOption">
                                        <option value="A">A</option>
                                        <option value="B">B</option>
                                        <option value="C">C</option>
                                        <option value="D">D</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="activity-footer-actions">
                <button type="submit" class="btn btn-action btn-action-save">
                    <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                        <path d="m9.2 16.6-4.1-4.1 1.4-1.4 2.7 2.7 8.3-8.3 1.4 1.4-9.7 9.7Z"/>
                    </svg>
                    Salvar Atividade
                </button>
                <a href="${pageContext.request.contextPath}/Disciplines?action=view&id=${param.disciplineId}" class="btn btn-action btn-action-cancel">
                    <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                        <path d="M6.4 5 5 6.4l5.6 5.6L5 17.6 6.4 19l5.6-5.6 5.6 5.6 1.4-1.4-5.6-5.6L19 6.4 17.6 5 12 10.6 6.4 5Z"/>
                    </svg>
                    Cancelar
                </a>
            </div>
        </form>
        <div class="modal fade" id="aiModal" tabindex="-1" aria-labelledby="aiModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="aiModalLabel"> Gerador de Questões com IA</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Material de Apoio (Texto base)</label>
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
                        A IA está a ler o material e a gerar as questões. Por favor, aguarde...
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
