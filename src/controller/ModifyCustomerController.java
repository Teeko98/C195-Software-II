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
import main.Customer;
import main.FirstLevelDivision;
import sql.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


//TODO
// implement Last_Update date

public class ModifyCustomerController implements Initializable {

    int customerId;
    String customerName;
    String customerAddress;
    String customerPostalCode;
    String customerPhoneNumber;

    ObservableList<FirstLevelDivision> allFirstLevelDivisions = DBFirstLevelDivisions.getAllFirstLevelDivisions();
    ObservableList<Country> allCountries = DBCountries.getAllCountries();
    int customerCountryId;
    int customerFirstLevelDivisionId;

    public Label customerIdLabel;
    public TextField customerNameTextField;
    public TextField addressTextField;
    public ComboBox stateComboBox;
    public ComboBox countryComboBox;
    public TextField postalCodeTextField;
    public TextField phoneNumberTextField;

    /**
     * Receives the selected customer object and fills out the form with existing data.
     *
     * @param customerToBeModified the customer that will be modified
     */
    public void receiveCustomerToBeModified(Customer customerToBeModified) {
        customerId = customerToBeModified.getCustomerId();
        customerName = customerToBeModified.getCustomerName();
        customerAddress = customerToBeModified.getCustomerAddress();
        FilteredList<FirstLevelDivision> firstLevelDivision = new FilteredList<FirstLevelDivision>(allFirstLevelDivisions, i-> i.getDivisionId() == customerToBeModified.getCustomerDivisionId());
        customerCountryId = firstLevelDivision.get(0).getCountryId();
        customerFirstLevelDivisionId = customerToBeModified.getCustomerDivisionId();
        customerPostalCode = customerToBeModified.getCustomerPostalCode();
        customerPhoneNumber = customerToBeModified.getCustomerPhone();
        customerIdLabel.setText(Integer.toString(customerId));
        customerNameTextField.setText(customerName);
        addressTextField.setText(customerAddress);
        FilteredList<Country> countryFilteredList = new FilteredList<Country>(allCountries, i-> i.getCountryId() == customerCountryId);
        countryComboBox.getSelectionModel().select(countryFilteredList.get(0).getCountryName());
        updateStateChoiceBox();
        stateComboBox.getSelectionModel().select(firstLevelDivision.get(0).getDivision());
        postalCodeTextField.setText(customerPostalCode);
        phoneNumberTextField.setText(customerPhoneNumber);
    }

    public ModifyCustomerController() throws SQLException {
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

            // "UPDATE client_schedule.customers SET Customer_Name = '', Address = '', Postal_Code = '', Phone = '', Created_By = '', Last_Updated_By = '', Division_ID = '' WHERE Customer_ID = ;"
            String updateStatement = "UPDATE client_schedule.customers " +
                    "SET Customer_Name = '" + customerName + "', " +
                    "Address = '" + customerAddress + "', " +
                    "Postal_Code = '" + customerPostalCode + "', " +
                    "Phone = '" + customerPhoneNumber + "', " +
                    "Last_Updated_By = '" + DBUsers.currentUser.getUsername() +"', " +
                    "Division_ID = '" + customerFirstLevelDivisionId + "' " +
                    "WHERE Customer_ID = " + customerId + ";";
            System.out.println(updateStatement);
            //statement.execute(updateStatement);
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
        customerName = customerNameTextField.getText();
        customerAddress = addressTextField.getText();

        FilteredList<FirstLevelDivision> selectedFirstLevelDivision = new FilteredList<FirstLevelDivision>(allFirstLevelDivisions, i-> i.getDivision() == stateComboBox.getValue());
        customerFirstLevelDivisionId = selectedFirstLevelDivision.get(0).getDivisionId();

        customerPostalCode = postalCodeTextField.getText();
        customerPhoneNumber = phoneNumberTextField.getText();
    }

    public void updateStateChoiceBox() {
        FilteredList<Country> selectedCountry = new FilteredList<Country>(allCountries, i-> i.getCountryName() == countryComboBox.getValue());
        customerCountryId = selectedCountry.get(0).getCountryId();

        stateComboBox.getItems().clear();
        FilteredList<FirstLevelDivision> selectedCountryFirstLevelDivisions = new FilteredList<FirstLevelDivision>(allFirstLevelDivisions, i-> i.getCountryId() == customerCountryId);
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

        // Initialize Combo Boxes
        for (int i = 0; i < allCountries.size(); i++) {
            countryComboBox.getItems().add(allCountries.get(i).getCountryName());
        }
        countryComboBox.setOnAction( e -> {
            updateStateChoiceBox();
        });
    }
}

