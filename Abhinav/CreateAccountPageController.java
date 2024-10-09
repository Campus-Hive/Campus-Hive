package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class CreateAccountPageController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button LoginButton;

    @FXML
    private Label statusLabel;

    @FXML
    private void handleCreateAccountButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("All fields must be filled.");
        } else if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match.");
        } else {
            // Perform account creation logic here
            boolean success = createAccount(username, password);
            if (success) {
                statusLabel.setText("Account created successfully.");
            } else {
                statusLabel.setText("Failed to create account.");
            }
        }
    }

    private boolean createAccount(String username, String password) {
        // Logic to create the account in the database or service
        return true; // Assume success for now
    }

    @FXML
    private void switchToLoginPage(ActionEvent event) {
        // Logic to switch to the login page
        Main.switchScene("Login Page.fxml");
    }
}
