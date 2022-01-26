package controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Appointment;
import main.Contact;
import main.Customer;
import main.User;
import sql.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.ResourceBundle;

public class ModifyAppointmentController implements Initializable {

    public Label appointmentIdLabel;
    public TextField titleTextField;
    public TextField descriptionTextField;
    public TextField locationTextField;
    public ComboBox contactComboBox;
    public TextField typeTextField;
    public DatePicker datePicker;
    public ComboBox startTimeHourComboBox;
    public ComboBox startTimeMinuteComboBox;
    public ComboBox startTimePeriodComboBox;
    public ComboBox endTimeHourComboBox;
    public ComboBox endTimeMinuteComboBox;
    public ComboBox endTimePeriodComboBox;
    public ComboBox userComboBox;
    public ComboBox customerComboBox;

    // Appointment Variables
    int appointmentId;
    String title;
    String description;
    String location;
    int contactId;
    String type;
    String startDateTime;
    String endDateTime;
    int userId;
    int customerId;
    LocalDateTime utcStartLocalDateTime;
    LocalDateTime utcEndLocalDateTime;
    String lastUpdate;

    ObservableList<Customer> allCustomers = DBCustomers.getAllCustomers();
    ObservableList<User> allUsers = DBUsers.getAllUsers();
    ObservableList<Contact> allContacts = DBContacts.getAllContacts();

    /**
     * Fills the modify appointment form with the data from the appointment to be modified.
     * @param appointmentToBeModified the passed in appointment to be modified.
     */
    public void receiveAppointmentToBeModified(Appointment appointmentToBeModified) {
        // Inputs variables from given appointment
        appointmentId = appointmentToBeModified.getAppointmentId();
        title = appointmentToBeModified.getTitle();
        description = appointmentToBeModified.getDescription();
        location = appointmentToBeModified.getLocation();
        type = appointmentToBeModified.getType();
        ZonedDateTime utcStartZonedDateTime = ZonedDateTime.of(appointmentToBeModified.getStartDate(), ZoneId.of("UTC"));
        ZonedDateTime utcEndZonedDateTime = ZonedDateTime.of(appointmentToBeModified.getEndDate(), ZoneId.of("UTC"));
        customerId = appointmentToBeModified.getCustomerId();
        userId = appointmentToBeModified.getUserId();
        contactId = appointmentToBeModified.getContactId();

        // Updates form with given appointment data
        appointmentIdLabel.setText(Integer.toString(appointmentId));
        titleTextField.setText(title);
        descriptionTextField.setText(description);
        locationTextField.setText(location);
        FilteredList<Contact> filteredContact = new FilteredList<>(allContacts, i-> i.getContactId() == contactId); //Gets selected contact object using the contact id
        contactComboBox.getSelectionModel().select(filteredContact.get(0).getContactName());
        typeTextField.setText(type);

        ZonedDateTime localStartZonedDateTime = ZonedDateTime.ofInstant(utcStartZonedDateTime.toInstant(), ZoneId.systemDefault()); //Converts passed in time from UTC to the users local time
        ZonedDateTime localEndZonedDateTime = ZonedDateTime.ofInstant(utcEndZonedDateTime.toInstant(), ZoneId.systemDefault()); //Converts passed in time from UTC to the users local time
        datePicker.setValue(localStartZonedDateTime.toLocalDate());
        if (localStartZonedDateTime.getHour() > 12) {
            startTimeHourComboBox.getSelectionModel().select(Integer.toString(localStartZonedDateTime.getHour() - 12));
            startTimePeriodComboBox.getSelectionModel().select("PM");
        } else {
            startTimeHourComboBox.getSelectionModel().select(Integer.toString(localStartZonedDateTime.getHour()));
            startTimePeriodComboBox.getSelectionModel().select("AM");
        }
        if (localStartZonedDateTime.getMinute() < 10) {
            startTimeMinuteComboBox.getSelectionModel().select("0" + localStartZonedDateTime.getMinute());
        } else {
            startTimeMinuteComboBox.getSelectionModel().select(Integer.toString(localStartZonedDateTime.getMinute()));
        }
        if (localEndZonedDateTime.getHour() > 12) {
            endTimeHourComboBox.getSelectionModel().select(Integer.toString(localEndZonedDateTime.getHour() - 12));
            endTimePeriodComboBox.getSelectionModel().select("PM");
        } else {
            endTimeHourComboBox.getSelectionModel().select(Integer.toString(localEndZonedDateTime.getHour()));
            endTimePeriodComboBox.getSelectionModel().select("AM");
        }
        if (localEndZonedDateTime.getMinute() < 10) {
            endTimeMinuteComboBox.getSelectionModel().select("0" + localEndZonedDateTime.getMinute());
        } else {
            endTimeMinuteComboBox.getSelectionModel().select(Integer.toString(localEndZonedDateTime.getMinute()));
        }

        FilteredList<User> filteredUser = new FilteredList<>(allUsers, i-> i.getUserId() == userId); // Gets selected user object using the given user id
        userComboBox.getSelectionModel().select(filteredUser.get(0).getUsername());
        FilteredList<Customer> filteredCustomer = new FilteredList<>(allCustomers, i-> i.getCustomerId() == customerId); // Gets selected customer object using given customer id
        customerComboBox.getSelectionModel().select(filteredCustomer.get(0).getCustomerName());

    }

