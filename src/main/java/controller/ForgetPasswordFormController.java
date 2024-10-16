package controller;

import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import repository.DaoFactory;
import repository.custom.impl.UserDaoImpl;
import service.ServiceFactory;
import service.SuperService;
import service.custom.UserService;
import util.DaoType;
import util.OTPUtil;
import util.ServiceType;

import java.io.IOException;

public class ForgetPasswordFormController {

    @FXML
    private Label otpMessage;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField txtEmail;

    UserService userService = ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @FXML
    void sendOTP(ActionEvent event) {
        String email = txtEmail.getText();
        if (email.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter your email address.").show();
            return;
        }

        User user = userService.findUserByEmail(email);
        if (user == null) {
            new Alert(Alert.AlertType.WARNING, "No user found with this email address.").show();
            return;
        }

        String otp = OTPUtil.generateOTP();
        userService.storeOTP(email, otp);
        System.out.println("Generated OTP: " + otp);

        boolean emailSent = userService.sendOTPEmail(email, otp);
        if (emailSent) {
            new Alert(Alert.AlertType.INFORMATION, "OTP has been sent to your email.").show();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/reset_passowrd_form.fxml"));
                AnchorPane root = loader.load();

                // Get the controller and set the email address
                ResetPasswordFormController resetPasswordFormController = loader.getController();
                resetPasswordFormController.setEmailAddress(email);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to send OTP. Please try again.").show();
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
