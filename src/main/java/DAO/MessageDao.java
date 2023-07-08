package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import Util.ConnectionUtil;

public class MessageDao {

    public AccountService accountService;
    private MessageService messageService;

    public List<Message> getAllMessages() {
        /*
         * Get All Messages
         * As a user, I should be able to submit a GET request on the endpoint GET
         * localhost:8080/messages.
         * 
         * The response body should contain a JSON representation of a list containing
         * all messages retrieved from the database. It is expected for the list to
         * simply be empty if there are no messages. The response status should always
         * be 200, which is the default.
         * 
         */
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            // Write SQL logic here
            String sql = "select * from Message";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                Message m = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                messages.add(m);
            }
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
        return messages;

    }

    public Message getMessageById(int id) {
        /*
         * Get One Message Given Message Id
         * As a user, I should be able to submit a GET request on the endpoint GET
         * localhost:8080/messages/{message_id}.
         * 
         * The response body should contain a JSON representation of the message
         * identified by the message_id. It is expected for the response body to simply
         * be empty if there is no such message. The response status should always be
         * 200, which is the default.
         */

        Connection conn = ConnectionUtil.getConnection();
        try {
            // Write SQL logic here
            String sql = "select * from Message where message_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getAllMessagesByAccountId(int id) {
        /*
         * Get All Messages From User Given Account Id
         * As a user, I should be able to submit a GET request on the endpoint GET
         * localhost:8080/accounts/{account_id}/messages.
         * 
         * The response body should contain a JSON representation of a list containing
         * all messages posted by a particular user, which is retrieved from the
         * database. It is expected for the list to simply be empty if there are no
         * messages. The response status should always be 200, which is the default
         */
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            // Write SQL logic here
            String sql = "select * from Message join Account on Message.posted_by = Account.account_id where account_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                Message m = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                messages.add(m);
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

        return messages;
    }

    public Message deleteMessageById(int id) {
        /*
         * Delete a Message Given Message Id
         * As a User, I should be able to submit a DELETE request on the endpoint DELETE
         * localhost:8080/messages/{message_id}.
         * 
         * The deletion of an existing message should remove an existing message from
         * the database. If the message existed, the response body should contain the
         * now-deleted message. The response status should be 200, which is the default.
         * 
         * If the message did not exist, the response status should be 200, but the
         * response body should be empty. This is because the DELETE verb is intended to
         * be idempotent, ie, multiple calls to the DELETE endpoint should respond with
         * the same type of response.
         */

        Connection conn = ConnectionUtil.getConnection();

        // before deleting the record we save a copy to return in the response
        Message m = getMessageById(id);

        try {
            // Write SQL logic here
            String sql = "delete from Message where message_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return m;
    }

    public Message updateMessageById(Message m) {
        /*
         * * Update Message Given Message Id
         * As a user, I should be able to submit a PATCH request on the endpoint PATCH
         * localhost:8080/messages/{message_id}. The request body should contain a new
         * message_text values to replace the message identified by message_id. The
         * request body can not be guaranteed to contain any other information.
         * 
         * The update of a message should be successful if and only if the message id
         * already exists and the new message_text is not blank and is not over 255
         * characters. If the update is successful, the response body should contain the
         * full updated message (including message_id, posted_by, message_text, and
         * time_posted_epoch), and the response status should be 200, which is the
         * default. The message existing on the database should have the updated
         * message_text.
         * 
         * If the update of the message is not successful for any reason, the response
         * status should be 400. (Client error)
         */
        Connection conn = ConnectionUtil.getConnection();
        try {
            // Write SQL logic here
            String sql = "update Message set message_text = ? where message_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, m.getMessage_text());
            preparedStatement.setInt(2, m.getMessage_id());

            // failed message update
            if (preparedStatement.executeUpdate() == 0) {
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // return updated Message obj
        return getMessageById(m.getMessage_id());
    }

    public Message createNewMessage(Message m) {
        /*
         * Create New Message
         * As a user, I should be able to submit a new post on the endpoint POST
         * localhost:8080/messages. The request body will contain a JSON representation
         * of a message, which should be persisted to the database, but will not contain
         * a message_id.
         * 
         * The creation of the message will be successful if and only if the
         * message_text is not blank, is under 255 characters, and posted_by refers to a
         * real, existing user. If successful, the response body should contain a JSON
         * of the message, including its message_id. The response status should be 200,
         * which is the default. The new message should be persisted to the database.
         * 
         * If the creation of the message is not successful, the response status should
         * be 400. (Client error)
         */

        Connection conn = ConnectionUtil.getConnection();
        try {
            // Write SQL logic here
            String sql = "insert into Message (posted_by, message_text, time_posted_epoch) values (?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, m.getPosted_by());
            preparedStatement.setString(2, m.getMessage_text());
            preparedStatement.setLong(3, m.getTime_posted_epoch());

            // failed message creation
            if (preparedStatement.executeUpdate() == 0) {
                return null;
            }

            // collect the generated account id and assign to the obj
            // then return updated obj
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int generated_account_id = rs.getInt(1);
                return new Message(generated_account_id, m.getPosted_by(), m.getMessage_text(),
                        m.getTime_posted_epoch());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
