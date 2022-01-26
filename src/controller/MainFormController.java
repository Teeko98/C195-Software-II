package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Appointment;
import main.Customer;
import sql.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    public RadioButton viewAllRadioButton;
    public RadioButton viewMonthRadioButton;
    public RadioButton viewWeekRadioButton;
    public ToggleGroup appointmentsViewToggleGroup;
    public TableView appointmentTableView;
    public TableColumn appointmentIdTableColumn;
    public TableColumn appointmentTitleTableColumn;
    public TableColumn appointmentDescriptionTableColumn;
    public TableColumn appointmentLocationTableColumn;
    public TableColumn appointmentContactTableColumn;
    public TableColumn appointmentTypeTableColumn;
    public TableColumn appointmentDateTableColumn;
    public TableColumn appointmentStartTimeTableColumn;
    public TableColumn appointmentEndTimeTableColumn;
    public TableColumn appointmentCustomerIdTableColumn;
    public TableColumn appointmentUserIdTableColumn;
    public TableView customerTableView;
    public TableColumn customerIdTableColumn;
    public TableColumn customerNameTableColumn;
    public TableColumn customerAddressTableColumn;
    public TableColumn customerPostalTableColumn;
    public TableColumn customerPhoneTableColumn;


    /**
     * Switches to the add appointment scene.
     */
    public void appointmentAddButtonPushed(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/AddAppointmentView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    /**
     * If an appointment is selected, that appointment is loaded into the modify appointment scene. If no appointment is selected, an error is displayed to the user.
     */
    public void appointmentModifyButtonPushed(ActionEvent event) throws IOException {
        Appointment appointmentToBeModified = (Appointment) appointmentTableView.getSelectionModel().getSelectedItem();
        if (appointmentToBeModified == null) {
            noneSelectedAlert("Appointment");
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyAppointmentView.fxml"));
            Parent sceneParent = loader.load();

            ModifyAppointmentController controller = loader.getController();

            controller.receiveAppointmentToBeModified(appointmentToBeModified);

            Scene newScene = new Scene(sceneParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(newScene);
            window.show();
        }
    }

    /**
     * If an appointment is selected, the user is asked to confirm the deletion of the appointment.
     * If the user selected yes, the appointment is deleted and the appointment tableview is updated.
     * The user is alerted upon successful deletion.
     * Otherwise, nothing changes.
     * if no appointment is selected, an error is displayed to the user.
     */
    public void appointmentDeleteButtonPushed() throws SQLException {
        Appointment appointmentToBeDeleted = (Appointment) appointmentTableView.getSelectionModel().getSelectedItem();

        if (appointmentToBeDeleted == null) {
            noneSelectedAlert("Appointment");
        } else if (confirmDeletionAlert("Appointment", appointmentToBeDeleted.getTitle())) {
            Connection connection = JDBC.getConnection();
            DBQuery.setStatement(connection);
            Statement statement = DBQuery.getStatement();

            String deleteStatement = "DELETE FROM client_schedule.appointments WHERE Appointment_ID ="+ appointmentToBeDeleted.getAppointmentId() + ";";
            statement.execute(deleteStatement);

            if (statement.getUpdateCount() > 0) {
                deletionSuccessfulAlert(appointmentToBeDeleted.getTitle());
            }
            updateAppointmentTableView();
        }
    }

    /**
     * Updates the appointment tableview with the appointment data from the database. The data can be further filtered by radio buttons.
     */
    public void updateAppointmentTableView() throws SQLException {
        ObservableList<Appointment> allAppointments = DBAppointments.getAllAppointments();
        ObservableList<Appointment> filteredAppointments = null;
        if (appointmentsViewToggleGroup.getSelectedToggle().equals(viewAllRadioButton)) {
            filteredAppointments = allAppointments;
        } else if (appointmentsViewToggleGroup.getSelectedToggle().equals(viewMonthRadioButton)) {
            filteredAppointments = new FilteredList<>(allAppointments, i-> i.getStartDate().isBefore(LocalDateTime.now().plusMonths(1)) && i.getStartDate().isAfter(LocalDateTime.now()));
        } else if (appointmentsViewToggleGroup.getSelectedToggle().equals(viewWeekRadioButton)) {
            filteredAppointments = new FilteredList<>(allAppointments, i-> i.getStartDate().isBefore(LocalDateTime.now().plusWeeks(1)) && i.getStartDate().isAfter(LocalDateTime.now()));
        }

        appointmentTableView.setItems(filteredAppointments);
        appointmentIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        appointmentTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        appointmentDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        appointmentLocationTableColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        appointmentContactTableColumn.setCellValueFactory(new PropertyValueFactory<>("ContactId"));
        appointmentTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        appointmentDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        appointmentStartTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        appointmentEndTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        appointmentCustomerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        appointmentUserIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("UserId"));
    }

    /**
     * This method is called upon a successful user login. Tests if a user has an appointment within the next 15 minutes.
     * If there are any appointments, the user is notified and given the option to view the upcoming appointment details.
     * If there are none found, the user is notified there are no upcoming appointments.
     */
    public void checkUpcomingAppointments() throws SQLException {
        ObservableList<Appointment> userAppointments = new FilteredList<>(DBAppointments.getAllAppointments(), i-> i.getUserId() == DBUsers.currentUser.getUserId());
        ObservableList<Appointment> appointmentsWithin15Minutes = FXCollections.observableArrayList();
        String upcomingAppointmentsString = "";
        for (int i = 0; i < userAppointments.size(); i++) {
            if (userAppointments.get(i).getStartTime().isBefore(LocalTime.now().plusMinutes(15)) &&
                    userAppointments.get(i).getStartTime().isAfter(LocalTime.now()) &&
                    userAppointments.get(i).getStartDate().toLocalDate().isEqual(LocalDate.now())) {
                appointmentsWithin15Minutes.add(userAppointments.get(i));
                upcomingAppointmentsString = upcomingAppointmentsString + "Appointment ID: " + userAppointments.get(i).getAppointmentId() +
                        " at " + userAppointments.get(i).getStartDate().toLocalTime() +
                        " on " + userAppointments.get(i).getStartDate().toLocalDate() + "\n";
            }
        }
        if (appointmentsWithin15Minutes.isEmpty()) {
            Alert appointmentAlert = new Alert(Alert.AlertType.INFORMATION);
            appointmentAlert.setTitle("Welcome");
            appointmentAlert.setHeaderText("Welcome " + DBUsers.currentUser.getUsername());
            appointmentAlert.setContentText("You have no upcoming appointments.");
            appointmentAlert.show();
        } else {
            Alert appointmentAlert = new Alert(Alert.AlertType.CONFIRMATION);
            appointmentAlert.setTitle("Welcome");
            appointmentAlert.setHeaderText("Welcome " + DBUsers.currentUser.getUsername());
            appointmentAlert.setContentText("You have " + appointmentsWithin15Minutes.size() + " upcoming appointment(s).");
            ButtonType viewAppointmentsButtonType = new ButtonType ("More Info");
            ButtonType okButtonType = new ButtonType ("Continue");
            appointmentAlert.getButtonTypes().setAll(viewAppointmentsButtonType, okButtonType);
            Optional<ButtonType> result = appointmentAlert.showAndWait();
            if (result.get() == viewAppointmentsButtonType) {
                Alert upcomingAppointments = new Alert(Alert.AlertType.INFORMATION);
                upcomingAppointments.setTitle("Upcoming Appointments");
                upcomingAppointments.setHeaderText(null);
                upcomingAppointments.setContentText(upcomingAppointmentsString);
                upcomingAppointments.showAndWait();
            }
        }
    }

    /**
     * Switches to the add customer scene.
     */
    public void customerAddButtonPushed(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/AddCustomerView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    /**
     * If a customer is selected, that customer is loaded into the modify customer scene.
     * If no customer is selected, and error is displayed to the user.
     */
    public void customerModifyButtonPushed(ActionEvent event) throws IOException {
        Customer customerToBeModified = (Customer) customerTableView.getSelectionModel().getSelectedItem();
        if (customerToBeModified == null) {
            noneSelectedAlert("Customer");
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyCustomerView.fxml"));
            Parent sceneParent = loader.load();

            ModifyCustomerController controller = loader.getController();

            controller.receiveCustomerToBeModified(customerToBeModified);

            Scene newScene = new Scene(sceneParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(newScene);
            window.show();
        }
    }

    /**
     * If a customer is selected, the database is checked to see if the customer has any appointments.
     * If there are no appointments, the user is asked to confirm the deletion of the customer.
     * If the user selects yes, the customer is deleted and the customer view is updated.
     * The user is alerted upon successful deletion.
     * Otherwise, nothing changes.
     * If no customer was selected, an error is displayed to the user.
     */
    public void customerDeleteButtonPushed() throws SQLException {
        Customer customerToBeDeleted = (Customer) customerTableView.getSelectionModel().getSelectedItem();
        if (customerToBeDeleted == null) {
            noneSelectedAlert("Customer");
            return;
        }

        ObservableList<Appointment> customersAppointments = new FilteredList<>(DBAppointments.getAllAppointments(), i-> i.getCustomerId() == customerToBeDeleted.getCustomerId());
        if (!customersAppointments.isEmpty()) {
            Alert deletionUnsuccessfulAlert = new Alert(Alert.AlertType.INFORMATION);
            deletionUnsuccessfulAlert.setTitle("Error Removing Customer");
            deletionUnsuccessfulAlert.setHeaderText("Error Removing Customer");
            deletionUnsuccessfulAlert.setContentText("Customer: " + customerToBeDeleted.getCustomerName() + " has one or more existing appointments. Please remove all existing appointments before attempting to remove a customer.");
            deletionUnsuccessfulAlert.show();
            return;
        }

        if (confirmDeletionAlert("Customer", customerToBeDeleted.getCustomerName())){
            Connection connection = JDBC.getConnection();
            DBQuery.setStatement(connection);
            Statement statement = DBQuery.getStatement();

            String deleteStatement = "DELETE FROM client_schedule.customers WHERE Customer_ID = " + customerToBeDeleted.getCustomerId() + ";";
            statement.execute(deleteStatement);
            deletionSuccessfulAlert(customerToBeDeleted.getCustomerName());

            updateCustomerTableView();
        }
    }

    /**
     * Updates the customer table view with the customer data from the database.
     */
    public void updateCustomerTableView() throws SQLException {
        customerTableView.setItems(DBCustomers.getAllCustomers());
        customerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        customerNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        customerAddressTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerAddress"));
        customerPostalTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerPostalCode"));
        customerPhoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerPhone"));
    }

    /**
     * Alerts the user if an item was not properly selected.
     * @param object the item that was not selected
     */
    public void noneSelectedAlert(String object) {
        Alert noCustomerSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
        noCustomerSelectedAlert.setTitle("No " + object + " Selected");
        noCustomerSelectedAlert.setHeaderText("No " + object + " Selected");
        noCustomerSelectedAlert.setContentText("Please select a valid " + object + " and try again.");
        noCustomerSelectedAlert.showAndWait();
    }

    /**
     * Confirms if the user would like to delete the selected object.
     * @param objectType the type of object to be deleted
     * @param objectName the name of the object to be deleted
     * @return true if the user has confirmed the deletion. false if the user cancels.
     */
    public boolean confirmDeletionAlert(String objectType, String objectName) {
        Alert confirmDeletionAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDeletionAlert.setTitle("Confirm Delete");
        confirmDeletionAlert.setHeaderText("Confirm Delete");
        confirmDeletionAlert.setContentText("Are you sure you would like to delete the " + objectType + ": " + objectName + "?");

        Optional<ButtonType> result = confirmDeletionAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    /**
     * Notifies the user that an item was successfully deleted.
     * @param object the object that was deleted
     */
    public void deletionSuccessfulAlert(String object) {
        Alert deletionSuccessfulAlert = new Alert(Alert.AlertType.INFORMATION);
        deletionSuccessfulAlert.setTitle("Deletion Successful");
        deletionSuccessfulAlert.setHeaderText("Deletion Successful");
        deletionSuccessfulAlert.setContentText(object + " was successfully removed.");
        deletionSuccessfulAlert.show();
    }

    /**
     * Switches to the appointments type report scene.
     */
    public void appointmentsTypeReportButtonPushed(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/AppointmentTypeReportView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    /**
     * Switches to the contact schedule report scene.
     */
    public void contactScheduleReportButtonPushed(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/ContactScheduleReportView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    /**
     * Switches to the appointment customer report scene.
     */
    public void appointmentCustomerReportButtonPushed(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/CustomerAppointmentReportView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    /**
     * Logs the user out and returns to the login form.
     */
    public void logOutButtonPushed(ActionEvent event) throws IOException {
        DBUsers.userLogout();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("Nat", Locale.getDefault());

        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/LoginSceneView.fxml"), resourceBundle);
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    /**
     * Initializes the customer and appointment table views.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            updateCustomerTableView();
            updateAppointmentTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
