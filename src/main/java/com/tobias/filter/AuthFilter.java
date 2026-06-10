package com.tobias.filter;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthFilter implements Filter {

    // Rotas que podem ser acessadas sem login.
    // O "/assets/" libera CSS, JavaScript e imagens usados nas telas públicas.
    private static final String[] PUBLIC_PATHS = {
        "/login",
        "/logout",
        "/user/register-form",
        "/user/register-save",
        "/assets/"
    };

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String path = req.getRequestURI().substring(contextPath.length());

        // Se a rota for pública, deixa a requisição seguir normalmente.
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Não cria uma sessão nova aqui; apenas pega a sessão se ela já existir.
        HttpSession session = req.getSession(false);

        // Sem sessão ou sem usuário logado, manda para a tela de login.
        if (session == null || session.getAttribute("userId") == null) {
            res.sendRedirect(contextPath + "/login");
            return;
        }

        // Usuário autenticado: permite acessar a página solicitada.
        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            // Rotas exatas são liberadas por igualdade; pastas públicas usam prefixo.
            if (path.equals(publicPath) || (publicPath.endsWith("/") && path.startsWith(publicPath))) {
                return true;
            }
        }

        return false;
    }
}
