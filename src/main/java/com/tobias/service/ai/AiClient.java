package com.tobias.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tobias.config.AiConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AiClient {

    public String generateContent(String prompt, String fileBase64, String fileMimeType) throws Exception {
        String apiKey = AiConfig.getGeminiApiKey();
        String model = AiConfig.getGeminiModel();

        if (apiKey == null || apiKey.isEmpty()) {
            throw new Exception("API Key do Google/Gemini não configurada no arquivo .env!");
        }

        String geminiUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode partsArray = mapper.createArrayNode();

        ObjectNode textNode = mapper.createObjectNode();
        textNode.put("text", prompt);
        partsArray.add(textNode);

        if (fileBase64 != null && !fileBase64.isEmpty()) {
            ObjectNode inlineDataNode = mapper.createObjectNode();
            inlineDataNode.put("mime_type", fileMimeType != null ? fileMimeType : "application/pdf");
            inlineDataNode.put("data", fileBase64);

            ObjectNode filePartNode = mapper.createObjectNode();
            filePartNode.set("inline_data", inlineDataNode);
            partsArray.add(filePartNode);
        }

        ObjectNode contentNode = mapper.createObjectNode();
        contentNode.set("parts", partsArray);

        ArrayNode contentsArray = mapper.createArrayNode();
        contentsArray.add(contentNode);

        ObjectNode requestBodyNode = mapper.createObjectNode();
        requestBodyNode.set("contents", contentsArray);

        String requestBody = mapper.writeValueAsString(requestBodyNode);

        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(geminiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Erro na API do Gemini: " + response.body());
        }

        JsonNode rootNode = mapper.readTree(response.body());
        String iaResponseText = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

        return iaResponseText.replaceAll("```json", "").replaceAll("```", "").trim();
    }

    public String generateContent(String prompt) throws Exception {
        return generateContent(prompt, null, null);
    }
}
