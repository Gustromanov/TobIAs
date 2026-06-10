<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="custom-container">
    <div class="card border-0 shadow-sm p-4 mt-4">
        <form action="${pageContext.request.contextPath}/Disciplines" method="post">
            <input type="hidden" name="action" value="new">
            <div class="mb-3">
                <label class="form-label">Nome da Disciplina</label>
                <input type="text" class="form-control" name="name" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Código da Disciplina (Ex: ADS101)</label>
                <input type="text" class="form-control" name="code" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Descrição</label>
                <textarea class="form-control" name="description" rows="3"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Salvar Disciplina</button>
            <a href="${pageContext.request.contextPath}/Disciplines" class="btn btn-outline-secondary">Cancelar</a>
        </form>
    </div>
</div>
