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
 * A controller for player requests of the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Josh Lefton, jlefton@purdue.edu
 * @version 5/5/2021
 */
@Controller
public final class PlayerRequestController {
    /**
     * The connection of the {@code PlayerRequestController} class.
     */
    private static final Connection connection;

    /**
     * The next player ID of the {@code PlayerRequestController} class.
     */
    private static int next_player_id;

    static {
        connection = DatabaseConnection.getConnection();

        next_player_id = getPlayer_id();
    } //static

    /**
     * Returns the ID to be assigned to the next player.
     *
     * @return the ID to be assigned to the next player
     */
    private static int getPlayer_id() {
        String query;
        ResultSet result_set;
        int id = 0;

        Objects.requireNonNull(PlayerRequestController.connection, "the connection is null");

        query = "SELECT MAX(player_id) AS max_id FROM player;";

        try (var statement = PlayerRequestController.connection.createStatement()) {
            PlayerRequestController.connection.setAutoCommit(false);
            result_set = statement.executeQuery(query);

            while (result_set.next()) {
                id = result_set.getInt("max_id");
            } //end while

            id++;
            PlayerRequestController.connection.commit();
        } catch (SQLException err) {
            try {
                PlayerRequestController.connection.rollback();
            } catch (SQLException err_2) {
                err_2.printStackTrace();
            }
            err.printStackTrace();
        } finally {
            try {
                PlayerRequestController.connection.setAutoCommit(true);
            } catch (SQLException err_3) {
                err_3.printStackTrace();
            }
        }//end try catch

        return id;
    } //getPlayer_id

