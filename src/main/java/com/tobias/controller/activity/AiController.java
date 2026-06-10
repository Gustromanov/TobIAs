package com.tobias.controller.activity;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobias.dto.GenerateQuestionsRequest;
import com.tobias.dto.GeneratedQuestionsResponse;
import com.tobias.service.ai.QuestionGenerationService;

@WebServlet({"/Ai"})
public class AiController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("generate".equals(action)) {
            generateQuestionsWithAi(request, response);
        }
    }

    private void generateQuestionsWithAi(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();

        try {
            GenerateQuestionsRequest aiRequest = mapper.readValue(request.getInputStream(), GenerateQuestionsRequest.class);
            QuestionGenerationService aiService = new QuestionGenerationService();
            GeneratedQuestionsResponse aiResponse = aiService.generate(aiRequest);
            mapper.writeValue(response.getWriter(), aiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String message = e.getMessage() == null ? "Falha ao gerar questões com IA." : e.getMessage();
            mapper.writeValue(response.getWriter(), mapper.createObjectNode().put("error", message));
        }
    }
}