package com.stats.sports;

import java.util.Objects;

/**
 * A deleted season in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Elijah Heminger, eheminge@purdue.edu
 * @version April 26, 2021
 */

public final class DeleteSeason {
    /**
     * The year of this delete sport.
     */
    private int year;

    /**
     * The sport name of this delete sport.
     */
    private String sport_name;

    /**
     * Constructs a newly allocated {@code DeleteSeason} object with the specified year and sport name.
     *
     * @param year the year to be used in construction
     * @param sport_name the sport name to be used in construction.
     */
    public DeleteSeason(int year, String sport_name) {
        this.year = year;
        this.sport_name = sport_name;
    }

    /**
     * Constructs a newly allocated {@code DeleteSeason} object with a default year of -1 and name of {@code null}.
     */
    public DeleteSeason() {
        this.year = -1;
        this.sport_name = null;
    }

    /**
     * Returns the year of the deleted season.
     *
     * @return the year of the deleted season
     */
    public int getYear() {return this.year;}

    /**
     * Returns the sport name of the deleted season.
     *
     * @return the sport name of the deleted season
     */
    public String getSportName() {return this.sport_name;}

    /**
     * Updates the year of this delete season with the specified year
     *
     * @param year the year to be used in the operation
     */
    public void setYear(int year) {this.year = year;}

    /**
     * Updates the sport name of this delete season with the specified sport name
     *
     * @param sport_name the sport name to be used in the operation
     */
    public void setSportName(String sport_name) {this.sport_name = sport_name;}

    /**
     * Returns the hash code of this delete season
     *
     * @return the hash code of this delete season
     */
    @Override
    public int hashCode() {return Objects.hash(this.year + this.sport_name);}

    /**
     * Determines whether or not the specified object is equal to this delete object. {@code true} is returned if and
     * only if the specified object is an instance of {@code DeleteSeason} and its year and sport name is equal to this
     * delete season's.
     *
     * @param obj the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this delete season and {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DeleteSeason) {
            return Objects.equals(this.year, ((DeleteSeason) obj).year) &&
                    Objects.equals(this.sport_name, ((DeleteSeason) obj).sport_name);
        }

        return false;
    }

    @Override
    public String toString() {
        String format = "DeleteSeason[year=%d,sport_id=%s]";

        return String.format(format, this.year, this.sport_name);
    }

}
