package controller;

import com.jfoenix.controls.JFXTextField;
import dto.Item;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.SuperService;
import service.custom.ItemService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ItemFormController implements Initializable {

    public TableColumn colItemId;
    public TableColumn colName;
    public TableColumn colSupId;
    public TableColumn colPrice;
    public TableColumn colQty;
    public TableColumn colSize;
    @FXML
    private TableColumn<?, ?> colCategory;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colPackSize;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableView<Item> tblItem;

    @FXML
    private JFXTextField txtCategory;

    @FXML
    private JFXTextField txtItemId;

    @FXML
    private JFXTextField txtItemName;

    @FXML
    private JFXTextField txtPackSize;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private JFXTextField txtSupplierId;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (txtItemId.getText().isEmpty() ||
                txtItemName.getText().isEmpty() ||
                txtSupplierId.getText().isEmpty() ||
                txtUnitPrice.getText().isEmpty() ||
                txtQtyOnHand.getText().isEmpty() ||
                txtPackSize.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        double unitPrice;
        try {
            unitPrice = Double.parseDouble(txtUnitPrice.getText());
            if (unitPrice < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid positive unit price.");
            alert.showAndWait();
            return;
        }

        int qtyOnHand;
        try {
            qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
            if (qtyOnHand < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid non-negative quantity on hand.");
            alert.showAndWait();
            return;
        }

        Item item = new Item(
                txtItemId.getText(),
                txtItemName.getText(),
                txtSupplierId.getText(),
                unitPrice,
                qtyOnHand,
                txtPackSize.getText()
        );

        ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        if (itemService.addItem(item)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Item Added Successfully...");
            alert.show();
            loadTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to add the item.");
            alert.show();
        }
    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        if(itemService.deleteItem(txtItemId.getText())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Item Deleted SuccessFully");
            alert.show();
            loadTable();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Item Didn't Found");
            alert.show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        Item item = itemService.searchItem(txtItemId.getText());
        if (item!=null) {
            txtItemId.setText(item.getItemId());
            txtItemName.setText(item.getName());
            txtSupplierId.setText(item.getSupId());
            txtUnitPrice.setText(String.valueOf(item.getPrice()));
            txtPackSize.setText(item.getSupId());
            txtQtyOnHand.setText(String.valueOf(item.getQty()));
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Item Not Found");
            alert.setHeaderText(null);
            alert.setContentText("No Item found with ID: " + txtItemId.getText());
            alert.showAndWait();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtItemId.getText().isEmpty() ||
                txtItemName.getText().isEmpty() ||
                txtSupplierId.getText().isEmpty() ||
                txtUnitPrice.getText().isEmpty() ||
                txtQtyOnHand.getText().isEmpty() ||
                txtPackSize.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        double unitPrice;
        try {
            unitPrice = Double.parseDouble(txtUnitPrice.getText());
            if (unitPrice < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid positive unit price.");
            alert.showAndWait();
            return;
        }

        int qtyOnHand;
        try {
            qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
            if (qtyOnHand < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid non-negative quantity on hand.");
            alert.showAndWait();
            return;
        }

        Item item = new Item(
                txtItemId.getText(),
                txtItemName.getText(),
                txtSupplierId.getText(),
                unitPrice,
                qtyOnHand,
                txtPackSize.getText()
        );

        ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        if (itemService.updateItem(item)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Item Updated Successfully..");
            alert.show();
            loadTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Item didn't update ...");
            alert.show();
        }
    }


    public void generateID() {
        try {
            String SQL = "SELECT MAX(itemId) FROM item";
            Connection connection = null;
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clothifystore", "root", "12345");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            PreparedStatement pstm = connection.prepareStatement(SQL);
            ResultSet resultSet = pstm.executeQuery();

            int newId = 1;

            if (resultSet.next()) {
                String lastId = resultSet.getString(1);
                if (lastId != null) {
                    newId = Integer.parseInt(lastId.substring(1)) + 1;
                }
            }
            txtItemId.setText(String.format("I%03d", newId));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));

        generateID();
        loadTable();

        tblItem.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                setTextToValues((Item) newValue);
            } else {
                return;
            }
        }));
    }

    private void setTextToValues(Item newValue) {
        txtItemId.setText(newValue.getItemId());
        txtItemName.setText(newValue.getName());
        txtSupplierId.setText(newValue.getSupId());
        txtQtyOnHand.setText(String.valueOf(newValue.getQty()));
        txtUnitPrice.setText(String.valueOf(newValue.getPrice()));
        txtPackSize.setText(newValue.getSize());
    }

    public void loadTable(){
        ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        ObservableList<Item> items = itemService.getAll();
        tblItem.setItems(items);
    }

    public void btnNewEmployeeOnAction(ActionEvent event) {
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

    public void btnAllReportsOnAction(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();

        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/report_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();
    }

    public void btnCustomersOnAction(ActionEvent event) {
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

    public void btnAddItemOnAction(ActionEvent actionEvent) {
    }

    public void btnSupplierOnAction(ActionEvent event) {
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

    public void btnAboutUsOnAction(ActionEvent event) {
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

    public void btnDashboardFormOnAction(ActionEvent event) {
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_dashboard.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
