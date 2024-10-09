package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DeleteUserAccountController {

    @FXML
    private TextField userIdField; // For entering the user ID
    @FXML
    private Button deleteButton;   // Confirm deletion button
    @FXML
    private CheckBox confirmationCheckBox; // Checkbox for confirmation
    @FXML
    private Label statusLabel;     // For displaying status messages

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        String userId = userIdField.getText();

        // Ensure user input is provided and confirmed via the checkbox
        if (userId.isEmpty()) {
            statusLabel.setText("User ID cannot be empty.");
        } else if (!confirmationCheckBox.isSelected()) {
            statusLabel.setText("Please confirm that you want to delete your account.");
        } else {
            // Perform account deletion logic here
            boolean success = deleteUserAccount(userId);
            if (success) {
                statusLabel.setText("User account deleted successfully.");
            } else {
                statusLabel.setText("Failed to delete user account.");
            }
        }
    }

    private boolean deleteUserAccount(String userId) {
        // Logic to delete the account from the database or service
        // This is a placeholder, integrate actual deletion logic here
        return true; // Assume success for now
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        // Handle cancellation logic (e.g., redirect to another page)
        statusLabel.setText("Account deletion canceled.");
    }
}
