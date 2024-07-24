package org.banking.models;

import org.banking.database.jdbc;

import java.sql.SQLException;

public class Transaction {

    private static final jdbc db = new jdbc();
    public static String message;
    public static double balance;

    public static void deposit(double amount, int account_no) throws SQLException {

        int cap = db.isVerified(account_no) ? 100000000 : 10000;

        if (db.fetchBalance(account_no) + amount > cap)
            throw new SQLException("You Account can't deposit more than " + cap);

        db.updateBalance(account_no, amount);

        db.commit();
    }

    public static void withdraw(double amount, int account_no) throws SQLException {

        balance = db.fetchBalance(account_no);
        if (balance < amount) {
            throw new SQLException("Insufficient funds");
        }
        db.updateBalance(account_no, -amount);
        db.commit();

    }

    public static void transfer(double amount, int from_account_no, int to_account_no) throws SQLException {

        if (!db.checkAccountExist(to_account_no))
            throw new SQLException("Account not found");

        if (to_account_no == from_account_no)
            throw new SQLException("Cannot transfer funds");

        message += to_account_no;
        balance = db.fetchBalance(from_account_no);
        if (balance < amount) {
            throw new SQLException("Insufficient funds");
        }
        db.updateBalance(from_account_no, -amount);

        message = "fund recieved from " + from_account_no;
        deposit(amount, to_account_no);

    }

}
