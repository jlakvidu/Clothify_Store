package controller;

import dto.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.SuperService;
import service.custom.*;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardFormController implements Initializable {

    public StackedBarChart<String,Number> stackedBarChart;
    public VBox chartContainer;
    public Label lblEmployees;
    public PieChart pieChart;
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

    @FXML
    void btnAddItemOnAction(ActionEvent event) {
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


    @FXML
    void btnAllReportsOnAction(ActionEvent event) {
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

    private StackedBarChart<String, Number> createStackedBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
        stackedBarChart.setTitle("Sales Overview");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Trousers");
        series1.getData().add(new XYChart.Data<>("January", 30));
        series1.getData().add(new XYChart.Data<>("February", 70));
        series1.getData().add(new XYChart.Data<>("March", 50));
        series1.getData().add(new XYChart.Data<>("April", 100));

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Shirts");
        series2.getData().add(new XYChart.Data<>("January", 50));
        series2.getData().add(new XYChart.Data<>("February", 60));
        series2.getData().add(new XYChart.Data<>("March", 80));
        series2.getData().add(new XYChart.Data<>("April", 90));

        stackedBarChart.getData().addAll(series1, series2);

        return stackedBarChart;
    }


    @FXML
    void btnCustomersOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/customer_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnNewEmployeeOnAction(ActionEvent event) {
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

    @FXML
    void btnSupplierOnAction(ActionEvent event) {
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

    public void loadAllCustomersValues(){
        CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
        ObservableList<Customer> customers = customerService.getAll();
        Integer allCustomers = customers.size();
        lblCustomers.setText(allCustomers.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPieChart();
        loadAllCustomersValues();
        loadTotalSalesValue();
        loadAllEmployeesValues();
        loadTotalSoldItemsValue();
        StackedBarChart<String, Number> stackedBarChart = createStackedBarChart();
        chartContainer.getChildren().add(stackedBarChart);
    }

    public void loadTotalSalesValue() {
        OrderDetailsService orderDetailsService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDERDETAILS);
        double totalSales = orderDetailsService.getTotalSalesPrice();
        lblSales.setText(String.format("%.2f", totalSales));
    }

    private void loadAllEmployeesValues(){
        EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
        ObservableList<Employee> employees = employeeService.getAll();
        Integer allCustomers = employees.size();
        lblEmployees.setText(allCustomers.toString());
    }

    public void loadTotalSoldItemsValue() {
        OrderDetailsService orderDetailsService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDERDETAILS);
        Integer totalSoldItems = orderDetailsService.getTotalSoldItems();
        lblItems.setText(String.valueOf(totalSoldItems));
    }

    private void loadPieChart() {
        PieChart.Data slice1 = new PieChart.Data("Clothing", 30);
        PieChart.Data slice2 = new PieChart.Data("Footwear", 25);
        PieChart.Data slice3 = new PieChart.Data("Accessories", 20);
        PieChart.Data slice4 = new PieChart.Data("Homewear", 15);
        PieChart.Data slice5 = new PieChart.Data("Others", 10);

        pieChart.getData().addAll(slice1, slice2, slice3, slice4, slice5);

        for (PieChart.Data slice : pieChart.getData()) {
            slice.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                showAlert(slice.getName(), slice.getPieValue());
            });
        }
    }

    private void showAlert(String name, double value) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pie Chart Slice Info");
        alert.setHeaderText("Slice Information");
        alert.setContentText("Type : " + name + "\nValue: " + value);
        alert.showAndWait();
    }

    public void btnLogOutOnAction(ActionEvent event) {
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
