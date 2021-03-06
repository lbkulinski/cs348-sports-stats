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
 * A controller for season requests of the the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Elijah Heminger, eheminge@purdue.edu
 * @version April 29, 2021
 */
@Controller
public final class SeasonRequestController {
    /**
     * The connection of the {@code RequestController} class.
     */
    private static final Connection connection;

    static {
        connection = DatabaseConnection.getConnection();
    } //static

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

        Objects.requireNonNull(SeasonRequestController.connection, "the connection is null");

        searchQuery = "SELECT sport_id FROM sports WHERE UPPER(name) = UPPER(?);";
        try (PreparedStatement preparedStatement = SeasonRequestController.connection.prepareStatement(searchQuery)) {
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

        Objects.requireNonNull(SeasonRequestController.connection, "the connection is null");

        model.addAttribute("season", season);

        year = season.getYear();

        sport_id = getSportIdFromName(season.getSport_name());

        insertStatement = "INSERT INTO season (season_year, sport_id) VALUES (?, ?);";

        try (PreparedStatement preparedStatement = SeasonRequestController.connection.prepareStatement(insertStatement)) {
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

        Objects.requireNonNull(SeasonRequestController.connection, "the connection is null");

        oldYear = editSeason.getOld_year();
        newYear = editSeason.getNew_year();

        oldSport_id = getSportIdFromName(editSeason.getOld_name());
        newSport_id = getSportIdFromName(editSeason.getNew_name());

        updateStatement = "UPDATE season SET season_year = ?, sport_id = ? WHERE season_year = ? AND sport_id = ?;";

        try (PreparedStatement preparedStatement = SeasonRequestController.connection.prepareStatement(updateStatement)) {
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

        Objects.requireNonNull(SeasonRequestController.connection, "the connection is null");

        year = deleteSeason.getYear();
        sport_id = getSportIdFromName(deleteSeason.getSport_name());

        deleteStatement = "DELETE FROM season WHERE season_year = ? AND sport_id = ?;";

        try (PreparedStatement preparedStatement = SeasonRequestController.connection.prepareStatement(deleteStatement)) {
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

        Objects.requireNonNull(SeasonRequestController.connection, "the connection is null");

        model.addAttribute("season", season);

        year = season.getYear();
        sport_id = getSportIdFromName(season.getSport_name());

        searchQuery = "SELECT season_year, sport_id FROM season WHERE season_year = ? and sport_id = ?;";

        try (PreparedStatement preparedStatement = SeasonRequestController.connection.prepareStatement(searchQuery)) {
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
        List<String> sport_names;
        int year;
        String sport_id;
        String format;
        StringBuilder stringBuilder;
        String tableString;
        String htmlString;

        Objects.requireNonNull(SeasonRequestController.connection, "the connection is null");

        query = "SELECT s1.season_year, s2.name FROM season s1 JOIN sports s2 ON s1.sport_id = s2.sport_id;";

        try (Statement statement = SeasonRequestController.connection.createStatement()) {
            resultSet = statement.executeQuery(query);

            years = new ArrayList<>();

            sport_names = new ArrayList<>();

            while (resultSet.next()) {
                sport_id = resultSet.getString("name");

                year = resultSet.getInt("season_year");

                sport_names.add(sport_id);

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
                "<tr><th>Year</th><th>Sport_Name</th></tr>\n" +
                "%s" +
                "</table>\n" +
                "</body>\n" +
                "</html>";

        stringBuilder = new StringBuilder();

        for (int i = 0; i < years.size(); i++) {
            year = years.get(i);

            sport_id = sport_names.get(i);

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
}