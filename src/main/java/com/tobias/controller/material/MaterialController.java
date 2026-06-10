package com.tobias.controller.material;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import com.tobias.application.FlashMessage;
import com.tobias.dao.DisciplineDAO;
import com.tobias.dao.MaterialDAO;
import com.tobias.dao.StudentDAO;
import com.tobias.dao.TeacherDAO;
import com.tobias.model.Material;
import com.tobias.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@MultipartConfig(maxFileSize = 25 * 1024 * 1024, maxRequestSize = 30 * 1024 * 1024)
@WebServlet({
    "/Material"
})
public class MaterialController extends HttpServlet {

    private MaterialDAO materialDAO = new MaterialDAO();
    private DisciplineDAO disciplineDAO = new DisciplineDAO();
    private TeacherDAO teacherDAO = new TeacherDAO();
    private StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        FlashMessage.get(request);

        switch (action == null ? "" : action) {
            case "new":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "download":
                downloadMaterial(request, response);
                return;
            default:
                response.sendRedirect(request.getContextPath() + "/Disciplines");
                return;
        }

        if (response.isCommitted()) {
            return;
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
                createMaterial(request, response);
                break;
            case "update":
                updateMaterial(request, response);
                break;
            case "delete":
                deleteMaterial(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/Disciplines");
                break;
        }
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int disciplineId = Integer.parseInt(request.getParameter("disciplineId"));

        if (!isProfessor(request, disciplineId)) {
            FlashMessage.set(request, "danger", "Somente o professor pode criar materiais.");
            response.sendRedirect(disciplineUrl(request, disciplineId));
            return;
        }

        request.setAttribute("disciplineId", disciplineId);
        request.setAttribute("pageHeading", "Novo Material");
        request.setAttribute("contentPage", "/WEB-INF/templates/material/form.jsp");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int materialId = Integer.parseInt(request.getParameter("id"));
        Material material = materialDAO.getById(materialId);

        if (material == null || !isProfessor(request, material.getDisciplineId())) {
            FlashMessage.set(request, "danger", "Somente o professor pode editar materiais.");
            response.sendRedirect(request.getContextPath() + "/Disciplines");
            return;
        }

        request.setAttribute("material", material);
        request.setAttribute("pageHeading", "Editar Material");
        request.setAttribute("contentPage", "/WEB-INF/templates/material/edit.jsp");
    }

    private void createMaterial(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int disciplineId = Integer.parseInt(request.getParameter("disciplineId"));

        if (!isProfessor(request, disciplineId)) {
            FlashMessage.set(request, "danger", "Somente o professor pode criar materiais.");
            response.sendRedirect(disciplineUrl(request, disciplineId));
            return;
        }

        Material material = buildMaterialFromRequest(request, 0, disciplineId, null);
        Integer materialId = materialDAO.create(material);

        if (materialId != null) {
            FlashMessage.set(request, "success", "Material cadastrado com sucesso!");
        } else {
            FlashMessage.set(request, "danger", "Não foi possível cadastrar o material.");
        }

        response.sendRedirect(disciplineUrl(request, disciplineId));
    }

    private void updateMaterial(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int materialId = Integer.parseInt(request.getParameter("materialId"));
        Material currentMaterial = materialDAO.getById(materialId);

        if (currentMaterial == null || !isProfessor(request, currentMaterial.getDisciplineId())) {
            FlashMessage.set(request, "danger", "Somente o professor pode atualizar materiais.");
            response.sendRedirect(request.getContextPath() + "/Disciplines");
            return;
        }

        Material material = buildMaterialFromRequest(request, materialId, currentMaterial.getDisciplineId(), currentMaterial);
        materialDAO.update(material);

        if (currentMaterial.hasFile() && material.hasFile()
                && !currentMaterial.getFilePath().equals(material.getFilePath())) {
            deleteStoredFile(currentMaterial.getFilePath());
        }

        FlashMessage.set(request, "success", "Material atualizado com sucesso!");
        response.sendRedirect(disciplineUrl(request, currentMaterial.getDisciplineId()));
    }

    private void deleteMaterial(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int materialId = Integer.parseInt(request.getParameter("materialId"));
        Material material = materialDAO.getById(materialId);

        if (material == null || !isProfessor(request, material.getDisciplineId())) {
            FlashMessage.set(request, "danger", "Somente o professor pode excluir materiais.");
            response.sendRedirect(request.getContextPath() + "/Disciplines");
            return;
        }

        materialDAO.delete(materialId);
        deleteStoredFile(material.getFilePath());
        FlashMessage.set(request, "success", "Material excluído com sucesso!");
        response.sendRedirect(disciplineUrl(request, material.getDisciplineId()));
    }