    public ModifyAppointmentController() throws SQLException {}

    /**
     * Returns to the main menu view.
     */
    public void returnToMainFormView(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/MainFormView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    /**
     * Called when the user presses the save button.
     * Checks if input data is valid, reads the input data into the appointment variables, and checks if there is an
     * appointment conflict. If successful, an SQL UPDATE statement is prepared with the given data, executed, and returns to
     * the main menu view.
     */
    public void saveButtonPushed(ActionEvent event) throws IOException, SQLException {
        if (isInputDataValid()) {
            readInputData();
            if (checkAppointmentConflict()) {
                return;
            }

            Connection connection = JDBC.getConnection();
            DBQuery.setStatement(connection);
            Statement statement = DBQuery.getStatement();

            String insertStatement = "UPDATE client_schedule.appointments SET " +
                    "Title = '" + title + "', " +
                    "Description = '" + description + "', " +
                    "Location = '" + location + "', " +
                    "Type = '" + type + "', " +
                    "Start = '" + startDateTime + "', " +
                    "End = '" + endDateTime + "', " +
                    "Last_Update = '" + lastUpdate + "', "  +
                    "Last_Updated_By = '" + DBUsers.currentUser.getUsername() + "', " +
                    "Customer_ID = " + customerId + ", " +
                    "User_ID = " + userId + ", " +
                    "Contact_ID = " + contactId +
                    " WHERE Appointment_ID = " + appointmentId + ";";
            statement.execute(insertStatement);
            returnToMainFormView(event);
        }
    }

    /**
     * Called when the cancel button is pushed.
     * Returns to the main menu view.
     */
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        returnToMainFormView(event);
    }

