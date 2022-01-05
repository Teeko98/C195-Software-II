package sql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Appointment;
import main.Customer;

import java.sql.*;

public class DBAppointments {
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        DBQuery.setStatement(connection);
        Statement statement = DBQuery.getStatement();

        String selectStatement = "SELECT * FROM client_schedule.appointments;";
        statement.execute(selectStatement);
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            int appointmentId = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            Date startDate = resultSet.getDate("Start");
            Date endDate = resultSet.getDate("End");
            Date createDate = resultSet.getDate("Create_Date");
            String createdBy = resultSet.getString("Created_By");
            Date lastUpdate = resultSet.getDate("Last_Update");
            String lastUpdatedBy = resultSet.getString("Last_Updated_By");
            int customerId = resultSet.getInt("Customer_ID");
            int userId = resultSet.getInt("User_ID");
            int contactId = resultSet.getInt("Contact_ID");

            Appointment appointment = new Appointment(appointmentId, title, description, location, type, startDate, endDate, createDate, createdBy, lastUpdate, lastUpdatedBy, customerId, userId, contactId);
            appointmentsList.add(appointment);
        }
        return appointmentsList;
    }
}
