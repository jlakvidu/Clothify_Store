package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.SuperService;
import service.custom.UserService;
import util.ServiceType;

import java.io.IOException;

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

        if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill in all fields.").show();
            return;
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!emailAddress.matches(emailRegex)) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid email address.").show();
            return;
        }

        if (userService.isEmailExists(emailAddress)) {
            new Alert(Alert.AlertType.WARNING, "This email address is already registered.").show();
            return;
        }

        if (password.length() < 8) {
            new Alert(Alert.AlertType.WARNING, "Password must be at least 8 characters long.").show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            new Alert(Alert.AlertType.WARNING, "Your passwords do not match, please re-enter.").show();
            return;
        }

        if (userService.registerUser(firstName, lastName, emailAddress, password, confirmPassword)) {
            new Alert(Alert.AlertType.INFORMATION, "You are registered successfully!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Registration failed. Please try again later.").show();
        }
    }


    public void btnSignInOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_main_page.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
