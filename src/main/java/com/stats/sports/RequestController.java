package com.stats.sports;

import org.springframework.stereotype.Controller;
import java.sql.Connection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.util.Objects;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.sql.SQLException;

@Controller
public final class RequestController {
    private static int nextId;

    private final static Connection connection;

    static {
        nextId = 0;

        connection = DatabaseConnection.getConnection();
    } //static

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

        Objects.requireNonNull(connection, "the connection is null");

        model.addAttribute("sport", sport);

        id = RequestController.nextId;

        RequestController.nextId++;

        name = sport.getName();

        insertStatement = "INSERT INTO sports (sport_id, name) VALUES (?, ?);";

        try (var preparedStatement = connection.prepareStatement(insertStatement)) {
            preparedStatement.setInt(idIndex, id);

            preparedStatement.setString(nameIndex, name);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();

            return "add-sport-failure";
        } //end try catch

        return "add-sport-success";
    } //addSportSubmit
}