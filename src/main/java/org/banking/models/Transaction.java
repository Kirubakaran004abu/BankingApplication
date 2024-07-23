package org.banking.models;

import org.banking.database.jdbc;

import java.sql.SQLException;

public class Transaction {

    private static jdbc db = new jdbc();
    public static String message;
    public static double balance;

    public static void deposit(double amount, int account_no) {
        try {
            db.updateBalance(account_no, amount);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void withdraw(double amount, int account_no) {
        try {
            balance = db.fetchBalance(account_no);
            if (balance < amount) {
                throw new SQLException("Insufficient funds");
            }
            db.updateBalance(account_no, -amount);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void transfer(double amount, int from_account_no, int to_account_no) {
        message += to_account_no;
        withdraw(amount, from_account_no);
        message = "fund recieved from " + from_account_no;
        deposit(amount, to_account_no);

    }

}
