package com.stats.sports;

import java.util.Objects;

/**
 * A deleted game in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Logan Kulinski, lbk@purdue.edu
 * @version April 28, 2021
 */
public final class DeleteGame {
    /**
     * The ID of this delete game.
     */
    private String id;

    /**
     * Constructs a newly allocated {@code DeleteGame} object with a default ID of {@code null}.
     */
    public DeleteGame() {
        this.id = null;
    } //DeleteGame

    /**
     * Returns the ID of this delete game.
     *
     * @return the ID of this delete game
     */
    public String getId() {
        return this.id;
    } //getOldName

    /**
     * Updates the ID of this delete game with the specified ID.
     *
     * @param id the ID to be used in the operation
     */
    public void setId(String id) {
        this.id = id;
    } //setOldName

    /**
     * Returns the hash code of this delete game.
     *
     * @return the hash code of this delete game
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this delete game. {@code true} is returned if and
     * only if the specified object is an instance of {@code DeleteGame} and its ID is equal to this delete game's. ID
     * comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this delete game and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof DeleteGame) {
            return Objects.equals(this.id, ((DeleteGame) object).id);
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this delete game. The {@code String} representations of two delete
     * games are equal if and only if the delete games are equal according to {@link DeleteGame#equals(Object)}.
     *
     * @return the {@code String} representation of this delete game
     */
    @Override
    public String toString() {
        String format = "DeleteGame[id=%s]";

        return String.format(format, this.id);
    } //toString
}
