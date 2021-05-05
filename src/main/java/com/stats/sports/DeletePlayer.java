package com.stats.sports;

import java.util.Objects;

/**
 * A deleted player in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Josh Lefton, jlefton@purdue.edu
 * @version 5/4/21
 */
public final class DeletePlayer {
    /**
     * The ID of this delete player.
     */
    private String player_id;

    /**
     * Constructs a newly allocated {@code DeletePlayer} object with a default ID of {@code null}.
     */
    public DeletePlayer() {
        this.player_id = null;
    } //DeletePlayer

    /**
     * Returns the ID of this delete player.
     *
     * @return the ID of this delete player
     */
    public String getId() {
        return this.player_id;
    } //getOldName

    /**
     * Updates the ID of this delete player with the specified ID.
     *
     * @param id the ID to be used in the operation
     */
    public void setId(String id) {
        this.player_id = id;
    } //setOldName

    /**
     * Returns the hash code of this delete player.
     *
     * @return the hash code of this delete player
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.player_id);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this delete player. {@code true} is returned if and
     * only if the specified object is an instance of {@code DeletePlayer} and its ID is equal to this delete player's. ID
     * comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this delete player and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof DeletePlayer) {
            return Objects.equals(this.player_id, ((DeletePlayer) object).player_id);
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this delete player. The {@code String} representations of two delete
     * players are equal if and only if the delete players are equal according to {@link DeletePlayer#equals(Object)}.
     *
     * @return the {@code String} representation of this delete player
     */
    @Override
    public String toString() {
        String format = "DeletePlayer[player_id=%s]";

        return String.format(format, this.player_id);
    } //toString
}