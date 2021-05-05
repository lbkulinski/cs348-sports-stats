package com.stats.sports;

import java.util.Objects;

/**
 * A searched player in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Josh Lefton, jlefton@purdue.edu
 * @version 5/4/21
 */
public final class SearchPlayer {
    /**
     * The field of this search player.
     */
    private String field;

    /**
     * The search value of this search player.
     */
    private String searchValue;

    /**
     * Constructs a newly allocated {@code SearchPlayer} object with a default field and search value of {@code null}.
     */
    public SearchPlayer() {
        this.field = null;

        this.searchValue = null;
    } //SearchPlayer

    /**
     * Returns the field of this search player.
     *
     * @return the field of this search player
     */
    public String getField() {
        return this.field;
    } //getField

    /**
     * Returns the search value of this search player.
     *
     * @return the search value of this search player
     */
    public String getSearchValue() {
        return this.searchValue;
    } //getSearchValue

    /**
     * Updates the field of this search player with the specified field.
     *
     * @param field the field to be used in the operation
     */
    public void setField(String field) {
        this.field = field;
    } //setField

    /**
     * Updates the search value of this search player with the specified search value.
     *
     * @param searchValue the search value to be used in the operation
     */
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    } //setSearchValue

    /**
     * Returns the hash code of this search player.
     *
     * @return the hash code of this search player
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.field, this.searchValue);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this search player. {@code true} is returned if and
     * only if the specified object is an instance of {@code SearchPlayer} and its field and search value are equal to
     * this search player's. {@code String} comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this search player and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof SearchPlayer) {
            boolean equal;

            equal = Objects.equals(this.field, ((SearchPlayer) object).field);

            equal &= Objects.equals(this.searchValue, ((SearchPlayer) object).searchValue);

            return equal;
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this search player. The {@code String} representations of two search
     * players are equal if and only if the search players are equal according to {@link SearchPlayer#equals(Object)}.
     *
     * @return the {@code String} representation of this search player
     */
    @Override
    public String toString() {
        String format = "SearchPlayer[field=%s, searchValue=%s]";

        return String.format(format, this.field, this.searchValue);
    } //toString
}