package com.tobias.controller.disciplines;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.tobias.application.FlashMessage;
import com.tobias.dao.ActivityDAO;
import com.tobias.dao.DisciplineDAO;
import com.tobias.dao.MaterialDAO;
import com.tobias.dao.StudentDAO;
import com.tobias.dao.TeacherDAO;
import com.tobias.dao.WarningDAO;
import com.tobias.model.Activity;
import com.tobias.model.Student;
import com.tobias.model.Discipline;
import com.tobias.model.Material;
import com.tobias.model.Teacher;
import com.tobias.model.User;
import com.tobias.model.Warning;

@WebServlet({ "/Disciplines" })
public class DisciplinesController extends HttpServlet {

    private DisciplineDAO dao = new DisciplineDAO();
    private StudentDAO studentDao = new StudentDAO();
    private TeacherDAO teacherDao = new TeacherDAO();
    private ActivityDAO activityDao = new ActivityDAO();
    private MaterialDAO materialDao = new MaterialDAO();
    private WarningDAO warningDao = new WarningDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        FlashMessage.get(request);

        switch (action == null ? "" : action) {
            case "new":
                showNewForm(request);
                break;
            case "enter":
                showEnterForm(request);
                break;
            case "view":
                showDisciplineDetails(request, response);
                return; 
            default:
                listDisciplines(request);
                break;
        }

        request.setAttribute("pageTitle", "Gestão Acadêmica");
        request.setAttribute("pageCss", "/assets/css/disciplines.css");
        request.getRequestDispatcher("/WEB-INF/templates/layout/base.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        switch (action == null ? "" : action) {
            case "new":
                createDiscipline(request, response);
                break;
            case "enter":
                enterDiscipline(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/Disciplines");
                break;
        }
    }

    private void showNewForm(HttpServletRequest request) {
        request.setAttribute("pageHeading", "Nova Disciplina");
        request.setAttribute("contentPage", "/WEB-INF/templates/disciplines/form.jsp");
    }

    private void showEnterForm(HttpServletRequest request) {
        request.setAttribute("pageHeading", "Entrar na Disciplina");
        request.setAttribute("contentPage", "/WEB-INF/templates/disciplines/form_enter.jsp");
    }

    private void listDisciplines(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("usuarioLogado");
        List<Discipline> lista = dao.listDisciplines(user.getId());
        request.setAttribute("listaDisciplines", lista);
        request.setAttribute("pageHeading", "Disciplinas");
        request.setAttribute("contentPage", "/WEB-INF/templates/disciplines/disciplines.jsp");
    }

    private void showDisciplineDetails(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int disciplineId = Integer.parseInt(request.getParameter("id"));
        User loggedUser = (User) request.getSession().getAttribute("usuarioLogado");
        
        Discipline discipline = loggedUser != null ? dao.getById(disciplineId, loggedUser.getId()) : null;
        User participant = resolveParticipant(discipline, loggedUser);

        if (discipline == null || participant == null) {
            FlashMessage.set(request, "danger", "Você não participa dessa disciplina.");
            response.sendRedirect(request.getContextPath() + "/Disciplines");
            return;
        }

        discipline.setUserRole(participant.getRoleName());
        List<Activity> activities = activityDao.listActivitiesByDiscipline(disciplineId);
        List<Material> materials = materialDao.listByDiscipline(disciplineId);
        List<User> students = dao.listStudentsByDiscipline(disciplineId);
        List<Warning> warnings = warningDao.listWarningsByDiscipline(disciplineId);

        request.setAttribute("discipline", discipline);
        request.setAttribute("participant", participant);
        request.setAttribute("activities", activities);
        request.setAttribute("materials", materials);
        request.setAttribute("students", students);
        request.setAttribute("warnings", warnings);
        
        request.setAttribute("pageHeading", "Sala de Aula");
        request.setAttribute("contentPage", "/WEB-INF/templates/disciplines/discipline_details.jsp");
        
        request.setAttribute("pageTitle", "Gestão Acadêmica");
        request.setAttribute("pageCss", "/assets/css/disciplines.css");
        request.getRequestDispatcher("/WEB-INF/templates/layout/base.jsp").forward(request, response);
    }

    private void createDiscipline(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String description = request.getParameter("description");

        Discipline d = new Discipline();
        d.setName(name);
        d.setCode(code);
        d.setDescription(description);

        User usuarioLogado = (User) request.getSession().getAttribute("usuarioLogado");
        if (usuarioLogado != null) {
            int teacherId = teacherDao.getOrCreateTeacher(usuarioLogado.getId());
            d.setIdProfessor(teacherId);
        }

        dao.save(d);

        FlashMessage.set(request, "success", "Disciplina cadastrada com sucesso!");
        response.sendRedirect(request.getContextPath() + "/Disciplines");
    }

    private void enterDiscipline(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String codeTyped = request.getParameter("code");
        User user = (User) request.getSession().getAttribute("usuarioLogado");
        
        if (codeTyped != null && user != null) {
            int disciplineId = dao.getByCode(codeTyped);

            if (disciplineId != -1) {
                int studentId = studentDao.getOrCreateStudent(user.getId());
                dao.enrollStudent(studentId, disciplineId);
                FlashMessage.set(request, "success", "Você entrou na disciplina com sucesso!");
            } else {
                FlashMessage.set(request, "danger", "Código de disciplina não encontrado.");
            }
        }
        response.sendRedirect(request.getContextPath() + "/Disciplines");
    }

    private User resolveParticipant(Discipline discipline, User loggedUser) {
        if (discipline == null || loggedUser == null || discipline.getUserRole() == null) {
            return null;
        }

        if ("PROFESSOR".equals(discipline.getUserRole())) {
            return teacherDao.getTeacherByUserId(loggedUser.getId());
        }

        if ("ALUNO".equals(discipline.getUserRole())) {
            return studentDao.getStudentByUserId(loggedUser.getId());
        }

        return null;
    }
}