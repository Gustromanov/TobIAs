package com.tobias.controller.auth;

import java.io.IOException;

import com.tobias.application.FlashMessage;
import com.tobias.config.PasswordHash;
import com.tobias.dao.AuthDAO;
import com.tobias.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/login", "/logout"})
public class AuthController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getServletPath();

        switch (action) {
            case "/logout":
                logout(request, response);
                return;

            case "/login":
            default:
                showLogin(request, response);
                return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getServletPath();

        switch (action) {
            case "/login":
                login(request, response);
                return;

            default:
                response.sendRedirect(request.getContextPath() + "/login");
                return;
        }
    }

    // ============================
    // Métodos auxiliares
    // ============================

    private void showLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FlashMessage.get(request);

        request.getRequestDispatcher("/WEB-INF/templates/auth/login.jsp")
                .forward(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        AuthDAO dao = new AuthDAO();
        User user = dao.buscarPorEmail(email);

        boolean senhaValida = false;

        if (user != null) {
            String senhaSalva = user.getPassword();

            if (PasswordHash.isBcryptHash(senhaSalva)) {
                senhaValida = PasswordHash.checkPassword(senha, senhaSalva);
            } else if (senhaSalva != null && senhaSalva.equals(senha)) {
                senhaValida = true;
            }
        }

        if (senhaValida) {
            HttpSession session = request.getSession();

            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("usuarioLogado", user);

            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        FlashMessage.set(request, "danger", "Credenciais inválidas");
        response.sendRedirect(request.getContextPath() + "/login");
    }
}