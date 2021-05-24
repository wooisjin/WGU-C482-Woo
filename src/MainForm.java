import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Optional;


/**
 * Main Form JavaFX Controller Class
 * @author Woo Jin An
 */
public class MainForm {
    private Inventory inventoryObj;
    private ObservableList<Part> partFilteredList = FXCollections.observableArrayList();
    private ObservableList<Product> productFilteredList = FXCollections.observableArrayList();

    // Main Form Controls from FXML Initialization
    @FXML private Button addPartButton;
    @FXML private Button modifyPartButton;
    @FXML private Button deletePartButton;

    @FXML private Button addProductButton;
    @FXML private Button modifyProductButton;
    @FXML private Button deleteProductButton;

    @FXML private TextField searchPartField;
    @FXML private TextField searchProductField;


    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partStockColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;

    @FXML private TableView<Product> productTableView;
    @FXML private TableColumn<Product, Integer> productIDColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productStockColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;

    @FXML private Button mainExitButton;

    /**
     * Runs first when main controller class is created
     */
    public MainForm(){
        inventoryObj = new Inventory();
        inventoryObj.initData();

//        makePartList();
//        makeProductList();

        Inventory.getAllParts().addListener(new ListChangeListener<Part>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Part> change) {
                updatePartFilteredData();
            }
        });

        Inventory.getAllProducts().addListener(new ListChangeListener<Product>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Product> change) {
                updateProductFilteredData();
            }
        });
    }

    /**
     * Runs second after class constructor
     */
    @FXML
    public void initialize(){
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTableView.setItems(Inventory.getAllParts());
        partTableView.setPlaceholder(new Label("(NO PART FOUND)"));

        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productTableView.setItems(Inventory.getAllProducts());
        productTableView.setPlaceholder(new Label("(NO PRODUCT FOUND)"));

        partTableView.setItems(partFilteredList);
        productTableView.setItems(productFilteredList);

        updatePartFilteredData();
        updateProductFilteredData();

        searchPartField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                updatePartFilteredData();
            }
        });

        searchProductField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                updateProductFilteredData();
            }
        });
    }

    /**
     * Updates filtered part table based on values in the part filter text box
     */
    private void updatePartFilteredData() {
        partFilteredList.clear();
        for (Part p : Inventory.getAllParts()) {
            if (matchesPartFilter(p)) {
                partFilteredList.add(p);
            }
        }
        reapplyPartTableSortOrder();
    }

    /**
     * Matches the eligible parts based on values in the text box
     * @param part an iterated part in the all parts table
     * @return A boolean value on whether part matches text filter
     */
    private boolean matchesPartFilter(Part part) {
        String filterString = searchPartField.getText();
        if (filterString == null || filterString.isEmpty()) {
            return true;
        }
        String lowerCaseFilterString = filterString.toLowerCase();

        if (part.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        } else if (String.valueOf(part.getId()).indexOf(lowerCaseFilterString) != -1){
            return true;
        }
        return false;
    }

    /**
     * Resorts the part table
     */
    private void reapplyPartTableSortOrder() {
        ArrayList<TableColumn<Part, ?>> sortOrder = new ArrayList<>(partTableView.getSortOrder());
        partTableView.getSortOrder().clear();
        partTableView.getSortOrder().addAll(sortOrder);
    }

    /**
     * Updates filtered Product table based on values in the part filter text box
     */
    private void updateProductFilteredData() {
        productFilteredList.clear();

        for (Product p : Inventory.getAllProducts()) {
            if (matchesProductFilter(p)) {
                productFilteredList.add(p);
            }
        }
        reapplyProductTableSortOrder();
    }

    /**
     * Matches the eligible products based on values in the text box
     * @param product an iterated product in the all product table
     * @return A boolean value on whether product matches text filter
     */
    private boolean matchesProductFilter(Product product) {
        String filterString = searchProductField.getText();
        if (filterString == null || filterString.isEmpty()) {
            return true;
        }
        String lowerCaseFilterString = filterString.toLowerCase();

        if (product.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        } else if (String.valueOf(product.getId()).indexOf(lowerCaseFilterString) != -1){
            return true;
        }
        return false;
    }

    /**
     * Resort the products table
     */
    private void reapplyProductTableSortOrder() {
        ArrayList<TableColumn<Product, ?>> sortOrder = new ArrayList<>(productTableView.getSortOrder());
        productTableView.getSortOrder().clear();
        productTableView.getSortOrder().addAll(sortOrder);
    }

    /**
     * Method to create test part data
     */
    public void makePartList(){
        Inventory.addPart(new Outsourced(1, "testPart1", 1.1, 5, 1, 10, "ent"));
        Inventory.addPart(new InHouse(2, "testPart2", 1.2, 6, 2, 11, 543645));
    }

    /**
     * Method to create test product data
     */
    public void makeProductList(){
        Product newProduct = new Product(6, "testProduct1", 1.1, 5, 1, 10);
        newProduct.addAssociatedPart(new Outsourced(1, "testPart1", 1.1, 5, 1, 10, "ent"));
        Inventory.addProduct(newProduct);
    }

    /**
     * Button opens part add form
     * RUNTIME ERROR
     * Catch DisplayParameterException Exception for invalid PartForm inputs
     */
    @FXML
    private void openAddPartForm(){
        try {
            Part emptyPart = new InHouse(-1, "", 0.0, 0, 0, 0, 0);
            PartForm.displayPartForm("add", emptyPart);
        }
        catch (DisplayParameterException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Button opens part modify form
     * RUNTIME ERROR
     * Catch DisplayParameterException Exception for invalid PartForm inputs
     * Catch NullPointerException for when no part is selected in the part table and modify part button is pressed
     */
    @FXML
    private void openModifyPartForm(){
        try {
            Part partObject = partTableView.getSelectionModel().getSelectedItem();
            PartForm.displayPartForm("modify", partObject);
        }
        catch (NullPointerException e){
            System.out.println("Select a Part");
        }
        catch (DisplayParameterException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Button deletes selected part
     * RUNTIME ERROR
     * Catch NullPointerException for when no part is selected in the part table and delete part button is pressed
     */
    @FXML
    private void openPartDeleteConfirm(){
        try {

            // Checks if part is associated with any parts
            Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
            ArrayList<Product> allAssociatedProductList = new ArrayList<>();

            for (Product prod: Inventory.getAllProducts()){
                for (Part par: prod.getAllAssociatedParts()){
                    if (selectedPart.getId() == par.getId()){
                        allAssociatedProductList.add(prod);
                    }
                }
            }

            if (allAssociatedProductList.size() != 0){
                // Adds the associated product's ID into the error message
                String errorMessage = "At least one Product is associated with this part. ID: ";
                for (Product p: allAssociatedProductList){
                    errorMessage = errorMessage + String.valueOf(p.getId()) + ", ";
                }

                Alert errorBox = new Alert(Alert.AlertType.ERROR);
                errorBox.setTitle("Error Box");
                errorBox.setContentText(errorMessage);
                errorBox.showAndWait();
            }
            else {

                // If no errors, open a delete confirmation box
                Alert confirmBox = new Alert(Alert.AlertType.CONFIRMATION);
                confirmBox.setTitle("Delete Confirm Box");
                confirmBox.setContentText("Would you like to delete this part?");
                Optional<ButtonType> confirmResult = confirmBox.showAndWait();

                if (confirmResult.get() == ButtonType.OK) {
                    Inventory.deletePart(selectedPart);
                }
            }
        }
        catch (NullPointerException e){
            System.out.println("Select a Part");
        }
    }

    /**
     * Opens the add product form
     */
    @FXML
    private void openAddProduct(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductForm.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the modify products form
     */
    @FXML
    private void openModifyProduct(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductForm.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            ProductForm controller = fxmlLoader.getController();
            // Pass the currently selected product from main form table
            controller.initData(productTableView.getSelectionModel().getSelectedItem());
            stage.show();
        }catch (NullPointerException e){
            System.out.println("Select a product");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deleted a selected Product
     * RUNTIME ERROR
     * Catch NullPointerException for when no product is selected in the product table and delete product button is pressed
     */
    @FXML
    private void openProductDeleteConfirm(){
        try {

            int apartSize = productTableView.getSelectionModel().getSelectedItem().getAllAssociatedParts().size();

            if (apartSize == 0) {

                // Opens a confirmation box for product deletion
                Alert confirmBox = new Alert(Alert.AlertType.CONFIRMATION);
                confirmBox.setTitle("Part Delete Confirm Box");
                confirmBox.setContentText("Would you like to delete this product?");
                Optional<ButtonType> confirmResult = confirmBox.showAndWait();

                if (confirmResult.get() == ButtonType.OK) {
                    Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
                    Inventory.deleteProduct(selectedProduct);
                }
            }
            else{
                Alert errorBox = new Alert(Alert.AlertType.ERROR);
                errorBox.setTitle("Error Box");
                errorBox.setContentText("This Product has an Associated Part");
                errorBox.showAndWait();
            }
        }
        catch (NullPointerException e){
            System.out.println("Select a Product");
        }
    }

    /**
     * Exits application when button is pressed
     */
    @FXML
    private void exitApp(){
        Stage stage = (Stage) mainExitButton.getScene().getWindow();
        stage.close();
    }
}
