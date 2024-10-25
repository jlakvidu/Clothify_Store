package controller;

import dto.Employee;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.EmployeeService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeReportFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colEmployeeId;

    @FXML
    private TableColumn<?, ?> colEmployeeName;

    @FXML
    private TableColumn<?, ?> colEmployeeTitle;

    @FXML
    private TableColumn<?, ?> colEmployeeAddress;

    @FXML
    private TableColumn<?, ?> colEmployeeEmail;

    @FXML
    private TableColumn<?, ?> colContactNumber;

    @FXML
    private TableView<Employee> tblEmployees;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colEmployeeTitle.setCellValueFactory(new PropertyValueFactory<>("employeeTitle"));
        colEmployeeAddress.setCellValueFactory(new PropertyValueFactory<>("employeeAddress"));
        colEmployeeEmail.setCellValueFactory(new PropertyValueFactory<>("employeeEmailAddress"));
        colContactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        loadEmployees();
    }

    private void loadEmployees() {
        EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
        ObservableList<Employee> allEmployees = employeeService.getAll();
        tblEmployees.setItems(allEmployees);
    }

    @FXML
    void handlePrintReportOnAction(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean proceed = printerJob.showPrintDialog(tblEmployees.getScene().getWindow());
            if (proceed) {
                Node reportNode = createReportNode();
                boolean success = printerJob.printPage(reportNode);
                if (success) {
                    printerJob.endJob();
                }
            }
        }
    }

    private Node createReportNode() {
        VBox reportNode = new VBox();
        reportNode.setSpacing(20);
        javafx.scene.control.Label header = new javafx.scene.control.Label("Employee Report");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A90E2; -fx-alignment: center;");
        reportNode.getChildren().add(header);

        HBox tableHeader = new HBox();
        tableHeader.setSpacing(20);
        tableHeader.setStyle("-fx-background-color: #D3D3D3; -fx-padding: 10;");

        javafx.scene.control.Label idHeader = new javafx.scene.control.Label("Employee ID");
        idHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        javafx.scene.control.Label nameHeader = new javafx.scene.control.Label("Name");
        nameHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        javafx.scene.control.Label titleHeader = new javafx.scene.control.Label("Title");
        titleHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        javafx.scene.control.Label addressHeader = new javafx.scene.control.Label("Address");
        addressHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        javafx.scene.control.Label emailHeader = new javafx.scene.control.Label("Email");
        emailHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        javafx.scene.control.Label contactHeader = new javafx.scene.control.Label("Contact Number");
        contactHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        tableHeader.getChildren().addAll(idHeader, nameHeader, titleHeader, addressHeader, emailHeader, contactHeader);
        reportNode.getChildren().add(tableHeader);

        for (Employee employee : tblEmployees.getItems()) {
            HBox employeeRow = new HBox();
            employeeRow.setSpacing(20);
            employeeRow.setStyle("-fx-background-color: #F0F8FF; -fx-padding: 10;");

            javafx.scene.control.Label idLabel = new javafx.scene.control.Label(employee.getEmployeeId());
            idLabel.setStyle("-fx-text-fill: #333;");
            javafx.scene.control.Label nameLabel = new javafx.scene.control.Label(employee.getEmployeeName());
            nameLabel.setStyle("-fx-text-fill: #4A90E2;");
            javafx.scene.control.Label titleLabel = new javafx.scene.control.Label(employee.getEmployeeTitle());
            titleLabel.setStyle("-fx-text-fill: #333;");
            javafx.scene.control.Label addressLabel = new javafx.scene.control.Label(employee.getEmployeeAddress());
            addressLabel.setStyle("-fx-text-fill: #4A90E2;");
            javafx.scene.control.Label emailLabel = new javafx.scene.control.Label(employee.getEmployeeEmailAddress());
            emailLabel.setStyle("-fx-text-fill: #333;");
            javafx.scene.control.Label contactLabel = new javafx.scene.control.Label(employee.getContactNumber());
            contactLabel.setStyle("-fx-text-fill: #4A90E2;");

            employeeRow.getChildren().addAll(idLabel, nameLabel, titleLabel, addressLabel, emailLabel, contactLabel);
            reportNode.getChildren().add(employeeRow);
        }

        return reportNode;
    }

    public void btnBackOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/report_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
