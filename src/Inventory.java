import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory class which holds the static Part and Product List for modification and readability
 * @author Woo Jin An
 */
public class Inventory {
    private static ObservableList<Part> allParts;
    private static ObservableList<Product> allProducts;

    public Inventory(){
    }

    /**
     * Initialize parts and products observableList with FXCollections
     */
    public void initData(){
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
    }

    /**
     * Generate a new ID with the number after the highest ID in the Inventory
     * @return int ID
     */
    public static int getNewID(){
        int maxID = 1;
        if (allParts.size() != 0){
            for (int i=0;i<allParts.size();i++){
                if (allParts.get(i).getId() >= maxID){
                    maxID = allParts.get(i).getId()+1;
                }
            }
        }
        if (allProducts.size() != 0){
            for (int i=0;i<allProducts.size();i++){
                if (allProducts.get(i).getId() >= maxID){
                    maxID = allProducts.get(i).getId()+1;
                }
            }
        }
        return maxID;
    }

    /**
     * Add a part to the inventory
     * @param newPart A new Part
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Add a product to the inventory
     * @param newProduct A new Product
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * Get the part object based on the part ID
     * @param partId int partId of a part
     * @return Part object for matching partId. Returns part with id=0 if no part is found
     */
    public static Part lookupPart(int partId){
        Part returnPart = new InHouse(0, "",0.0,0,0,0, 0);
        for (int i=0;i<allParts.size();i++){
            if (allParts.get(i).getId() == partId){
                returnPart = allParts.get(i);
                break;
            }
        }
        return returnPart;
    }

    /**
     * Get the product object based on the product ID
     * @param productId int productId of a part
     * @return Product object for matching productID. Returns product with id=0 if no product is found
     */
    public static Product lookupProduct(int productId){
        Product returnProduct = new Product(0, "",0.0,0,0,0);
        for (int i=0;i<allProducts.size();i++){
            if (allProducts.get(i).getId() == productId){
                returnProduct = allProducts.get(i);
                break;
            }
        }
        return returnProduct;
    }

    /**
     * Get the part object based on the part name
     * @param partName String partName of a part
     * @return Part object for matching partName. Returns part with id=0 if no part is found
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> returnPartList = FXCollections.observableArrayList();
        for (int i=0;i<allParts.size();i++) {
            if (allParts.get(i).getName().equals(partName)) {
                returnPartList.add(allParts.get(i));
            }
        }
        return returnPartList;
    }

    /**
     * Get the product object based on the product name
     * @param productName String productName of a part
     * @return Product object for matching productName. Returns product with id=0 if no product is found
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> returnProductList = FXCollections.observableArrayList();
        for (int i=0;i<allProducts.size();i++) {
            if (allProducts.get(i).getName().equals(productName)) {
                returnProductList.add(allProducts.get(i));
            }
        }
        return returnProductList;
    }

    /**
     * Replace the part at the specified index with a new Part
     * @param index where the modifying part is placed in allParts
     * @param selectedPart A new part to replace the existing part
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * Replace the product at the specified index with a new Product
     * @param index where the modifying product is placed in allProducts
     * @param newProduct A new product to replace the existing product
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * Deletes the part in the inventory based on the part argument
     * @param selectedPart An existing part object to delete in allParts
     * @return boolean for whether the part was deleted successfully or not
     */
    public static boolean deletePart(Part selectedPart){
        boolean isDeleted = false;
        for(int i=0;i<allParts.size();i++){
            if (allParts.get(i).getId() == selectedPart.getId()){
                allParts.remove(i);
                isDeleted = true;
                break;
            }
        }
        return isDeleted;
    }

    /**
     * Deletes the product in the inventory based on the product argument
     * @param selectedProduct An existing product object to delete in allProducts
     * @return boolean for whether the product was deleted successfully or not
     */
    public static boolean deleteProduct(Product selectedProduct){
        boolean isDeleted = false;
        for(int i=0;i<allProducts.size();i++){
            if (allProducts.get(i).getId() == selectedProduct.getId()){
                allProducts.remove(i);
                isDeleted = true;
                break;
            }
        }
        return isDeleted;
    }

    /**
     * Get the ObservableList object for all parts in the inventory
     * @return an ObservableList of all parts
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Get the ObservableList object for all product in the inventory
     * @return an ObservableList of all products
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

}
