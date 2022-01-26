package controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Appointment;
import main.Contact;
import sql.DBAppointments;
import sql.DBContacts;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ContactScheduleReportController implements Initializable {
    public ComboBox contactComboBox;
    public TableView appointmentTableView;
    public TableColumn appointmentIdTableColumn;
    public TableColumn appointmentTitleTableColumn;
    public TableColumn appointmentDescriptionTableColumn;
    public TableColumn appointmentTypeTableColumn;
    public TableColumn appointmentDateTableColumn;
    public TableColumn appointmentStartTimeTableColumn;
    public TableColumn appointmentEndTimeTableColumn;
    public TableColumn appointmentCustomerIdTableColumn;

    ObservableList<Appointment> allAppointments = DBAppointments.getAllAppointments();
    ObservableList<Contact> allContacts = DBContacts.getAllContacts();

    public ContactScheduleReportController() throws SQLException {
    }

    /**
     * Generates a tableview of all appointments filtered by the selected contact ID
     */
    public void generateButtonPushed() {
        Contact selectedContact = new FilteredList<>(allContacts, i-> i.getContactName() == contactComboBox.getValue().toString()).get(0);
        ObservableList<Appointment> filteredAppointments = new FilteredList<>(allAppointments, i-> i.getContactId() == selectedContact.getContactId());
        appointmentTableView.setItems(filteredAppointments);
        appointmentIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        appointmentTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        appointmentDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        appointmentTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        appointmentDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        appointmentStartTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        appointmentEndTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        appointmentCustomerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
    }

    /**
     * Returns to the main view
     */
    public void returnButtonPushed(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/MainFormView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    /**
     * Initializes the contacts combo box with data from the SQL database
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < allContacts.size(); i++) {
            contactComboBox.getItems().add(allContacts.get(i).getContactName());
        }
        contactComboBox.getSelectionModel().select(0);

    }
}
