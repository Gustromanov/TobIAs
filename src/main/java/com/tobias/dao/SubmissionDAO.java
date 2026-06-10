package com.tobias.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tobias.model.ActivityReportRow;
import com.tobias.model.ActivitySubmission;
import com.tobias.model.SubmissionAnswer;

public class SubmissionDAO extends BaseDAO {

    public boolean hasSubmission(int activityId, int studentId) {
        // Verifica se o aluno ja enviou esta atividade para evitar envio duplicado.
        String sql = "SELECT 1 FROM submissao WHERE atividade_id = ? AND aluno_id = ?";

        try (Connection con = getConnection();
             PreparedStatement post = con.prepareStatement(sql)) {

            post.setInt(1, activityId);
            post.setInt(2, studentId);

            try (ResultSet result = post.executeQuery()) {
                return result.next();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    public Integer createSubmission(int activityId, int studentId, Map<Integer, String> answers) {
        // Cria uma submissao e depois salva cada resposta ligada a essa submissao.
        String submissionSql = "INSERT INTO submissao(aluno_id, atividade_id) VALUES (?, ?)";
        String answerSql = "INSERT INTO resposta_questao(questao_id, aluno_id, submissao_id, resposta) VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection()) {
            // Transacao: submissao e respostas precisam ser salvas juntas.
            con.setAutoCommit(false);

            try (PreparedStatement submissionPost = con.prepareStatement(submissionSql, Statement.RETURN_GENERATED_KEYS)) {
                submissionPost.setInt(1, studentId);
                submissionPost.setInt(2, activityId);
                submissionPost.executeUpdate();

                try (ResultSet keys = submissionPost.getGeneratedKeys()) {
                    if (!keys.next()) {
                        con.rollback();
                        return null;
                    }

                    int submissionId = keys.getInt(1);

                    try (PreparedStatement answerPost = con.prepareStatement(answerSql)) {
                        // Cada item do Map liga uma questao a resposta digitada pelo aluno.
                        for (Map.Entry<Integer, String> answer : answers.entrySet()) {
                            answerPost.setInt(1, answer.getKey());
                            answerPost.setInt(2, studentId);
                            answerPost.setInt(3, submissionId);
                            answerPost.setString(4, answer.getValue());
                            answerPost.addBatch();
                        }

                        answerPost.executeBatch();
                    }

                    con.commit();
                    return submissionId;
                }
            } catch (Exception e) {
                con.rollback();
                System.out.println(e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public void deleteByActivity(int activityId) {
        String sql = "DELETE FROM submissao WHERE atividade_id = ?";

        try (Connection con = getConnection();
             PreparedStatement post = con.prepareStatement(sql)) {

            post.setInt(1, activityId);
            post.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<ActivitySubmission> listByActivity(int activityId) {
        // Lista as entregas de uma atividade para o professor acompanhar quem respondeu.
        List<ActivitySubmission> submissions = new ArrayList<>();
        String sql = "SELECT s.id, s.atividade_id, s.aluno_id, s.data_envio, u.nome, u.email " +
        "FROM submissao s " +
        "INNER JOIN aluno a ON s.aluno_id = a.id " +
        "INNER JOIN usuario u ON a.usuario_id = u.id " +
        "WHERE s.atividade_id = ? " +
        "ORDER BY s.data_envio DESC";

        try (Connection con = getConnection();
             PreparedStatement post = con.prepareStatement(sql)) {

            post.setInt(1, activityId);

            try (ResultSet result = post.executeQuery()) {
                while (result.next()) {
                    ActivitySubmission submission = buildSubmission(result);
                    submissions.add(submission);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return submissions;
    }

    public ActivitySubmission getById(int submissionId) {
        // Busca uma entrega especifica, incluindo o aluno que enviou.
        String sql = "SELECT s.id, s.atividade_id, s.aluno_id, s.data_envio, u.nome, u.email " +
        "FROM submissao s " +
        "INNER JOIN aluno a ON s.aluno_id = a.id " +
        "INNER JOIN usuario u ON a.usuario_id = u.id " +
        "WHERE s.id = ?";

        try (Connection con = getConnection();
             PreparedStatement post = con.prepareStatement(sql)) {

            post.setInt(1, submissionId);

            try (ResultSet result = post.executeQuery()) {
                if (result.next()) {
                    return buildSubmission(result);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public List<SubmissionAnswer> listAnswersBySubmission(int submissionId) {
        // Junta respostas com questoes para o professor ver enunciado e resposta lado a lado.
        List<SubmissionAnswer> answers = new ArrayList<>();
        String sql = "SELECT q.id AS questao_id, q.enunciado, q.peso, rq.resposta " +
        "FROM resposta_questao rq " +
        "INNER JOIN questao q ON rq.questao_id = q.id " +
        "WHERE rq.submissao_id = ? " +
        "ORDER BY q.id";

        try (Connection con = getConnection();
             PreparedStatement post = con.prepareStatement(sql)) {

            post.setInt(1, submissionId);

            try (ResultSet result = post.executeQuery()) {
                while (result.next()) {
                    SubmissionAnswer answer = new SubmissionAnswer();
                    answer.setQuestionId(result.getInt("questao_id"));
                    answer.setQuestionText(result.getString("enunciado"));
                    answer.setQuestionWeight(result.getFloat("peso"));
                    answer.setAnswerText(result.getString("resposta"));
                    answers.add(answer);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return answers;
    }

    public List<ActivityReportRow> buildActivityReport(int activityId) {
        List<ActivityReportRow> report = new ArrayList<>();
        String sql = "SELECT s.id AS submissao_id, s.aluno_id, s.data_envio, u.nome, u.email, " +
        "COUNT(q.id) AS total_questoes, " +
        "SUM(CASE " +
        "WHEN me.questao_id IS NOT NULL AND UPPER(TRIM(COALESCE(rq.resposta, ''))) = UPPER(TRIM(me.opcao_correta)) THEN 1 " +
        "WHEN d.questao_id IS NOT NULL AND LOWER(TRIM(COALESCE(rq.resposta, ''))) = LOWER(TRIM(COALESCE(d.resposta, ''))) THEN 1 " +
        "ELSE 0 END) AS total_acertos " +
        "FROM submissao s " +
        "INNER JOIN aluno a ON s.aluno_id = a.id " +
        "INNER JOIN usuario u ON a.usuario_id = u.id " +
        "INNER JOIN questao q ON q.atividade_id = s.atividade_id " +
        "LEFT JOIN resposta_questao rq ON rq.submissao_id = s.id AND rq.questao_id = q.id " +
        "LEFT JOIN multipla_escolha me ON me.questao_id = q.id " +
        "LEFT JOIN dissertativa d ON d.questao_id = q.id " +
        "WHERE s.atividade_id = ? " +
        "GROUP BY s.id, s.aluno_id, s.data_envio, u.nome, u.email " +
        "ORDER BY u.nome";

        try (Connection con = getConnection();
             PreparedStatement post = con.prepareStatement(sql)) {

            post.setInt(1, activityId);

            try (ResultSet result = post.executeQuery()) {
                while (result.next()) {
                    ActivityReportRow row = new ActivityReportRow();
                    row.setSubmissionId(result.getInt("submissao_id"));
                    row.setStudentId(result.getInt("aluno_id"));
                    row.setStudentName(result.getString("nome"));
                    row.setStudentEmail(result.getString("email"));
                    row.setTotalQuestions(result.getInt("total_questoes"));
                    row.setCorrectAnswers(result.getInt("total_acertos"));

                    Timestamp submittedAt = result.getTimestamp("data_envio");
                    if (submittedAt != null) {
                        row.setSubmittedAt(submittedAt.toLocalDateTime());
                    }

                    report.add(row);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return report;
    }

    private ActivitySubmission buildSubmission(ResultSet result) throws Exception {
        // Monta o objeto usado nas telas de entregas.
        ActivitySubmission submission = new ActivitySubmission();
        submission.setId(result.getInt("id"));
        submission.setActivityId(result.getInt("atividade_id"));
        submission.setStudentId(result.getInt("aluno_id"));
        submission.setStudentName(result.getString("nome"));
        submission.setStudentEmail(result.getString("email"));

        Timestamp submittedAt = result.getTimestamp("data_envio");
        if (submittedAt != null) {
            submission.setSubmittedAt(submittedAt.toLocalDateTime());
        }

        return submission;
    }
}
