package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.SupplierService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EmployeeSupplierFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colCompany;

    @FXML
    private TableColumn<?, ?> colContactNumber;

    @FXML
    private TableColumn<?, ?> colEmailAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colItem;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableView<Supplier> tblSupplier;

    @FXML
    private JFXTextField txtContactNumber;

    @FXML
    private JFXTextField txtSupplierCompany;

    @FXML
    private JFXTextField txtSupplierEmailAddress;

    @FXML
    private JFXTextField txtSupplierId;

    @FXML
    private JFXTextField txtSupplierItem;

    @FXML
    private JFXTextField txtSupplierName;

    @FXML
    private JFXComboBox<String> txtTitle;

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
    void btnAddOnAction(ActionEvent event) {
        SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);
        Supplier supplier = new Supplier(
                txtSupplierId.getText(),
                txtTitle.getValue().toString(),
                txtSupplierName.getText(),
                txtSupplierItem.getText(),
                txtSupplierCompany.getText(),
                txtContactNumber.getText(),
                txtSupplierEmailAddress.getText()
        );
        if (supplierService.addSupplier(supplier)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Supplier Added Successfully...");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Supplier Didn't added....");
            alert.show();
        }
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
    void btnDeleteOnAction(ActionEvent event) {
        SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);
        if(supplierService.deleteSupplier(txtSupplierId.getText())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Supplier Deleted SuccessFully");
            alert.show();
            loadTable();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Supplier Didn't Found");
            alert.show();
        }
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
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

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);
        Supplier supplier = supplierService.searchSupplier(txtSupplierId.getText());
        if (supplier!=null) {
            txtTitle.setValue(supplier.getSupplierTitle());
            txtSupplierName.setText(supplier.getSupplierName());
            txtSupplierItem.setText(supplier.getSupplierItem());
            txtSupplierCompany.setText(supplier.getSupplierCompany());
            txtContactNumber.setText(supplier.getSupplierContactNumber());
            txtSupplierEmailAddress.setText(supplier.getSupplierEmailAddress());
        } else {
            // Show an alert if no customer is found
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Supplier Not Found");
            alert.setHeaderText(null);
            alert.setContentText("No employee found with ID: " + txtSupplierId.getText());
            alert.showAndWait();
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

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Supplier supplier = new Supplier(
                txtSupplierId.getText(),
                txtTitle.getValue().toString(),
                txtSupplierName.getText(),
                txtSupplierItem.getText(),
                txtSupplierCompany.getText(),
                txtContactNumber.getText(),
                txtSupplierEmailAddress.getText()
        );
        SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);
        if(supplierService.updateSupplier(supplier)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Supplier Updated Successfully..");
            alert.show();
            loadTable();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Supplier didn't Updated ...");
            alert.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("supplierTitle"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colItem.setCellValueFactory(new PropertyValueFactory<>("supplierItem"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("supplierCompany"));
        colContactNumber.setCellValueFactory(new PropertyValueFactory<>("supplierContactNumber"));
        colEmailAddress.setCellValueFactory(new PropertyValueFactory<>("supplierEmailAddress"));
        ObservableList<String> titles = FXCollections.observableArrayList();
        titles.add("Mr ");
        titles.add("Miss ");
        titles.add("Ms ");
        txtTitle.setItems(titles);
        generateID();
        loadTable();
        tblSupplier.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                setTextToValues((Supplier) newValue);
            } else {
                return;
            }
        }));
    }

    private void setTextToValues(Supplier newValue) {
        txtSupplierId.setText(newValue.getSupplierId());
        txtTitle.setValue(newValue.getSupplierTitle());
        txtSupplierName.setText(newValue.getSupplierName());
        txtSupplierItem.setText(newValue.getSupplierItem());
        txtSupplierCompany.setText(newValue.getSupplierCompany());
        txtContactNumber.setText(newValue.getSupplierContactNumber());
        txtSupplierEmailAddress.setText(newValue.getSupplierEmailAddress());
    }

    public void generateID() {
        try {
            String SQL = "SELECT MAX(supplierId) FROM supplierentity";
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
            txtSupplierId.setText(String.format("S%03d", newId));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadTable(){
        SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);
        ObservableList<Supplier> suppliers = supplierService.getAll();
        tblSupplier.setItems(suppliers);
    }
}
