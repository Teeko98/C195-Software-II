package sql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

public class DBAppointments {
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        DBQuery.setStatement(connection);
        Statement statement = DBQuery.getStatement();

        String selectStatement = "SELECT * FROM client_schedule.appointments;";
        statement.execute(selectStatement);
        ResultSet resultSet = statement.getResultSet();

        ZoneId utcZoneId = ZoneId.of("UTC");
        ZoneId localZoneId = ZoneId.systemDefault();

        while (resultSet.next()) {
            int appointmentId = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            ZonedDateTime utcStartZonedDateTime = ZonedDateTime.of(resultSet.getTimestamp("Start").toLocalDateTime(), utcZoneId);
            ZonedDateTime localStartZonedDateTime = ZonedDateTime.ofInstant(utcStartZonedDateTime.toInstant(), localZoneId);
            ZonedDateTime utcEndZonedDateTime = ZonedDateTime.of(resultSet.getTimestamp("End").toLocalDateTime(), utcZoneId);
            ZonedDateTime localEndZonedDateTime = ZonedDateTime.ofInstant(utcEndZonedDateTime.toInstant(), localZoneId);
            ZonedDateTime utcCreateZonedDateTime = ZonedDateTime.of(resultSet.getTimestamp("Create_Date").toLocalDateTime(), utcZoneId);
            ZonedDateTime localCreateZonedDateTime = ZonedDateTime.ofInstant(utcCreateZonedDateTime.toInstant(), localZoneId);
            String createdBy = resultSet.getString("Created_By");
            ZonedDateTime utcLastUpdateZonedDateTime = ZonedDateTime.of(resultSet.getTimestamp("Last_Update").toLocalDateTime(), utcZoneId);
            ZonedDateTime localLastUpdateZonedDateTime = ZonedDateTime.ofInstant(utcLastUpdateZonedDateTime.toInstant(), localZoneId);
            String lastUpdatedBy = resultSet.getString("Last_Updated_By");
            int customerId = resultSet.getInt("Customer_ID");
            int userId = resultSet.getInt("User_ID");
            int contactId = resultSet.getInt("Contact_ID");


            Appointment appointment = new Appointment(appointmentId, title, description, location, type,
                    localStartZonedDateTime.toLocalDateTime(), localEndZonedDateTime.toLocalDateTime(),
                    localCreateZonedDateTime.toLocalDateTime(), createdBy,
                    localLastUpdateZonedDateTime.toLocalDateTime(), lastUpdatedBy, customerId, userId, contactId);
            appointmentsList.add(appointment);
        }
        return appointmentsList;
    }
}
