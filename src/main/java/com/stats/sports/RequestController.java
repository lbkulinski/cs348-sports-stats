package com.stats.sports;

import org.springframework.stereotype.Controller;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Objects;
import java.sql.Statement;
import java.sql.SQLException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.sql.PreparedStatement;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.ArrayList;

/**
 * A controller for requests of the the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Logan Kulinski, lbk@purdue.edu
 * @version April 28, 2021
 */
@Controller
public final class RequestController {
    /**
     * The connection of the {@code RequestController} class.
     */
    private static final Connection connection;

    /**
     * The next sport ID of the {@code RequestController} class.
     */
    private static int nextSportId;

    /**
     * The next game ID of the {@code RequestController} class.
     */
    private static int nextGameId;

    static {
        connection = DatabaseConnection.getConnection();

        nextSportId = getSportId();

        nextGameId = getGameId();
    } //static

    /**
     * Returns the ID to be assigned to the next sport.
     *
     * @return the ID to be assigned to the next sport
     */
    private static int getSportId() {
        String query;
        ResultSet resultSet;
        int id = 0;

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        query = "SELECT MAX(sport_id) AS max_id FROM sports;";

        try (Statement statement = RequestController.connection.createStatement()) {
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                id = resultSet.getInt("max_id");
            } //end while

            id++;
        } catch (SQLException e) {
            e.printStackTrace();
        } //end try catch

