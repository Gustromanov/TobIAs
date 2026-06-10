<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="custom-container">
    <div class="card border-0 shadow-sm p-4 mt-4">
        <form action="${pageContext.request.contextPath}/Disciplines" method="post">
            <input type="hidden" name="action" value="enter">
            <div class="mb-3">
                <label class="form-label">Código da Disciplina para Entrar</label>
                <input type="text" class="form-control" name="code" placeholder="Ex: ADS101" required>
            </div>
            <div class="d-flex gap-2">
                <button type="submit" class="btn btnadd text-white">Entrar na Disciplina</button>
                <a href="${pageContext.request.contextPath}/Disciplines" class="btn btn-outline-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>