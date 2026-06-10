package com.tobias.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class FlashMessage {

    public static void set(HttpServletRequest request, String type, String message) {
        HttpSession session = request.getSession();

        session.setAttribute("flashType", type);
        session.setAttribute("flashMessage", message);
    }

    public static void get(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String type = (String) session.getAttribute("flashType");
        String message = (String) session.getAttribute("flashMessage");

        request.setAttribute("flashType", type);
        request.setAttribute("flashMessage", message);

        clear(session);
    }

    public static void clear(HttpSession session) {
        session.removeAttribute("flashType");
        session.removeAttribute("flashMessage");
    }
}