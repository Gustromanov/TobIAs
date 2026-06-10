package com.tobias.controller.activity;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.tobias.application.FlashMessage;
import com.tobias.dao.ActivityDAO;
import com.tobias.dao.DisciplineDAO;
import com.tobias.dao.QuestionDAO;
import com.tobias.dao.StudentDAO;
import com.tobias.dao.SubmissionDAO;
import com.tobias.model.Activity;
import com.tobias.model.ActivitySubmission;
import com.tobias.model.Question;
import com.tobias.model.Student;
import com.tobias.model.SubmissionAnswer;
import com.tobias.model.User;

@WebServlet({"/Submission"})
public class SubmissionController extends HttpServlet {

    private ActivityDAO activityDAO = new ActivityDAO();
    private SubmissionDAO submissionDAO = new SubmissionDAO();
    private DisciplineDAO disciplineDAO = new DisciplineDAO();
    private QuestionDAO questionDAO = new QuestionDAO();
    private StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        FlashMessage.get(request);

        if ("view".equals(action)) {
            showSubmissionDetails(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/Disciplines");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("submit".equals(action)) {
            submitActivity(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/Disciplines");
        }
    }

    private void showSubmissionDetails(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int submissionId = Integer.parseInt(request.getParameter("id"));
        ActivitySubmission submission = submissionDAO.getById(submissionId);
        Activity activity = submission != null ? activityDAO.getById(submission.getActivityId()) : null;

        if (submission == null || activity == null || !isProfessor(request, activity.getIdDiscipline())) {
            FlashMessage.set(request, "danger", "Somente o professor da disciplina pode ver as respostas.");
            response.sendRedirect(request.getContextPath() + "/Disciplines");
            return;
        }

        List<SubmissionAnswer> answers = submissionDAO.listAnswersBySubmission(submissionId);

        request.setAttribute("activity", activity);
        request.setAttribute("submission", submission);
        request.setAttribute("answers", answers);
        request.setAttribute("pageHeading", "Respostas do Aluno");
        request.setAttribute("contentPage", "/WEB-INF/templates/activity/submission_details.jsp");
        
        request.setAttribute("pageTitle", "Gestão Acadêmica");
        request.setAttribute("pageCss", "/assets/css/disciplines.css");
        request.getRequestDispatcher("/WEB-INF/templates/layout/base.jsp").forward(request, response);
    }

    private void submitActivity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int activityId = Integer.parseInt(request.getParameter("activityId"));
        User user = (User) request.getSession().getAttribute("usuarioLogado");
        String role = user != null ? disciplineDAO.getUserRole(activityDAO.getById(activityId).getIdDiscipline(), user.getId()) : null;

        if (user == null || !"ALUNO".equals(role)) {
            FlashMessage.set(request, "danger", "Somente alunos da disciplina podem enviar a atividade.");
            response.sendRedirect(request.getContextPath() + "/Activity?action=view&id=" + activityId);
            return;
        }

        Student student = studentDAO.getStudentByUserId(user.getId());
        int studentId = student.getStudentId();

        if (submissionDAO.hasSubmission(activityId, studentId)) {
            FlashMessage.set(request, "danger", "Você já enviou essa atividade.");
            response.sendRedirect(request.getContextPath() + "/Activity?action=view&id=" + activityId);
            return;
        }

        List<Question> questions = questionDAO.listByActivity(activityId);
        Map<Integer, String> answers = new LinkedHashMap<>();

        for (Question question : questions) {
            String answer = request.getParameter("answer_" + question.getId());
            if (answer != null && !answer.isBlank()) {
                answers.put(question.getId(), answer.trim());
            }
        }

        if (answers.isEmpty()) {
            FlashMessage.set(request, "danger", "Preencha pelo menos uma resposta antes de enviar.");
            response.sendRedirect(request.getContextPath() + "/Activity?action=view&id=" + activityId);
            return;
        }

        Integer submissionId = submissionDAO.createSubmission(activityId, studentId, answers);
        if (submissionId != null) {
            FlashMessage.set(request, "success", "Atividade enviada com sucesso!");
        } else {
            FlashMessage.set(request, "danger", "Não foi possível enviar a atividade.");
        }

        response.sendRedirect(request.getContextPath() + "/Activity?action=view&id=" + activityId);
    }

    private boolean isProfessor(HttpServletRequest request, int disciplineId) {
        User user = (User) request.getSession().getAttribute("usuarioLogado");
        if(user == null) return false;
        return "PROFESSOR".equals(disciplineDAO.getUserRole(disciplineId, user.getId()));
    }
}