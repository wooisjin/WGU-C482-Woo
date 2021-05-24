import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Product Form Controller Class
 * @author Woo Jin An
 */
public class ProductForm {

    private Inventory inventoryObj;
    private ObservableList<Part> partFilteredList = FXCollections.observableArrayList();
    private Product selectedProduct;

    @FXML private Button addPartToA;
    @FXML private Button removePartFromA;
    @FXML private Button saveButton;
    @FXML private Button closeButton;

    @FXML private TextField productIDField;
    @FXML private TextField productNameField;
    @FXML private TextField productInvField;
    @FXML private TextField productPriceField;
    @FXML private TextField productMaxField;
    @FXML private TextField productMinField;

    @FXML private TextField partSearchField;

    @FXML private TableView<Part> allPartTable;
    @FXML private TableColumn<Part, Integer> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partStockColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;

    @FXML private TableView<Part> aPartTable;
    @FXML private TableColumn<Part, Integer> apartIDColumn;
    @FXML private TableColumn<Part, String> apartNameColumn;
    @FXML private TableColumn<Part, Integer> apartStockColumn;
    @FXML private TableColumn<Part, Double> apartPriceColumn;

    /**
     * First Initialization when the Product controller class is initialized
     */
    public ProductForm(){
        inventoryObj = new Inventory();
        this.selectedProduct = new Product(-1,"",0,0,0,0);
        this.partFilteredList.addAll(Inventory.getAllParts());

        Inventory.getAllParts().addListener(new ListChangeListener<Part>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Part> change) {
                updatePartFilteredData();
            }
        });
    }

    /**
     * Public method to pass inputs to controller class
     * @param selectedProduct Product object for when modifying the product
     */
    public void initData(Product selectedProduct){
        this.selectedProduct = selectedProduct;
        aPartTable.setItems(selectedProduct.getAllAssociatedParts());
        productIDField.setText(String.valueOf(selectedProduct.getId()));
        productNameField.setText(selectedProduct.getName());
        productInvField.setText(String.valueOf(selectedProduct.getStock()));
        productPriceField.setText(String.valueOf(selectedProduct.getPrice()));
        productMaxField.setText(String.valueOf(selectedProduct.getMax()));
        productMinField.setText(String.valueOf(selectedProduct.getMin()));

    }

    /**
     * second initialization. Runs after controller class constructor
     */
    @FXML
    public void initialize(){
        // Set All Part list column value types
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        allPartTable.setPlaceholder(new Label("(NO PART FOUND)"));

        // Add values to table based eligible parts
        allPartTable.setItems(Inventory.getAllParts());
        aPartTable.setItems(selectedProduct.getAllAssociatedParts());

        // Set Associated Part list column types
        apartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        apartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        apartStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        apartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        allPartTable.setItems(partFilteredList);

        // Listener for part finder text box
        partSearchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                updatePartFilteredData();
            }
        });
    }

    /**
     * Updates filtered table after text input on filter text box
     */
    private void updatePartFilteredData() {
        partFilteredList.clear();

        for (Part p : Inventory.getAllParts()) {
            if (matchesPartFilter(p)) {
                partFilteredList.add(p);
            }
        }
        // Must re-sort table after items changed
        reapplyPartTableSortOrder();
    }

    /**
     * Matches the eligible parts based on values in the text box
     * @param part an iterated part in the all parts table
     * @return A boolean value on whether part matches text filter
     */
    private boolean matchesPartFilter(Part part) {
        String filterString = partSearchField.getText();
        if (filterString == null || filterString.isEmpty()) {
            // No filter --> Add all.
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
     * Resort filter part list order
     */
    private void reapplyPartTableSortOrder() {
        ArrayList<TableColumn<Part, ?>> sortOrder = new ArrayList<>(allPartTable.getSortOrder());
        allPartTable.getSortOrder().clear();
        allPartTable.getSortOrder().addAll(sortOrder);
    }

    /**
     * Method to be ran when save button is pressed
     */
    @FXML
    private void saveInfo(){
        // Initialize product class input variables
        int ID;
        String Name = "";
        double Price = 0;
        int Stock = 0;
        int Max = 0;
        int Min = 0;

        boolean isError = false;
        String errorReason = "";

        // ID Field Input Validating
        try {
            // get the values written in the textboxes for number format error
            Name = productNameField.getText();
            Price = Double.parseDouble(productPriceField.getText());
            Stock = Integer.parseInt(productInvField.getText());
            Max = Integer.parseInt(productMaxField.getText());
            Min = Integer.parseInt(productMinField.getText());

            // Raise new errors for custom rules
            if (Max < Min){
                throw new Exception("Max is less than Min");
            }
        }
        catch (NumberFormatException e){
            errorReason = "An Input is Not Valid";
            isError = true;
        }
        catch (Exception e){
            errorReason = e.getMessage();
            isError = true;
        }

        // Creates an error alert box when there is an error
        if (isError){
            Alert errorBox = new Alert(Alert.AlertType.ERROR);
            errorBox.setTitle("Error Box");
            errorBox.setContentText(errorReason);
            errorBox.showAndWait();
        }
        else {

            // ask for confirmation for save
            Alert confirmBox = new Alert(Alert.AlertType.CONFIRMATION);
            confirmBox.setTitle("Confirm Box");
            confirmBox.setContentText("Would you like to save this product?");
            Optional<ButtonType> confirmResult = confirmBox.showAndWait();

            // If selectedProduct has an -1 ID, it is a new product. Create new product, generate new ID, and append to list
            if ((confirmResult.get() == ButtonType.OK) && (selectedProduct.getId() == -1)) {
                ID = Inventory.getNewID();
                selectedProduct.setId(ID);
                selectedProduct.setPrice(Price);
                selectedProduct.setStock(Stock);
                selectedProduct.setName(Name);
                selectedProduct.setMax(Max);
                selectedProduct.setMin(Min);
                Inventory.addProduct(selectedProduct);
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();

            // If selectedProduct does not have an -1 ID, it is a modifying product. Change existing product object values.
            } else if ((confirmResult.get() == ButtonType.OK) && (selectedProduct.getId() != -1)){
                ID = selectedProduct.getId();
                selectedProduct.setId(ID);
                selectedProduct.setPrice(Price);
                selectedProduct.setStock(Stock);
                selectedProduct.setName(Name);
                selectedProduct.setMax(Max);
                selectedProduct.setMin(Min);

                int productIndex = 0;
                for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
                    if (Inventory.getAllProducts().get(i).getId() == selectedProduct.getId()) {
                        productIndex = i;
                        break;
                    }
                }
                Inventory.updateProduct(productIndex, selectedProduct);
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
            }
        }
    }

    /**
     * Add new part to associated part list. Does not add if same ID already exists.
     */
    @FXML
    private void addAssociatedPart(){
        Part selectedPart = allPartTable.getSelectionModel().getSelectedItem();
        selectedProduct.addAssociatedPart(selectedPart);
    }

    /**
     * Removes an associated part from associated part list
     */
    @FXML
    private void removeAssociatedPart(){
        Part selectedPart = aPartTable.getSelectionModel().getSelectedItem();
        try{
            Alert confirmBox = new Alert(Alert.AlertType.CONFIRMATION);
            confirmBox.setTitle("Remove Confirmation");
            confirmBox.setContentText("Would you like to remove this associated part?");
            Optional<ButtonType> confirmResult = confirmBox.showAndWait();

            if (confirmResult.get() == ButtonType.OK) {
                selectedProduct.deleteAssociatedPart(selectedPart);
            }
        }
        catch (NullPointerException e){
            System.out.println("Select a Part");
        }
    }

    /**
     * Exits the app when clicked
     */
    @FXML
    private void exitApp(){
        // get a handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
