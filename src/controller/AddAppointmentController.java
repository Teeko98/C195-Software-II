package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

    public TextField appointmentIdTextField;
    public TextField userIdTextField;
    public TextField customerIdTextField;
    public TextField titleTextField;
    public TextField descriptionTextField;
    public TextField locationTextField;
    public TextField contactTextField;
    public TextField typeTextField;
    public TextField urlTextField;
    public ChoiceBox dateMonthChoiceBox;
    public ChoiceBox dateDayChoiceBox;
    public ChoiceBox dateYearChoiceBox;
    public ChoiceBox startTimeHourChoiceBox;
    public ChoiceBox startTimeMinuteChoiceBox;
    public ChoiceBox startTimePeriodChoiceBox;
    public ChoiceBox endTimeHourChoiceBox;
    public ChoiceBox endTimeMinuteChoiceBox;
    public ChoiceBox endTimePeriodChoiceBox;

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
        dateMonthChoiceBox.getItems().addAll("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec");
        dateMonthChoiceBox.setValue("Jan");
        for (int i = 1; i < 32; i++) {
            dateDayChoiceBox.getItems().add(i);
        }
        dateDayChoiceBox.setValue(1);
        for (int i = 2021; i < 2033; i++) {
            dateYearChoiceBox.getItems().add(i);
        }
        dateYearChoiceBox.setValue(2021);
        for (int i = 1; i < 13; i++) {
            startTimeHourChoiceBox.getItems().add(i);
            endTimeHourChoiceBox.getItems().add(i);
        }
        startTimeHourChoiceBox.setValue(12);
        startTimeMinuteChoiceBox.getItems().addAll("00", 15, 30, 45);
        startTimeMinuteChoiceBox.setValue("00");
        startTimePeriodChoiceBox.getItems().addAll("AM", "PM");
        startTimePeriodChoiceBox.setValue("PM");
        endTimeHourChoiceBox.setValue(1);
        endTimeMinuteChoiceBox.getItems().addAll("00", 15, 30, 45);
        endTimeMinuteChoiceBox.setValue("00");
        endTimePeriodChoiceBox.getItems().addAll("AM", "PM");
        endTimePeriodChoiceBox.setValue("PM");

    }
}
