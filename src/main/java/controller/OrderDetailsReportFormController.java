package controller;

import dto.OrderDetails;
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
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean proceed = printerJob.showPrintDialog(tblOrderDetails.getScene().getWindow());
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

        javafx.scene.control.Label header = new javafx.scene.control.Label("Order Details Report");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A90E2; -fx-alignment: center;");
        reportNode.getChildren().add(header);

        HBox tableHeader = new HBox();
        tableHeader.setSpacing(20);
        tableHeader.setStyle("-fx-background-color: #D3D3D3; -fx-padding: 10;");

        javafx.scene.control.Label orderIdHeader = new javafx.scene.control.Label("Order ID");
        javafx.scene.control.Label itemIdHeader = new javafx.scene.control.Label("Item ID");
        javafx.scene.control.Label qtyHeader = new javafx.scene.control.Label("Quantity");
        javafx.scene.control.Label priceHeader = new javafx.scene.control.Label("Price");

        tableHeader.getChildren().addAll(orderIdHeader, itemIdHeader, qtyHeader, priceHeader);
        reportNode.getChildren().add(tableHeader);

        for (OrderDetails orderDetail : tblOrderDetails.getItems()) {
            HBox orderRow = new HBox();
            orderRow.setSpacing(20);
            orderRow.setStyle("-fx-background-color: #F0F8FF; -fx-padding: 10;");

            javafx.scene.control.Label orderIdLabel = new javafx.scene.control.Label(orderDetail.getOrderId());
            javafx.scene.control.Label itemIdLabel = new javafx.scene.control.Label(orderDetail.getItemId());
            javafx.scene.control.Label qtyLabel = new javafx.scene.control.Label(String.valueOf(orderDetail.getQty()));
            javafx.scene.control.Label priceLabel = new javafx.scene.control.Label(String.valueOf(orderDetail.getPrice()));

            orderRow.getChildren().addAll(orderIdLabel, itemIdLabel, qtyLabel, priceLabel);
            reportNode.getChildren().add(orderRow);
        }

        return reportNode;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
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