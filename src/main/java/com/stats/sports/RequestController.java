package com.stats.sports;

import org.springframework.stereotype.Controller;
import java.sql.Connection;
import java.sql.Statement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.util.Objects;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

@Controller
public final class RequestController {
    private static final Connection connection;

    private static int nextId;

    static {
        connection = DatabaseConnection.getConnection();

        nextId = getId();
    } //static

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

    @GetMapping("add-sport")
    public String addSportForm(Model model) {
        Sport sport;

        Objects.requireNonNull(model, "the specified model is null");

        sport = new Sport();

        model.addAttribute("sport", sport);

        return "add-sport";
    } //addSportForm

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

    @GetMapping("search-sport")
    public String searchSportForm(Model model) {
        Sport sport;

        Objects.requireNonNull(model, "the specified model is null");

        sport = new Sport();

        model.addAttribute("sport", sport);

        return "search-sport";
    } //searchSportForm

    @PostMapping(value = "search-sport", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String searchSportSubmit(@ModelAttribute Sport sport, Model model) {
        String name;
        String searchQuery;
        int nameIndex = 1;
        ResultSet resultSet;
        List<String> names;
        String format;
        StringBuilder stringBuilder;
        String tableString;
        String htmlString;

        Objects.requireNonNull(sport, "the specified sport is null");

        Objects.requireNonNull(model, "the specified model is null");

        Objects.requireNonNull(RequestController.connection, "the connection is null");

        model.addAttribute("sport", sport);

        name = sport.getName();

        searchQuery = "SELECT name FROM sports WHERE UPPER(name) = UPPER(?);";

        try (PreparedStatement preparedStatement = RequestController.connection.prepareStatement(searchQuery)) {
            preparedStatement.setString(nameIndex, name);

            resultSet = preparedStatement.executeQuery();

            names = new ArrayList<>();

            while (resultSet.next()) {
                name = resultSet.getString("name");

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
                 "<tr><th>Name</th></tr>\n" +
                 "%s" +
                 "</table>\n" +
                 "</body>\n" +
                 "</html>";

        stringBuilder = new StringBuilder();

        for (String nameString : names) {
            stringBuilder.append("<tr>");

            stringBuilder.append("<td>");

            stringBuilder.append(nameString);

            stringBuilder.append("</td>");

            stringBuilder.append("</tr>\n");
        } //end for

        tableString = stringBuilder.toString();

        htmlString = String.format(format, tableString);

        return htmlString;
    } //searchSportSubmit
}