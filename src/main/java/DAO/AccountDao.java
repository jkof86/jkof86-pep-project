package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDao {

    public List<Account> getAllAccounts() {
        Connection conn = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            // Write SQL logic here
            String sql = "select * from Account";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account a = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));

                accounts.add(a);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account register(Account a) {
        // The registration will be successful if and only if the username is not blank,
        // the password is at least 4 characters
        // long, and an Account with that username does not already exist. If all these
        // conditions are met,
        // the response body should contain a JSON of the Account, including its
        // account_id. The response status should be 200 OK, which is the default. The
        // new account should be persisted to the database.
        // If the registration is not successful, the response status should be 400.
        // (Client error)

        Connection conn = new ConnectionUtil().getConnection();

        // first we check for existing accounts
        List<Account> accounts = new ArrayList<>();
        accounts = getAllAccounts();

        //first we check for blank username and password length
        if ((a.getUsername() == "") ||
            (a.getPassword().length() < 4)) {
            return null;
        }
        
        //if username already exists, return null
        for (Account i : accounts) {
            if (a.getUsername() == i.getUsername()) {
                return null;
            }
        }

        //if we reach this far, all checks pass and we can register account to DB
            try {
                // Write SQL logic here
                String sql = "insert into Account (username, password) values (?, ?)";

                // needed to recover the auto generated account id for later use
                PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, a.getUsername());
                preparedStatement.setString(2, a.getPassword());
                //failed registration
                if (preparedStatement.executeUpdate() == 0) {
                    return null;
                }
                // collect the generated account id and assign to the obj
                // then return updated obj
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    int generated_account_id = rs.getInt(1);
                    return new Account(generated_account_id, a.getUsername(), a.getPassword());
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        return null;
    }

    public Account login(Account a) {
        /*
         * As a user, I should be able to verify my login on the endpoint POST
         * localhost:8080/login.
         * The request body will contain a JSON representation of an Account, not
         * containing an account_id.
         * In the future, this action may generate a Session token to allow the user to
         * securely use the site.
         * We will not worry about this for now.The login will be successful if and only
         * if the username and password
         * provided in the request body JSON match a real account existing on the
         * database.
         * If successful, the response body should contain a JSON of the account in the
         * response body,
         * including its account_id. The response status should be 200 OK, which is the
         * default.
         * If the login is not successful, the response status should be 401.
         * (Unauthorized)
         */

        Connection conn = new ConnectionUtil().getConnection();

        // query the DB for the matching username and password
        try {
            // Write SQL logic here
            String sql = "select * from Account where username = ? and password = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, a.getUsername());
            preparedStatement.setString(2, a.getPassword());
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }
}
