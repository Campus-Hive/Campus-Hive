package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class Main extends Application {
    private static Stage primaryStage; // Store the primary stage for navigation

    @Override
    public void start(Stage primaryStage) {
        try {
            Main.primaryStage = primaryStage; // Set primary stage
            Parent root = FXMLLoader.load(getClass().getResource("Create Account Page.fxml"));
            Scene scene = new Scene(root, 800, 400);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to switch scenes
    public static void switchScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlFile));
            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to generate OTP, send email, and switch to OTP verification page
    public static void sendOTPAndSwitchScene(String email) {
        try {
            // Generate OTP
            String otp = OTPUtils.generateOTP();
            System.out.println("Generated OTP: " + otp); // Log OTP for debug purposes

            // Send OTP via email
            EmailSender.sendOTPEmail(email, otp);

            // Switch to OTP page after sending the OTP email
            switchScene("OTPPage.fxml");

            // OTP should be passed to the OTPController here (if needed)
            // You may need to modify the controller to pass the OTP for comparison
        } catch (MessagingException e) {
            System.out.println("Failed to send OTP email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
