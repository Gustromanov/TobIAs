package com.tobias.dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tobias.model.Activity;

public class ActivityDAO extends BaseDAO{

    public Integer createActivity(Activity activity){
        String sql = "INSERT INTO atividade(disciplina_id, titulo, data_atribuicao, data_entrega, peso) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
            PreparedStatement post = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Preenche os dados da atividade no INSERT.
            post.setInt(1, activity.getIdDiscipline());
            post.setString(2, activity.getTitle());
            post.setDate(3, Date.valueOf(activity.getSubmitDate()));
            if (activity.getDeliveryDate() != null) {
                post.setDate(4, Date.valueOf(activity.getDeliveryDate()));
            } else {
                post.setDate(4, null);
            }
            post.setFloat(5, activity.getPeso());
            post.executeUpdate();

            // Pega o id criado pelo banco para ligar as questoes nessa atividade.
            try (ResultSet generatedKeys = post.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public List<Activity> listActivitiesByDiscipline(int disciplineId){
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT id, disciplina_id, titulo, data_atribuicao, data_entrega, peso FROM atividade WHERE disciplina_id = ? ORDER BY data_entrega";

        try (Connection con = getConnection();
            PreparedStatement post = con.prepareStatement(sql)) {

            post.setInt(1, disciplineId);

            try (ResultSet result = post.executeQuery()) {
                while (result.next()) {
                    Date deliveryDate = result.getDate("data_entrega");

                    Activity activity = new Activity(
                        result.getString("titulo"),
                        result.getDate("data_atribuicao").toLocalDate(),
                        deliveryDate != null ? deliveryDate.toLocalDate() : null,
                        result.getFloat("peso"),
                        result.getInt("id"),
                        result.getInt("disciplina_id")
                    );

                    activities.add(activity);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return activities;
    }

    public Activity getById(int id){
        String sql = "SELECT id, disciplina_id, titulo, data_atribuicao, data_entrega, peso FROM atividade WHERE id = ?";

        try (Connection con = getConnection();
            PreparedStatement post = con.prepareStatement(sql)) {

            post.setInt(1, id);

            try (ResultSet result = post.executeQuery()) {
                if (result.next()) {
                    Date deliveryDate = result.getDate("data_entrega");

                    return new Activity(
                        result.getString("titulo"),
                        result.getDate("data_atribuicao").toLocalDate(),
                        deliveryDate != null ? deliveryDate.toLocalDate() : null,
                        result.getFloat("peso"),
                        result.getInt("id"),
                        result.getInt("disciplina_id")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public void updateActivity(Activity activity){
        String sql = "UPDATE atividade SET titulo = ?, data_atribuicao = ?, data_entrega = ?, peso = ? WHERE id = ?";

        try (Connection con = getConnection();
            PreparedStatement post = con.prepareStatement(sql)) {

            post.setString(1, activity.getTitle());
            post.setDate(2, Date.valueOf(activity.getSubmitDate()));
            if (activity.getDeliveryDate() != null) {
                post.setDate(3, Date.valueOf(activity.getDeliveryDate()));
            } else {
                post.setDate(3, null);
            }
            post.setFloat(4, activity.getPeso());
            post.setInt(5, activity.getId());
            post.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deleteActivity(int id){
        String sql = "DELETE FROM atividade WHERE id = ?";

        try (Connection con = getConnection();
            PreparedStatement post = con.prepareStatement(sql)) {

            post.setInt(1, id);
            post.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
