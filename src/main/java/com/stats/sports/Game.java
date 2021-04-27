package com.stats.sports;

import java.util.Objects;

/**
 * A game in the sports statistics application.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Logan Kulinski, lbk@purdue.edu
 * @version April 27, 2021
 */
public final class Game {
    /**
     * The date of this game.
     */
    private String date;

    /**
     * The season ID of this game.
     */
    private String seasonId;

    /**
     * The home team ID of this game.
     */
    private String homeTeamId;

    /**
     * The away team ID of this game
     */
    private String awayTeamId;

    /**
     * The home team score of this game.
     */
    private String homeTeamScore;

    /**
     * The away team score of this game.
     */
    private String awayTeamScore;

    /**
     * Constructs a newly allocated {@code Game} object with a default date, season ID, home team ID, away team ID,
     * home team score, and away team score of {@code null}.
     */
    public Game() {
        this.date = null;

        this.seasonId = null;

        this.homeTeamId = null;

        this.awayTeamId = null;

        this.homeTeamScore = null;

        this.awayTeamScore = null;
    } //Game

    /**
     * Returns the date of this game.
     *
     * @return the date of this game
     */
    public String getDate() {
        return this.date;
    } //getDate

    /**
     * Returns the season ID of this game.
     *
     * @return the season ID of this game
     */
    public String getSeasonId() {
        return this.seasonId;
    } //getSeasonId

    /**
     * Returns the home team ID of this game.
     *
     * @return the home team ID of this game
     */
    public String getHomeTeamId() {
        return this.homeTeamId;
    } //getHomeTeamId

    /**
     * Returns the away team ID of this game.
     *
     * @return the away team ID of this game
     */
    public String getAwayTeamId() {
        return this.awayTeamId;
    } //getAwayTeamId

    /**
     * Returns the home team score of this game.
     *
     * @return the home team score of this game
     */
    public String getHomeTeamScore() {
        return this.homeTeamScore;
    } //getHomeTeamScore

    /**
     * Returns the away team score of this game.
     *
     * @return the away team score of this game
     */
    public String getAwayTeamScore() {
        return this.awayTeamScore;
    } //getAwayTeamScore

    /**
     * Updates the date of this game with the specified date.
     *
     * @param date the date to be used in the update
     */
    public void setDate(String date) {
        this.date = date;
    } //setDate

    /**
     * Updates the season ID of this game with the specified season ID.
     *
     * @param seasonId the season ID to be used in the update
     */
    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    } //setSeasonId

    /**
     * Updates the home team ID of this game with the specified home team ID.
     *
     * @param homeTeamId the home team ID to be used in the update
     */
    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    } //setHomeTeamId

    /**
     * Updates the away team ID of this game with the specified away team ID.
     *
     * @param awayTeamId the away team ID to be used in the update
     */
    public void setAwayTeamId(String awayTeamId) {
        this.awayTeamId = awayTeamId;
    } //setAwayTeamId

    /**
     * Updates the home team score of this game with the specified home team score.
     *
     * @param homeTeamScore the home team score to be used in the update
     */
    public void setHomeTeamScore(String homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    } //setHomeTeamScore

    /**
     * Updates the away team score of this game with the specified away team score.
     *
     * @param awayTeamScore the away team score to be used in the update
     */
    public void setAwayTeamScore(String awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    } //setAwayTeamScore

    /**
     * Returns the hash code of this game.
     *
     * @return the hash code of this game
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.date, this.seasonId, this.homeTeamId, this.awayTeamId, this.homeTeamScore,
                            this.awayTeamScore);
    } //hashCode

    /**
     * Determines whether or not the specified object is equal to this game. {@code true} is returned if and only if
     * the specified object is an instance of {@code Game} and its date, season ID, home team ID, away team ID, home
     * team score, and away team score are equal to this game's. {@code String} comparisons are case-sensitive.
     *
     * @param object the object to be used in the comparisons
     * @return {@code true}, if the specified object is equal to this game and {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Game) {
            boolean equal;

            equal = Objects.equals(this.date, ((Game) object).date);

            equal &= Objects.equals(this.seasonId, ((Game) object).seasonId);

            equal &= Objects.equals(this.homeTeamId, ((Game) object).homeTeamId);

            equal &= Objects.equals(this.awayTeamId, ((Game) object).awayTeamId);

            equal &= Objects.equals(this.homeTeamScore, ((Game) object).homeTeamScore);

            equal &= Objects.equals(this.awayTeamScore, ((Game) object).awayTeamScore);

            return equal;
        } //end if

        return false;
    } //equals

    /**
     * Returns the {@code String} representation of this game. The {@code String} representations of two games are
     * equal if and only if the games are equal according to {@link Game#equals(Object)}.
     *
     * @return the {@code String} representation of this game
     */
    @Override
    public String toString() {
        String format = "Game[date=%s, seasonId=%s, homeTeamId=%s, awayTeamId=%s, homeTeamScore=%s, awayTeamScore=%s]";

        return String.format(format, this.date, this.seasonId, this.homeTeamId, this.awayTeamId, this.homeTeamScore,
                             this.awayTeamScore);
    } //toString
}