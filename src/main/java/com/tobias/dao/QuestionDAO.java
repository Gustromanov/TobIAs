package com.tobias.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tobias.model.Question;
import com.tobias.model.OpenQuestion;
import com.tobias.model.ClosedQuestion;

public class QuestionDAO extends BaseDAO {

    public void createQuestion(Question question) {
        String sql = "INSERT INTO questao(atividade_id, enunciado, peso) VALUES (?, ?, ?)";

        try (Connection con = getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement post = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                post.setInt(1, question.getActivityId());
                post.setString(2, question.getStatement());
                post.setFloat(3, question.getWeight());
                post.executeUpdate();

                try (ResultSet keys = post.getGeneratedKeys()) {
                    if (!keys.next()) {
                        con.rollback();
                        return;
                    }

                    int questionId = keys.getInt(1);
                    saveQuestionDetails(con, questionId, question);
                    con.commit();
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
    }

    public List<Question> listByActivity(int activityId) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT q.id, q.atividade_id, q.enunciado, q.peso, " +
                "d.resposta, me.opcao_correta, me.opcao_a, me.opcao_b, me.opcao_c, me.opcao_d " +
                "FROM questao q " +
                "LEFT JOIN dissertativa d ON q.id = d.questao_id " +
                "LEFT JOIN multipla_escolha me ON q.id = me.questao_id " +
                "WHERE q.atividade_id = ? " +
                "ORDER BY q.id";

        try (Connection con = getConnection();
             PreparedStatement post = con.prepareStatement(sql)) {

            post.setInt(1, activityId);

            try (ResultSet result = post.executeQuery()) {
                while (result.next()) {
                    questions.add(buildQuestion(result));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return questions;
    }

    public Question getById(int id) {
        String sql = "SELECT q.id, q.atividade_id, q.enunciado, q.peso, " +
                "d.resposta, me.opcao_correta, me.opcao_a, me.opcao_b, me.opcao_c, me.opcao_d " +
                "FROM questao q " +
                "LEFT JOIN dissertativa d ON q.id = d.questao_id " +
                "LEFT JOIN multipla_escolha me ON q.id = me.questao_id " +
                "WHERE q.id = ?";

        try (Connection con = getConnection();
             PreparedStatement post = con.prepareStatement(sql)) {

            post.setInt(1, id);

            try (ResultSet result = post.executeQuery()) {
                if (result.next()) {
                    return buildQuestion(result);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public void updateQuestion(Question question) {
        String sql = "UPDATE questao SET enunciado = ?, peso = ? WHERE id = ?";

        try (Connection con = getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement post = con.prepareStatement(sql)) {
                post.setString(1, question.getStatement());
                post.setFloat(2, question.getWeight());
                post.setInt(3, question.getId());
                post.executeUpdate();

                deleteQuestionDetails(con, question.getId());
                saveQuestionDetails(con, question.getId(), question);
                con.commit();
            } catch (Exception e) {
                con.rollback();
                System.out.println(e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deleteQuestion(int id) {
        String sql = "DELETE FROM questao WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement post = con.prepareStatement(sql)) {

            post.setInt(1, id);
            post.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private Question buildQuestion(ResultSet result) throws Exception {
        String correctOption = result.getString("opcao_correta");

        if (correctOption != null) {
            return new ClosedQuestion(
                    result.getInt("id"),
                    result.getFloat("peso"),
                    result.getString("enunciado"),
                    result.getInt("atividade_id"),
                    correctOption,
                    result.getString("opcao_a"),
                    result.getString("opcao_b"),
                    result.getString("opcao_c"),
                    result.getString("opcao_d"));
        }

        return new OpenQuestion(
                result.getInt("id"),
                result.getFloat("peso"),
                result.getString("enunciado"),
                result.getInt("atividade_id"),
                result.getString("resposta"));
    }

    private void saveQuestionDetails(Connection con, int questionId, Question question) throws Exception {
        if (question instanceof ClosedQuestion) {
            ClosedQuestion closedQuestion = (ClosedQuestion) question;
            String sql = "INSERT INTO multipla_escolha(questao_id, opcao_correta, opcao_a, opcao_b, opcao_c, opcao_d) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement post = con.prepareStatement(sql)) {
                post.setInt(1, questionId);
                post.setString(2, closedQuestion.getCorrectOption());
                post.setString(3, closedQuestion.getOptionA());
                post.setString(4, closedQuestion.getOptionB());
                post.setString(5, closedQuestion.getOptionC());
                post.setString(6, closedQuestion.getOptionD());
                post.executeUpdate();
            }
            return;
        }

        OpenQuestion openQuestion = (OpenQuestion) question;
        String sql = "INSERT INTO dissertativa(questao_id, resposta) VALUES (?, ?)";

        try (PreparedStatement post = con.prepareStatement(sql)) {
            post.setInt(1, questionId);
            post.setString(2, openQuestion.getExpectedAnswer());
            post.executeUpdate();
        }
    }

    private void deleteQuestionDetails(Connection con, int questionId) throws Exception {
        try (PreparedStatement post = con.prepareStatement("DELETE FROM dissertativa WHERE questao_id = ?")) {
            post.setInt(1, questionId);
            post.executeUpdate();
        }

        try (PreparedStatement post = con.prepareStatement("DELETE FROM multipla_escolha WHERE questao_id = ?")) {
            post.setInt(1, questionId);
            post.executeUpdate();
        }
    }
}