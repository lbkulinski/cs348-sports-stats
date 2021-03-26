package com.stats.sports;

import java.util.Objects;

public final class Sport {
    private int id;

    private String name;

    public Sport(int id, String name) {
        this.id = id;

        this.name = name;
    } //Sport

    public Sport() {
        this(0, null);
    } //Sport

    public int getId() {
        return this.id;
    } //getId

    public String getName() {
        return this.name;
    } //getName

    public void setId(int id) {
        this.id = id;
    } //setId

    public void setName(String name) {
        this.name = name;
    } //setName

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    } //hashCode

    @Override
    public boolean equals(Object object) {
        if (object instanceof Sport) {
            boolean equal;

            equal = this.id == ((Sport) object).id;

            equal &= Objects.equals(this.name, ((Sport) object).name);

            return equal;
        } //end if

        return false;
    } //equals

    @Override
    public String toString() {
        String format = "Sport[id=%d, name=%s]";

        return String.format(format, this.id, this.name);
    } //toString
}
