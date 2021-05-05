package com.stats.sports;

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
import java.util.ArrayList;
import java.util.List;
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
            TeamRequestController.connection.setAutoCommit(false);
            result_set = statement.executeQuery(query);

            while (result_set.next()) {
                id = result_set.getInt("max_id");
            } //end while

            id++;
            TeamRequestController.connection.commit();
        } catch (SQLException err) {
            try {
                TeamRequestController.connection.rollback();
            } catch (SQLException err_2) {
                err_2.printStackTrace();
            }
            err.printStackTrace();
        } finally {
            try {
                TeamRequestController.connection.setAutoCommit(true);
            } catch (SQLException err_3) {
                err_3.printStackTrace();
            }
        }//end try catch
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

        try {
            TeamRequestController.connection.setAutoCommit(false);
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(data_source).withProcedureName("ADD_TEAM");
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("in_team_id", team_id)
                    .addValue("in_team_name", team_name)
                    .addValue("in_sport_id", sport_id);
            Map<String, Object> out = jdbcCall.execute(parameters);
            TeamRequestController.connection.commit();
        } catch (SQLException err) {
            try {
                TeamRequestController.connection.rollback();
            } catch (SQLException err_2) {
                err_2.printStackTrace();
            }
            err.printStackTrace();
        } finally {
            try {
                TeamRequestController.connection.setAutoCommit(true);
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }


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
            return "edit-team-failure-id-invalid";
        } //end try catch

        field = editTeam.getField();

        value = editTeam.getNewValue();

        DataSource data_source = DataSourceConfig.getDataSource();

        try {
            TeamRequestController.connection.setAutoCommit(false);
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(data_source).withProcedureName("EDIT_TEAM");
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("in_team_id", team_id)
                    .addValue("in_field", field)
                    .addValue("in_value", value);
            Map<String, Object> out = jdbcCall.execute(parameters);
            TeamRequestController.connection.commit();
        } catch (SQLException err) {
            try {
                TeamRequestController.connection.rollback();
            } catch (SQLException err_2) {
                err_2.printStackTrace();
            }
            err.printStackTrace();
        } finally {
            try {
                TeamRequestController.connection.setAutoCommit(true);
            } catch (SQLException err_3) {
                err_3.printStackTrace();
            }
        }//end try catch
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

        try {
            TeamRequestController.connection.setAutoCommit(false);
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(data_source).withProcedureName("DELETE_TEAM");
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("in_team_id", team_id);
            Map<String, Object> out = jdbcCall.execute(parameters);
            TeamRequestController.connection.commit();
        } catch (SQLException err) {
            try {
                TeamRequestController.connection.rollback();
            } catch (SQLException err_2) {
                err_2.printStackTrace();
            }
            err.printStackTrace();
        } finally {
            try {
                TeamRequestController.connection.setAutoCommit(true);
            } catch (SQLException err_3) {
                err_3.printStackTrace();
            }
        }//end try catch

        return "delete-team-success";
    } //deleteTeamSubmit

    /**
     * Returns the form for searching for a team.
     *
     * @param model the model to be used in the operation
     * @return the form for searching for a team
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("search-team")
    public String searchTeamForm(Model model) {
        SearchTeam searchTeam;

        Objects.requireNonNull(model, "the specified model is null");

        searchTeam = new SearchTeam();

        model.addAttribute("searchTeam", searchTeam);

        return "search-team";
    } //searchTeamForm

    /**
     * Handles the request for searching for the specified search team.
     *
     * @param searchTeam the search team to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to searching for the specified search team
     * @throws NullPointerException if the specified search team or model is {@code null}
     */
    @PostMapping(value = "search-team", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String searchTeamSubmit(@ModelAttribute SearchTeam searchTeam, Model model) {
        String field;
        String searchValueString;
        int searchValueInteger = 0;
        String whereClause;
        String format;
        String searchQuery;
        int searchValueIndex = 1;
        ResultSet resultSet;
        List<Integer> team_ids;
        List<String> team_names;
        List<Integer> sport_ids;
        int team_id;
        String team_name;
        int sport_id;
        StringBuilder stringBuilder;
        String tableString;
        String htmlString;

        Objects.requireNonNull(searchTeam, "the specified search team is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(TeamRequestController.connection, "the connection is null");

        model.addAttribute("searchTeam", searchTeam);

        field = searchTeam.getField();

        searchValueString = searchTeam.getSearchValue();

        if (!Objects.equals(field, "team_name")) {
            try {
                searchValueInteger = Integer.parseInt(searchValueString);
            } catch (NumberFormatException e) {
                return "<!DOCTYPE HTML>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<title>Search Team</title>\n" +
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>Search Team</h1>\n" +
                        "<p>No teams with that search value exist.</p>\n" +
                        "</body>\n" +
                        "</html>";
            } //end try catch
        } //end if

        if (Objects.equals(field, "team_name")) {
            whereClause = "UPPER(team_name) = UPPER(?)";
        } else {
            format = "%s = ?";

            whereClause = String.format(format, field);
        } //end if

        format = "SELECT * FROM team WHERE %s;";

        searchQuery = String.format(format, whereClause);

        try (var statement = TeamRequestController.connection.prepareStatement(searchQuery)) {
            TeamRequestController.connection.setAutoCommit(false);
            if (Objects.equals(field, "team_name")) {
                statement.setString(searchValueIndex, searchValueString);
            } else {
                statement.setInt(searchValueIndex, searchValueInteger);
            } //end if

            resultSet = statement.executeQuery();

            team_ids = new ArrayList<>();

            team_names = new ArrayList<>();

            sport_ids = new ArrayList<>();

            while (resultSet.next()) {
                team_id = resultSet.getInt("team_id");

                team_name = resultSet.getString("team_name");

                sport_id = resultSet.getInt("sport_id");

                team_ids.add(team_id);

                team_names.add(team_name);

                sport_ids.add(sport_id);

                TeamRequestController.connection.commit();
            } //end while
        } catch (SQLException e) {
            e.printStackTrace();

            return "search-team-failure";
        } //end try catch

        if (team_ids.isEmpty()) {
            return "<!DOCTYPE HTML>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>Search Team</title>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>Search Team</h1>\n" +
                    "<p>No teams with that search value exist.</p>\n" +
                    "</body>\n" +
                    "</html>";
        } //end if

        format = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Search Team</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Search Team</h1>\n" +
                "<table border = '1'>\n" +
                "<tr><th>Team ID</th><th>team_name</th><th>sport_id</th>\n</tr>" +
                "%s" +
                "</table>\n" +
                "</body>\n" +
                "</html>";

        stringBuilder = new StringBuilder();

        for (int i = 0; i < team_ids.size(); i++) {
            team_id = team_ids.get(i);

            team_name = team_names.get(i);

            sport_id = sport_ids.get(i);

            stringBuilder.append("<tr>");

            stringBuilder.append("<td>");

            stringBuilder.append(team_id);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(team_name);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(sport_id);

            stringBuilder.append("</td>");

            stringBuilder.append("</tr>\n");
        } //end for

        tableString = stringBuilder.toString();

        htmlString = String.format(format, tableString);

        return htmlString;
    } //searchTeamSubmit

    /**
     * Handles the request for listing teams.
     *
     * @return the response to listing teams
     */
    @GetMapping(value = "list-teams", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String listTeams() {
        String query;
        ResultSet resultSet;
        List<Integer> team_ids;
        List<String> team_names;
        List<Integer> sport_ids;

        int team_id;
        String team_name;
        int sport_id;
        String format;
        StringBuilder stringBuilder;
        String tableString;
        String htmlString;

        Objects.requireNonNull(TeamRequestController.connection, "the connection is null");

        query = "SELECT * FROM team;";

        try (var statement = TeamRequestController.connection.createStatement()) {
            resultSet = statement.executeQuery(query);

            team_ids = new ArrayList<>();

            team_names = new ArrayList<>();

            sport_ids = new ArrayList<>();

            while (resultSet.next()) {
                team_id = resultSet.getInt("team_id");

                team_name = resultSet.getString("team_name");

                sport_id = resultSet.getInt("sport_id");

                team_ids.add(team_id);

                team_names.add(team_name);

                sport_ids.add(sport_id);
            } //end while
        } catch (SQLException e) {
            e.printStackTrace();

            return "list-teams-failure";
        } //end try catch

        if (team_ids.isEmpty()) {
            return "<!DOCTYPE HTML>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>List Teams</title>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>List Teams</h1>\n" +
                    "<p>No teams exist.</p>\n" +
                    "</body>\n" +
                    "</html>";
        } //end if

        format = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>List Teams</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>List Teams</h1>\n" +
                "<table border = '1'>\n" +
                "<tr><th>Team ID</th><th>Team Name</th><th>Sport ID</th></tr>\n" +
                "%s" +
                "</table>\n" +
                "</body>\n" +
                "</html>";

        stringBuilder = new StringBuilder();

        for (int i = 0; i < team_ids.size(); i++) {
            team_id = team_ids.get(i);

            team_name = team_names.get(i);

            sport_id = sport_ids.get(i);

            stringBuilder.append("<tr>");

            stringBuilder.append("<td>");

            stringBuilder.append(team_id);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(team_name);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(sport_id);

            stringBuilder.append("</td>");

            stringBuilder.append("</tr>\n");
        } //end for

        tableString = stringBuilder.toString();

        htmlString = String.format(format, tableString);

        return htmlString;
    } //listTeams

        @GetMapping("team-page")
    public String teamPage() {
        return "team-page";
    } //teamPage
}