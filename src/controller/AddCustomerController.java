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
import java.util.ResourceBundle;

//TODO
// Fix auto increment issue
// CREATE_DATE and LAST_UPDATE

public class AddCustomerController implements Initializable {

    int newCustomerId = DBCustomer.getAllCustomers().get(DBCustomer.getAllCustomers().size() - 1).getCustomerId() + 1;
    String newCustomerName;
    String newCustomerAddress;
    String newCustomerPostalCode;
    String newCustomerPhoneNumber;

    ObservableList<FirstLevelDivision> allFirstLevelDivisions = DBFirstLevelDivisions.getAllFirstLevelDivisions();
    ObservableList<Country> allCountries = DBCountries.getAllCountries();
    int selectedCountryId;
    int selectedFirstLevelDivisionId;

    public Label customerIdLabel;
    public TextField customerNameTextField;
    public TextField addressTextField;
    public ComboBox stateComboBox;
    public ComboBox countryComboBox;
    public TextField postalCodeTextField;
    public TextField phoneNumberTextField;


    public AddCustomerController() throws SQLException {
    }

    public void returnToMainFormView(ActionEvent event) throws IOException {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/MainFormView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    public void saveButtonPushed(ActionEvent event) throws IOException, SQLException {
        if (isInputDataValid()) {
            readInputData();

            Connection connection = JDBC.getConnection();
            DBQuery.setStatement(connection);
            Statement statement = DBQuery.getStatement();

            String insertStatement = "INSERT INTO client_schedule.customers(Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID) VALUES (" +
                    "'" + newCustomerName + "', " +
                    "'" + newCustomerAddress + "', " +
                    "'" + newCustomerPostalCode + "', " +
                    "'" + newCustomerPhoneNumber + "', " +
                    "'" + DBUsers.currentUser.getUsername() + "', " +
                    "'" + DBUsers.currentUser.getUsername() + "', " +
                    "'" + selectedFirstLevelDivisionId + "');";
            statement.execute(insertStatement);
            returnToMainFormView(event);
        }
    }

    public void cancelButtonPushed(ActionEvent event) throws IOException {
        returnToMainFormView(event);
    }

    public boolean isInputDataValid() {
        if (customerNameTextField.getText().equals("")
                || customerNameTextField.getText().equals("")
                || postalCodeTextField.getText().equals("")
                || phoneNumberTextField.getText().equals("")) {
            return false;
        } else if (countryComboBox.getSelectionModel().isEmpty() || stateComboBox.getSelectionModel().isEmpty()) {
            return false;
        }
        return true;
    }

    public void readInputData() throws SQLException {
        newCustomerName = customerNameTextField.getText();
        newCustomerAddress = addressTextField.getText();

        FilteredList<FirstLevelDivision> selectedFirstLevelDivision= new FilteredList<FirstLevelDivision>(allFirstLevelDivisions, i-> i.getDivision() == stateComboBox.getValue());
        selectedFirstLevelDivisionId = selectedFirstLevelDivision.get(0).getDivisionId();

        newCustomerPostalCode = postalCodeTextField.getText();
        newCustomerPhoneNumber = phoneNumberTextField.getText();

    }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerIdLabel.setText(Integer.toString(newCustomerId));

        // Initialize Combo Boxes
        for (int i = 0; i < allCountries.size(); i++) {
            countryComboBox.getItems().add(allCountries.get(i).getCountryName());
        }
        countryComboBox.setOnAction( e -> {
            updateStateChoiceBox();
        });








    }
}
