package com.stats.sports;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

/**
 * An application for sports statistics.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Logan Kulinski, lbk@purdue.edu
 * @version March 26, 2021
 */
@SpringBootApplication
public class Application {
    /**
     * Runs the sports statistics application.
     *
     * @param args the command line arguments
     */
	public static void main(String[] args) {
	    int expectedLength = 4;
	    String format;
	    String url;

	    if (args.length != expectedLength) {
            System.out.println("Error: a username, password, host, and database must be specified!");

	        return;
        } //end if

		format = "jdbc:mysql://%s/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&" +
				 "serverTimezone=UTC";

	    url = String.format(format, args[2], args[3]);

	    System.setProperty("url", url);

	    System.setProperty("username", args[0]);

	    System.setProperty("password", args[1]);

		SpringApplication.run(Application.class, args);
	} //main
}