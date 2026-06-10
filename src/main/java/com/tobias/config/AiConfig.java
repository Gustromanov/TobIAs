package com.tobias.config;

import io.github.cdimascio.dotenv.Dotenv;

public class AiConfig {
    private static final String DEFAULT_GEMINI_MODEL = "gemini-2.5-flash";
    private static Dotenv dotenv;

    static {
        try {
            dotenv = Dotenv.load();
        } catch (Exception e) {
            System.out.println("Arquivo .env não encontrado, usando variáveis de sistema.");
        }
    }

    public static String getGeminiApiKey() {
        String key = System.getenv("GOOGLE_API_KEY");
        if ((key == null || key.isBlank()) && dotenv != null) {
            key = dotenv.get("GOOGLE_API_KEY");
        }

        if (key == null || key.isBlank()) {
            key = System.getenv("GEMINI_API_KEY");
        }
        if ((key == null || key.isBlank()) && dotenv != null) {
            key = dotenv.get("GEMINI_API_KEY");
        }
        return key;
    }

    public static String getGeminiModel() {
        String model = System.getenv("GEMINI_MODEL");
        if ((model == null || model.isBlank()) && dotenv != null) {
            model = dotenv.get("GEMINI_MODEL");
        }

        return model == null || model.isBlank() ? DEFAULT_GEMINI_MODEL : model.trim();
    }
}
