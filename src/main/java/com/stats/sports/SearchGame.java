package com.stats.sports;

import java.util.Objects;

/**
 * A searched game in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Logan Kulinski, lbk@purdue.edu
 * @version April 29, 2021
 */
public final class SearchGame {
    /**
     * The field of this search game.
     */
    private String field;

    /**
     * The search value of this search game.
     */
    private String searchValue;

    /**
     * Constructs a newly allocated {@code SearchGame} object with a default field and search value of {@code null}.
     */
    public SearchGame() {
        this.field = null;

        this.searchValue = null;
    } //SearchGame

    /**
     * Returns the field of this search game.
     *
     * @return the field of this search game
     */
    public String getField() {
        return this.field;
    } //getField

    /**
     * Returns the search value of this search game.
     *
     * @return the search value of this search game
     */
    public String getSearchValue() {
        return this.searchValue;
    } //getSearchValue

    /**
     * Updates the field of this search game with the specified field.
     *
     * @param field the field to be used in the operation
     */
    public void setField(String field) {
        this.field = field;
    } //setField

    /**
     * Updates the search value of this search game with the specified search value.
     *
     * @param searchValue the search value to be used in the operation
     */
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    } //setSearchValue

    /**
     * Returns the hash code of this search game.
     *
     * @return the hash code of this search game
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.field, this.searchValue);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this search game. {@code true} is returned if and
     * only if the specified object is an instance of {@code SearchGame} and its field and search value are equal to
     * this search game's. {@code String} comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this search game and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof SearchGame) {
            boolean equal;

            equal = Objects.equals(this.field, ((SearchGame) object).field);

            equal &= Objects.equals(this.searchValue, ((SearchGame) object).searchValue);

            return equal;
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this search game. The {@code String} representations of two search
     * games are equal if and only if the search games are equal according to {@link SearchGame#equals(Object)}.
     *
     * @return the {@code String} representation of this search game
     */
    @Override
    public String toString() {
        String format = "SearchGame[field=%s, searchValue=%s]";

        return String.format(format, this.field, this.searchValue);
    } //toString
}