    /**
     * Performs multiple tests to confirm the user provided data is valid for our database.
     * Alerts the user if there is a conflict.
     */
    public boolean isInputDataValid() throws SQLException {
        // Tests if there are any empty data fields
        if (titleTextField.getText().equals("") ||
                descriptionTextField.getText().equals("") ||
                locationTextField.getText().equals("") ||
                contactComboBox.getValue() == null ||
                typeTextField.getText().equals("") ||
                datePicker.getValue() == null ||
                startTimeHourComboBox.getValue() == null ||
                startTimeMinuteComboBox.getValue() == null ||
                startTimeHourComboBox.getValue() == null ||
                endTimeHourComboBox.getValue() == null ||
                endTimeMinuteComboBox.getValue() == null ||
                endTimePeriodComboBox.getValue() == null ||
                userComboBox.getValue() == null ||
                customerComboBox.getValue() == null) {
            inputErrorAlert("emptyField");
            return false;
        }

        // Tests if the selected date has passed the current date of the business (EST)
        if (datePicker.getValue().isBefore(LocalDate.now(ZoneId.of("America/New_York")))) {
            inputErrorAlert("datePassed");
            return false;
        }

        // Tests if the selected start time is before selected end time
        int startHour = Integer.parseInt(startTimeHourComboBox.getValue().toString());
        int endHour = Integer.parseInt(endTimeHourComboBox.getValue().toString());
        if (startTimePeriodComboBox.getValue().toString().equals("PM")) {
            startHour += 12;
        }
        if (endTimePeriodComboBox.getValue().toString().equals("PM")) {
            endHour += 12;
        }
        int startMinute = Integer.parseInt(startTimeMinuteComboBox.getValue().toString());
        int endMinute = Integer.parseInt(endTimeMinuteComboBox.getValue().toString());
        if (startHour > endHour) {
            inputErrorAlert("endBeforeStart");
            return false;
        } else if (startHour == endHour) {
            if (startMinute >= endMinute) {
                inputErrorAlert("endBeforeStart");
                return false;
            }
        }

        // Tests if the selected start and end time are within business hours (>= 08:00, <= 22:00 EST)
        LocalDate localDate =  LocalDate.of(datePicker.getValue().getYear(), datePicker.getValue().getMonthValue(), datePicker.getValue().getDayOfMonth());
        ZoneId localZoneId = ZoneId.systemDefault();

        LocalTime startLocalTime;
        if ((startTimePeriodComboBox.getValue().toString().equals("PM")) && (Integer.parseInt(startTimeHourComboBox.getValue().toString()) != 12)) {
            startLocalTime = LocalTime.of(Integer.parseInt(startTimeHourComboBox.getValue().toString()) + 12, Integer.parseInt(startTimeMinuteComboBox.getValue().toString()));
        } else if ((startTimePeriodComboBox.getValue().toString().equals("AM")) && (Integer.parseInt(startTimeHourComboBox.getValue().toString()) == 12)) {
            startLocalTime = LocalTime.of(0, Integer.parseInt(startTimeMinuteComboBox.getValue().toString()));
        } else {
            startLocalTime = LocalTime.of(Integer.parseInt(startTimeHourComboBox.getValue().toString()), Integer.parseInt(startTimeMinuteComboBox.getValue().toString()));
        }
        LocalDateTime startLocalDateTime = LocalDateTime.of(localDate, startLocalTime);
        ZonedDateTime startLocalZonedDateTime = ZonedDateTime.of(startLocalDateTime, localZoneId);

        LocalTime endLocalTime;
        if ((endTimePeriodComboBox.getValue().toString().equals("PM")) && (Integer.parseInt(endTimeHourComboBox.getValue().toString()) != 12)) {
            endLocalTime = LocalTime.of(Integer.parseInt(endTimeHourComboBox.getValue().toString()) + 12, Integer.parseInt(endTimeMinuteComboBox.getValue().toString()));
        } else if ((endTimePeriodComboBox.getValue().toString().equals("AM")) && (Integer.parseInt(endTimeHourComboBox.getValue().toString()) == 12)) {
            endLocalTime = LocalTime.of(0, Integer.parseInt(endTimeMinuteComboBox.getValue().toString()));
        } else {
            endLocalTime = LocalTime.of(Integer.parseInt(endTimeHourComboBox.getValue().toString()), Integer.parseInt(endTimeMinuteComboBox.getValue().toString()));
        }
        LocalDateTime endLocalDateTime = LocalDateTime.of(localDate, endLocalTime);
        ZonedDateTime endLocalZonedDateTime = ZonedDateTime.of(endLocalDateTime, localZoneId);

        ZoneId utcZoneId = ZoneId.of("America/New_York"); // Converts the selected time from the users local time zone to EST to ensure it is within store hours
        ZonedDateTime estStartZonedDateTime = ZonedDateTime.ofInstant(startLocalZonedDateTime.toInstant(), utcZoneId);
        ZonedDateTime estEndZonedDateTime = ZonedDateTime.ofInstant(endLocalZonedDateTime.toInstant(), utcZoneId);

        if ((estStartZonedDateTime.getHour() < 8) || (estStartZonedDateTime.getHour() > 22)) { // Tests if start time is within business hours
            inputErrorAlert("outsideOfBusinessHours");
            return false;
        }
        if ((estEndZonedDateTime.getHour() < 8) || (estEndZonedDateTime.getHour() > 22)) { // Tests if end time is within business hours
            inputErrorAlert("outsideOfBusinessHours");
            return false;
        } else if (estEndZonedDateTime.getHour() == 22) {
            if (!(estEndZonedDateTime.getMinute() == 0)) {
                inputErrorAlert("outsideOfBusinessHours");
                return false;
            }
        }
        return true; // If all tests are passed, true is returned
    }

