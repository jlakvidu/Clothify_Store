package controller;

import dto.*;
import entity.ItemEntity;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import service.ServiceFactory;
import service.SuperService;
import service.custom.CustomerService;
import service.custom.ItemService;
import service.custom.OrderService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.Date;

public class EmployeeDashboardFormController implements Initializable {

    public TextField txtCatagory;
    public TextField txtEmail;
    public TextField txtEmpId;
    public TextField txtDiscount;
    @FXML
    private ComboBox<String> cmdCustomerId;

    @FXML
    private ComboBox<String> cmdItemCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblOrderDate;

    @FXML
    private Label lblOrderTime;

    @FXML
    private TableView<CartTM> tblOrderDetails;

    @FXML
    private TextField txtCustomerAddress;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtOrderId;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtStock;

    @FXML
    private TextField txtUnitPrice;

    ObservableList<CartTM> cartTMS = FXCollections.observableArrayList();


    @FXML
    void btnAboutUsOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_about_us_form.fxml"))));
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
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_item_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        String itemId = cmdItemCode.getValue();
        String name = txtDescription.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());
        int currentStock = Integer.parseInt(txtStock.getText());

        if (currentStock < qty) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid QTY. Stock available: " + currentStock);
            alert.show();
        } else {
            double total = unitPrice * qty;
            cartTMS.add(new CartTM(itemId, name, qty, unitPrice, total));
            calcNetTotal();
            int updateStock = updateStock01(itemId, qty);
            refreshStockDisplay(itemId);

            tblOrderDetails.setItems(cartTMS);
        }
    }

    private int updateStock01(String itemId, int qtyToAdd) {
        int updatedStock = -1;
        try {
            ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
            Item item = itemService.searchItem(itemId);
            if (item != null) {
                int currentStock = item.getQty();
                updatedStock = currentStock - qtyToAdd;
                item.setQty(updatedStock);
                itemService.updateItem(item);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating stock: " + e.getMessage());
            alert.show();
        }
        return updatedStock;
    }


    private void refreshStockDisplay(String itemId) {
        ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        Item item = itemService.searchItem(itemId);
        if (item != null) {
            txtStock.setText(String.valueOf(item.getQty())); // Update the stock TextField
        }
    }



    private void calcNetTotal() {
        Double total = 0.0;

        for (CartTM cartTM : cartTMS) {
            total += cartTM.getTotal();
        }

        String discountText = txtDiscount.getText();
        double discount = 0.0;

        try {
            discount = Double.parseDouble(discountText);
        } catch (NumberFormatException e) {
            discount = 0.0;
        }

        total -= discount;

        if (total < 0) {
            total = 0.0;
        }

        lblNetTotal.setText(total.toString() + " /=");
    }


    @FXML
    void btnCustomersOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_customer_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        if(cartTMS.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING, "No items in the cart to place an order.");
            alert.show();
        }else {
            for (CartTM cartItem : cartTMS) {
                ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
                Item dbItem = itemService.searchItem(cartItem.getItemId());
                int newQty = dbItem.getQty() - cartItem.getQty();
                dbItem.setQty(newQty);
                itemService.updateItem(dbItem);
            }

            List<OrderDetails> orderDetailsList=new ArrayList<>();

            cartTMS.forEach(obj->{
                orderDetailsList.add(new OrderDetails(txtOrderId.getText(),obj.getItemId(),obj.getQty(), obj.getPrice()));
            });

            Order order = new Order(txtOrderId.getText(), LocalDateTime.now(), txtEmpId.getText(),txtEmail.getText(), orderDetailsList);
            OrderService orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
            orderService.placeOrder(order);

            cartTMS.clear();
            //lblItemCount.setText("Item Count :- 0");
            //lblSubTotal.setText("Sub Total :- 0.0/=");
            //generateOrderId();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order placed successfully!");
            alert.show();
        }
    }




    @FXML
    void btnRemoveOnAction(ActionEvent event) {
        CartTM selectedItem = tblOrderDetails.getSelectionModel().getSelectedItem();


        if (selectedItem != null) {
            String itemId = selectedItem.getItemId();
            int qtyToRemove = selectedItem.getQty();

            cartTMS.remove(selectedItem);
            //viewItemId.setText("");
            //viewItemName.setText("");

            int updatedStock = updateStock(itemId, qtyToRemove);

            refreshStockDisplay(itemId);

            calcNetTotal();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select a table row to delete");
            alert.show();
        }
    }


    private int updateStock(String itemId, int qtyToRemove) {
        int updatedStock = -1;
        try {
            ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
            Item item = itemService.searchItem(itemId);
            if (item != null) {
                int currentStock = item.getQty();
                updatedStock = currentStock + qtyToRemove;
                item.setQty(updatedStock);
                itemService.updateItem(item);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating stock: " + e.getMessage());
            alert.show();
        }
        return updatedStock;
    }



    @FXML
    void btnSupplierOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_supplier_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        generateID();
        loadCustomerIds();
        loadItemIds();
        loadDateAndTime();
        cmdCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newVal) -> {
            if (newVal!=null){
                searchCustomer(newVal);
            }
        });

        cmdItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newVal) -> {
            if (newVal!=null){
                searchItemCode(newVal);
            }
        });
    }

    private void searchItemCode(String newVal) {
        ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        Item item =  itemService.searchItem(newVal);
        txtDescription.setText(item.getName());
        txtStock.setText(String.valueOf(item.getQty()));
        txtUnitPrice.setText(String.valueOf(item.getPrice()));
    }

    private void searchCustomer(String newVal) {
        CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
        Customer customer =  customerService.searchCustomer(newVal);
        txtCustomerName.setText(customer.getCustName());
        txtCustomerAddress.setText(customer.getCustAddress());
    }

    public void loadCustomerIds(){
        CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
        ObservableList<String> customerIds = customerService.getCustomerIds();
        cmdCustomerId.setItems(customerIds);
    }

    public void loadItemIds(){
        ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        ObservableList<String> itemIds = itemService.getItemIds();
        for (String itemId : itemIds) {
            System.out.println(itemId);
        }
        cmdItemCode.setItems(itemIds);
    }

    private void loadDateAndTime() {

        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String dateNow = f.format(date);

        lblOrderDate.setText(dateNow);


        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime now = LocalTime.now();
            lblOrderTime.setText(now.getHour() + " : " + now.getMinute() + " : " + now.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    public void generateID() {
        try {
            String SQL = "SELECT MAX(orderId) FROM orders";
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
            txtOrderId.setText(String.format("O%03d", newId));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnPlaceOrderWindowOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_dashboard.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
