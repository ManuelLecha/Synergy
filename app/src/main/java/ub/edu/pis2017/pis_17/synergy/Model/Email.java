package ub.edu.pis2017.pis_17.synergy.Model;

/**
 * Created by kanales on 22/04/2018.
 */

public class Email {
    private String value;

    public Email(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Email email = (Email) o;

        return value != null ? value.equals(email.value) : email.value == null;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
