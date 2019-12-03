package katlasik.board.dtos;

import java.util.Objects;

public class RegistrationCheck {

    private String field;
    private boolean taken;

    public RegistrationCheck(String field, boolean taken) {
        this.field = field;
        this.taken = taken;
    }

    public String getField() {
        return field;
    }

    public boolean isTaken() {
        return taken;
    }

    @Override
    public String toString() {
        return "RegistrationCheck{" +
                "field='" + field + '\'' +
                ", taken='" + taken + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationCheck that = (RegistrationCheck) o;
        return Objects.equals(field, that.field) &&
                Objects.equals(taken, that.taken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, taken);
    }
}
