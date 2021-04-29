package com.stats.sports;

import java.util.Objects;

/**
 * An edited game in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Logan Kulinski, lbk@purdue.edu
 * @version April 28, 2021
 */
public final class EditGame {
    /**
     * The ID of this edit game.
     */
    private String id;

    /**
     * The field of this edit game.
     */
    private String field;

    /**
     * The new value of this edit game.
     */
    private String newValue;

    /**
     * Constructs a newly allocated {@code EditGame} object with a default ID, field, and new value of {@code null}.
     */
    public EditGame() {
        this.id = null;

        this.field = null;

        this.newValue = null;
    } //EditGame

    /**
     * Returns the ID of this edit game.
     *
     * @return the ID of this edit game
     */
    public String getId() {
        return this.id;
    } //getId

    /**
     * Returns the field of this edit game.
     *
     * @return the field of this edit game
     */
    public String getField() {
        return this.field;
    } //getField

    /**
     * Returns the new value of this edit game.
     *
     * @return the new value of this edit game
     */
    public String getNewValue() {
        return this.newValue;
    } //getNewValue

    /**
     * Updates the ID of this edit game with the specified ID.
     *
     * @param id the ID to be used in the operation
     */
    public void setId(String id) {
        this.id = id;
    } //setId

    /**
     * Updates the field of this edit game with the specified field.
     *
     * @param field the field to be used in the operation
     */
    public void setField(String field) {
        this.field = field;
    } //setField

    /**
     * Updates the new value of this edit game with the specified new value.
     *
     * @param newValue the new value to be used in the operation
     */
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    } //setNewValue

    /**
     * Returns the hash code of this edit game.
     *
     * @return the hash code of this edit game
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.field, this.newValue);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this edit game. {@code true} is returned if and only
     * if the specified object is an instance of {@code EditGame} and its ID, field, and new value are equal to this
     * edit game's. {@code String} comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this edit game and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof EditGame) {
            boolean equal;

            equal = Objects.equals(this.id, ((EditGame) object).id);

            equal &= Objects.equals(this.field, ((EditGame) object).field);

            equal &= Objects.equals(this.newValue, ((EditGame) object).newValue);

            return equal;
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this edit game. The {@code String} representations of two edit
     * games are equal if and only if the edit games are equal according to {@link EditGame#equals(Object)}.
     *
     * @return the {@code String} representation of this edit game
     */
    @Override
    public String toString() {
        String format = "EditGame[id=%s, field=%s, newValue=%s]";

        return String.format(format, this.id, this.field, this.newValue);
    } //toString
}