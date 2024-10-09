package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField invitationCodeField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String invitationCode = invitationCodeField.getText();

        // Simple validation
        if (username.isEmpty() || password.isEmpty() || invitationCode.isEmpty()) {
            errorLabel.setText("All fields are required.");
            errorLabel.setVisible(true);
        } else {
            // Placeholder for database login verification logic
            // boolean isValidUser = checkCredentialsInDatabase(username, password, invitationCode);

            // For now, assume successful login
            boolean isValidUser = true; // Replace with actual database logic

            if (isValidUser) {
                // Hide any error message
                errorLabel.setVisible(false);

                // Load the next page (LoginPageOTP)
                loadLoginPageOTP();
            } else {
                errorLabel.setText("Invalid login credentials.");
                errorLabel.setVisible(true);
            }
        }
    }

    // Placeholder for actual database login logic
    private boolean checkCredentialsInDatabase(String username, String password, String invitationCode) {
        // Implement database connection and validation here
        return true; // Placeholder for successful login validation
    }

    // Method to load the OTP login page
    private void loadLoginPageOTP() {
        try {
            // Load the OTP login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login Page OTP.fxml"));
            Parent root = loader.load();

            // Get current stage and set the new scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load OTP page.");
            errorLabel.setVisible(true);
        }
    }
}
