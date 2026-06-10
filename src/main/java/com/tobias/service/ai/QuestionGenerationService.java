package com.tobias.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobias.dto.GenerateQuestionsRequest;
import com.tobias.dto.GeneratedQuestionsResponse;

public class QuestionGenerationService {

    private AiClient aiClient;
    private ObjectMapper mapper;

    public QuestionGenerationService() {
        this.aiClient = new AiClient();
        this.mapper = new ObjectMapper();
    }

    public GeneratedQuestionsResponse generate(GenerateQuestionsRequest request) throws Exception {
        String promptTemplate;

       
        if ("ABERTA".equals(request.getQuestionType())) {
            promptTemplate = QuestionPromptTemplate.PROMPT_ABERTA;
        } else if ("FECHADA".equals(request.getQuestionType())) {
            promptTemplate = QuestionPromptTemplate.PROMPT_FECHADA;
        } else {
            throw new IllegalArgumentException("Tipo de questão inválido.");
        }


        String finalPrompt = String.format(promptTemplate, 
                request.getQuantity(), 
                request.getDifficulty(), 
                request.getMaterial());

        String jsonResponse = aiClient.generateContent(finalPrompt, request.getFileBase64(),request.getFileMimeType());

        return mapper.readValue(jsonResponse, GeneratedQuestionsResponse.class);
    }
}