package com.stats.sports;

import java.util.Objects;

/**
 * An edited player in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Josh Lefton, jlefton@purdue.edu
 * @version 5/4/2021
 */
public final class EditPlayer {
    /**
     * The ID of this edit player.
     */
    private String player_id;

    /**
     * The field of this edit player.
     */
    private String field;

    /**
     * The new value of this edit player.
     */
    private String newValue;

    /**
     * Constructs a newly allocated {@code EditPlayer} object with a default ID, field, and new value of {@code null}.
     */
    public EditPlayer() {
        this.player_id = null;

        this.field = null;

        this.newValue = null;
    } //EditPlayer

    /**
     * Returns the ID of this edit player.
     *
     * @return the ID of this edit player
     */
    public String getId() {
        return this.player_id;
    } //getId

    /**
     * Returns the field of this edit player.
     *
     * @return the field of this edit player
     */
    public String getField() {
        return this.field;
    } //getField

    /**
     * Returns the new value of this edit player.
     *
     * @return the new value of this edit player
     */
    public String getNewValue() {
        return this.newValue;
    } //getNewValue

    /**
     * Updates the ID of this edit player with the specified ID.
     *
     * @param id the ID to be used in the operation
     */
    public void setId(String id) {
        this.player_id = id;
    } //setId

    /**
     * Updates the field of this edit player with the specified field.
     *
     * @param field the field to be used in the operation
     */
    public void setField(String field) {
        this.field = field;
    } //setField

    /**
     * Updates the new value of this edit player with the specified new value.
     *
     * @param newValue the new value to be used in the operation
     */
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    } //setNewValue

    /**
     * Returns the hash code of this edit player.
     *
     * @return the hash code of this edit player
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.player_id, this.field, this.newValue);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this edit player. {@code true} is returned if and only
     * if the specified object is an instance of {@code EditPlayer} and its ID, field, and new value are equal to this
     * edit player's. {@code String} comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this player and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof EditPlayer) {
            boolean equal;

            equal = Objects.equals(this.player_id, ((EditPlayer) object).player_id);

            equal &= Objects.equals(this.field, ((EditPlayer) object).field);

            equal &= Objects.equals(this.newValue, ((EditPlayer) object).newValue);

            return equal;
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this edit player. The {@code String} representations of two edit
     * players are equal if and only if the edit players are equal according to {@link EditPlayer#equals(Object)}.
     *
     * @return the {@code String} representation of this edit player
     */
    @Override
    public String toString() {
        String format = "EditPlayer[player_id=%s, field=%s, newValue=%s]";

        return String.format(format, this.player_id, this.field, this.newValue);
    } //toString
}