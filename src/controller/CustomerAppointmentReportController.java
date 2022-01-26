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
import main.Customer;
import sql.DBAppointments;
import sql.DBCustomers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerAppointmentReportController implements Initializable {
    public ComboBox customerComboBox;
    public Label totalAppointmentsLabel;
    public Label totalAppointmentsResultLabel;
    public Button generateButton;
    public Button returnButton;

    ObservableList<Appointment> allAppointments = DBAppointments.getAllAppointments();
    ObservableList<Customer> allCustomers = DBCustomers.getAllCustomers();

    public CustomerAppointmentReportController() throws SQLException {
    }

    /**
     * Calculates the number of appointments that exist of a given type and month.
     * Alerts the user if the text field is left empty.
     */
    public void generateButtonPushed() {
        Customer selectedCustomer = new FilteredList<>(allCustomers, i-> i.getCustomerName().equals(customerComboBox.getValue().toString())).get(0);
        ObservableList<Appointment> filteredAppointments = new FilteredList<>(allAppointments, i-> i.getCustomerId() == selectedCustomer.getCustomerId());
        totalAppointmentsLabel.setVisible(true);
        totalAppointmentsResultLabel.setText(Integer.toString(filteredAppointments.size()));
        totalAppointmentsResultLabel.setVisible(true);
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
     * Initializes customer name data from the database.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < allCustomers.size(); i++) {
            customerComboBox.getItems().add(allCustomers.get(i).getCustomerName());
        }

    }
}
