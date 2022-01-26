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
import sql.DBAppointments;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppointmentTypeReportController implements Initializable {
    public ComboBox monthComboBox;
    public TextField typeTextField;
    public Label totalAppointmentsLabel;
    public Label totalAppointmentsResultLabel;
    public Button generateButton;
    public Button returnButton;

    ObservableList<Appointment> allAppointments = DBAppointments.getAllAppointments();

    public AppointmentTypeReportController() throws SQLException {
    }

    /**
     * Calculates the number of appointments that exist of a given type and month.
     * Alerts the user if the text field is left empty.
     */
    public void generateButtonPushed() {
        if (typeTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error empty text field");
            alert.setHeaderText("Error empty text field");
            alert.setContentText("Please enter a valid Type.");
            alert.show();
            return;
        }
        ObservableList<Appointment> filteredByMonthAndType = new FilteredList<>(allAppointments, i-> i.getStartDate().getMonthValue() - 1 == monthComboBox.getSelectionModel().getSelectedIndex() &&
                i.getType().equals(typeTextField.getText()));
        totalAppointmentsLabel.setVisible(true);
        totalAppointmentsResultLabel.setText(Integer.toString(filteredByMonthAndType.size()));
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
     * Initializes the months combo box
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthComboBox.getItems().addAll("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");
        monthComboBox.getSelectionModel().select(0);
    }
}
