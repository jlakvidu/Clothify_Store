package controller;

import dto.CartTM;
import dto.Receipt;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

public class ReceiptFormController implements Initializable {
    public Label lblOrderId;
    public Label lblCustomerName;
    public Label lblCustomerAddress;
    public TableView<CartTM> tblItems;
    public TableColumn<CartTM, String> colItemId;
    public TableColumn<CartTM, String> colItemName;
    public TableColumn<CartTM, Integer> colQty;
    public TableColumn<CartTM, Double> colPrice;
    public TableColumn<CartTM, Double> colTotal;
    public Label lblTotalPrice;
    public Label lblDiscount;
    public Label lblDate;
    public Label lblTime;

    public void initializeReceipt(Receipt receipt) {
        lblOrderId.setText(receipt.getOrderId());
        lblCustomerName.setText(receipt.getCustomerName());
        lblCustomerAddress.setText(receipt.getCustomerAddress());

        for (CartTM item : receipt.getItems()) {
            tblItems.getItems().add(item);
        }

        lblTotalPrice.setText(receipt.getTotalPrice() + " /=");
    }

    public void handlePrintReceipt(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean proceed = printerJob.showPrintDialog(tblItems.getScene().getWindow());
            if (proceed) {
                Node receiptNode = createReceiptNode();
                boolean success = printerJob.printPage(receiptNode);
                if (success) {
                    printerJob.endJob();
                } else {
                    System.out.println("Print job failed.");
                }
            }
        }
    }

    private Node createReceiptNode() {
        VBox receiptNode = new VBox();
        receiptNode.setSpacing(10);

        receiptNode.setStyle("-fx-padding: 20; -fx-background-color: #f9f9f9; -fx-border-color: #007bff; -fx-border-width: 2; -fx-font-family: 'Arial';");

        Label orderIdLabel = new Label("Order ID: " + lblOrderId.getText());
        orderIdLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #007bff;");
        receiptNode.getChildren().add(orderIdLabel);

        Label customerNameLabel = new Label("Customer: " + lblCustomerName.getText());
        customerNameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");
        receiptNode.getChildren().add(customerNameLabel);

        Label customerAddressLabel = new Label("Address: " + lblCustomerAddress.getText());
        customerAddressLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");
        receiptNode.getChildren().add(customerAddressLabel);

        Label separator = new Label("--------------------------------------------------");
        separator.setStyle("-fx-text-fill: #007bff; -fx-font-weight: bold;");
        receiptNode.getChildren().add(separator);

        double totalPrice = 0.0;

        for (CartTM item : tblItems.getItems()) {
            double itemTotal = item.getTotal();
            totalPrice += itemTotal;
            Label itemLabel = new Label(item.getName() + ": " + item.getQty() + " x " + String.format("%.2f", item.getPrice()) + " /=" + " = " + String.format("%.2f", itemTotal) + " /=");
            itemLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");
            receiptNode.getChildren().add(itemLabel);
        }

        Label totalSeparator = new Label("--------------------------------------------------");
        totalSeparator.setStyle("-fx-text-fill: #007bff; -fx-font-weight: bold;");
        receiptNode.getChildren().add(totalSeparator);

        Label totalPriceLabel = new Label("Total: " + String.format("%.2f", totalPrice) + " /=");
        totalPriceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ff5733;");
        receiptNode.getChildren().add(totalPriceLabel);

        Label dateLabel = new Label("Date: " + lblDate.getText());
        dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        receiptNode.getChildren().add(dateLabel);

        Label timeLabel = new Label("Time: " + lblTime.getText());
        timeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        receiptNode.getChildren().add(timeLabel);

        return receiptNode;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        loadDateAndTime();
    }

    private void loadDateAndTime() {

        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String dateNow = f.format(date);

        lblDate.setText(dateNow);


        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime now = LocalTime.now();
            lblTime.setText(now.getHour() + " : " + now.getMinute() + " : " + now.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
