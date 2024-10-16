package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import service.ServiceFactory;
import service.SuperService;
import service.custom.UserService;
import util.ServiceType;

public class AdminSignUpFormController {

    @FXML
    private JFXTextField txtConfirmPassword;

    @FXML
    private JFXTextField txtEmailAddress;

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    void btnSigUpOnAction(ActionEvent event) {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String emailAddress = txtEmailAddress.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        UserService userService = ServiceFactory.getInstance().getServiceType(ServiceType.USER);

        if (!password.equals(confirmPassword)) {
            new Alert(Alert.AlertType.INFORMATION, "Your passwords do not match, please re-enter.").show();
        } else {
            if (userService.registerUser(firstName, lastName, emailAddress, password, confirmPassword)) {
                new Alert(Alert.AlertType.INFORMATION, "You are registered successfully!").show();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Registration failed. Please try again later.").show();
            }
        }
    }

}
