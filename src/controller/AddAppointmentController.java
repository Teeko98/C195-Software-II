package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

    public Label appointmentIdLabel;
    public TextField userIdTextField;
    public TextField customerIdTextField;
    public TextField titleTextField;
    public TextField descriptionTextField;
    public TextField locationTextField;
    public TextField contactTextField;
    public TextField typeTextField;
    public DatePicker datePicker;
    public ComboBox startTimeHourComboBox;
    public ComboBox startTimeMinuteComboBox;
    public ComboBox startTimePeriodComboBox;
    public ComboBox endTimeHourComboBox;
    public ComboBox endTimeMinuteComboBox;
    public ComboBox endTimePeriodComboBox;

    public void returnToMainFormView(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/MainFormView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    public void saveButtonPushed(ActionEvent event) throws IOException {
        if (isInputDataValid()) {
            returnToMainFormView(event);
        }
    }

    public void cancelButtonPushed(ActionEvent event) throws IOException {
        returnToMainFormView(event);
    }

    public boolean isInputDataValid() {

        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize Choice Boxes
        for (int i = 1; i < 13; i++) {
            startTimeHourComboBox.getItems().add(i);
            endTimeHourComboBox.getItems().add(i);
        }
        startTimeMinuteComboBox.getItems().addAll("00", "15", "30", "45");
        startTimePeriodComboBox.getItems().addAll("AM", "PM");
        endTimeMinuteComboBox.getItems().addAll("00", "15", "30", "45");
        endTimePeriodComboBox.getItems().addAll("AM", "PM");

    }
}