    /**
     * Reads input data into local variables to be inserted into the database.
     */
    public void readInputData() {
        // Gets data from text fields
        title = titleTextField.getText();
        description = descriptionTextField.getText();
        location = locationTextField.getText();

        // Gets ID numbers from selected combo box using the database
        FilteredList<Contact> selectedContact = new FilteredList<>(allContacts, i-> i.getContactName() == contactComboBox.getValue());
        contactId = selectedContact.get(0).getContactId();
        type = typeTextField.getText();
        FilteredList<User> selectedUser = new FilteredList<>(allUsers, i-> i.getUsername() == userComboBox.getValue());
        userId = selectedUser.get(0).getUserId();
        FilteredList<Customer> selectedCustomer = new FilteredList<>(allCustomers, i-> i.getCustomerName() == customerComboBox.getValue());
        customerId = selectedCustomer.get(0).getCustomerId();

        // Creates a LocalDate variable from the given date. This will later be used in combination with LocalTime variables for both start and end times to create LocalDateTime variables for start and end.
        LocalDate localDate =  LocalDate.of(datePicker.getValue().getYear(), datePicker.getValue().getMonthValue(), datePicker.getValue().getDayOfMonth());
        ZoneId localZoneId = ZoneId.systemDefault();

        // Creates LocalTime variable for start time and reads in the given data
        LocalTime startLocalTime;
        if ((startTimePeriodComboBox.getValue().toString().equals("PM")) && (Integer.parseInt(startTimeHourComboBox.getValue().toString()) != 12)) {
            startLocalTime = LocalTime.of(Integer.parseInt(startTimeHourComboBox.getValue().toString()) + 12, Integer.parseInt(startTimeMinuteComboBox.getValue().toString()));
        } else if ((startTimePeriodComboBox.getValue().toString().equals("AM")) && (Integer.parseInt(startTimeHourComboBox.getValue().toString()) == 12)) {
            startLocalTime = LocalTime.of(0, Integer.parseInt(startTimeMinuteComboBox.getValue().toString()));
        } else {
            startLocalTime = LocalTime.of(Integer.parseInt(startTimeHourComboBox.getValue().toString()), Integer.parseInt(startTimeMinuteComboBox.getValue().toString()));
        }
        LocalDateTime startLocalDateTime = LocalDateTime.of(localDate, startLocalTime);
        ZonedDateTime startLocalZonedDateTime = ZonedDateTime.of(startLocalDateTime, localZoneId);

        // Creates LocalTime variable for end time and reads in the given data
        LocalTime endLocalTime;
        if ((endTimePeriodComboBox.getValue().toString().equals("PM")) && (Integer.parseInt(endTimeHourComboBox.getValue().toString()) != 12)) {
            endLocalTime = LocalTime.of(Integer.parseInt(endTimeHourComboBox.getValue().toString()) + 12, Integer.parseInt(endTimeMinuteComboBox.getValue().toString()));
        } else if ((endTimePeriodComboBox.getValue().toString().equals("AM")) && (Integer.parseInt(endTimeHourComboBox.getValue().toString()) == 12)) {
            endLocalTime = LocalTime.of(0, Integer.parseInt(endTimeMinuteComboBox.getValue().toString()));
        } else {
            endLocalTime = LocalTime.of(Integer.parseInt(endTimeHourComboBox.getValue().toString()), Integer.parseInt(endTimeMinuteComboBox.getValue().toString()));
        }
        LocalDateTime endLocalDateTime = LocalDateTime.of(localDate, endLocalTime);
        ZonedDateTime endLocalZonedDateTime = ZonedDateTime.of(endLocalDateTime, localZoneId);

        // Converts the given start and end ZonedDateTime to UTC
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcStartZonedDateTime = ZonedDateTime.ofInstant(startLocalZonedDateTime.toInstant(), utcZoneId);
        ZonedDateTime utcEndZonedDateTime = ZonedDateTime.ofInstant(endLocalZonedDateTime.toInstant(), utcZoneId);

        // Once the given date and time is converted to UTC, the data is converted into a string that is formatted for the sql datetime data type.
        startDateTime = (utcStartZonedDateTime.toLocalDate().toString() + " " + utcStartZonedDateTime.toLocalTime().toString());
        endDateTime = (utcEndZonedDateTime.toLocalDate().toString() + " " + utcEndZonedDateTime.toLocalTime().toString());

        // These variables will be used later for checking if there is an appointment overlap
        utcStartLocalDateTime = utcStartZonedDateTime.toLocalDateTime();
        utcEndLocalDateTime = utcEndZonedDateTime.toLocalDateTime();


        // Gets the current local date time for the Last_Update datetime variable in our database
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        ZonedDateTime currentLocalZonedDateTime = ZonedDateTime.of(currentLocalDateTime, localZoneId);
        ZonedDateTime utcCurrentZonedDateTime = ZonedDateTime.ofInstant(currentLocalZonedDateTime.toInstant(), utcZoneId);
        lastUpdate = (utcCurrentZonedDateTime.toLocalDate().toString() + " " + utcCurrentZonedDateTime.toLocalTime().toString());
    }

