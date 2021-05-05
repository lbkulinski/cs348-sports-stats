package com.stats.sports;

import java.util.Objects;

/**
 * A deleted team in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Josh Lefton, jlefton@purdue.edu
 * @version 5/4/21
 */
public final class DeleteTeam {
    /**
     * The ID of this delete team.
     */
    private String team_id;

    /**
     * Constructs a newly allocated {@code DeleteTeam} object with a default ID of {@code null}.
     */
    public DeleteTeam() {
        this.team_id = null;
    } //DeleteTeam

    /**
     * Returns the ID of this delete team.
     *
     * @return the ID of this delete team
     */
    public String getId() {
        return this.team_id;
    } //getOldName

    /**
     * Updates the ID of this delete team with the specified ID.
     *
     * @param id the ID to be used in the operation
     */
    public void setId(String id) {
        this.team_id = id;
    } //setOldName

    /**
     * Returns the hash code of this delete team.
     *
     * @return the hash code of this delete team
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.team_id);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this delete team. {@code true} is returned if and
     * only if the specified object is an instance of {@code DeleteTeam} and its ID is equal to this delete team's. ID
     * comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this delete team and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof DeleteTeam) {
            return Objects.equals(this.team_id, ((DeleteTeam) object).team_id);
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this delete team. The {@code String} representations of two delete
     * teams are equal if and only if the delete teams are equal according to {@link DeleteTeam#equals(Object)}.
     *
     * @return the {@code String} representation of this delete team
     */
    @Override
    public String toString() {
        String format = "DeleteTeam[team_id=%s]";

        return String.format(format, this.team_id);
    } //toString
}