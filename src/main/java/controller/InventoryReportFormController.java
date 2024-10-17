package controller;

import dto.Item;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import service.ServiceFactory;
import service.custom.ItemService;
import util.ServiceType;

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryReportFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colItemId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colSize;

    @FXML
    private TableColumn<?, ?> colSupId;

    @FXML
    private TableView<Item> tblItems;

    @FXML
    void handlePrintReport(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));

        loadItems();
    }

    private void loadItems() {
        ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        ObservableList<Item> all = itemService.getAll();
        tblItems.setItems(all);

    }

    @FXML
    public void handlePrintReportOnAction() {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean proceed = printerJob.showPrintDialog(tblItems.getScene().getWindow());
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

        javafx.scene.control.Label header = new javafx.scene.control.Label("Inventory Report");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A90E2;");  // Blue text color
        reportNode.getChildren().add(header);

        for (Item item : tblItems.getItems()) {
            VBox itemBox = new VBox();
            itemBox.setSpacing(5);
            itemBox.setStyle("-fx-border-color: #4A90E2; -fx-border-width: 2px; -fx-padding: 10; -fx-background-color: #F0F8FF;"); // Light blue background

            javafx.scene.control.Label itemIdLabel = new javafx.scene.control.Label("Item ID: " + item.getItemId());
            itemIdLabel.setStyle("-fx-text-fill: #333; -fx-font-weight: bold;");

            javafx.scene.control.Label nameLabel = new javafx.scene.control.Label("Name: " + item.getName());
            nameLabel.setStyle("-fx-text-fill: #4A90E2;");

            javafx.scene.control.Label supIdLabel = new javafx.scene.control.Label("Supplier ID: " + item.getSupId());
            supIdLabel.setStyle("-fx-text-fill: #333;");

            javafx.scene.control.Label priceLabel = new javafx.scene.control.Label("Price: $" + item.getPrice());
            priceLabel.setStyle("-fx-text-fill: #4A90E2; -fx-font-weight: bold;");

            javafx.scene.control.Label qtyLabel = new javafx.scene.control.Label("Quantity: " + item.getQty());
            qtyLabel.setStyle("-fx-text-fill: #333;");

            javafx.scene.control.Label sizeLabel = new javafx.scene.control.Label("Size: " + item.getSize());
            sizeLabel.setStyle("-fx-text-fill: #4A90E2;");

            itemBox.getChildren().addAll(itemIdLabel, nameLabel, supIdLabel, priceLabel, qtyLabel, sizeLabel);

            reportNode.getChildren().add(itemBox);
        }

        return reportNode;
    }
}