    /**
     * Tests if the given appointment conflicts with any existing appointments the customer may have in the database.
     * Alerts the user if there is a conflict.
     */

    public boolean checkAppointmentConflict() throws SQLException {
        FilteredList<Customer> selectedCustomer = new FilteredList<>(allCustomers, i-> i.getCustomerName() == customerComboBox.getValue());
        customerId = selectedCustomer.get(0).getCustomerId();
        ObservableList<Appointment> customerAppointments = new FilteredList<Appointment>(DBAppointments.getAllAppointments(), i-> i.getCustomerId() == customerId && i.getAppointmentId() != appointmentId);
        for (int i = 0; i < customerAppointments.size(); i++) {
            if ((customerAppointments.get(i).getStartDate().isBefore(utcStartLocalDateTime) && customerAppointments.get(i).getEndDate().isAfter(utcStartLocalDateTime)) ||
                    (customerAppointments.get(i).getStartDate().isEqual(utcStartLocalDateTime)) ||
                    (customerAppointments.get(i).getStartDate().isAfter(utcStartLocalDateTime) && customerAppointments.get(i).getStartDate().isBefore(utcEndLocalDateTime))) {
                inputErrorAlert("appointmentConflict");
                return true;
            }
        }
        return false;
    }

    /**
     * Alerts the user of any errors that may have occurred
     * @param error the error to display to the user
     */
    public void inputErrorAlert(String error) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Invalid Input");
        switch (error) {
            case "emptyField":
                alert.setContentText("Error: One or more fields is empty.");
                break;
            case "datePassed":
                alert.setContentText("Error: Selected date has passed.");
                break;
            case "endBeforeStart":
                alert.setContentText("Error: Start time must be before end time.");
                break;
            case "outsideOfBusinessHours":
                alert.setContentText("Error: Appointment time must fall within the business hours 8:00am to 10:00pm EST");
                break;
            case "appointmentConflict":
                alert.setContentText("Error: Customer appointment conflicts with one or more existing appointments.");
                break;
            default:
                alert.setContentText("Unknown error has occurred.");
        }
        alert.show();
    }

    /**
     * Initializes the combo boxes with data from the database.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize Combo Boxes
        for (int i = 1; i < 13; i++) {
            startTimeHourComboBox.getItems().add(i);
            endTimeHourComboBox.getItems().add(i);
        }
        startTimeMinuteComboBox.getItems().addAll("00", "15", "30", "45");
        startTimePeriodComboBox.getItems().addAll("AM", "PM");
        endTimeMinuteComboBox.getItems().addAll("00", "15", "30", "45");
        endTimePeriodComboBox.getItems().addAll("AM", "PM");

        // Contact Combo Box
        for (int i = 0; i < allContacts.size(); i++) {
            contactComboBox.getItems().add(allContacts.get(i).getContactName());
        }

        // User Combo Box
        for (int i = 0; i < allUsers.size(); i++) {
            userComboBox.getItems().add(allUsers.get(i).getUsername());
        }

        // Customer Combo Box
        for (int i = 0; i < allCustomers.size(); i++) {
            customerComboBox.getItems().add(allCustomers.get(i).getCustomerName());
        }

    }
}