    private void downloadMaterial(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int materialId = Integer.parseInt(request.getParameter("id"));
        Material material = materialDAO.getById(materialId);

        if (material == null || !hasDisciplineAccess(request, material.getDisciplineId()) || !material.hasFile()) {
            FlashMessage.set(request, "danger", "Material não encontrado ou sem arquivo disponível.");
            response.sendRedirect(request.getContextPath() + "/Disciplines");
            return;
        }

        Path file = Paths.get(material.getFilePath());
        if (!Files.exists(file)) {
            FlashMessage.set(request, "danger", "Arquivo do material não foi encontrado no servidor.");
            response.sendRedirect(disciplineUrl(request, material.getDisciplineId()));
            return;
        }

        String fileName = material.getOriginalFileName() != null && !material.getOriginalFileName().isBlank()
                ? material.getOriginalFileName()
                : file.getFileName().toString();
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");

        response.setContentType(material.getContentType() != null ? material.getContentType() : "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
        response.setContentLengthLong(Files.size(file));
        Files.copy(file, response.getOutputStream());
    }

    private Material buildMaterialFromRequest(HttpServletRequest request, int materialId, int disciplineId, Material currentMaterial)
            throws IOException, ServletException {
        Material material = new Material();
        material.setId(materialId);
        material.setDisciplineId(disciplineId);
        material.setTitle(request.getParameter("title"));
        material.setContent(request.getParameter("content"));

        if (currentMaterial != null) {
            material.setFilePath(currentMaterial.getFilePath());
            material.setOriginalFileName(currentMaterial.getOriginalFileName());
            material.setContentType(currentMaterial.getContentType());
            material.setFileSize(currentMaterial.getFileSize());
        }

        Part filePart = request.getPart("file");
        if (filePart != null && filePart.getSize() > 0) {
            StoredFile storedFile = saveUploadedFile(filePart);
            material.setFilePath(storedFile.path());
            material.setOriginalFileName(storedFile.originalName());
            material.setContentType(storedFile.contentType());
            material.setFileSize(storedFile.size());
        }

        return material;
    }

    private StoredFile saveUploadedFile(Part filePart) throws IOException {
        String originalName = sanitizeFileName(filePart.getSubmittedFileName());
        Path uploadDir = getUploadDir();
        Files.createDirectories(uploadDir);

        String storedName = UUID.randomUUID() + "-" + originalName;
        Path storedPath = uploadDir.resolve(storedName);

        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, storedPath, StandardCopyOption.REPLACE_EXISTING);
        }

        return new StoredFile(storedPath.toString(), originalName, filePart.getContentType(), filePart.getSize());
    }

    private Path getUploadDir() {
        String configuredDir = System.getProperty("tobias.upload.dir");
        if (configuredDir != null && !configuredDir.isBlank()) {
            return Paths.get(configuredDir, "materials");
        }

        return Paths.get(System.getProperty("java.io.tmpdir"), "tobias", "materials");
    }

    private String sanitizeFileName(String submittedFileName) {
        if (submittedFileName == null || submittedFileName.isBlank()) {
            return "material";
        }

        return Paths.get(submittedFileName).getFileName().toString().replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private void deleteStoredFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return;
        }

        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private boolean isProfessor(HttpServletRequest request, int disciplineId) {
        User user = getLoggedUser(request);
        User participant = user != null ? resolveParticipant(disciplineId, user) : null;
        return participant != null && participant.canManageDiscipline();
    }

    private boolean hasDisciplineAccess(HttpServletRequest request, int disciplineId) {
        User user = getLoggedUser(request);
        return user != null && resolveParticipant(disciplineId, user) != null;
    }

    private User resolveParticipant(int disciplineId, User user) {
        String role = disciplineDAO.getUserRole(disciplineId, user.getId());

        if ("PROFESSOR".equals(role)) {
            return teacherDAO.getTeacherByUserId(user.getId());
        }

        if ("ALUNO".equals(role)) {
            return studentDAO.getStudentByUserId(user.getId());
        }

        return null;
    }

    private User getLoggedUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("usuarioLogado");
    }

    private String disciplineUrl(HttpServletRequest request, int disciplineId) {
        return request.getContextPath() + "/Disciplines?action=view&id=" + disciplineId;
    }

    private record StoredFile(String path, String originalName, String contentType, long size) {
    }
}