    /**
     * Returns the form for adding a player.
     *
     * @param model the model to be used in the operation
     * @return the form for adding a player
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("add-player")
    public String addPlayerForm(Model model) {
        Player player;

        Objects.requireNonNull(model, "the specified model is null");

        player = new Player();

        model.addAttribute("player", player);

        return "add-player";
    } //addPlayerForm

    /**
     * Handles the request for attempting to add the specified player.
     *
     * @param player  the player to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to attempting to add the specified player
     * @throws NullPointerException if the specified player or model is {@code null}
     */
    @PostMapping("add-player")
    public String addPlayerSubmit(@ModelAttribute Player player, Model model) {
        int player_id;
        String name;
        String team_id_string;
        int team_id;

        Objects.requireNonNull(player, "the specified player is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(PlayerRequestController.connection, "the connection is null");

        model.addAttribute("player", player);

        player_id = PlayerRequestController.next_player_id;

        PlayerRequestController.next_player_id++;

        name = player.getName();

        team_id_string = player.getTeam_id();

        try {
            team_id = Integer.parseInt(team_id_string);
        } catch (NumberFormatException e) {
            return "add-player-failure-team-id-invalid";
        } //end try catch
        DataSource data_source = DataSourceConfig.getDataSource();

        try {
            PlayerRequestController.connection.setAutoCommit(false);
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(data_source).withProcedureName("ADD_PLAYER");
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("in_player_id", player_id)
                    .addValue("in_name", name)
                    .addValue("in_team_id", team_id);
            Map<String, Object> out = jdbcCall.execute(parameters);
            PlayerRequestController.connection.commit();
        } catch (SQLException err) {
            try {
                PlayerRequestController.connection.rollback();
            } catch (SQLException err_2) {
                err_2.printStackTrace();
            }
            err.printStackTrace();
        } finally {
            try {
                PlayerRequestController.connection.setAutoCommit(true);
            } catch (SQLException err_3) {
                err_3.printStackTrace();
            }
        }//end try catch

        return "add-player-success";
    } //addTeamSubmit

    /**
     * Returns the form for editing a player.
     *
     * @param model the model to be used in the operation
     * @return the form for editing a player
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("edit-player")
    public String editPlayerForm(Model model) {
        EditPlayer editPlayer;

        Objects.requireNonNull(model, "the specified model is null");

        editPlayer = new EditPlayer();

        model.addAttribute("editPlayer", editPlayer);

        return "edit-player";
    } //editPlayerForm

    /**
     * Handles the request for attempting to edit a player using the specified edit player.
     *
     * @param editPlayer the edit player to be used in the operation
     * @param model    the model to be used in the operation
     * @return the response to attempting to edit a player using the specified edit player
     * @throws NullPointerException if the specified edit player or model is {@code null}
     */
    @PostMapping("edit-player")
    public String editPlayerSubmit(@ModelAttribute EditPlayer editPlayer, Model model) {
        String idString;
        int player_id;
        String field;
        String value;

        Objects.requireNonNull(editPlayer, "the specified edit player is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(PlayerRequestController.connection, "the connection is null");

        idString = editPlayer.getId();

        try {
            player_id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return "edit-player-failure-id-invalid";
        } //end try catch

        field = editPlayer.getField();

        value = editPlayer.getNewValue();

        DataSource data_source = DataSourceConfig.getDataSource();

        try {
            PlayerRequestController.connection.setAutoCommit(false);
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(data_source).withProcedureName("EDIT_PLAYER");
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("in_player_id", player_id)
                    .addValue("in_field", field)
                    .addValue("in_value", value);
            Map<String, Object> out = jdbcCall.execute(parameters);
            PlayerRequestController.connection.commit();
        } catch (SQLException err) {
            try {
                PlayerRequestController.connection.rollback();
            } catch (SQLException err_2) {
                err_2.printStackTrace();
            }
            err.printStackTrace();
        } finally {
            try {
                PlayerRequestController.connection.setAutoCommit(true);
            } catch (SQLException err_3) {
                err_3.printStackTrace();
            }
        }//end try catch
        return "edit-player-success";
    } //editPlayerSubmit

    /**
     * Returns the form for deleting a player.
     *
     * @param model the model to be used in the operation
     * @return the form for deleting a player
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("delete-player")
    public String deletePlayerForm(Model model) {
        DeletePlayer deletePlayer;

        Objects.requireNonNull(model, "the specified model is null");

        deletePlayer = new DeletePlayer();

        model.addAttribute("deletePlayer", deletePlayer);

        return "delete-player";
    } //deletePlayerForm

    /**
     * Handles the request for attempting to delete the specified delete player.
     *
     * @param deletePlayer the delete player to be used in the operation
     * @param model      the model to be used in the operation
     * @return the response to attempting to delete the specified delete player
     * @throws NullPointerException if the specified delete player or model is {@code null}
     */
    @PostMapping("delete-player")
    public String deletePlayerSubmit(@ModelAttribute DeletePlayer deletePlayer, Model model) {
        String idString;
        int player_id;

        Objects.requireNonNull(deletePlayer, "the specified delete player is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(PlayerRequestController.connection, "the connection is null");

        idString = deletePlayer.getId();

        try {
            player_id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return "delete-player-failure-id-invalid";
        } //end try catch

        DataSource data_source = DataSourceConfig.getDataSource();

        try {
            PlayerRequestController.connection.setAutoCommit(false);
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(data_source).withProcedureName("DELETE_PLAYER");
            SqlParameterSource parameters = new MapSqlParameterSource().addValue("in_player_id", player_id);
            Map<String, Object> out = jdbcCall.execute(parameters);
            PlayerRequestController.connection.commit();
        } catch (SQLException err) {
            try {
                PlayerRequestController.connection.rollback();
            } catch (SQLException err_2) {
                err_2.printStackTrace();
            }
            err.printStackTrace();
        } finally {
            try {
                PlayerRequestController.connection.setAutoCommit(true);
            } catch (SQLException err_3) {
                err_3.printStackTrace();
            }
        }//end try catch

        return "delete-player-success";
    } //deletePlayerSubmit

    /**
     * Returns the form for searching for a player.
     *
     * @param model the model to be used in the operation
     * @return the form for searching for a player
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("search-player")
    public String searchPlayerForm(Model model) {
        SearchPlayer searchPlayer;

        Objects.requireNonNull(model, "the specified model is null");

        searchPlayer = new SearchPlayer();

        model.addAttribute("searchPlayer", searchPlayer);

        return "search-player";
    } //searchPlayerForm

    /**
     * Handles the request for searching for the specified search player.
     *
     * @param searchPlayer the search player to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to searching for the specified search player
     * @throws NullPointerException if the specified search player or model is {@code null}
     */
    @PostMapping(value = "search-player", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String searchPlayerSubmit(@ModelAttribute SearchPlayer searchPlayer, Model model) {
        String field;
        String searchValueString;
        int searchValueInteger = 0;
        String whereClause;
        String format;
        String searchQuery;
        int searchValueIndex = 1;
        ResultSet resultSet;
        List<Integer> player_ids;
        List<String> names;
        List<Integer> team_ids;
        int player_id;
        String name;
        int team_id;
        StringBuilder stringBuilder;
        String tableString;
        String htmlString;

        Objects.requireNonNull(searchPlayer, "the specified search player is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(PlayerRequestController.connection, "the connection is null");

        model.addAttribute("searchPlayer", searchPlayer);

        field = searchPlayer.getField();

        searchValueString = searchPlayer.getSearchValue();

        if (!Objects.equals(field, "name")) {
            try {
                searchValueInteger = Integer.parseInt(searchValueString);
            } catch (NumberFormatException e) {
                return "<!DOCTYPE HTML>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<title>Search Player</title>\n" +
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>Search Player</h1>\n" +
                        "<p>No players with that search value exist.</p>\n" +
                        "</body>\n" +
                        "</html>";
            } //end try catch
        } //end if

        if (Objects.equals(field, "name")) {
            whereClause = "UPPER(name) = UPPER(?)";
        } else {
            format = "%s = ?";

            whereClause = String.format(format, field);
        } //end if

        format = "SELECT * FROM player WHERE %s;";

        searchQuery = String.format(format, whereClause);

        try (var statement = PlayerRequestController.connection.prepareStatement(searchQuery)) {
            if (Objects.equals(field, "name")) {
                statement.setString(searchValueIndex, searchValueString);
            } else {
                statement.setInt(searchValueIndex, searchValueInteger);
            } //end if

            resultSet = statement.executeQuery();

            player_ids = new ArrayList<>();

            names = new ArrayList<>();

            team_ids = new ArrayList<>();

            while (resultSet.next()) {
                player_id = resultSet.getInt("player_id");

                name = resultSet.getString("name");

                team_id = resultSet.getInt("team_id");

                player_ids.add(player_id);

                names.add(name);

                team_ids.add(team_id);
            } //end while
        } catch (SQLException e) {
            e.printStackTrace();

            return "search-player-failure";
        } //end try catch

        if (player_ids.isEmpty()) {
            return "<!DOCTYPE HTML>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>Search Player</title>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>Search Player</h1>\n" +
                    "<p>No players with that search value exist.</p>\n" +
                    "</body>\n" +
                    "</html>";
        } //end if

        format = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Search Player</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Search Player</h1>\n" +
                "<table border = '1'>\n" +
                "<tr><th>Player ID</th><th>Name</th><th>Team ID</th>\n</tr>" +
                "%s" +
                "</table>\n" +
                "</body>\n" +
                "</html>";

        stringBuilder = new StringBuilder();

        for (int i = 0; i < player_ids.size(); i++) {
            player_id = player_ids.get(i);

            name = names.get(i);

            team_id = team_ids.get(i);

            stringBuilder.append("<tr>");

            stringBuilder.append("<td>");

            stringBuilder.append(player_id);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(name);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(team_id);

            stringBuilder.append("</td>");

            stringBuilder.append("</tr>\n");
        } //end for

        tableString = stringBuilder.toString();

        htmlString = String.format(format, tableString);

        return htmlString;
    } //searchPlayerSubmit

    /**
     * Handles the request for listing players.
     *
     * @return the response to listing players
     */
    @GetMapping(value = "list-players", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String listPlayers() {
        String query;
        ResultSet resultSet;
        List<Integer> player_ids;
        List<String> names;
        List<Integer> team_ids;

        int player_id;
        String name;
        int team_id;
        String format;
        StringBuilder stringBuilder;
        String tableString;
        String htmlString;

        Objects.requireNonNull(PlayerRequestController.connection, "the connection is null");

        query = "SELECT * FROM player;";

        try (var statement = PlayerRequestController.connection.createStatement()) {
            resultSet = statement.executeQuery(query);

            player_ids = new ArrayList<>();

            names = new ArrayList<>();

            team_ids = new ArrayList<>();

            while (resultSet.next()) {
                player_id = resultSet.getInt("player_id");

                name = resultSet.getString("name");

                team_id = resultSet.getInt("team_id");

                player_ids.add(player_id);

                names.add(name);

                team_ids.add(team_id);
            } //end while
        } catch (SQLException e) {
            e.printStackTrace();

            return "list-players-failure";
        } //end try catch

        if (player_ids.isEmpty()) {
            return "<!DOCTYPE HTML>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>List Players</title>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>List Players</h1>\n" +
                    "<p>No players exist.</p>\n" +
                    "</body>\n" +
                    "</html>";
        } //end if

        format = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>List Players</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>List Players</h1>\n" +
                "<table border = '1'>\n" +
                "<tr><th>Player ID</th><th>Player Name</th><th>Team ID</th></tr>\n" +
                "%s" +
                "</table>\n" +
                "</body>\n" +
                "</html>";

        stringBuilder = new StringBuilder();

        for (int i = 0; i < player_ids.size(); i++) {
            player_id = player_ids.get(i);

            name = names.get(i);

            team_id = team_ids.get(i);

            stringBuilder.append("<tr>");

            stringBuilder.append("<td>");

            stringBuilder.append(player_id);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(name);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(team_id);

            stringBuilder.append("</td>");

            stringBuilder.append("</tr>\n");
        } //end for

        tableString = stringBuilder.toString();

        htmlString = String.format(format, tableString);

        return htmlString;
    } //listPlayers

    @GetMapping("player-page")
    public String playerPage() {
        return "player-page";
    } //playerPage
}