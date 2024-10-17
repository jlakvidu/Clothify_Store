package controller;

import dto.Supplier;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import service.ServiceFactory;
import service.custom.SupplierService;
import util.ServiceType;

import java.net.URL;
import java.util.ResourceBundle;

public class SupplierReportFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private TableColumn<?, ?> colSupplierTitle;

    @FXML
    private TableColumn<?, ?> colSupplierName;

    @FXML
    private TableColumn<?, ?> colSupplierItem;

    @FXML
    private TableColumn<?, ?> colSupplierCompany;

    @FXML
    private TableColumn<?, ?> colSupplierContactNumber;

    @FXML
    private TableColumn<?, ?> colSupplierEmailAddress;

    @FXML
    private TableView<Supplier> tblSuppliers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierTitle.setCellValueFactory(new PropertyValueFactory<>("supplierTitle"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colSupplierItem.setCellValueFactory(new PropertyValueFactory<>("supplierItem"));
        colSupplierCompany.setCellValueFactory(new PropertyValueFactory<>("supplierCompany"));
        colSupplierContactNumber.setCellValueFactory(new PropertyValueFactory<>("supplierContactNumber"));
        colSupplierEmailAddress.setCellValueFactory(new PropertyValueFactory<>("supplierEmailAddress"));

        loadSuppliers();
    }

    private void loadSuppliers() {
        SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);
        ObservableList<Supplier> allSuppliers = supplierService.getAll();
        tblSuppliers.setItems(allSuppliers);
    }

    @FXML
    void handlePrintReportOnAction(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean proceed = printerJob.showPrintDialog(tblSuppliers.getScene().getWindow());
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

        javafx.scene.control.Label header = new javafx.scene.control.Label("Supplier Report");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A90E2;");

        reportNode.getChildren().add(header);

        VBox table = new VBox();
        table.setSpacing(10);

        HBox tableHeader = new HBox();
        tableHeader.setStyle("-fx-background-color: #4A90E2; -fx-padding: 10px;");

        tableHeader.getChildren().addAll(
                createStyledLabel("Supplier ID", true),
                createStyledLabel("Title", true),
                createStyledLabel("Name", true),
                createStyledLabel("Item", true),
                createStyledLabel("Company", true),
                createStyledLabel("Contact", true),
                createStyledLabel("Email", true)
        );
        table.getChildren().add(tableHeader);

        boolean alternate = false;
        for (Supplier supplier : tblSuppliers.getItems()) {
            HBox row = new HBox();
            row.setStyle("-fx-padding: 8px; " + (alternate ? "-fx-background-color: #F5F5F5;" : "-fx-background-color: #FFFFFF;"));
            alternate = !alternate;

            row.getChildren().addAll(
                    createStyledLabel(supplier.getSupplierId(), false),
                    createStyledLabel(supplier.getSupplierTitle(), false),
                    createStyledLabel(supplier.getSupplierName(), false),
                    createStyledLabel(supplier.getSupplierItem(), false),
                    createStyledLabel(supplier.getSupplierCompany(), false),
                    createStyledLabel(supplier.getSupplierContactNumber(), false),
                    createStyledLabel(supplier.getSupplierEmailAddress(), false)
            );

            table.getChildren().add(row);
        }

        reportNode.getChildren().add(table);
        return reportNode;
    }

    private javafx.scene.control.Label createStyledLabel(String text, boolean isHeader) {
        javafx.scene.control.Label label = new javafx.scene.control.Label(text);
        label.setMinWidth(100);
        label.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 0 10px; " +
                        (isHeader
                                ? "-fx-font-weight: bold; -fx-text-fill: #FFFFFF;"
                                : "-fx-text-fill: #333333;"
                        )
        );
        return label;
    }

}
