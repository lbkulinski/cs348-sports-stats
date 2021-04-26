package com.stats.sports;

import java.util.Objects;

/**
 * A deleted sport in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Logan Kulinski, lbk@purdue.edu
 * @version April 26, 2021
 */
public final class DeleteSport {
    /**
     * The ID of this delete sport.
     */
    private String id;

    /**
     * Constructs a newly allocated {@code DeleteSport} object with the specified ID.
     *
     * @param id the ID to be used in construction
     */
    public DeleteSport(String id) {
        this.id = id;
    } //DeleteSport

    /**
     * Constructs a newly allocated {@code DeleteSport} object with a default ID of {@code null}.
     */
    public DeleteSport() {
        this(null);
    } //DeleteSport

    /**
     * Returns the ID of this delete sport.
     *
     * @return the ID of this delete sport
     */
    public String getId() {
        return this.id;
    } //getOldName

    /**
     * Updates the ID of this delete sport with the specified ID.
     *
     * @param id the ID to be used in the operation
     */
    public void setId(String id) {
        this.id = id;
    } //setOldName

    /**
     * Returns the hash code of this delete sport.
     *
     * @return the hash code of this delete sport
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this delete sport. {@code true} is returned if and
     * only if the specified object is an instance of {@code DeleteSport} and its ID is equal to this delete sport's.
     * ID comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this delete sport and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof DeleteSport) {
            return Objects.equals(this.id, ((DeleteSport) object).id);
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this delete sport. The {@code String} representations of two delete
     * sports are equal if and only if the delete sports are equal according to {@link DeleteSport#equals(Object)}.
     *
     * @return the {@code String} representation of this delete sport
     */
    @Override
    public String toString() {
        String format = "DeleteSport[id=%s]";

        return String.format(format, this.id);
    } //toString
}
