package controller;

import dto.Customer;
import dto.Supplier;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.CustomerService;
import service.custom.SupplierService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardFormController implements Initializable {

    @FXML
    private Label lblCustomers;

    @FXML
    private Label lblItems;

    @FXML
    private Label lblOrders;

    @FXML
    private Label lblSales;

    @FXML
    void btnAboutUsOnAction(ActionEvent event) {

    }

    @FXML
    void btnAddItemOnAction(ActionEvent event) {
        // Open the new Item Form window
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/item_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        // Close the current dashboard window
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }


    @FXML
    void btnAllReportsOnAction(ActionEvent event) {

    }

    @FXML
    void btnCustomersOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/customer_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    @FXML
    void btnNewEmployeeOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/supplier_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    public void loadAllCustomersValues(){
        CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
        ObservableList<Customer> customers = customerService.getAll();
        Integer allCustomers = customers.size();
        lblCustomers.setText(allCustomers.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAllCustomersValues();
    }
}