        return id;
    } //getSportId

    /**
     * Returns the ID to be assigned to the next game.
     *
     * @return the ID to be assigned to the next game
     */
    private static int getGameId() {
        String query;
        ResultSet resultSet;
        int id = 0;

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        query = "SELECT MAX(game_id) AS max_id FROM game;";

        try (Statement statement = RequestController.connection.createStatement()) {
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
     * Returns the sport id bt use of the sport name
     *
     * @param name the name of the sport
     * @return the id if the sport is in the database, "" if otherwise.
     */
    private static int getSportIdFromName(String name) {
        String searchQuery;
        int id = -1;
        ResultSet resultSet;

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        searchQuery = "SELECT sport_id FROM sports WHERE UPPER(name) = UPPER(?);";
        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(searchQuery)) {
            preparedStatement.setString(1, name);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("sport_id");
            } //end while
        } catch (SQLException e) {
            e.printStackTrace();

            return -1;
        }
        return id;
    }

    /**
     * Returns the form for adding a sport.
     *
     * @param model the model to be used in the operation
     * @return the form for adding a sport
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("add-sport")
    public String addSportForm(Model model) {
        Sport sport;

        Objects.requireNonNull(model, "the specified model is null");

        sport = new Sport();

        model.addAttribute("sport", sport);

        return "add-sport";
    } //addSportForm

    /**
     * Handles the request for attempting to add the specified sport.
     *
     * @param sport the sport to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to attempting to add the specified sport
     * @throws NullPointerException if the specified sport or model is {@code null}
     */
    @PostMapping("add-sport")
    public String addSportSubmit(@ModelAttribute Sport sport, Model model) {
        int id;
        String name;
        String insertStatement;
        int idIndex = 1;
        int nameIndex = 2;

        Objects.requireNonNull(sport, "the specified sport is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        model.addAttribute("sport", sport);

        id = RequestController.nextSportId;

        RequestController.nextSportId++;

        name = sport.getName();

        insertStatement = "INSERT INTO sports (sport_id, name) VALUES (?, ?);";

        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(insertStatement)) {
            preparedStatement.setInt(idIndex, id);

            preparedStatement.setString(nameIndex, name);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();

            return "add-sport-failure";
        } //end try catch

        return "add-sport-success";
    } //addSportSubmit

    /**
     * Returns the form for editing a sport.
     *
     * @param model the model to be used in the operation
     * @return the form for editing a sport
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("edit-sport")
    public String editSportForm(Model model) {
        EditSport editSport;

        Objects.requireNonNull(model, "the specified model is null");

        editSport = new EditSport();

        model.addAttribute("editSport", editSport);

        return "edit-sport";
    } //editSportForm

    /**
     * Handles the request for attempting to edit a sport using the specified edit sport.
     *
     * @param editSport the edit sport to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to attempting to edit a sport using the specified edit sport
     * @throws NullPointerException if the specified edit sport or model is {@code null}
     */
    @PostMapping("edit-sport")
    public String editSportSubmit(@ModelAttribute EditSport editSport, Model model) {
        String idString;
        int id;
        String newName;
        String updateStatement;
        int newNameIndex = 1;
        int idIndex = 2;
        int rowsAffected;

        Objects.requireNonNull(editSport, "the specified edit sport is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        idString = editSport.getId();

        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return "edit-sport-failure-id-invalid";
        } //end try catch

        newName = editSport.getNewName();

        updateStatement = "UPDATE sports SET name = ? WHERE sport_id = ?;";

        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(updateStatement)) {
            preparedStatement.setString(newNameIndex, newName);

            preparedStatement.setInt(idIndex, id);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

            return "edit-sport-failure";
        } //end try catch

        if (rowsAffected > 0) {
            return "edit-sport-success";
        } else {
            return "edit-sport-failure-not-found";
        } //end if
    } //editSportSubmit

    /**
     * Returns the form for deleting a sport.
     *
     * @param model the model to be used in the operation
     * @return the form for deleting a sport
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("delete-sport")
    public String deleteSportForm(Model model) {
        DeleteSport deleteSport;

        Objects.requireNonNull(model, "the specified model is null");

        deleteSport = new DeleteSport();

        model.addAttribute("deleteSport", deleteSport);

        return "delete-sport";
    } //deleteSportForm

    /**
     * Handles the request for attempting to delete the specified delete sport.
     *
     * @param deleteSport the delete sport to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to attempting to delete the specified delete sport
     * @throws NullPointerException if the specified delete sport or model is {@code null}
     */
    @PostMapping("delete-sport")
    public String deleteSportSubmit(@ModelAttribute DeleteSport deleteSport, Model model) {
        String idString;
        int id;
        String deleteStatement;
        int idIndex = 1;
        int rowsAffected;

        Objects.requireNonNull(deleteSport, "the specified delete sport is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        idString = deleteSport.getId();

        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return "delete-sport-failure-id-invalid";
        } //end try catch

        deleteStatement = "DELETE FROM sports WHERE sport_id = ?;";

        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(deleteStatement)) {
            preparedStatement.setInt(idIndex, id);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

            return "delete-sport-failure";
        } //end try catch

        if (rowsAffected > 0) {
            return "delete-sport-success";
        } else {
            return "delete-sport-failure-not-found";
        } //end if
    } //deleteSportSubmit

    /**
     * Returns the form for searching for a sport.
     *
     * @param model the model to be used in the operation
     * @return the form for searching for a sport
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("search-sport")
    public String searchSportForm(Model model) {
        Sport sport;

        Objects.requireNonNull(model, "the specified model is null");

        sport = new Sport();

        model.addAttribute("sport", sport);

        return "search-sport";
    } //searchSportForm

    /**
     * Handles the request for searching for the specified sport.
     *
     * @param sport the sport to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to searching for the specified sport
     * @throws NullPointerException if the specified sport or model is {@code null}
     */
    @PostMapping(value = "search-sport", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String searchSportSubmit(@ModelAttribute Sport sport, Model model) {
        String name;
        String searchQuery;
        int nameIndex = 1;
        ResultSet resultSet;
        List<Integer> ids;
        List<String> names;
        int id;
        String format;
        StringBuilder stringBuilder;
        String tableString;
        String htmlString;

        Objects.requireNonNull(sport, "the specified sport is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        model.addAttribute("sport", sport);

        name = sport.getName();

        searchQuery = "SELECT * FROM sports WHERE UPPER(name) = UPPER(?);";

        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(searchQuery)) {
            preparedStatement.setString(nameIndex, name);

            resultSet = preparedStatement.executeQuery();

            ids = new ArrayList<>();

            names = new ArrayList<>();

            while (resultSet.next()) {
                id = resultSet.getInt("sport_id");

                name = resultSet.getString("name");

                ids.add(id);

                names.add(name);
            } //end while
        } catch (SQLException e) {
            e.printStackTrace();

            return "search-sport-failure";
        } //end try catch

        if (names.isEmpty()) {
            htmlString = "<!DOCTYPE HTML>\n" +
                         "<html>\n" +
                         "<head>\n" +
                         "<title>Search Sport</title>\n" +
                         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                         "</head>\n" +
                         "<body>\n" +
                         "<h1>Search Sport</h1>\n" +
                         "<p>No sports with that name exist.</p>\n" +
                         "</body>\n" +
                         "</html>";

            return htmlString;
        } //end if

        format = "<!DOCTYPE HTML>\n" +
                 "<html>\n" +
                 "<head>\n" +
                 "<title>Search Sport</title>\n" +
                 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                 "</head>\n" +
                 "<body>\n" +
                 "<h1>Search Sport</h1>\n" +
                 "<table border = '1'>\n" +
                 "<tr><th>ID</th><th>Name</th></tr>\n" +
                 "%s" +
                 "</table>\n" +
                 "</body>\n" +
                 "</html>";

        stringBuilder = new StringBuilder();

        for (int i = 0; i < ids.size(); i++) {
            id = ids.get(i);

            name = names.get(i);

            stringBuilder.append("<tr>");

            stringBuilder.append("<td>");

            stringBuilder.append(id);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(name);

            stringBuilder.append("</td>");

            stringBuilder.append("</tr>\n");
        } //end for

        tableString = stringBuilder.toString();

        htmlString = String.format(format, tableString);

        return htmlString;
    } //searchSportSubmit

    /**
     * Handles the request for listing sports.
     *
     * @return the response to listing sports
     */
    @GetMapping(value = "list-sports", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String listSports() {
        String query;
        ResultSet resultSet;
        List<Integer> ids;
        List<String> names;
        int id;
        String name;
        String format;
        StringBuilder stringBuilder;
        String tableString;
        String htmlString;

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        query = "SELECT * FROM sports;";

        try (Statement statement = RequestController.connection.createStatement()) {
            resultSet = statement.executeQuery(query);

            ids = new ArrayList<>();

            names = new ArrayList<>();

            while (resultSet.next()) {
                id = resultSet.getInt("sport_id");

                name = resultSet.getString("name");

                ids.add(id);

                names.add(name);
            } //end while
        } catch (SQLException e) {
            e.printStackTrace();

            return "list-sports-failure";
        } //end try catch

        if (names.isEmpty()) {
            htmlString = "<!DOCTYPE HTML>\n" +
                         "<html>\n" +
                         "<head>\n" +
                         "<title>List Sports</title>\n" +
                         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                         "</head>\n" +
                         "<body>\n" +
                         "<h1>List Sports</h1>\n" +
                         "<p>No sports exist.</p>\n" +
                         "</body>\n" +
                         "</html>";

            return htmlString;
        } //end if

        format = "<!DOCTYPE HTML>\n" +
                 "<html>\n" +
                 "<head>\n" +
                 "<title>List Sports</title>\n" +
                 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                 "</head>\n" +
                 "<body>\n" +
                 "<h1>List Sports</h1>\n" +
                 "<table border = '1'>\n" +
                 "<tr><th>ID</th><th>Name</th></tr>\n" +
                 "%s" +
                 "</table>\n" +
                 "</body>\n" +
                 "</html>";

        stringBuilder = new StringBuilder();

        for (int i = 0; i < ids.size(); i++) {
            id = ids.get(i);

            name = names.get(i);

            stringBuilder.append("<tr>");

            stringBuilder.append("<td>");

            stringBuilder.append(id);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(name);

            stringBuilder.append("</td>");

            stringBuilder.append("</tr>\n");
        } //end for

        tableString = stringBuilder.toString();

        htmlString = String.format(format, tableString);

        return htmlString;
    } //listSports

    /**
     * Handles the request for the sport page.
     *
     * @return the response to requesting the sport page
     */
    @GetMapping("sport-page")
    public String sportPage() {
        return "sport-page";
    } //sportPage

    /**
     * Returns the form for adding a season.
     *
     * @param model the model to be used in the operation
     * @return the form for adding a season
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("add-season")
    public String addSeasonForm(Model model) {
        Season season;

        Objects.requireNonNull(model, "the specified model is null");

        season = new Season();

        model.addAttribute("season", season);

        return "add-season";
    } //addSeasonForm

    /**
     * Handles the request for attempting to add the specified season.
     *
     * @param season the season to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to attempting to add the specified season
     * @throws NullPointerException if the specified sport or model is {@code null}
     */
    @PostMapping("add-season")
    public String addSeasonSubmit(@ModelAttribute Season season, Model model) {
        int year;
        int sport_id;
        String insertStatement;
        int yearIndex = 1;
        int nameIndex = 2;

        Objects.requireNonNull(season, "the specified sport is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        model.addAttribute("season", season);

        year = season.getYear();

        sport_id = getSportIdFromName(season.getSport_name());

        insertStatement = "INSERT INTO season (season_year, sport_id) VALUES (?, ?);";

        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(insertStatement)) {
            preparedStatement.setInt(yearIndex, year);

            preparedStatement.setInt(nameIndex, sport_id);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();

            return "add-season-failure";
        } //end try catch

        return "add-season-success";
    } //addSeasonSubmit

    /**
     * Returns the form for editing a season.
     *
     * @param model the model to be used in the operation
     * @return the form for editing a season
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("edit-season")
    public String editSeasonForm(Model model) {
        EditSeason editSeason;

        Objects.requireNonNull(model, "the specified model is null");

        editSeason = new EditSeason();

        model.addAttribute("editSeason", editSeason);

        return "edit-season";
    } //editSeasonForm

    /**
     * Handles the request for attempting to edit a season using the specified edit season.
     *
     * @param editSeason the edit sport to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to attempting to edit a season using the specified edit season
     * @throws NullPointerException if the specified edit season or model is {@code null}
     */
    @PostMapping("edit-season")
    public String editSeasonSubmit(@ModelAttribute EditSeason editSeason, Model model) {
        int oldYear;
        int newYear;
        int oldSport_id;
        int newSport_id;
        String updateStatement;
        int newYearIndex = 1;
        int newSportIndex = 2;
        int oldYearIndex = 3;
        int oldSportIndex = 4;
        int rowsAffected;

        Objects.requireNonNull(editSeason, "the specified edit sport is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        oldYear = editSeason.getOld_year();
        newYear = editSeason.getNew_year();

        oldSport_id = getSportIdFromName(editSeason.getOld_name());
        newSport_id = getSportIdFromName(editSeason.getNew_name());

        updateStatement = "UPDATE season SET season_year = ?, sport_id = ? WHERE season_year = ? AND sport_id = ?;";

        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(updateStatement)) {
            preparedStatement.setInt(newYearIndex, newYear);

            preparedStatement.setInt(newSportIndex, newSport_id);

            preparedStatement.setInt(oldYearIndex, oldYear);

            preparedStatement.setInt(oldSportIndex, oldSport_id);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

            return "edit-season-failure";
        } //end try catch

        if (rowsAffected > 0) {
            return "edit-season-success";
        } else {
            return "edit-season-failure-not-found";
        } //end if
    } //editSeasonSubmit

    /**
     * Returns the form for deleting a season.
     *
     * @param model the model to be used in the operation
     * @return the form for deleting a season
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("delete-season")
    public String deleteSeasonForm(Model model) {
        DeleteSeason deleteSeason;

        Objects.requireNonNull(model, "the specified model is null");

        deleteSeason = new DeleteSeason();

        model.addAttribute("deleteSeason", deleteSeason);

        return "delete-season";
    } //deleteSeasonForm

    /**
     * Handles the request for attempting to delete the specified delete season.
     *
     * @param deleteSeason the delete season to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to attempting to delete the specified delete season
     * @throws NullPointerException if the specified delete season or model is {@code null}
     */
    @PostMapping("delete-season")
    public String deleteSeasonSubmit(@ModelAttribute DeleteSeason deleteSeason, Model model) {
        int year;
        int sport_id;
        String deleteStatement;
        int yearIndex = 1;
        int sport_idIndex = 2;
        int rowsAffected;

        Objects.requireNonNull(deleteSeason, "the specified delete sport is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        year = deleteSeason.getYear();
        sport_id = getSportIdFromName(deleteSeason.getSport_name());

        deleteStatement = "DELETE FROM season WHERE season_year = ? AND sport_id = ?;";

        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(deleteStatement)) {
            preparedStatement.setInt(yearIndex, year);

            preparedStatement.setInt(sport_idIndex, sport_id);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

            return "delete-season-failure";
        } //end try catch

        if (rowsAffected > 0) {
            return "delete-season-success";
        } else {
            return "delete-season-failure-not-found";
        } //end if
    } //deleteSeasonSubmit

    /**
     * Returns the form for searching for a season.
     *
     * @param model the model to be used in the operation
     * @return the form for searching for a season
     * @throws NullPointerException if the specified model is {@code null}
     */
    @GetMapping("search-season")
    public String searchSeasonForm(Model model) {
        Season season;

        Objects.requireNonNull(model, "the specified model is null");

        season = new Season();

        model.addAttribute("season", season);

        return "search-season";
    } //searchSeasonForm

    /**
     * Handles the request for searching for the specified season.
     *
     * @param season the season to be used in the operation
     * @param model the model to be used in the operation
     * @return the response to searching for the specified season
     * @throws NullPointerException if the specified sport or model is {@code null}
     */
    @PostMapping(value = "search-season", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String searchSeasonSubmit(@ModelAttribute Season season, Model model) {
        int sport_id;
        int year;
        int return_year;
        int return_id;
        String searchQuery;
        int yearIndex = 1;
        int sport_idIndex = 2;
        ResultSet resultSet;
        List<Integer> years;
        List<Integer> ids;
        String format;
        StringBuilder stringBuilder;
        String tableString;
        String htmlString;

        Objects.requireNonNull(season, "the specified sport is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        model.addAttribute("season", season);

        year = season.getYear();
        sport_id = getSportIdFromName(season.getSport_name());

        searchQuery = "SELECT season_year, sport_id FROM season WHERE season_year = ? and sport_id = ?;";

        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(searchQuery)) {
            preparedStatement.setInt(yearIndex, year);

            preparedStatement.setInt(sport_idIndex, sport_id);

            resultSet = preparedStatement.executeQuery();

            ids = new ArrayList<>();

            years = new ArrayList<>();

            while (resultSet.next()) {
                return_id = resultSet.getInt("sport_id");

                return_year = resultSet.getInt("season_year");

                ids.add(return_id);

                years.add(year);
            } //end while
        } catch (SQLException e) {
            e.printStackTrace();

            return "search-season-failure";
        } //end try catch

        if (years.isEmpty()) {
            htmlString = "<!DOCTYPE HTML>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>Search Season</title>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>Search Season</h1>\n" +
                    "<p>No seasons with that name year.</p>\n" +
                    "</body>\n" +
                    "</html>";

            return htmlString;
        } //end if

        format = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Search Season</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Search Season</h1>\n" +
                "<table border = '1'>\n" +
                "<tr><th>Year</th><th>Sport_id</th></tr>\n" +
                "%s" +
                "</table>\n" +
                "</body>\n" +
                "</html>";

        stringBuilder = new StringBuilder();

        for (int i = 0; i < ids.size(); i++) {
            return_id = ids.get(i);

            year = years.get(i);

            stringBuilder.append("<tr>");

            stringBuilder.append("<td>");

            stringBuilder.append(year);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(return_id);

            stringBuilder.append("</td>");

            stringBuilder.append("</tr>\n");
        } //end for

        tableString = stringBuilder.toString();

        htmlString = String.format(format, tableString);

        return htmlString;
    } //searchSeasonSubmit

    /**
     * Handles the request for listing seasons.
     *
     * @return the response to listing seasons
     */
    @GetMapping(value = "list-seasons", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String listSeasons() {
        String query;
        ResultSet resultSet;
        List<Integer> years;
        List<Integer> sport_ids;
        int year;
        int sport_id;
        String format;
        StringBuilder stringBuilder;
        String tableString;
        String htmlString;

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        query = "SELECT season_year, sport_id FROM season;";

        try (Statement statement = RequestController.connection.createStatement()) {
            resultSet = statement.executeQuery(query);

            years = new ArrayList<>();

            sport_ids = new ArrayList<>();

            while (resultSet.next()) {
                sport_id = resultSet.getInt("sport_id");

                year = resultSet.getInt("season_year");

                sport_ids.add(sport_id);

                years.add(year);
            } //end while
        } catch (SQLException e) {
            e.printStackTrace();

            return "list-seasons-failure";
        } //end try catch

        if (years.isEmpty()) {
            htmlString = "<!DOCTYPE HTML>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>List Seasons</title>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>List Seasons</h1>\n" +
                    "<p>No seasons exist.</p>\n" +
                    "</body>\n" +
                    "</html>";

            return htmlString;
        } //end if

        format = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>List Seasons</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>List Seasons</h1>\n" +
                "<table border = '1'>\n" +
                "<tr><th>Year</th><th>Sport_id</th></tr>\n" +
                "%s" +
                "</table>\n" +
                "</body>\n" +
                "</html>";

        stringBuilder = new StringBuilder();

        for (int i = 0; i < years.size(); i++) {
            year = years.get(i);

            sport_id = sport_ids.get(i);

            stringBuilder.append("<tr>");

            stringBuilder.append("<td>");

            stringBuilder.append(year);

            stringBuilder.append("</td>");

            stringBuilder.append("<td>");

            stringBuilder.append(sport_id);

            stringBuilder.append("</td>");

            stringBuilder.append("</tr>\n");
        } //end for

        tableString = stringBuilder.toString();

        htmlString = String.format(format, tableString);

        return htmlString;
    } //listSeasons

    /**
     * Handles the request for the season page.
     *
     * @return the response to requesting the season page
     */
    @GetMapping("season-page")
    public String seasonPage() {
        return "season-page";
    } //sportPage

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

        Objects.requireNonNull(RequestController.connection, "the connection is null");

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

        id = RequestController.nextGameId;

        RequestController.nextGameId++;

        insertStatement = "INSERT INTO game (game_id, game_date, home_team_score, away_team_score, home_team_id, " +
                          "away_team_id, season_id) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(insertStatement)) {
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

        Objects.requireNonNull(RequestController.connection, "the connection is null");

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

        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(updateStatement)) {
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
    public String deleteSportSubmit(@ModelAttribute DeleteGame deleteGame, Model model) {
        String idString;
        int id;
        String deleteStatement;
        int idIndex = 1;
        int rowsAffected;

        Objects.requireNonNull(deleteGame, "the specified delete game is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        idString = deleteGame.getId();

        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return "delete-game-failure-id-invalid";
        } //end try catch

        deleteStatement = "DELETE FROM game WHERE game_id = ?;";

        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(deleteStatement)) {
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
    } //deleteSportSubmit

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

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        query = "SELECT * FROM game;";

        try (Statement statement = RequestController.connection.createStatement()) {
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

        if (dates.isEmpty()) {
            htmlString = "<!DOCTYPE HTML>\n" +
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

            return htmlString;
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
}