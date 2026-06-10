package com.tobias.controller.user;

import java.io.IOException;

import com.tobias.application.FlashMessage;
import com.tobias.config.PasswordHash;
import com.tobias.dao.UserDAO;
import com.tobias.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, 
    maxFileSize = 1024 * 1024 * 10,       
    maxRequestSize = 1024 * 1024 * 15     
)

@WebServlet({
    "/user/register-form",
    "/user/register-save",
    "/user/update-form",
    "/user/update-save",
    "/user/read",
    "/user/delete"
})
public class UserController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getServletPath();

        switch (action) {
            case "/user/register-form":
                showRegisterForm(request, response);
                return;

            case "/user/update-form":
                showUpdateForm(request, response);
                return;

            case "/user/read":
                return;

            case "/user/delete":
                deleteUser(request,response);
                return;

            default:
                response.sendRedirect(request.getContextPath() + "/404");
                return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getServletPath();

        switch (action) {
            case "/user/register-save":
                registerUser(request, response);
                return;

            case "/user/update-save":
                userUpdate(request,response);
                return;

            default:
                response.sendRedirect(request.getContextPath() + "/404");
                return;
        }
    }

    private void showRegisterForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FlashMessage.get(request);

        request.getRequestDispatcher("/WEB-INF/templates/user/register.jsp")
                .forward(request, response);
    }

    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FlashMessage.get(request);

        request.setAttribute("pageHeading", "Meu Perfil");
        request.setAttribute("contentPage", "/WEB-INF/templates/user/update.jsp");
        request.setAttribute("pageJs", "/assets/js/cpf-mask.js");
        request.getRequestDispatcher("/WEB-INF/templates/layout/base.jsp")
                .forward(request, response);
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String cpfText = request.getParameter("cpf");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        try {
            String cpf = onlyDigits(cpfText);
            if (!isValidCpf(cpf)) {
                FlashMessage.set(request, "danger", "CPF inválido.");
                response.sendRedirect(request.getContextPath() + "/user/register-form");
                return;
            }

            String senhaHash = PasswordHash.hashPassword(senha);

            User user = new User(nome, cpf, email, senhaHash, 0);

            UserDAO userDAO = new UserDAO();
            userDAO.inserirUsuario(user); 

            FlashMessage.set(request, "success", "Usuário cadastrado com sucesso!");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;

        } catch (Exception e) {
            e.printStackTrace();

            String msg = "Falha ao cadastrar usuário";

            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate")) {
                msg = "CPF ou e-mail já cadastrado";
            }

            FlashMessage.set(request, "danger", msg);
            response.sendRedirect(request.getContextPath() + "/user/register-form");
        }
    }

    private void userUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException{
        try{
            String name = request.getParameter("nome");
            String cpfText = request.getParameter("cpf");
            String email = request.getParameter("email");
            String password = request.getParameter("senha");
            User userLogado = (User) request.getSession().getAttribute("usuarioLogado");
            String cpf = onlyDigits(cpfText);
            if (!isValidCpf(cpf)) {
                FlashMessage.set(request, "danger", "CPF inválido.");
                response.sendRedirect(request.getContextPath() + "/user/update-form");
                return;
            }

            int id = userLogado.getId();

            if(password== null || password.isBlank()){
                password = userLogado.getPassword();
            }else{
                password = PasswordHash.hashPassword(password);
            }

            User user = new User(name,cpf,email,password,id);

            Part filePart = request.getPart("foto");
            if(filePart != null && filePart.getSize() > 0){
                String fileName = java.nio.file.Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                String newFileName = "user_"+ userLogado.getId()+"_"+fileName;

                String uploadPath = getServletContext().getRealPath("")+File.separator+"assets"+File.separator+"images"+File.separator+"avatar";

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()){
                    uploadDir.mkdirs();
                }

                filePart.write(uploadPath + File.separator + newFileName);
                user.setPhoto(newFileName);
            }else{
                user.setPhoto(userLogado.getPhoto());
            }

            UserDAO dao = new UserDAO();
            dao.updateUser(user);

            request.getSession().setAttribute("usuarioLogado", user);

            FlashMessage.set(request, "success", "Usuário atualizado com sucesso!");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;

        }catch(Exception e){
            String msg = "Falha ao atualizar dados";
            FlashMessage.set(request,"danger",msg);
            response.sendRedirect(request.getContextPath() + "/user/update-form");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
        User userLoged = (User) request.getSession().getAttribute("usuarioLogado");
        if(userLoged != null){
            try{
                UserDAO dao = new UserDAO();
                dao.deleteUser(userLoged.getId());

                request.getSession().invalidate();

                FlashMessage.set(request, "success", "Sua conta e todos os dados vinculados foram excluídos.");
                response.sendRedirect(request.getContextPath() + "/");
            }catch(Exception e){
                e.printStackTrace();
                FlashMessage.set(request, "danger", "Erro ao excluir a conta.");
                response.sendRedirect(request.getContextPath() + "/user/update-form");
            }
        }
    }

    private String onlyDigits(String value) {
        if (value == null) {
            return "";
        }

        return value.replaceAll("\\D", "");
    }

    private boolean isValidCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int firstDigit = calculateCpfDigit(cpf, 9);
        int secondDigit = calculateCpfDigit(cpf, 10);

        return firstDigit == Character.getNumericValue(cpf.charAt(9))
                && secondDigit == Character.getNumericValue(cpf.charAt(10));
    }

    private int calculateCpfDigit(String cpf, int length) {
        int sum = 0;

        for (int i = 0; i < length; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (length + 1 - i);
        }

        int remainder = (sum * 10) % 11;
        return remainder == 10 ? 0 : remainder;
    }
}
