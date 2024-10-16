package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.Customer;
import dto.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ServiceFactory;
import service.custom.CustomerService;
import service.custom.EmployeeService;
import util.ServiceType;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EmployeeFormController implements Initializable {

    public JFXComboBox txtTitle;
    public TableColumn coltitle;
    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colContactNumber;

    @FXML
    private TableColumn<?, ?> colEmailAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableView<Employee> tblEmployee;

    @FXML
    private JFXTextField txtContactNumber;

    @FXML
    private JFXTextField txtEmployeeAddress;

    @FXML
    private JFXTextField txtEmployeeEmailAddress;

    @FXML
    private JFXTextField txtEmployeeId;

    @FXML
    private JFXTextField txtEmployeeName;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
        Employee employee = new Employee(
                txtEmployeeId.getText(),
                txtEmployeeName.getText(),
                txtTitle.getValue().toString(),
                txtEmployeeAddress.getText(),
                txtEmployeeEmailAddress.getText(),
                txtContactNumber.getText()
        );
        if (employeeService.addEmployee(employee)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Employee Added Successfully...");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Employee Didn't added....");
            alert.show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
        if(employeeService.deleteEmployee(txtEmployeeId.getText())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Employee Deleted SuccessFully");
            alert.show();
            loadTable();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Employee Didn't Found");
            alert.show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
        Employee employee = employeeService.searchEmployee(txtEmployeeId.getText());
        if (employee!=null) {
            txtTitle.setValue(employee.getEmployeeTitle());
            txtEmployeeName.setText(employee.getEmployeeName());
            txtEmployeeAddress.setText(employee.getEmployeeAddress());
            txtEmployeeEmailAddress.setText(employee.getEmployeeEmailAddress());
            txtContactNumber.setText(employee.getContactNumber());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Employee Not Found");
            alert.setHeaderText(null);
            alert.setContentText("No employee found with ID: " + txtEmployeeId.getText());
            alert.showAndWait();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Employee employee = new Employee(
                txtEmployeeId.getText(),
                txtEmployeeName.getText(),
                txtTitle.getValue().toString(),
                txtEmployeeAddress.getText(),
                txtEmployeeEmailAddress.getText(),
                txtContactNumber.getText()
        );
        EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
        if(employeeService.updateEmployee(employee)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Employee Updated Successfully..");
            alert.show();
            loadTable();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Employee didn't Updated ...");
            alert.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        coltitle.setCellValueFactory(new PropertyValueFactory<>("employeeTitle"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("employeeAddress"));
        colEmailAddress.setCellValueFactory(new PropertyValueFactory<>("employeeEmailAddress"));
        colContactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        ObservableList<String> titles = FXCollections.observableArrayList();
        titles.add("Mr ");
        titles.add("Miss ");
        titles.add("Ms ");
        txtTitle.setItems(titles);
        generateID();
        loadTable();
        tblEmployee.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                setTextToValues((Employee) newValue);
            } else {
                return;
            }
        }));
    }

    private void setTextToValues(Employee newValue) {
        txtEmployeeId.setText(newValue.getEmployeeId());
        txtTitle.setValue(newValue.getEmployeeTitle());
        txtEmployeeName.setText(newValue.getEmployeeName());
        txtEmployeeAddress.setText(newValue.getEmployeeAddress());
        txtEmployeeEmailAddress.setText(newValue.getEmployeeEmailAddress());
        txtContactNumber.setText(newValue.getContactNumber());
    }

    public void generateID() {
        try {
            String SQL = "SELECT MAX(employeeId) FROM employeeentity";
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
            txtEmployeeId.setText(String.format("E%03d", newId));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadTable(){
        EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
        ObservableList<Employee> employees = employeeService.getAll();
        tblEmployee.setItems(employees);
    }
}
