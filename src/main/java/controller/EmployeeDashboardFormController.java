package controller;

import com.jfoenix.controls.JFXTextField;
import dto.*;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.ServiceFactory;
import service.custom.CustomerService;
import service.custom.EmployeeService;
import service.custom.ItemService;
import service.custom.OrderService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeDashboardFormController implements Initializable {

    public TextField txtEmail;
    public TextField txtDiscount;
    public ComboBox<String> cmdEmployeeId;
    public JFXTextField txtEmployeeName;
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
        loadEmployeeIds();
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

        cmdEmployeeId.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newVal) -> {
            if (newVal!=null){
                searchEmployee(newVal);
            }
        });

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
            updateStock01(itemId, qty);
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
            txtStock.setText(String.valueOf(item.getQty()));
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
    void btnPlaceOrderOnAction(ActionEvent event) {
        if(cartTMS.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING, "No items in the cart to place an order.");
            alert.show();
        }else {

            List<OrderDetails> orderDetailsList=new ArrayList<>();

            cartTMS.forEach(obj->{
                orderDetailsList.add(new OrderDetails(txtOrderId.getText(),obj.getItemId(),obj.getQty(), obj.getPrice()));
            });

            Order order = new Order(txtOrderId.getText(), LocalDateTime.now(), cmdEmployeeId.getValue(),txtEmail.getText(), orderDetailsList);
            OrderService orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
            orderService.placeOrder(order);
            
            Receipt receipt = new Receipt(
                    txtOrderId.getText(),
                    txtCustomerName.getText(),
                    txtCustomerAddress.getText(),
                    new ArrayList<>(cartTMS),
                    Double.parseDouble(txtDiscount.getText()),
                    Double.parseDouble(lblNetTotal.getText().split(" ")[0]),
                    LocalDateTime.now()
            );

            showReceipt(receipt);
            cartTMS.clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order placed successfully!");
            alert.show();
        }
    }

    private void showReceipt(Receipt receipt) {
        Stage receiptStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/receipt_form_fxml.fxml"));
            Parent root = loader.load();

            ReceiptFormController controller = loader.getController();
            controller.initializeReceipt(receipt);

            receiptStage.setScene(new Scene(root));
            receiptStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void btnRemoveOnAction(ActionEvent event) {
        CartTM selectedItem = tblOrderDetails.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String itemId = selectedItem.getItemId();
            int qtyToRemove = selectedItem.getQty();
            cartTMS.remove(selectedItem);
            updateStock(itemId, qtyToRemove);

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

    private void searchEmployee(String newVal) {
        EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
        Employee employee = employeeService.searchEmployee(newVal);
        txtEmail.setText(employee.getEmployeeEmailAddress());
        txtEmployeeName.setText(employee.getEmployeeName());
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

    public void loadEmployeeIds(){
        EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
        ObservableList<String> employeeIds = employeeService.getEmployeeIds();
        cmdEmployeeId.setItems(employeeIds);
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

    public void btnLogOutOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_main_page.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

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
}
