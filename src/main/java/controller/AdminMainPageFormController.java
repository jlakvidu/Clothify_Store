package controller;

import com.jfoenix.controls.JFXTextField;
import dto.User;
import entity.UserEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.UserService;
import util.ServiceType;

import java.io.IOException;

public class AdminMainPageFormController {

    @FXML
    public ImageView leftImageView;
    @FXML
    public ImageView rightImageView;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextField txtEmailAddress;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    void btnEmployeeOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_main_page.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    @FXML
    void btnSignInOnAction(ActionEvent event) {
        String email = txtEmailAddress.getText();
        String password = txtPassword.getText();

        UserService userService = ServiceFactory.getInstance().getServiceType(ServiceType.USER);
        User user = userService.loginUser(email, password);

        if (user != null) {
            Stage stage = new Stage();
            try {
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_dashboard.fxml"))));
                stage.setTitle("Admin Sign Up");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load sign-up form.").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Invalid User Name Or Password.").show();
        }
    }

    public void btnSignUpOnAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_sign_up_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    public void btnForgetPwOnAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/forget_pw_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }
}
