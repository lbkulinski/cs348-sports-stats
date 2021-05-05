package com.stats.sports;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
import java.util.Objects;

/**
 * A controller for team requests of the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Josh Lefton, jlefton@purdue.edu
 * @version 5/3/2021
 */
@Controller
public final class TeamRequestController {
    /**
     * The connection of the {@code TeamRequestController} class.
     */
    private static final Connection connection;

    /**
     * The next team ID of the {@code TeamRequestController} class.
     */
    private static int next_team_id;

    static {
        connection = DatabaseConnection.getConnection();

        next_team_id = getTeam_id();
    } //static

    /**
     * Returns a DataSource object representing the database
     *
     * @return the DataSource object representing the database
     */
    @Configuration
    public static class DataSourceConfig {

        @Bean
        public static DataSource getDataSource() {
            DataSourceBuilder dsBuilder = DataSourceBuilder.create();
            dsBuilder.url(System.getProperty("url"));
            dsBuilder.username(System.getProperty("username"));
            dsBuilder.password(System.getProperty("password"));
            return dsBuilder.build();
        }
    }

    /**
     * Returns the ID to be assigned to the next team.
     *
     * @return the ID to be assigned to the next team
     */
    private static int getTeam_id() {
        String query;
        ResultSet result_set;
        int id = 0;

        Objects.requireNonNull(TeamRequestController.connection, "the connection is null");

        query = "SELECT MAX(team_id) AS max_id FROM team;";

        try (var statement = TeamRequestController.connection.createStatement()) {
            result_set = statement.executeQuery(query);

            while (result_set.next()) {
                id = result_set.getInt("max_id");
            } //end while

            id++;
        } catch (SQLException e) {
            e.printStackTrace();
        } //end try catch

        return id;
    } //getTeam_id

    /**
     * Returns the form for adding a team.
     *
     * @param model the model to be used in the operation
     * @return the form for adding a team
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("add-team")
    public String addTeamForm(Model model) {
        Team team;

        Objects.requireNonNull(model, "the specified model is null");

        team = new Team();

        model.addAttribute("team", team);

        return "add-team";
    } //addTeamForm

    /**
     * Handles the request for attempting to add the specified team.
     *
     * @param team  the team to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to attempting to add the specified team
     * @throws NullPointerException if the specified team or model is {@code null}
     */
    @PostMapping("add-team")
    public String addTeamSubmit(@ModelAttribute Team team, Model model) {
        int team_id;
        String team_name;
        String sport_id_string;
        int sport_id;
        String insertStatement;
        int id_index = 1;
        int team_name_index = 2;

        Objects.requireNonNull(team, "the specified team is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(TeamRequestController.connection, "the connection is null");

        model.addAttribute("team", team);

        team_id = TeamRequestController.next_team_id;

        TeamRequestController.next_team_id++;

        team_name = team.getTeam_name();

        sport_id_string = team.getSport_id();

        try {
            sport_id = Integer.parseInt(sport_id_string);
        } catch (NumberFormatException e) {
            return "add-team-failure-sport-id-invalid";
        } //end try catch
        DataSource data_source = DataSourceConfig.getDataSource();

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(data_source).withProcedureName("ADD_TEAM");
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("in_team_id", team_id)
                .addValue("in_team_name", team_name)
                .addValue("in_sport_id", sport_id);
        Map<String, Object> out = jdbcCall.execute(parameters);

        return "add-team-success";
    } //addSportSubmit

    /**
     * Returns the form for editing a team.
     *
     * @param model the model to be used in the operation
     * @return the form for editing a team
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("edit-team")
    public String editTeamForm(Model model) {
        EditTeam editTeam;

        Objects.requireNonNull(model, "the specified model is null");

        editTeam = new EditTeam();

        model.addAttribute("editTeam", editTeam);

        return "edit-team";
    } //editTeamForm

    /**
     * Handles the request for attempting to edit a team using the specified edit team.
     *
     * @param editTeam the edit team to be used in the operation
     * @param model    the model to be used in the operation
     * @return the response to attempting to edit a team using the specified edit team
     * @throws NullPointerException if the specified edit team or model is {@code null}
     */
    @PostMapping("edit-team")
    public String editTeamSubmit(@ModelAttribute EditTeam editTeam, Model model) {
        String idString;
        int team_id;
        String field;
        String value;

        Objects.requireNonNull(editTeam, "the specified edit team is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(TeamRequestController.connection, "the connection is null");

        idString = editTeam.getId();

        try {
            team_id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return "edit-team-failure-team-id-invalid";
        } //end try catch

        field = editTeam.getField();

        value = editTeam.getNewValue();

        DataSource data_source = DataSourceConfig.getDataSource();

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(data_source).withProcedureName("EDIT_TEAM");
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("in_team_id", team_id)
                .addValue("in_field", field)
                .addValue("in_value", value);
        Map<String, Object> out = jdbcCall.execute(parameters);
        return "edit-team-success";
    } //editTeamSubmit

    /**
     * Returns the form for deleting a team.
     *
     * @param model the model to be used in the operation
     * @return the form for deleting a team
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("delete-team")
    public String deleteTeamForm(Model model) {
        DeleteTeam deleteTeam;

        Objects.requireNonNull(model, "the specified model is null");

        deleteTeam = new DeleteTeam();

        model.addAttribute("deleteTeam", deleteTeam);

        return "delete-team";
    } //deleteTeamForm

    /**
     * Handles the request for attempting to delete the specified delete team.
     *
     * @param deleteTeam the delete team to be used in the operation
     * @param model      the model to be used in the operation
     * @return the response to attempting to delete the specified delete team
     * @throws NullPointerException if the specified delete team or model is {@code null}
     */
    @PostMapping("delete-team")
    public String deleteTeamSubmit(@ModelAttribute DeleteTeam deleteTeam, Model model) {
        String idString;
        int team_id;

        Objects.requireNonNull(deleteTeam, "the specified delete team is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(TeamRequestController.connection, "the connection is null");

        idString = deleteTeam.getId();

        try {
            team_id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return "delete-team-failure-id-invalid";
        } //end try catch

        DataSource data_source = DataSourceConfig.getDataSource();

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(data_source).withProcedureName("DELETE_TEAM");
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("in_team_id", team_id);
        Map<String, Object> out = jdbcCall.execute(parameters);

        return "delete-team-success";
    } //deleteTeamSubmit
}