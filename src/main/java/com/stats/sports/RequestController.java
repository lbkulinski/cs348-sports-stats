package com.stats.sports;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.util.Objects;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public final class RequestController {
    private static int nextId;

    static {
        nextId = 0;
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
        model.addAttribute("sport", sport);

        sport.setId(RequestController.nextId);

        RequestController.nextId++;

        System.out.println(sport);

        return "add-sport-success";
    } //addSportSubmit
}