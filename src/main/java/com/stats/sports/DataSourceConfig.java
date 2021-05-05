package com.stats.sports;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * A DataSource connection to the sports statistics database.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Josh Lefton, jlefton@purdue.edu
 * @version 5/5/2021
 */

@Configuration
public class DataSourceConfig {
    /**
     * Returns a DataSource object representing the database
     *
     * @return the DataSource object representing the database
     */
    @Bean
    public static DataSource getDataSource() {
        DataSourceBuilder dsBuilder = DataSourceBuilder.create();
        dsBuilder.url(System.getProperty("url"));
        dsBuilder.username(System.getProperty("username"));
        dsBuilder.password(System.getProperty("password"));
        return dsBuilder.build();
    }
}
