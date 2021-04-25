package com.stats.sports;

import java.util.Objects;

/**
 * An edited sport in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Logan Kulinski, lbk@purdue.edu
 * @version April 25, 2021
 */
public final class EditSport {
    /**
     * The old name of this edit sport.
     */
    private String oldName;

    /**
     * The new name of this edit sport.
     */
    private String newName;

    /**
     * Constructs a newly allocated {@code EditSport} object with the specified old name and new name.
     *
     * @param oldName the old name to be used in construction
     * @param newName the new name to be used in construction
     */
    public EditSport(String oldName, String newName) {
        this.oldName = oldName;

        this.newName = newName;
    } //EditSport

    /**
     * Constructs a newly allocated {@code EditSport} object with a default old name and new name of {@code null}.
     */
    public EditSport() {
        this(null, null);
    } //EditSport

    /**
     * Returns the old name of this edit sport.
     *
     * @return the old name of this edit sport
     */
    public String getOldName() {
        return this.oldName;
    } //getOldName

    /**
     * Returns the new name of this edit sport.
     *
     * @return the new name of this edit sport
     */
    public String getNewName() {
        return this.newName;
    } //getNewName

    /**
     * Updates the old name of this edit sport with the specified old name.
     *
     * @param oldName the old name to be used in the operation
     */
    public void setOldName(String oldName) {
        this.oldName = oldName;
    } //setOldName

    /**
     * Updates the new name of this edit sport with the specified new name.
     *
     * @param newName the new name to be used in the operation
     */
    public void setNewName(String newName) {
        this.newName = newName;
    } //setNewName

    /**
     * Returns the hash code of this edit sport.
     *
     * @return the hash code of this edit sport
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.oldName, this.newName);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this edit sport. {@code true} is returned if and only
     * if the specified object is an instance of {@code EditSport} and its old name and new name are equal to this edit
     * sport's. Name comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this edit sport and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof EditSport) {
            boolean equal;

            equal = Objects.equals(this.oldName, ((EditSport) object).oldName);

            equal &= Objects.equals(this.newName, ((EditSport) object).newName);

            return equal;
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this edit sport. The {@code String} representations of two edit
     * sports are equal if and only if the edit sports are equal according to {@link EditSport#equals(Object)}.
     *
     * @return the {@code String} representation of this edit sport
     */
    @Override
    public String toString() {
        String format = "EditSport[oldName=%s, newName=%s]";

        return String.format(format, this.oldName, this.newName);
    } //toString
}
