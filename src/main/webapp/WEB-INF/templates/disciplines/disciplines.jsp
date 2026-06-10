<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tobias.model.Discipline" %>

<div class="custom-container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="h4 mb-0">Listagem de Disciplinas</h2>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/Disciplines?action=enter" class="btn btnadd text-white">
                <img src="${pageContext.request.contextPath}/assets/images/enter.png" alt="botão entrar na disciplina">
                Entrar
            </a>
            <a href="${pageContext.request.contextPath}/Disciplines?action=new" class="btn btnadd text-white">
                <img src="${pageContext.request.contextPath}/assets/images/btnadd.png" alt="botão adicionar disciplina">
                Criar
            </a>
        </div>
    </div>

    <div class="card border-0 shadow-sm">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="bg-light">
                    <tr>
                        <th>Id</th>
                        <th>Nome</th>
                        <th>Código</th>
                        <th>Professor</th>
                        <th>Meu papel</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<Discipline> lista = (List<Discipline>) request.getAttribute("listaDisciplines");
                        
                        if (lista != null && !lista.isEmpty()) {
                            for (Discipline disciplina : lista) { 
                    %>     
                            <tr onclick="window.location.href='${pageContext.request.contextPath}/Disciplines?action=view&id=<%= disciplina.getId() %>'" style="cursor: pointer;">
                                <td>#<%= disciplina.getId() %></td>
                                <td><%= disciplina.getName() %></td>
                                <td><%= disciplina.getCode() %></td>
                                <td><%= disciplina.getProfessorName()%></td>
                                <td>
                                    <span class="badge <%= disciplina.isProfessor() ? "bg-primary" : "bg-secondary" %>">
                                        <%= disciplina.getUserRole() %>
                                    </span>
                                </td>
                            </tr>
                    <% 
                            } 
                        } else {
                    %>
                            <tr>
                                <td colspan="5" class="text-center text-muted py-3">Nenhuma disciplina cadastrada ainda.</td>
                            </tr>
                    <% 
                        } 
                    %>
                </tbody>
            </table>
        </div>
    </div>
</div>
