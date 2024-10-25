package controller;

import dto.OrderDetails;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.OrderDetailsService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderDetailsReportFormController implements Initializable {

    @FXML
    private TableColumn<OrderDetails, String> colItemId;

    @FXML
    private TableColumn<OrderDetails, Integer> colId;

    @FXML
    private TableColumn<OrderDetails, String> colOrderId;

    @FXML
    private TableColumn<OrderDetails, Double> colPrice;

    @FXML
    private TableColumn<OrderDetails, Integer> colQty;

    @FXML
    private TableView<OrderDetails> tblOrderDetails;

    @FXML
    void btnBackOnAction(ActionEvent event) {
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

    @FXML
    void handlePrintReportOnAction(ActionEvent event) {
        // Print report logic here
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Specify cell value factories for each column
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadOrderDetails();
    }

    private void loadOrderDetails() {
        OrderDetailsService orderDetailsService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDERDETAILS);
        ObservableList<OrderDetails> allOrderDetails = orderDetailsService.getAll();
        tblOrderDetails.setItems(allOrderDetails);
    }
}