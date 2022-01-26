package sql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Contact;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBContacts {

    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> contactsList = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        DBQuery.setStatement(connection);
        Statement statement = DBQuery.getStatement();

        String selectStatement = "SELECT * FROM client_schedule.contacts;";
        statement.execute(selectStatement);
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            int contactId = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");
            String email = resultSet.getString("Email");

            Contact contact = new Contact(contactId, contactName, email);
            contactsList.add(contact);
        }
        return contactsList;
    }
}
