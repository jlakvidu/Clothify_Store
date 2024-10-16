package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.UserService;
import util.ServiceType;

import java.io.IOException;

public class ResetPasswordFormController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private TextField txtOTP;

    private String emailAddress;

    UserService userService = ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    public void setEmailAddress(String email) {
        this.emailAddress = email;
        System.out.println("Email Address Set: " + this.emailAddress);
    }

    @FXML
    void btnResetPWOnAction(ActionEvent event) {
        resetPassword();
    }

    public void resetPassword() {
        String otp = txtOTP.getText().trim();
        String newPassword = txtNewPassword.getText().trim();
        System.out.println("Entered OTP: " + otp);
        System.out.println("Email Address: " + emailAddress);

        if (userService.validateOTP(emailAddress, otp)) {
            userService.resetPassword(emailAddress, newPassword);
            new Alert(Alert.AlertType.INFORMATION, "Password has been reset.").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Invalid OTP.").show();
        }
    }

    public void btnBackToLoginOnAction(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();

        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_main_page.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();
    }
}
