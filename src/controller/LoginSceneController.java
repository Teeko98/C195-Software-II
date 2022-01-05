package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sql.DBUsers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginSceneController implements Initializable {

    public Label userLoginLabel;
    public Label usernameLabel;
    public Label passwordLabel;
    public Label systemLanguageLabel;
    public Label resourceBundleLabel;
    public Button loginButton;
    public Button closeButton;
    public TextField usernameTextField;
    public PasswordField passwordPasswordField;

    ResourceBundle resourceBundle;

    String username;
    String password;

    /**
     * Gets user input and tests if it is a valid login.
     * Called when the login button is pushed.
     */
    public void loginButtonPushed(ActionEvent event) throws IOException, SQLException {
        username = usernameTextField.getText();
        password = passwordPasswordField.getText();

        if(DBUsers.userLogin(username, password)) {
            validLogin(event);
        } else {
            invalidLogin();
        }
    }

    /**
     * Launches the main view of the application when a user has logged on successfully.
     */
    public void validLogin(Event event) throws IOException {
        System.out.println("User \"" + username + "\" has successfully logged in.");

        Parent sceneParent = FXMLLoader.load(getClass().getResource("/view/MainFormView.fxml"));
        Scene newScene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(newScene);
        window.show();
    }

    /**
     * Displays an alert box when a user has entered invalid login credentials.
     */
    public void invalidLogin() {
        System.out.println("Error: Invalid login credentials!");

        Alert invalidLoginAlert = new Alert(Alert.AlertType.ERROR);
        if (Locale.getDefault().getLanguage().equals("fr")) {
            invalidLoginAlert.setTitle(resourceBundle.getString("InvalidLogin"));
            invalidLoginAlert.setHeaderText(resourceBundle.getString("InvalidLogin"));
            invalidLoginAlert.setContentText(resourceBundle.getString("EnterValid"));
        } else {
            invalidLoginAlert.setTitle("Invalid Login Credentials");
            invalidLoginAlert.setHeaderText("Invalid Login Credentials");
            invalidLoginAlert.setContentText("Please enter a valid username and password.");
        }

        invalidLoginAlert.showAndWait();
        return;
    }

    /**
     * Exits the application when the close button is pushed.
     */
    public void closeButtonPushed() {
        System.exit(0);
    }

    /**
     * Initializes the login screen and translates the UI based on the user's system language.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Locale.getDefault().getLanguage().equals("fr")) {
            userLoginLabel.setText(resourceBundle.getString("User") + " " + resourceBundle.getString("Login"));
            usernameLabel.setText(resourceBundle.getString("Username"));
            passwordLabel.setText(resourceBundle.getString("Password"));
            usernameTextField.setPromptText(resourceBundle.getString("Enter") + " " + resourceBundle.getString("Username"));
            passwordPasswordField.setPromptText(resourceBundle.getString("Enter") + " " + resourceBundle.getString("Password"));
            loginButton.setText(resourceBundle.getString("Login"));
            closeButton.setText(resourceBundle.getString("Close"));
            systemLanguageLabel.setText(resourceBundle.getString("SystemLanguage"));
            resourceBundleLabel.setText(resourceBundle.getString("LanguageCode"));
            this.resourceBundle = resourceBundle;
        }

    }
}