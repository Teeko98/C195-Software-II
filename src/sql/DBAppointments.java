package sql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Appointment;
import main.Customer;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
            LocalDateTime startLocalDateTime = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endLocalDateTime = resultSet.getTimestamp("End").toLocalDateTime();
            LocalDateTime createLocalDateTime = resultSet.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = resultSet.getString("Created_By");
            LocalDateTime lastUpdateLocalDateTime = resultSet.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdatedBy = resultSet.getString("Last_Updated_By");
            int customerId = resultSet.getInt("Customer_ID");
            int userId = resultSet.getInt("User_ID");
            int contactId = resultSet.getInt("Contact_ID");

            Appointment appointment = new Appointment(appointmentId, title, description, location, type, startLocalDateTime, endLocalDateTime, createLocalDateTime, createdBy, lastUpdateLocalDateTime, lastUpdatedBy, customerId, userId, contactId);
            appointmentsList.add(appointment);
        }
        return appointmentsList;
    }
}
