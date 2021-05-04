package com.stats.sports;

import org.springframework.stereotype.Controller;

import java.sql.*;
import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A controller for the index page.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Jack Doherty, doherty9@purdue.edu
 * @version May 3, 2021
 */

public class IndexController {

    /**
     * Handles the request for the index page.
     *
     * @return the response to requesting the index page
     */
    @GetMapping("/")
    public String indexPage() {
        return "index";
    } //indexPage
}
