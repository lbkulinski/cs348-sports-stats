package com.stats.sports;

import java.util.Objects;

/**
 * A team in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Josh Lefton, jlefton@purdue.edu
 * @version 5/3/2021
 */
public final class Team {
    /**
     * The unique ID of this team.
     */
    private String team_id;

    /**
     * The name of this team.
     */
    private String team_name;

    /**
     * The ID of the sport in which this team competes.
     */
    private String sport_id;


    /**
     * Constructs a newly allocated {@code Team} with team_id, team_name, and sport_id.
     */
    public Team() {
        this.team_id = null;

        this.team_name = null;

        this.sport_id = null;
    } // Team

    /**
     * Returns the ID of this team.
     
     * @return the ID of this team
     */
    public String getTeam_id() {
        return this.team_id;
    } //getTeam_id

    /**
     * Returns the name of this team.
     *
     * @return the name of this team
     */
    public String getTeam_name() {
        return this.team_name;
    } //getTeam_name

    /**
     * Returns the ID of the sport in which this team competes.
     *
     * @return the ID of the sport in which this team competes
     */
    public String getSport_id() {
        return this.sport_id;
    } //getSport_id


    /**
     * Updates the ID of this team with the specified team_id.
     *
     * @param team_id the ID of the team to be used in the update
     */
    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    } //setTeam_id

    /**
     * Updates the name of this team with the specified team_name.
     *
     * @param team_name the team_name to be used in the update
     */
    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    } //setTeam_name

    /**
     * Updates the sport ID of the sport in which this team plays with the specified sport ID.
     *
     * @param sport_id the sport ID to be used in the update
     */
    public void setSport_id(String sport_id) {
        this.sport_id = sport_id;
    } //setSport_id


    /**
     * Returns the hash code of this team.
     *
     * @return the hash code of this team
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.team_id, this.team_name, this.sport_id);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this team. {@code true} is returned if and only if
     * the specified object is an instance of {@code Team} and its team_id, team_name, and sport_id are equal to this
     * team's. {@code String} comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this team and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Team) {
            boolean equal;

            equal = Objects.equals(this.team_id, ((Team) object).team_id);

            equal &= Objects.equals(this.team_name, ((Team) object).team_name);

            equal &= Objects.equals(this.sport_id, ((Team) object).sport_id);

            return equal;
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this player. The {@code String} representations of two teams are
     * equal if and only if the players are equal according to {@link Team#equals(Object)}.
     *
     * @return the {@code String} representation of this team
     */
    @Override
    public String toString() {
        String format = "Player[team_id=%s, team_name=%s, sport_id=%s]";

        return String.format(format, this.team_id, this.team_name, this.sport_id);
    } //toString
}
