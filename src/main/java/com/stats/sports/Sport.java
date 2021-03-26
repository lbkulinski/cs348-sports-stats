package com.stats.sports;

import java.util.Objects;

public final class Sport {
    private String name;

    public Sport(String name) {
        this.name = name;
    } //Sport

    public Sport() {
        this(null);
    } //Sport

    public String getName() {
        return this.name;
    } //getName

    public void setName(String name) {
        this.name = name;
    } //setName

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    } //hashCode

    @Override
    public boolean equals(Object object) {
        if (object instanceof Sport) {
            return Objects.equals(this.name, ((Sport) object).name);
        } //end if

        return false;
    } //equals

    @Override
    public String toString() {
        String format = "Sport[name=%s]";

        return String.format(format, this.name);
    } //toString
}
