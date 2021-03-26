package com.stats.sports;

import java.util.Objects;

/**
 * A sport in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Logan Kulinski, lbk@purdue.edu
 * @version March 26, 2021
 */
public final class Sport {
    /**
     * The name of this sport.
     */
    private String name;

    /**
     * Constructs a newly allocated {@code Sport} object with the specified name.
     *
     * @param name the name to be used in construction
     */
    public Sport(String name) {
        this.name = name;
    } //Sport

    /**
     * Constructs a newly allocated {@code Sport} object with a default name of {@code null}.
     */
    public Sport() {
        this(null);
    } //Sport

    /**
     * Returns the name of this sport.
     *
     * @return the name of this sport
     */
    public String getName() {
        return this.name;
    } //getName

    /**
     * Updates the name of this sport with the specified name.
     *
     * @param name the name to be used in the operation
     */
    public void setName(String name) {
        this.name = name;
    } //setName

    /**
     * Returns the hash code of this sport.
     *
     * @return the hash code of this sport
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this sport. {@code true} is returned if and only if
     * the specified object is an instance of {@code Sport} and its name is equal to this sport's. Name comparisons are
     * case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this sport and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Sport) {
            return Objects.equals(this.name, ((Sport) object).name);
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this sport. The {@code String} representations of two sports are
     * equal if and only if the sports are equal according to {@link Sport#equals(Object)}.
     *
     * @return the {@code String} representation of this sport
     */
    @Override
    public String toString() {
        String format = "Sport[name=%s]";

        return String.format(format, this.name);
    } //toString
}