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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.CustomerService;
import service.custom.SupplierService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardFormController implements Initializable {

    public StackedBarChart<String,Number> stackedBarChart;
    public VBox chartContainer;
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

        // Close the current dashboard window
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

        // Close the current dashboard window
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }


    @FXML
    void btnAllReportsOnAction(ActionEvent event) {

    }

    private StackedBarChart<String, Number> createStackedBarChart() {
        // Define axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create the StackedBarChart
        stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
        stackedBarChart.setTitle("Sales Overview");

        // Define the data series
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

        // Add the series to the chart
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

        // Close the current dashboard window
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
        loadAllCustomersValues();
        StackedBarChart<String, Number> stackedBarChart = createStackedBarChart();
        chartContainer.getChildren().add(stackedBarChart);
    }
}
