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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Country;
import main.FirstLevelDivision;
import sql.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {
    public TextField customerNameTextField;
    public TextField addressTextField;
    public ComboBox stateComboBox;
    public ComboBox countryComboBox;
    public TextField postalCodeTextField;
    public TextField phoneNumberTextField;

    // Customer Variables
    String name;
    String address;
    String postalCode;
    String phoneNumber;
    ObservableList<FirstLevelDivision> allFirstLevelDivisions = DBFirstLevelDivisions.getAllFirstLevelDivisions();
    ObservableList<Country> allCountries = DBCountries.getAllCountries();
    int selectedCountryId;
    int selectedFirstLevelDivisionId;
    String createDateTime;

    public AddCustomerController() throws SQLException {
    }

    /**
     * Returns to the main view form.
     */
    public void returnToMainFormView(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/MainFormView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    /**
     * If user input data is valid, it is read into local variables and then inserted into the database using an SQL INSERT statement.
     * Returns to main menu upon successful insert.
     */
    public void saveButtonPushed(ActionEvent event) throws IOException, SQLException {
        if (isInputDataValid()) {
            readInputData();

            Connection connection = JDBC.getConnection();
            DBQuery.setStatement(connection);
            Statement statement = DBQuery.getStatement();

            String insertStatement = "INSERT INTO client_schedule.customers(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (" +
                    "'" + name + "', " +
                    "'" + address + "', " +
                    "'" + postalCode + "', " +
                    "'" + phoneNumber + "', " +
                    "'" + createDateTime  + "', " +
                    "'" + DBUsers.currentUser.getUsername() + "', " +
                    "'" + createDateTime + "', " +
                    "'" + DBUsers.currentUser.getUsername() + "', " +
                    "'" + selectedFirstLevelDivisionId + "');";
            statement.execute(insertStatement);
            returnToMainFormView(event);
        }
    }

    /**
     * Returns to the main view.
     */
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        returnToMainFormView(event);
    }

    /**
     * Tests if input data is valid.
     * @return true if data is valid, false if there was an error.
     */
    public boolean isInputDataValid() {
        if (customerNameTextField.getText().equals("") ||
                customerNameTextField.getText().equals("") ||
                postalCodeTextField.getText().equals("") ||
                phoneNumberTextField.getText().equals("")) {
            return false;
        } else if (countryComboBox.getSelectionModel().isEmpty() ||
                stateComboBox.getSelectionModel().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Reads the input data into local variables.
     */
    public void readInputData() throws SQLException {
        name = customerNameTextField.getText();
        address = addressTextField.getText();

        FilteredList<FirstLevelDivision> selectedFirstLevelDivision = new FilteredList<FirstLevelDivision>(allFirstLevelDivisions, i-> i.getDivision() == stateComboBox.getValue());
        selectedFirstLevelDivisionId = selectedFirstLevelDivision.get(0).getDivisionId();

        postalCode = postalCodeTextField.getText();
        phoneNumber = phoneNumberTextField.getText();

        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        ZonedDateTime currentLocalZonedDateTime = ZonedDateTime.of(currentLocalDateTime, ZoneId.systemDefault());
        ZonedDateTime utcLocalZonedDateTime = ZonedDateTime.ofInstant(currentLocalZonedDateTime.toInstant(), ZoneId.of("UTC"));
        createDateTime = (utcLocalZonedDateTime.toLocalDate().toString() + " " + utcLocalZonedDateTime.toLocalTime().toString());
    }

    /**
     * Updates the state combo box with the appropriate values based on what is selected in the country combo box.
     */
    public void updateStateChoiceBox() {
        FilteredList<Country> selectedCountry = new FilteredList<Country>(allCountries, i-> i.getCountryName() == countryComboBox.getValue());
        selectedCountryId = selectedCountry.get(0).getCountryId();

        stateComboBox.getItems().clear();
        FilteredList<FirstLevelDivision> selectedCountryFirstLevelDivisions = new FilteredList<FirstLevelDivision>(allFirstLevelDivisions, i-> i.getCountryId() == selectedCountryId);
        for (int i = 0; i < selectedCountryFirstLevelDivisions.size(); i++) {
            stateComboBox.getItems().add(selectedCountryFirstLevelDivisions.get(i).getDivision());
        }
        if (selectedCountryFirstLevelDivisions.size() < 10) {
            stateComboBox.setVisibleRowCount(selectedCountryFirstLevelDivisions.size());
        } else {
            stateComboBox.setVisibleRowCount(10);
        }
    }

    /**
     * Initializes the country combo box
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize Combo Boxes
        for (int i = 0; i < allCountries.size(); i++) {
            countryComboBox.getItems().add(allCountries.get(i).getCountryName());
        }
        countryComboBox.setOnAction( e -> {
            updateStateChoiceBox();
        });
    }
}
