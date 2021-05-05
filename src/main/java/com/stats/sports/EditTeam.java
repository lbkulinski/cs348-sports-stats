package com.stats.sports;

import java.util.Objects;

/**
 * An edited game in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Josh Lefton, jlefton@purdue.edu
 * @version 5/4/2021
 */
public final class EditTeam {
    /**
     * The ID of this edit team.
     */
    private String team_id;

    /**
     * The field of this edit team.
     */
    private String field;

    /**
     * The new value of this edit team.
     */
    private String newValue;

    /**
     * Constructs a newly allocated {@code EditTeam} object with a default ID, field, and new value of {@code null}.
     */
    public EditTeam() {
        this.team_id = null;

        this.field = null;

        this.newValue = null;
    } //EditTeam

    /**
     * Returns the ID of this edit team.
     *
     * @return the ID of this edit team
     */
    public String getId() {
        return this.team_id;
    } //getId

    /**
     * Returns the field of this edit team.
     *
     * @return the field of this edit team
     */
    public String getField() {
        return this.field;
    } //getField

    /**
     * Returns the new value of this edit team.
     *
     * @return the new value of this edit team
     */
    public String getNewValue() {
        return this.newValue;
    } //getNewValue

    /**
     * Updates the ID of this edit team with the specified ID.
     *
     * @param id the ID to be used in the operation
     */
    public void setId(String id) {
        this.team_id = id;
    } //setId

    /**
     * Updates the field of this edit team with the specified field.
     *
     * @param field the field to be used in the operation
     */
    public void setField(String field) {
        this.field = field;
    } //setField

    /**
     * Updates the new value of this edit team with the specified new value.
     *
     * @param newValue the new value to be used in the operation
     */
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    } //setNewValue

    /**
     * Returns the hash code of this edit team.
     *
     * @return the hash code of this edit team
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.team_id, this.field, this.newValue);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this edit team. {@code true} is returned if and only
     * if the specified object is an instance of {@code EditTeam} and its ID, field, and new value are equal to this
     * edit team's. {@code String} comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this team and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof EditTeam) {
            boolean equal;

            equal = Objects.equals(this.team_id, ((EditTeam) object).team_id);

            equal &= Objects.equals(this.field, ((EditTeam) object).field);

            equal &= Objects.equals(this.newValue, ((EditTeam) object).newValue);

            return equal;
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this edit team. The {@code String} representations of two edit
     * teams are equal if and only if the edit teams are equal according to {@link EditTeam#equals(Object)}.
     *
     * @return the {@code String} representation of this edit team
     */
    @Override
    public String toString() {
        String format = "EditTeam[team_id=%s, field=%s, newValue=%s]";

        return String.format(format, this.team_id, this.field, this.newValue);
    } //toString
}
