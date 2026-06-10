package com.tobias.service.ai;

public class QuestionPromptTemplate {

    public static String PROMPT_ABERTA = 
        "Tu és um assistente educacional especializado em criar questões para atividades escolares.\n" +
        "Gera %d questões discursivas com dificuldade %s, usando exclusivamente o material de apoio abaixo.\n\n" +
        "Regras:\n" +
        "- Usa apenas informações presentes no material.\n" +
        "- Cada questão deve ter um enunciado claro.\n" +
        "- Cada questão deve ter uma resposta esperada.\n" +
        "- Responde SOMENTE com JSON válido. Não uses Markdown ou crases (```json). Não escrevas texto antes ou depois do JSON.\n\n" +
        "Formato obrigatório:\n" +
        "{\n" +
        "  \"questions\": [\n" +
        "    {\n" +
        "      \"type\": \"ABERTA\",\n" +
        "      \"statement\": \"Enunciado da questão\",\n" +
        "      \"expectedAnswer\": \"Resposta esperada\"\n" +
        "    }\n" +
        "  ]\n" +
        "}\n\n" +
        "Material de apoio:\n%s";

    public static String PROMPT_FECHADA = 
        "Tu és um assistente educacional especializado em criar questões para atividades escolares.\n" +
        "Gera %d questões de múltipla escolha com dificuldade %s, usando exclusivamente o material de apoio abaixo.\n\n" +
        "Regras:\n" +
        "- Usa apenas informações presentes no material.\n" +
        "- Cada questão deve ter exatamente quatro alternativas: A, B, C e D.\n" +
        "- Cada questão deve ter exatamente uma alternativa correta.\n" +
        "- Responde SOMENTE com JSON válido. Não uses Markdown ou crases (```json). Não escrevas texto antes ou depois do JSON.\n\n" +
        "Formato obrigatório:\n" +
        "{\n" +
        "  \"questions\": [\n" +
        "    {\n" +
        "      \"type\": \"FECHADA\",\n" +
        "      \"statement\": \"Enunciado da questão\",\n" +
        "      \"alternatives\": {\n" +
        "        \"A\": \"Texto A\",\n" +
        "        \"B\": \"Texto B\",\n" +
        "        \"C\": \"Texto C\",\n" +
        "        \"D\": \"Texto D\"\n" +
        "      },\n" +
        "      \"correctOption\": \"A\"\n" +
        "    }\n" +
        "  ]\n" +
        "}\n\n" +
        "Material de apoio:\n%s";
}