package com.tobias.service.ai;

import java.util.List;

import com.tobias.model.Activity;
import com.tobias.model.ActivityReportRow;

public class ActivityReportGenerationService {
    private AiClient aiClient;

    public ActivityReportGenerationService() {
        this.aiClient = new AiClient();
    }

    public String generate(Activity activity, List<ActivityReportRow> rows) throws Exception {
        StringBuilder data = new StringBuilder();

        for (ActivityReportRow row : rows) {
            data.append("- Aluno: ").append(row.getStudentName())
                    .append(" | Email: ").append(row.getStudentEmail())
                    .append(" | Acertos: ").append(row.getCorrectAnswers())
                    .append("/").append(row.getTotalQuestions())
                    .append(" | Enviado em: ")
                    .append(row.getSubmittedAt() == null ? "sem data" : row.getSubmittedAt())
                    .append("\n");
        }

        String prompt = "Gere um relatório curto, claro e em português do Brasil para o professor.\n"
                + "Atividade: " + activity.getTitle() + "\n"
                + "Peso da atividade: " + activity.getPeso() + "\n"
                + "Use somente os dados abaixo. Informe quais alunos fizeram a atividade e quantas questões acertaram.\n"
                + "No final, inclua uma observação geral de desempenho em 1 ou 2 frases.\n"
                + "Dados:\n" + (data.length() == 0 ? "Nenhuma entrega registrada." : data.toString());

        return aiClient.generateContent(prompt);
    }
}
