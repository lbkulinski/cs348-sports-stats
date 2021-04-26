package com.stats.sports;

import java.util.Objects;

/**
 * An edited season in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Elijah Heminger, eheminge@purdue.edu
 * @version April 26, 2021
 */
public final class EditSeason {
    /**
     * The old year of the edited season
     */
    private int old_year;

    /**
     * The old sport name of the edited season
     */
    private String old_name;

    /**
     * The new year of the edited season
     */
    private int new_year;

    /**
     * The new name of the edited season
     */

    private String new_name;

    /**
     * Constructs a newly allocated {@code EditSeason} object with the specified params
     *
     * @param old_year the old year used in construction
     * @param old_name the old name used in construction
     * @param new_year the new year used in construction
     * @param new_name the new name used in construction
     */
    public EditSeason(int old_year, String old_name, int new_year, String new_name) {
        this.old_year = old_year;
        this.old_name = old_name;
        this.new_year = new_year;
        this.new_name = new_name;
    } //EditSeason

    /**
     * Constructs a newly allocated {@code EditSeason} with default parameters
     */
    public EditSeason() {
        this.old_year = -1;
        this.old_name = null;
        this.new_year = -1;
        this.new_name = null;
    }

    public int getOld_year() {return this.old_year;}

    public String getOld_name() {return this.old_name;}

    public int getNew_year() {return this.new_year;}

    public String getNew_name() {return this.new_name;}

    public void setOld_year(int old_year) {this.old_year = old_year;}

    public void setOld_name(String old_name) {this.old_name = old_name;}

    public void setNew_year(int new_year) {this.new_year = new_year;}

    public void setNew_name(String new_name) {this.new_name = new_name;}

    @Override
    public int hashCode() {return Objects.hash(this.old_year + this.old_name + this.new_year + this.new_name);}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EditSeason) {
            boolean equal;
            equal = Objects.equals(this.old_year, ((EditSeason) obj).old_year);

            equal &= Objects.equals(this.old_name, ((EditSeason) obj).old_name);

            equal &= Objects.equals(this.new_year, ((EditSeason) obj).new_year);

            equal &= Objects.equals(this.new_name, ((EditSeason) obj).new_name);

            return equal;
        }

        return false;
    }

    @Override
    public String toString() {
        String format = "EditSeason[old_year=%d,old_name=%s,new_year=%d,new_name=%s]";

        return String.format(format, this.old_year, this.old_name, this.new_year, this.new_name);
    }

}
