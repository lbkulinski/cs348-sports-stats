package com.stats.sports;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A controller for game requests of the the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Logan Kulinski, lbk@purdue.edu
 * @version April 29, 2021
 */
@Controller
public final class GameRequestController {
    /**
     * The connection of the {@code GameRequestController} class.
     */
    private static final Connection connection;

    /**
     * The next game ID of the {@code GameRequestController} class.
     */
    private static int nextGameId;

    static {
        connection = DatabaseConnection.getConnection();

        nextGameId = getGameId();
    } //static

    /**
     * Returns the ID to be assigned to the next game.
     *
     * @return the ID to be assigned to the next game
     */
    private static int getGameId() {
        String query;
        ResultSet resultSet;
        int id = 0;

        Objects.requireNonNull(GameRequestController.connection, "the connection is null");

        query = "SELECT MAX(game_id) AS max_id FROM game;";

        try (var statement = GameRequestController.connection.createStatement()) {
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                id = resultSet.getInt("max_id");
            } //end while

            id++;
        } catch (SQLException e) {
            e.printStackTrace();
        } //end try catch

        return id;
    } //getGameId

    /**
     * Returns the form for adding a game.
     *
     * @param model the model to be used in the operation
     * @return the form for adding a game
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("add-game")
    public String addGameForm(Model model) {
        Game game;

        Objects.requireNonNull(model, "the specified model is null");

        game = new Game();

        model.addAttribute("game", game);

        return "add-game";
    } //addGameForm

    /**
     * Handles the request for attempting to add the specified game.
     *
     * @param game the game to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to attempting to add the specified game
     * @throws NullPointerException if the specified game or model is {@code null}
     */
    @PostMapping("add-game")
    public String addGameSubmit(@ModelAttribute Game game, Model model) {
        String date;
        String seasonIdString;
        int seasonId;
        String homeTeamIdString;
        int homeTeamId;
        String awayTeamIdString;
        int awayTeamId;
        String homeTeamScoreString;
        int homeTeamScore;
        String awayTeamScoreString;
        int awayTeamScore;
        int id;
        String insertStatement;
        int idIndex = 1;
        int dateIndex = 2;
        int homeTeamScoreIndex = 3;
        int awayTeamScoreIndex = 4;
        int homeTeamIdIndex = 5;
        int awayTeamIdIndex = 6;
        int seasonIdIndex = 7;

        Objects.requireNonNull(game, "the specified game is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(GameRequestController.connection, "the connection is null");

        model.addAttribute("game", game);

        date = game.getDate();

        seasonIdString = game.getSeasonId();

        try {
            seasonId = Integer.parseInt(seasonIdString);
        } catch (NumberFormatException e) {
            return "add-game-failure-season-id-invalid";
        } //end try catch

        homeTeamIdString = game.getHomeTeamId();

        try {
            homeTeamId = Integer.parseInt(homeTeamIdString);
        } catch (NumberFormatException e) {
            return "add-game-failure-home-team-id-invalid";
        } //end try catch

        awayTeamIdString = game.getAwayTeamId();

        try {
            awayTeamId = Integer.parseInt(awayTeamIdString);
        } catch (NumberFormatException e) {
            return "add-game-failure-away-team-id-invalid";
        } //end try catch

        homeTeamScoreString = game.getHomeTeamScore();

        try {
            homeTeamScore = Integer.parseInt(homeTeamScoreString);
        } catch (NumberFormatException e) {
            return "add-game-failure-home-team-score-invalid";
        } //end try catch

        awayTeamScoreString = game.getAwayTeamScore();

        try {
            awayTeamScore = Integer.parseInt(awayTeamScoreString);
        } catch (NumberFormatException e) {
            return "add-game-failure-away-team-score-invalid";
        } //end try catch

        id = GameRequestController.nextGameId;

        GameRequestController.nextGameId++;

        insertStatement = "INSERT INTO game (game_id, game_date, home_team_score, away_team_score, home_team_id, " +
                          "away_team_id, season_id) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (var preparedStatement = GameRequestController.connection.prepareStatement(insertStatement)) {
            preparedStatement.setInt(idIndex, id);

            preparedStatement.setString(dateIndex, date);

            preparedStatement.setInt(homeTeamScoreIndex, homeTeamScore);

            preparedStatement.setInt(awayTeamScoreIndex, awayTeamScore);

            preparedStatement.setInt(homeTeamIdIndex, homeTeamId);

            preparedStatement.setInt(awayTeamIdIndex, awayTeamId);

            preparedStatement.setInt(seasonIdIndex, seasonId);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();

            return "add-game-failure";
        } //end try catch

        return "add-game-success";
    } //addGameSubmit

    /**
     * Returns the form for editing a game.
     *
     * @param model the model to be used in the operation
     * @return the form for editing a game
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("edit-game")
    public String editGameForm(Model model) {
        EditGame editGame;

        Objects.requireNonNull(model, "the specified model is null");

        editGame = new EditGame();

        model.addAttribute("editGame", editGame);

        return "edit-game";
    } //editGameForm

    /**
     * Handles the request for attempting to edit a game using the specified edit game.
     *
     * @param editGame the edit game to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to attempting to edit a game using the specified edit game
     * @throws NullPointerException if the specified edit game or model is {@code null}
     */
    @PostMapping("edit-game")
    public String editGameSubmit(@ModelAttribute EditGame editGame, Model model) {
        String idString;
        int id;
        String field;
        String newValueString;
        String errorFileName = null;
        int newValueInteger = 0;
        String format;
        String updateStatement;
        int newValueIndex = 1;
        int idIndex = 2;
        int rowsAffected;

        Objects.requireNonNull(editGame, "the specified edit game is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(GameRequestController.connection, "the connection is null");

        idString = editGame.getId();

        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return "edit-game-failure-game-id-invalid";
        } //end try catch

        field = editGame.getField();

        newValueString = editGame.getNewValue();

        switch (field) {
            case "season_id": {
                errorFileName = "edit-game-failure-season-id-invalid";

                break;
            } //case "season_id"
            case "home_team_id": {
                errorFileName = "edit-game-failure-home-team-id-invalid";

                break;
            } //"home_team_id"
            case "away_team_id": {
                errorFileName = "edit-game-failure-away-team-id-invalid";

                break;
            } //"away_team_id"
            case "home_team_score": {
                errorFileName = "edit-game-failure-home-team-score-invalid";

                break;
            } //"home_team_score"
            case "away_team_score": {
                errorFileName = "edit-game-failure-away-team-score-invalid";
            } //"away_team_score"
        } //end switch

        if (!Objects.equals(field, "game_date")) {
            try {
                newValueInteger = Integer.parseInt(newValueString);
            } catch (NumberFormatException e) {
                return errorFileName;
            } //end try catch
        } //end if

        format = "UPDATE game SET %s = ? WHERE game_id = ?;";

        updateStatement = String.format(format, field);

        try (var preparedStatement = GameRequestController.connection.prepareStatement(updateStatement)) {
            if (Objects.equals(field, "game_date")) {
                preparedStatement.setString(newValueIndex, newValueString);
            } else {
                preparedStatement.setInt(newValueIndex, newValueInteger);
            } //end if

            preparedStatement.setInt(idIndex, id);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

            return "edit-game-failure";
        } //end try catch

        if (rowsAffected > 0) {
            return "edit-game-success";
        } else {
            return "edit-game-failure-not-found";
        } //end if
    } //editGameSubmit

    /**
     * Returns the form for deleting a game.
     *
     * @param model the model to be used in the operation
     * @return the form for deleting a game
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("delete-game")
    public String deleteGameForm(Model model) {
        DeleteGame deleteGame;

        Objects.requireNonNull(model, "the specified model is null");

        deleteGame = new DeleteGame();

        model.addAttribute("deleteGame", deleteGame);

        return "delete-game";
    } //deleteGameForm

    /**
     * Handles the request for attempting to delete the specified delete game.
     *
     * @param deleteGame the delete game to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to attempting to delete the specified delete game
     * @throws NullPointerException if the specified delete game or model is {@code null}
     */
    @PostMapping("delete-game")
    public String deleteGameSubmit(@ModelAttribute DeleteGame deleteGame, Model model) {
        String idString;
        int id;
        String deleteStatement;
        int idIndex = 1;
        int rowsAffected;

        Objects.requireNonNull(deleteGame, "the specified delete game is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(GameRequestController.connection, "the connection is null");

        idString = deleteGame.getId();

        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return "delete-game-failure-id-invalid";
        } //end try catch

        deleteStatement = "DELETE FROM game WHERE game_id = ?;";

        try (var preparedStatement = GameRequestController.connection.prepareStatement(deleteStatement)) {
            preparedStatement.setInt(idIndex, id);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

            return "delete-game-failure";
        } //end try catch

        if (rowsAffected > 0) {
            return "delete-game-success";
        } else {
            return "delete-game-failure-not-found";
        } //end if
    } //deleteGameSubmit

    /**
     * Returns the form for searching for a game.
     *
     * @param model the model to be used in the operation
     * @return the form for searching for a game
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("search-game")
    public String searchGameForm(Model model) {
        SearchGame searchGame;

        Objects.requireNonNull(model, "the specified model is null");

        searchGame = new SearchGame();

        model.addAttribute("searchGame", searchGame);

        return "search-game";
    } //searchGameForm

    /**
     * Handles the request for searching for the specified search game.
     *
     * @param searchGame the search game to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to searching for the specified search game
     * @throws NullPointerException if the specified search game or model is {@code null}
     */
    @PostMapping(value = "search-game", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String searchGameSubmit(@ModelAttribute SearchGame searchGame, Model model) {
        String field;
        String searchValueString;
        int searchValueInteger = 0;
        String whereClause;
        String format;
        String searchQuery;
        int searchValueIndex = 1;
        ResultSet resultSet;
        List<Integer> ids;
        List<String> dates;
        List<Integer> seasonIds;
        List<Integer> homeTeamIds;
        List<Integer> awayTeamIds;
        List<Integer> homeTeamScores;
        List<Integer> awayTeamScores;
        int id;
        String date;
        int seasonId;
        int homeTeamId;
        int awayTeamId;
        int homeTeamScore;
        int awayTeamScore;
        StringBuilder stringBuilder;
        String tableString;
        String htmlString;

        Objects.requireNonNull(searchGame, "the specified search game is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(GameRequestController.connection, "the connection is null");

        model.addAttribute("searchGame", searchGame);

        field = searchGame.getField();

        searchValueString = searchGame.getSearchValue();

        if (!Objects.equals(field, "game_date")) {
            try {
                searchValueInteger = Integer.parseInt(searchValueString);
            } catch (NumberFormatException e) {
                return "<!DOCTYPE HTML>\n" +
                       "<html>\n" +
                       "<head>\n" +
                       "<title>Search Game</title>\n" +
                       "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                       "</head>\n" +
                       "<body>\n" +
                       "<h1>Search Game</h1>\n" +
                       "<p>No games with that search value exist.</p>\n" +
                       "</body>\n" +
                       "</html>";
            } //end try catch
        } //end if

        if (Objects.equals(field, "game_date")) {
            whereClause = "UPPER(game_date) = UPPER(?)";
        } else {
            format = "%s = ?";

            whereClause = String.format(format, field);
        } //end if

        format = "SELECT * FROM game WHERE %s;";

        searchQuery = String.format(format, whereClause);

        try (var statement = GameRequestController.connection.prepareStatement(searchQuery)) {
            if (Objects.equals(field, "game_date")) {
                statement.setString(searchValueIndex, searchValueString);
            } else {
                statement.setInt(searchValueIndex, searchValueInteger);
            } //end if

            resultSet = statement.executeQuery();

            ids = new ArrayList<>();

            dates = new ArrayList<>();

            seasonIds = new ArrayList<>();

            homeTeamIds = new ArrayList<>();

            awayTeamIds = new ArrayList<>();

            homeTeamScores = new ArrayList<>();

            awayTeamScores = new ArrayList<>();

            while (resultSet.next()) {
                id = resultSet.getInt("game_id");

                date = resultSet.getString("game_date");

                seasonId = resultSet.getInt("season_id");

                homeTeamId = resultSet.getInt("home_team_id");

                awayTeamId = resultSet.getInt("away_team_id");

                homeTeamScore = resultSet.getInt("home_team_score");

                awayTeamScore = resultSet.getInt("away_team_score");

                ids.add(id);

                dates.add(date);

                seasonIds.add(seasonId);

                homeTeamIds.add(homeTeamId);

                awayTeamIds.add(awayTeamId);

                homeTeamScores.add(homeTeamScore);

                awayTeamScores.add(awayTeamScore);
            } //end while
        } catch (SQLException e) {
            e.printStackTrace();

            return "search-game-failure";
        } //end try catch

        if (ids.isEmpty()) {
            return "<!DOCTYPE HTML>\n" +
                   "<html>\n" +
                   "<head>\n" +
                   "<title>Search Game</title>\n" +
                   "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                   "</head>\n" +
                   "<body>\n" +
                   "<h1>Search Game</h1>\n" +
                   "<p>No games with that search value exist.</p>\n" +
                   "</body>\n" +
                   "</html>";
        } //end if

        format = "<!DOCTYPE HTML>\n" +
                 "<html>\n" +
                 "<head>\n" +
                 "<title>Search Game</title>\n" +
                 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                 "</head>\n" +
                 "<body>\n" +
                 "<h1>Search Game</h1>\n" +
                 "<table border = '1'>\n" +
                 "<tr><th>ID</th><th>Date</th><th>Season ID</th><th>Home team ID</th><th>Away team ID</th>" +
                 "<th>Home team score</th><th>Away team score</th></tr>\n" +
                 "%s" +
                 "</table>\n" +
                 "</body>\n" +
                 "</html>";

        stringBuilder = new StringBuilder();

        for (int i = 0; i < ids.size(); i++) {
            id = ids.get(i);

            date = dates.get(i);

            seasonId = seasonIds.get(i);

            homeTeamId = homeTeamIds.get(i);

            awayTeamId = awayTeamIds.get(i);

            homeTeamScore = homeTeamScores.get(i);

            awayTeamScore = awayTeamScores.get(i);

            stringBuilder.append("<tr>");

            stringBuilder.append("<td>");

            stringBuilder.append(id);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(date);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(seasonId);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(homeTeamId);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(awayTeamId);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(homeTeamScore);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(awayTeamScore);

            stringBuilder.append("</td>");

            stringBuilder.append("</tr>\n");
        } //end for

        tableString = stringBuilder.toString();

        htmlString = String.format(format, tableString);

        return htmlString;
    } //searchGameSubmit

    /**
     * Handles the request for listing games.
     *
     * @return the response to listing games
     */
    @GetMapping(value = "list-games", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String listGames() {
        String query;
        ResultSet resultSet;
        List<Integer> ids;
        List<String> dates;
        List<Integer> seasonIds;
        List<Integer> homeTeamIds;
        List<Integer> awayTeamIds;
        List<Integer> homeTeamScores;
        List<Integer> awayTeamScores;
        int id;
        String date;
        int seasonId;
        int homeTeamId;
        int awayTeamId;
        int homeTeamScore;
        int awayTeamScore;
        String format;
        StringBuilder stringBuilder;
        String tableString;
        String htmlString;

        Objects.requireNonNull(GameRequestController.connection, "the connection is null");

        query = "SELECT * FROM game;";

        try (var statement = GameRequestController.connection.createStatement()) {
            resultSet = statement.executeQuery(query);

            ids = new ArrayList<>();

            dates = new ArrayList<>();

            seasonIds = new ArrayList<>();

            homeTeamIds = new ArrayList<>();

            awayTeamIds = new ArrayList<>();

            homeTeamScores = new ArrayList<>();

            awayTeamScores = new ArrayList<>();

            while (resultSet.next()) {
                id = resultSet.getInt("game_id");

                date = resultSet.getString("game_date");

                seasonId = resultSet.getInt("season_id");

                homeTeamId = resultSet.getInt("home_team_id");

                awayTeamId = resultSet.getInt("away_team_id");

                homeTeamScore = resultSet.getInt("home_team_score");

                awayTeamScore = resultSet.getInt("away_team_score");

                ids.add(id);

                dates.add(date);

                seasonIds.add(seasonId);

                homeTeamIds.add(homeTeamId);

                awayTeamIds.add(awayTeamId);

                homeTeamScores.add(homeTeamScore);

                awayTeamScores.add(awayTeamScore);
            } //end while
        } catch (SQLException e) {
            e.printStackTrace();

            return "list-games-failure";
        } //end try catch

        if (ids.isEmpty()) {
            return "<!DOCTYPE HTML>\n" +
                   "<html>\n" +
                   "<head>\n" +
                   "<title>List Games</title>\n" +
                   "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                   "</head>\n" +
                   "<body>\n" +
                   "<h1>List Games</h1>\n" +
                   "<p>No games exist.</p>\n" +
                   "</body>\n" +
                   "</html>";
        } //end if

        format = "<!DOCTYPE HTML>\n" +
                 "<html>\n" +
                 "<head>\n" +
                 "<title>List Games</title>\n" +
                 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                 "</head>\n" +
                 "<body>\n" +
                 "<h1>List Games</h1>\n" +
                 "<table border = '1'>\n" +
                 "<tr><th>ID</th><th>Date</th><th>Season ID</th><th>Home team ID</th><th>Away team ID</th>" +
                 "<th>Home team score</th><th>Away team score</th></tr>\n" +
                 "%s" +
                 "</table>\n" +
                 "</body>\n" +
                 "</html>";

        stringBuilder = new StringBuilder();

        for (int i = 0; i < ids.size(); i++) {
            id = ids.get(i);

            date = dates.get(i);

            seasonId = seasonIds.get(i);

            homeTeamId = homeTeamIds.get(i);

            awayTeamId = awayTeamIds.get(i);

            homeTeamScore = homeTeamScores.get(i);

            awayTeamScore = awayTeamScores.get(i);

            stringBuilder.append("<tr>");

            stringBuilder.append("<td>");

            stringBuilder.append(id);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(date);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(seasonId);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(homeTeamId);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(awayTeamId);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(homeTeamScore);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(awayTeamScore);

            stringBuilder.append("</td>");

            stringBuilder.append("</tr>\n");
        } //end for

        tableString = stringBuilder.toString();

        htmlString = String.format(format, tableString);

        return htmlString;
    } //listGames

    /**
     * Handles the request for the game page.
     *
     * @return the response to requesting the game page
     */
    @GetMapping("game-page")
    public String gamePage() {
        return "game-page";
    } //gamePage
}