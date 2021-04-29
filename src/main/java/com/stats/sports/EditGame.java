package com.stats.sports;

import java.util.Objects;

public final class EditGame {
    private String id;

    private String field;

    private String newValue;

    public EditGame() {
        this.id = null;

        this.field = null;

        this.newValue = null;
    } //EditGame

    public String getId() {
        return this.id;
    } //getId

    public String getField() {
        return this.field;
    } //getField

    public String getNewValue() {
        return this.newValue;
    } //getNewValue

    public void setId(String id) {
        this.id = id;
    } //setId

    public void setField(String field) {
        this.field = field;
    } //setField

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    } //setNewValue

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.field, this.newValue);
    } //hashCode

    @Override
    public boolean equals(Object object) {
        if (object instanceof EditGame) {
            boolean equal;

            equal = Objects.equals(this.id, ((EditGame) object).id);

            equal &= Objects.equals(this.field, ((EditGame) object).field);

            equal &= Objects.equals(this.newValue, ((EditGame) object).newValue);

            return equal;
        } //end if

        return false;
    } //equals

    @Override
    public String toString() {
        String format = "EditGame[id=%s, field=%s, newValue=%s]";

        return String.format(format, this.id, this.field, this.newValue);
    } //toString
}