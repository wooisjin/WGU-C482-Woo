import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main Class to start the Inventory Program
 *
 * FUTURE ENHANCEMENT
 * 1. Saving Inventory data to disk
 * 2. Read Inventory data from disk
 * 3. Add more exceptions/errors to form inputs
 *      - Max, Min, Stock, and Price cannot be less than 0
 *      - Check Company Name in Part from Outsourced Company list
 * 4. Capability to add custom ID to Parts and Products
 *
 * @author Woo Jin An
 */
public class Main extends Application{

    /**
     * @param args main() method for the Inventory Program
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param primaryStage JavaFX Stage object inputted by JavaFX launch()
     * @throws Exception Throws Exception for FMLLoader
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Main Form Scene Initialization
        Scene mainScene;

        Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        mainScene = new Scene(root);

        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Main Form");
        primaryStage.show();
    }
}
