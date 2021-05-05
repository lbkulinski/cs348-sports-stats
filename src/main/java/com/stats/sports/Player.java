package com.stats.sports;

import java.util.Objects;

/**
 * A player in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Josh Lefton, jlefton@purdue.edu
 * @version 5/3/2021
 */
public final class Player {
    /**
     * The unique ID of this player.
     */
    private String player_id;

    /**
     * The name of this player.
     */
    private String name;

    /**
     * The ID of the team for which this player plays.
     */
    private String team_id;


    /**
     * Constructs a newly allocated {@code Player} with player_id, name, and team_id.
     */
    public Player() {
        this.player_id = null;

        this.name = null;

        this.team_id = null;
    } // Player

    /**
     * Returns the ID of this player.
     
     * @return the ID of this player
     */
    public String getPlayer_id() {
        return this.player_id;
    } //getPlayer_id

    /**
     * Returns the name of this player.
     *
     * @return the name of this player
     */
    public String getName() {
        return this.name;
    } //getName

    /**
     * Returns the ID of the team for which this player plays.
     *
     * @return the ID of the team for which this player plays
     */
    public String getTeam_id() {
        return this.team_id;
    } //getTeam_id


    /**
     * Updates the ID of this player with the specified player_id.
     *
     * @param player_id the ID of the player to be used in the update
     */
    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    } //setPlayer_id

    /**
     * Updates the name of this player with the specified name.
     *
     * @param name the name to be used in the update
     */
    public void setName(String name) {
        this.name = name;
    } //setName

    /**
     * Updates the team ID of the team for which this player plays with the specified team ID.
     *
     * @param team_id the team ID to be used in the update
     */
    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    } //setTeam_id


    /**
     * Returns the hash code of this player.
     *
     * @return the hash code of this player
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.player_id, this.name, this.team_id);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this player. {@code true} is returned if and only if
     * the specified object is an instance of {@code Player} and its player_id, name, and team_id are equal to this
     * player's. {@code String} comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this player and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Player) {
            boolean equal;

            equal = Objects.equals(this.player_id, ((Player) object).player_id);

            equal &= Objects.equals(this.name, ((Player) object).name);

            equal &= Objects.equals(this.team_id, ((Player) object).team_id);

            return equal;
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this player. The {@code String} representations of two players are
     * equal if and only if the players are equal according to {@link Player#equals(Object)}.
     *
     * @return the {@code String} representation of this player
     */
    @Override
    public String toString() {
        String format = "Player[player_id=%s, name=%s, team_id=%s]";

        return String.format(format, this.player_id, this.name, this.team_id);
    } //toString
}
