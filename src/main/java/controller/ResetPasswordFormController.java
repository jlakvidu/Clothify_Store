package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import service.ServiceFactory;
import service.custom.UserService;
import util.ServiceType;

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
}
