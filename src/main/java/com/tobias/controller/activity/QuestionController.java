package com.tobias.controller.activity;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.tobias.application.FlashMessage;
import com.tobias.dao.ActivityDAO;
import com.tobias.dao.DisciplineDAO;
import com.tobias.dao.QuestionDAO;
import com.tobias.dao.SubmissionDAO;
import com.tobias.model.Activity;
import com.tobias.model.ClosedQuestion;
import com.tobias.model.OpenQuestion;
import com.tobias.model.Question;
import com.tobias.model.User;

@WebServlet({"/Question"})
public class QuestionController extends HttpServlet {

    private QuestionDAO dao = new QuestionDAO();
    private ActivityDAO activityDAO = new ActivityDAO();
    private DisciplineDAO disciplineDAO = new DisciplineDAO();
    private SubmissionDAO submissionDAO = new SubmissionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        FlashMessage.get(request);

        if ("edit".equals(action)) {
            showQuestionEditForm(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/Disciplines");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "" : action) {
            case "update":
                updateQuestion(request, response);
                break;
            case "delete":
                deleteQuestion(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/Disciplines");
                break;
        }
    }

    private void showQuestionEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int questionId = Integer.parseInt(request.getParameter("id"));
        Question question = dao.getById(questionId);

        if (question != null) {
            Activity activity = activityDAO.getById(question.getActivityId());
            if (activity != null && !isProfessor(request, activity.getIdDiscipline())) {
                FlashMessage.set(request, "danger", "Somente o professor pode editar questões.");
                question = null;
            }
        }

        request.setAttribute("question", question);
        request.setAttribute("pageHeading", "Editar Questão");
        request.setAttribute("pageJs", "/assets/js/activity.js");
        request.setAttribute("contentPage", "/WEB-INF/templates/activity/question_edit.jsp");
        
        request.setAttribute("pageTitle", "Gestão Acadêmica");
        request.setAttribute("pageCss", "/assets/css/disciplines.css");
        request.getRequestDispatcher("/WEB-INF/templates/layout/base.jsp").forward(request, response);
    }

    private void updateQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int questionId = Integer.parseInt(request.getParameter("questionId"));
        int activityId = Integer.parseInt(request.getParameter("activityId"));
        Activity activity = activityDAO.getById(activityId);
        
        if (activity == null || !isProfessor(request, activity.getIdDiscipline())) {
            FlashMessage.set(request, "danger", "Somente o professor pode atualizar questões.");
            response.sendRedirect(request.getContextPath() + "/Activity?action=view&id=" + activityId);
            return;
        }

        Question question = buildQuestionFromRequest(request, questionId, activityId);

        dao.updateQuestion(question);
        submissionDAO.deleteByActivity(activityId);
        FlashMessage.set(request, "success", "Questão atualizada com sucesso! As submissões anteriores foram resetadas.");
        response.sendRedirect(request.getContextPath() + "/Activity?action=view&id=" + activityId);
    }

    private void deleteQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int questionId = Integer.parseInt(request.getParameter("questionId"));
        int activityId = Integer.parseInt(request.getParameter("activityId"));
        Activity activity = activityDAO.getById(activityId);
        
        if (activity == null || !isProfessor(request, activity.getIdDiscipline())) {
            FlashMessage.set(request, "danger", "Somente o professor pode excluir questões.");
            response.sendRedirect(request.getContextPath() + "/Activity?action=view&id=" + activityId);
            return;
        }

        dao.deleteQuestion(questionId);
        submissionDAO.deleteByActivity(activityId);
        FlashMessage.set(request, "success", "Questão excluída com sucesso!");
        response.sendRedirect(request.getContextPath() + "/Activity?action=view&id=" + activityId);
    }

    private Question buildQuestionFromRequest(HttpServletRequest request, int questionId, int activityId) {
        String text = request.getParameter("questionText");
        float weight = Float.parseFloat(request.getParameter("questionWeight"));
        String type = request.getParameter("questionType");

        if ("FECHADA".equals(type)) {
            return new ClosedQuestion(
                    questionId, weight, text, activityId,
                    request.getParameter("correctOption"),
                    request.getParameter("optionA"),
                    request.getParameter("optionB"),
                    request.getParameter("optionC"),
                    request.getParameter("optionD"));
        }
        return new OpenQuestion(questionId, weight, text, activityId, request.getParameter("expectedAnswer"));
    }

    private boolean isProfessor(HttpServletRequest request, int disciplineId) {
        User user = (User) request.getSession().getAttribute("usuarioLogado");
        if(user == null) return false;
        return "PROFESSOR".equals(disciplineDAO.getUserRole(disciplineId, user.getId()));
    }
}