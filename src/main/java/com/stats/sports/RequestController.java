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
 * @version April 26, 2021
 */
@Controller
public final class RequestController {
    /**
     * The connection of the {@code RequestController} class.
     */
    private static final Connection connection;

    /**
     * The next ID of the {@code RequestController} class.
     */
    private static int nextId;

    static {
        connection = DatabaseConnection.getConnection();

        nextId = getId();
    } //static

    /**
     * Returns the ID to be assigned to the next sport.
     *
     * @return the ID to be assigned to the next sport
     */
    private static int getId() {
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
    } //getId

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

        id = RequestController.nextId;

        RequestController.nextId++;

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

        searchQuery = "SELECT sport_id, name FROM sports WHERE UPPER(name) = UPPER(?);";

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

        query = "SELECT sport_id, name FROM sports;";

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
}