package com.tobias.controller.warning;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.tobias.application.FlashMessage;
import com.tobias.dao.WarningDAO;
import com.tobias.model.Comment;
import com.tobias.model.Warning;
import com.tobias.model.User;

@WebServlet({"/Warning"})
public class WarningController extends HttpServlet {

    private WarningDAO dao = new WarningDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        User user = (User) request.getSession().getAttribute("usuarioLogado");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        switch (action == null ? "" : action) {
            case "new":
                createWarning(request, response, user);
                break;
            case "new-comment":
                createComment(request, response, user);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/Disciplines");
                break;
        }
    }

    private void createWarning(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        int disciplineId = Integer.parseInt(request.getParameter("disciplineId"));
        String content = request.getParameter("content");

        // Regra de negócio: Apenas quem está logado pode criar, e no JSP vamos bloquear para ser só professor
        if (content != null && !content.trim().isEmpty()) {
            Warning warning = new Warning();
            warning.setDisciplineId(disciplineId);
            warning.setUserId(user.getId());
            warning.setContent(content.trim());

            dao.createWarning(warning);
            FlashMessage.set(request, "success", "Aviso publicado com sucesso no mural!");
        } else {
            FlashMessage.set(request, "danger", "O conteúdo do aviso não pode estar vazio.");
        }

        // Redireciona de volta para a aba "mural" da disciplina
        response.sendRedirect(request.getContextPath() + "/Disciplines?action=view&id=" + disciplineId + "&tab=mural");
    }

    private void createComment(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        int warningId = Integer.parseInt(request.getParameter("warningId"));
        int disciplineId = Integer.parseInt(request.getParameter("disciplineId"));
        String content = request.getParameter("content");

        if (content != null && !content.trim().isEmpty()) {
            Comment comment = new Comment();
            comment.setWarningId(warningId);
            comment.setUserId(user.getId());
            comment.setContent(content.trim());

            dao.addComment(comment);
            FlashMessage.set(request, "success", "Comentário adicionado!");
        } else {
            FlashMessage.set(request, "danger", "O comentário não pode estar vazio.");
        }

        response.sendRedirect(request.getContextPath() + "/Disciplines?action=view&id=" + disciplineId + "&tab=mural");
    }
}