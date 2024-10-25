package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.UserService;
import util.ServiceType;

import java.io.IOException;

public class EmployeeMainPageFormController {

    @FXML
    public JFXPasswordField txtPassword;
    @FXML
    private JFXTextField txtEmailAddress;

    @FXML
    void btnSignInOnAction(ActionEvent event) {
        String email = txtEmailAddress.getText();
        String password = txtPassword.getText();

        UserService userService = ServiceFactory.getInstance().getServiceType(ServiceType.USER);
        User user = userService.loginUser(email, password);

        if (user != null) {
            Stage newStage = new Stage();
            try {
                newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_dashboard.fxml"))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            newStage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } else {
            new Alert(Alert.AlertType.WARNING, "Invalid User Name Or Password.").show();
        }
    }

    public void btnSignUpOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_sign_up_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void btnForgetPwOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/forget_pw_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnAdminOnAction(ActionEvent event) {
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
