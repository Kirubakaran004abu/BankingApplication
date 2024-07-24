package org.banking.database;

import jakarta.servlet.http.HttpServletRequest;
import org.banking.models.Credential;
import org.banking.models.Hashing;
import org.banking.models.Transaction;
import org.banking.models.User;

import java.sql.*;

public class jdbc {

    private Connection conn;
    private Statement statement;

    public jdbc() {
        try {
            String username = "root";
            String password = "karan004";
            String url = "jdbc:mysql://localhost:3306/banking";
            conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);
            statement = conn.createStatement();

        } catch (SQLException e) {
            System.out.println("SQL Exception : "+ e.getMessage());
            System.out.println("Vendor Error : "+ e.getErrorCode());
        }
    }

    public Credential checkPassword(int account_no, String password) throws SQLException {
        ResultSet resultSet = statement.executeQuery(
                "SELECT username, password, is_admin FROM credintials WHERE account_no = " + account_no
        );
        resultSet.next();

        try {
            if (Hashing.verifyPassword(resultSet.getString("password"), password)) {
                System.out.println("Password Matched " + password);
                return new Credential(resultSet.getString("username"),
                        resultSet.getBoolean("is_admin"),
                        true,
                        account_no);
            }
        } catch (SQLException e) {
            throw new SQLException("Account doesn't exist");
        }

        throw new SQLException("Incorrect Password");
    }

    public void updatePassword(int account_no, String password) throws SQLException {
        password = Hashing.hashPassword(password);
        statement.executeUpdate(
                String.format("UPDATE credintials SET password ='%s' WHERE account_no = %d", password, account_no)
        );
        conn.commit();
    }

    public ResultSet fetchUsers() throws SQLException {
        String columns = " account_no, username, mobile, email, date_of_birth ";
        return statement.executeQuery(
                "SELECT" + columns + "FROM details;"
        );
    }

    public ResultSet fetchUser(int account_no) throws SQLException {
        if (!this.checkAccountExist(account_no)){
            throw new SQLException("Account Not Found");
        }

        String columns = " username, mobile, email, date_of_birth, id_proof, Address ";
        ResultSet rs = statement.executeQuery(
                "SELECT" + columns + "FROM details WHERE account_no = " + account_no
        );
        rs.next();
        return rs;
    }

    public ResultSet fetchTransaction(int account_no) throws SQLException {
        String columns = " date_time, description, amount, new_balance ";
        return statement.executeQuery(
                "select  * from (SELECT" + columns + "FROM transactions WHERE account_no = " +
                        account_no + " Order By date_time desc limit 10) as datas order by date_time;"
        );
    }

    public User insertUser(User user) throws SQLException {
        do {
            user.change_account_number();
        } while (this.checkAccountExist(user.get_account_number()));

        PreparedStatement insertStatement = conn.prepareStatement(
                "INSERT INTO details (account_no, username, mobile, email, date_of_birth, account_type, id_proof, Address) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);"
        );

        insertStatement.setInt(1, user.get_account_number());
        insertStatement.setString(2, user.get_full_name());
        insertStatement.setString(3, user.get_mobile());
        insertStatement.setString(4, user.get_email());
        insertStatement.setString(5, user.get_date_of_birth());
        insertStatement.setString(6, user.get_account_type());
        insertStatement.setString(7, user.get_id_proof());
        insertStatement.setString(8, user.get_address());

        insertStatement.executeUpdate();

        conn.commit();

        PreparedStatement updateStatement = conn.prepareStatement("UPDATE credintials SET password = ? WHERE account_no = ?");

        updateStatement.setInt(2, user.get_account_number());
        updateStatement.setString(1, Hashing.hashPassword(user.get_password()));

        updateStatement.executeUpdate();

        System.out.println(user.get_account_number() + " " + user.get_password());
        conn.commit();
        return user;
    }

    public double fetchBalance(int account_no) throws SQLException {
        ResultSet rs = statement.executeQuery(
                "SELECT balance from customers where account_no = " + account_no
        );
        if (rs.next())
            return rs.getDouble("balance");
        else
            throw new SQLException("Account Not Found");
    }

    public void updateBalance(int account_no, double amount) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO transactions (account_no, amount, description) values (?, ?, ?);"
        );
        ps.setInt(1, account_no);
        ps.setDouble(2, amount);
        ps.setString(3, Transaction.message);
        ps.executeUpdate();

        System.out.println(account_no + " " + amount);
    }

    public void deleteUser(int account_no) throws SQLException {
        statement.executeUpdate("delete from details where account_no = " + account_no);
        System.out.println("Successfully Deleted User " + account_no);
        conn.commit();
    }

    public boolean checkAccountExist(int account_no) throws SQLException {
        return (boolean) statement.executeQuery(
                "SELECT 1 FROM credintials WHERE account_no = " + account_no
        ).next();
    }

    public void modifyUser(int account_no, HttpServletRequest request) throws SQLException {
        String[] columns = {"username", "mobile", "email", "address", "id_proof"};
        StringBuilder query = new StringBuilder();
        String temp;
        for (String column : columns) {
            temp = request.getParameter(column);
            if (query.isEmpty()) {
                query.append(temp == null || temp.isEmpty() ? "" : column + "='" + temp + "'");
            } else {
                query.append(temp == null || temp.isEmpty() ? "" : ", " + column + "='" + temp + "'");
            }

        }

        statement.executeUpdate("UPDATE details SET " + query.toString() + " WHERE account_no = " + account_no);
        conn.commit();
        System.out.println("UPDATE details SET " + query.toString() + " WHERE account_no = " + account_no);
    }

    public boolean isVerified(int account_no) throws SQLException {
        ResultSet rs = statement.executeQuery(
                "SELECT is_verified FROM customers WHERE account_no = " + account_no
        );
        rs.next();
        return rs.getBoolean("is_verified");
    }

    public void commit(){
        try {
            if (conn != null) {
                conn.commit();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception during close: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception during close: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        jdbc jdbc = new jdbc();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException : " + e.getMessage());
            return;
        }

        try {
            jdbc.checkPassword(12345, "1543");
        } catch (SQLException e) {
            System.out.println("SQL Exception : "+ e.getMessage());
            System.out.println("Vendor Error : "+ e.getErrorCode());
        }
    }
}
