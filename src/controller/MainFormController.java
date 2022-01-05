package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Customer;
import main.Main;
import sql.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {


    // Appointment Table View Objects
    public TableView appointmentTableView;
    public TableColumn appointmentIdTableColumn;
    public TableColumn appointmentTitleTableColumn;
    public TableColumn appointmentDescriptionTableColumn;
    public TableColumn appointmentLocationTableColumn;
    public TableColumn appointmentContactTableColumn;
    public TableColumn appointmentTypeTableColumn;
    public TableColumn appointmentStartTableColumn;
    public TableColumn appointmentEndTableColumn;
    public TableColumn appointmentCustomerIdTableColumn;
    public TableColumn appointmentUserIdTableColumn;


    // Customer Table View Objects
    public TableView customerTableView;
    public TableColumn customerIdTableColumn;
    public TableColumn customerNameTableColumn;
    public TableColumn customerAddressTableColumn;
    public TableColumn customerPostalTableColumn;
    public TableColumn customerPhoneTableColumn;


    /**
     * Appointment Table Methods
     */




    public void appointmentAddButtonPushed(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/AddAppointmentView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    //TODO
    public void appointmentModifyButtonPushed(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/ModifyAppointmentView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    //TODO
    public void appointmentDeleteButtonPushed() {


    }

    /**
     * Customer Table Methods
     */

    public void customerAddButtonPushed(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/AddCustomerView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    public void customerModifyButtonPushed(ActionEvent event) throws IOException {
        Customer customerToBeModified = (Customer) customerTableView.getSelectionModel().getSelectedItem();
        if (customerToBeModified == null) {
            noCustomerSelectedAlert();
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

    //TODO
    // Customer delete button needs to test if customer has any active appointments and alert the user that they must be removed first
    // Customer delete confirmation prompt and deletion successful prompt
    public void customerDeleteButtonPushed() throws SQLException {
        Customer customerToBeDeleted = (Customer) customerTableView.getSelectionModel().getSelectedItem();

        if (customerToBeDeleted == null) {
            noCustomerSelectedAlert();
        } else {
            Connection connection = JDBC.getConnection();
            DBQuery.setStatement(connection);
            Statement statement = DBQuery.getStatement();

            String insertStatement = "DELETE FROM client_schedule.customers WHERE Customer_ID = " + customerToBeDeleted.getCustomerId() + ";";
            statement.execute(insertStatement);

            updateCustomerTableView();
        }
    }

    public void noCustomerSelectedAlert() {
        Alert noCustomerSelectedAlert = new Alert(Alert.AlertType.ERROR);
        noCustomerSelectedAlert.setTitle("No Customer Selected");
        noCustomerSelectedAlert.setHeaderText("No Customer Selected");
        noCustomerSelectedAlert.setContentText("Please select a customer and try again.");
        noCustomerSelectedAlert.showAndWait();
    }

    public void updateCustomerTableView() throws SQLException {
        customerTableView.setItems(DBCustomer.getAllCustomers());
        customerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        customerNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        customerAddressTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerAddress"));
        customerPostalTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerPostalCode"));
        customerPhoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerPhone"));
    }

    //TODO
    // Figure out what is supposed to go in contact table column(contact id or link the contact name by the contact id?)
    public void updateAppointmentTableView() throws SQLException {
        appointmentTableView.setItems(DBAppointments.getAllAppointments());
        appointmentIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        appointmentTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        appointmentDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        appointmentLocationTableColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        appointmentContactTableColumn.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        appointmentTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        appointmentStartTableColumn.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        appointmentEndTableColumn.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        appointmentCustomerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        appointmentUserIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("UserId"));
    }


    /**
     * Control Methods
     */

    //TODO
    public void viewAppointmentsButtonPushed() {

    }

    //TODO
    public void appointmentsTypeReportButtonPushed() {

    }


    //TODO
    public void consultantScheduleReportButtonPushed() {

    }

    //TODO
    public void appointmentCustomerReportButtonPushed() {

    }


    public void logOutButtonPushed(ActionEvent event) throws IOException {
        DBUsers.userLogout();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("Nat", Locale.getDefault());

        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/LoginSceneView.fxml"), resourceBundle);
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }


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
