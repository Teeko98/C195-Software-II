package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {


    // Appointment Table View Objects
    public TableView appointmentTableView;
    public TableColumn appointmentIdTableColumn;
    public TableColumn appointmentUserIdTableColumn;
    public TableColumn appointmentCustomerIdTableColumn;
    public TableColumn appointmentTitleTableColumn;
    public TableColumn appointmentStartTableColumn;
    public TableColumn appointmentEndTableColumn;

    // Customer Table View Objects
    public TableView customerTableView;
    public TableColumn customerIdTableColumn;
    public TableColumn customerNameTableColumn;
    public TableColumn customerAddressIdTableColumn;
    public TableColumn customerActiveTableColumn;


    /**
     * Appointment Table Methods
     */

    public void appointmentAddButtonPushed() {

    }

    public void appointmentModifyButtonPushed() {

    }

    public void appointmentDeleteButtonPushed() {

    }

    /**
     * Customer Table Methods
     */

    public void customerAddButtonPushed() {

    }

    public void customerModifyButtonPushed() {

    }

    public void customerDeleteButtonPushed() {

    }


    /**
     * Control Methods
     */

    public void viewAppointmentsButtonPushed() {

    }

    public void appointmentsTypeReportButtonPushed() {

    }

    public void consultantScheduleReportButtonPushed() {

    }

    public void appointmentCustomerReportButtonPushed(){

    }

    public void logOutButtonPushed(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/LoginSceneView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
