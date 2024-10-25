package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.CustomerService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    public TableView tblCustomer;
    public TableColumn colId;
    public TableColumn colTitle;
    public TableColumn colName;
    public TableColumn colDob;
    public TableColumn colSalary;
    public TableColumn colAddress;
    public TableColumn colCity;
    public TableColumn colProvince;
    public TableColumn colPostalCode;
    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCity;

    @FXML
    private DatePicker txtDob;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPostalCode;

    @FXML
    private JFXTextField txtProvince;

    @FXML
    private JFXTextField txtSalary;

    @FXML
    private JFXComboBox<String> txtTitle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("custTitle"));
        colName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("custAddress"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        loadTable();
        generateID();
        ObservableList<String> titles = FXCollections.observableArrayList();
        titles.add("Mr ");
        titles.add("Miss ");
        titles.add("Ms ");
        txtTitle.setItems(titles);
        tblCustomer.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                setTextToValues((Customer) newValue);
            } else {
                return;
            }
        }));
    }

    private void setTextToValues(Customer newValue) {
        txtId.setText(newValue.getCustId());
        txtTitle.setValue(newValue.getCustTitle());
        txtName.setText(newValue.getCustName());
        txtDob.setValue(newValue.getDob());
        txtSalary.setText(String.valueOf(newValue.getSalary()));
        txtAddress.setText(newValue.getCustAddress());
        txtCity.setText(newValue.getCity());
        txtProvince.setText(newValue.getProvince());
        txtPostalCode.setText(newValue.getPostalCode());
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void loadTable(){
        CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
        ObservableList<Customer> customers = customerService.getAll();
        tblCustomer.setItems(customers);
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (txtId.getText().isEmpty() || txtTitle.getValue() == null || txtName.getText().isEmpty() ||
                txtDob.getValue() == null || txtSalary.getText().isEmpty() || txtAddress.getText().isEmpty() ||
                txtCity.getText().isEmpty() || txtProvince.getText().isEmpty() || txtPostalCode.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields must be filled out.");
            alert.show();
            return;
        }

        if (!isNumeric(txtSalary.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid numeric value for salary.");
            alert.show();
            return;
        }

        if (!isNumeric(txtPostalCode.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid numeric postal code.");
            alert.show();
            return;
        }

        Customer customer = new Customer(
                txtId.getText(),
                txtTitle.getValue(),
                txtName.getText(),
                txtDob.getValue(),
                Double.parseDouble(txtSalary.getText()),
                txtAddress.getText(),
                txtCity.getText(),
                txtProvince.getText(),
                txtPostalCode.getText()
        );

        CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
        if (customerService.addCustomer(customer)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Customer Added Successfully...");
            alert.show();
            loadTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Customer Couldn't Be Added.");
            alert.show();
        }
    }

    public void btnUpdateOnAction(ActionEvent event) {
        if (txtId.getText().isEmpty() || txtTitle.getValue() == null || txtName.getText().isEmpty() ||
                txtDob.getValue() == null || txtSalary.getText().isEmpty() || txtAddress.getText().isEmpty() ||
                txtCity.getText().isEmpty() || txtProvince.getText().isEmpty() || txtPostalCode.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields must be filled out.");
            alert.show();
            return;
        }

        if (!isNumeric(txtSalary.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid numeric value for salary.");
            alert.show();
            return;
        }

        if (!isNumeric(txtPostalCode.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid numeric postal code.");
            alert.show();
            return;
        }

        Customer customer = new Customer(
                txtId.getText(),
                txtTitle.getValue(),
                txtName.getText(),
                txtDob.getValue(),
                Double.parseDouble(txtSalary.getText()),
                txtAddress.getText(),
                txtCity.getText(),
                txtProvince.getText(),
                txtPostalCode.getText()
        );

        CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
        if (customerService.updateCustomer(customer)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Customer Updated Successfully..");
            alert.show();
            loadTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Customer didn't Update ...");
            alert.show();
        }
    }


    public void btnSearchOnAction(ActionEvent actionEvent) {
        CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
        Customer customer = customerService.searchCustomer(txtId.getText());
        if (customer!=null) {
            txtTitle.setValue(customer.getCustTitle());
            txtName.setText(customer.getCustName());
            txtDob.setValue(customer.getDob());
            txtSalary.setText(String.valueOf(customer.getSalary()));
            txtAddress.setText(customer.getCustAddress());
            txtCity.setText(customer.getCity());
            txtProvince.setText(customer.getProvince());
            txtPostalCode.setText(customer.getPostalCode());
        } else {
            // Show an alert if no customer is found
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer Not Found");
            alert.setHeaderText(null);
            alert.setContentText("No customer found with ID: " + txtId.getText());
            alert.showAndWait();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
        if(customerService.deleteCustomer(txtId.getText())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Customer Deleted SuccessFully");
            alert.show();
            loadTable();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Customer Didn't Found");
            alert.show();
        }
    }

    public void generateID() {
        try {
            String SQL = "SELECT MAX(CustID) FROM customerentity";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clothifystore", "root", "12345");
            PreparedStatement pstm = connection.prepareStatement(SQL);
            ResultSet resultSet = pstm.executeQuery();

            int newId = 1;

            if (resultSet.next()) {
                String lastId = resultSet.getString(1);
                if (lastId != null) {
                    newId = Integer.parseInt(lastId.substring(1)) + 1;
                }
            }
            txtId.setText(String.format("C%03d", newId));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnNewEmployeeOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void btnAllReportsOnAction(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();

        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/report_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();
    }

    public void btnCustomersOnAction(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();

        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/customer_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();
    }

    public void btnAddItemOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/item_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void btnSupplierOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/supplier_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void btnAboutUsOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/about_us_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void btnDashboardOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_dashboard.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}

