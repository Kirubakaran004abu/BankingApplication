package org.banking.models;

public class Credential {
    public String name;
    public int accountNumber;
    public boolean is_admin;
    public boolean logged_in;

    public Credential(String name, boolean admin, boolean logged_in, int accountNumber) {
        this.name = name;
        this.is_admin = admin;
        this.logged_in = logged_in;
        this.accountNumber = accountNumber;
    }
}
