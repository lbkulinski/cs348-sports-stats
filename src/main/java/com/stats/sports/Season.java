package com.stats.sports;

import java.util.Objects;

/**
 * A season in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Elijah Heminger, eheminge@purdue.edu
 * @version April 26, 2021
 */

public final class Season {
    /**
     * The year of the season
     */
    private int year;

    /**
     * The sport name of the season
     */
    private String sport_name;

    /**
     * Constructs a newly allocated {@code Season} object with the specified year and sport id.
     *
     * @param year the year to be used in construction
     * @param sport_name the sport name to be used in construction
     */
    public Season(int year, String sport_name) {
        this.year = year;
        this.sport_name = sport_name;
    }

    /**
     * Constructs a newly allocated {@code Season} object with a default year of -1 and sport_id of {@code null}
     */
    public Season() {
        this.year = -1;
        this.sport_name = null;
    }

    /**
     * Returns the year of this season.
     *
     * @return the year of this season.
     */
    public int getYear() {return this.year;} //getYear

    /**
     * Returns the sport_name associated with this season.
     *
     * @return the sport_name of this season.
     */
    public String getSportName() {return this.sport_name;} //getSportName

    /**
     * Updates the year of this season with the specified date
     *
     * @param year the year to be used in the operation
     */
    public void setYear(int year) {this.year = year;} //setYear

    /**
     * Updates the sport_id to the specified value
     *
     * @param sport_name the sport_name to be used in the operation
     */
    public void setSportName(String sport_name) {this.sport_name = sport_name;} //setSportName

    /**
     * Returns the hash code of this season
     *
     * @return the hash code of this season
     */
    @Override
    public int hashCode() { return Objects.hash(this.year + sport_name); } //hashCode

    /**
     * Determines whether or not the specified object is equal to this season. {@code true} is returned if and only if
     * the specified object is an instance of {@code Season} and its year and sport_id are equal to this seasons.
     * @param obj the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this season {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Season) {
            return Objects.equals(this.year, ((Season) obj).year) && Objects.equals(this.sport_name, ((Season) obj).sport_name);
        } //end if

        return false;
    } //equals

    @Override
    public String toString() {
        String format = "Season[year=%d,sport_name=%s]";

        return String.format(format, this.year, this.sport_name);
    } //toString
}


