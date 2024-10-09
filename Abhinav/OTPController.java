package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class OTPController {

    @FXML
    private PasswordField otpField;

    @FXML
    private Button verifyButton;

    @FXML
    private Label errorLabel;

    private String generatedOTP; // Store OTP generated from previous step

    // Initialize the controller and OTP value (this would typically be injected or passed)
    public void initialize(String otp) {
        this.generatedOTP = otp;
    }

    @FXML
    private void handleVerifyButtonAction(ActionEvent event) {
        String otpInput = otpField.getText();

        // Basic validation for OTP (e.g., checking if it's 6 digits)
        if (otpInput.isEmpty()) {
            showErrorMessage("OTP cannot be empty.");
        } else if (!otpInput.matches("\\d{6}")) {
            showErrorMessage("OTP must be a 6-digit number.");
        } else {
            // Actual OTP validation
            boolean isOTPValid = validateOTP(otpInput);

            if (isOTPValid) {
                proceedToNextStep(); // Proceed if OTP is valid
            } else {
                showErrorMessage("Invalid OTP. Please try again.");
            }
        }
    }

    // Method to display error messages
    private void showErrorMessage(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    // Validate OTP with the backend or pre-stored OTP
    private boolean validateOTP(String otpInput) {
        // Replace with actual logic (backend verification, database, etc.)
        return otpInput.equals(generatedOTP);
    }

    // Proceed to the next step after successful OTP verification
    private void proceedToNextStep() {
        try {
            // Load the next screen (e.g., dashboard or success page)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NextScreen.fxml"));
            Parent root = loader.load();
            
            // Set up the new scene
            Stage stage = (Stage) verifyButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
