package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sql.JDBC;

import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 *
 * @author Alberto Sosa
 */


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Test French Language
        //Locale.setDefault(new Locale("fr"));

        ResourceBundle resourceBundle = ResourceBundle.getBundle("Nat", Locale.getDefault());


        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginSceneView.fxml"), resourceBundle);
        primaryStage.setTitle("C195 Software II");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();



        launch(args);
        JDBC.closeConnection();
    }
}
