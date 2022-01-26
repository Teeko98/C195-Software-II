package sql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Customer;

import java.sql.*;

public class DBCustomers {

    /**
     * @return an observable list of the Customer class of all customers in the sql server.
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        ObservableList<Customer> customersList = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        DBQuery.setStatement(connection);
        Statement statement = DBQuery.getStatement();

        String selectStatement = "SELECT * FROM client_schedule.customers;";
        statement.execute(selectStatement);
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            int customerId = resultSet.getInt("Customer_ID");
            String name = resultSet.getString("Customer_Name");
            String address = resultSet.getString("Address");
            String postalCode = resultSet.getString("Postal_Code");
            String phone = resultSet.getString("Phone");
            Date createDate = resultSet.getDate("Create_Date");
            String createdBy = resultSet.getString("Created_By");
            Date lastUpdate = resultSet.getDate("Last_Update");
            String lastUpdatedBy = resultSet.getString("Last_Updated_By");
            int divisionId = resultSet.getInt("Division_ID");

            Customer customer = new Customer(customerId, name, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
            customersList.add(customer);
        }
        return customersList;
    }
}