import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Optional;

/**
 * PartForm Alert box class opened from the MainForm controller
 * @author Woo Jin An
 */
public class PartForm {

    /**
     * static method to initialize and run all Part form functionalities
     * @param functionParam A String input 'ADD' OR 'MODIFY' that specifies whether the part form is adding or modifying a part
     * @param partObject The part object to be added to the Inventory when saved
     * @throws DisplayParameterException Custom exception thrown for displayPartForm invalid param
     */
    public static void displayPartForm(String functionParam, Part partObject) throws DisplayParameterException{
        if (!functionParam.equalsIgnoreCase("add") & !functionParam.equalsIgnoreCase("modify")){
            throw new DisplayParameterException("Function Parameter not 'ADD' OR 'MODIFY'", functionParam);
        }

        // Stage Parameters
        Stage PartStage = new Stage();
        PartStage.initModality(Modality.APPLICATION_MODAL);
        PartStage.setMinHeight(200);
        PartStage.setMinWidth(200);

        // Layout Parameters
        GridPane partGrid = new GridPane();
        partGrid.setPadding(new Insets(25, 25, 25, 25));
        partGrid.setHgap(10);
        partGrid.setVgap(15);

        // Inventory Object
        Inventory inventoryObj = new Inventory();

        // Part Form Labels
        Text functionPartLabel = new Text();
        Label partIDLabel = new Label("ID");
        Label partNameLabel = new Label("Name");
        Label partInvLabel = new Label("Inv");
        Label partPriceLabel = new Label("Price/Cost");
        Label partMaxLabel = new Label("Max");
        Label partMinLabel = new Label("Min");
        Label partIDorCompanyLabel = new Label();

        // Text Input Fields
        TextField partIDText = new TextField();
        partIDText.setEditable(false);
        TextField partNameText = new TextField();
        TextField partInvText = new TextField();
        TextField partPriceText = new TextField();
        TextField partMaxText = new TextField();
        TextField partMinText = new TextField();
        TextField partIDorCompanyText = new TextField();

        if (!partObject.getName().equals("") && partObject instanceof InHouse){
            partIDText.setText(String.valueOf(partObject.getId()));
            partNameText.setText(partObject.getName());
            partPriceText.setText(String.valueOf(partObject.getPrice()));
            partInvText.setText(String.valueOf(partObject.getStock()));
            partMinText.setText(String.valueOf(partObject.getMin()));
            partMaxText.setText(String.valueOf(partObject.getMax()));
            partIDorCompanyText.setText(String.valueOf(((InHouse) partObject).getMachineId()));

        }
        else if (!partObject.getName().equals("") && partObject instanceof Outsourced){
            partIDText.setText(String.valueOf(partObject.getId()));
            partNameText.setText(partObject.getName());
            partPriceText.setText(String.valueOf(partObject.getPrice()));
            partInvText.setText(String.valueOf(partObject.getStock()));
            partMinText.setText(String.valueOf(partObject.getMin()));
            partMaxText.setText(String.valueOf(partObject.getMax()));
            partIDorCompanyText.setText(((Outsourced) partObject).getCompanyName());
        }

        // Toggle Group for Source Radio Buttons
        ToggleGroup sourceToggle = new ToggleGroup();
        RadioButton insourcedRadio = new RadioButton();
        RadioButton outsourcedRadio = new RadioButton();
        insourcedRadio.setText("In-House");
        outsourcedRadio.setText("Outsourced");
        insourcedRadio.setToggleGroup(sourceToggle);
        outsourcedRadio.setToggleGroup(sourceToggle);

        sourceToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                RadioButton selectedToggle = (RadioButton) sourceToggle.getSelectedToggle();
                if (selectedToggle.getText().equals("In-House")) {
                    partIDorCompanyLabel.setText("Machine ID        ");
                }
                else if (selectedToggle.getText().equals("Outsourced")) {
                    partIDorCompanyLabel.setText("Company Name");
                }
            }
        });
        insourcedRadio.setSelected(true);

        // Save and Close Buttons
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> PartStage.close());

        // Save Button EventHandler<ActionEvents> to validate and save Inputs
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            RadioButton selectedToggle = (RadioButton) sourceToggle.getSelectedToggle();

            if (checkInputs(selectedToggle,
                partNameText.getText(),
                partInvText.getText(),
                partPriceText.getText(),
                partMaxText.getText(),
                partMinText.getText(),
                partIDorCompanyText.getText())){

                Alert confirmBox = new Alert(Alert.AlertType.CONFIRMATION);
                confirmBox.setTitle("Confirm Box");
                confirmBox.setContentText("Would you like to save this part?");
                Optional<ButtonType> confirmResult = confirmBox.showAndWait();

                if (confirmResult.get() == ButtonType.OK){

                    int partID = partObject.getId();
                    String partName = partNameText.getText();
                    int partInv = Integer.parseInt(partInvText.getText());
                    double partPrice = Double.parseDouble(partPriceText.getText());
                    int partMax = Integer.parseInt(partMaxText.getText());
                    int partMin = Integer.parseInt(partMinText.getText());
                    String partIDorCompany = partIDorCompanyText.getText();

                    if (selectedToggle.getText().equals("In-House")) {
                        if (partID == -1) {
                            partID = Inventory.getNewID();
                            int partMachineID = Integer.parseInt(partIDorCompany);
                            InHouse newPart = new InHouse(partID, partName, partPrice, partInv, partMin, partMax, partMachineID);
                            Inventory.addPart(newPart);
                        }
                        else{
                            int partMachineID = Integer.parseInt(partIDorCompany);
                            InHouse modPart = new InHouse(partID, partName, partPrice, partInv, partMin, partMax, partMachineID);

                            int modPartIndex = 0;
                            for (int i=0;i<Inventory.getAllParts().size();i++) {
                                if (Inventory.getAllParts().get(i).getId() == partID) {
                                    modPartIndex = i;
                                    break;
                                }
                            }
                            Inventory.updatePart(modPartIndex, modPart);
                        }
                    } else {
                        if (partID == -1) {
                            partID = Inventory.getNewID();
                            String partCompany = partIDorCompany;
                            Outsourced newPart = new Outsourced(partID, partName, partPrice, partInv, partMin, partMax, partCompany);
                            Inventory.addPart(newPart);
                        }
                        else{
                            String partCompany = partIDorCompany;
                            Outsourced modPart = new Outsourced(partID, partName, partPrice, partInv, partMin, partMax, partCompany);

                            int modPartIndex = 0;
                            for (int i=0;i<Inventory.getAllParts().size();i++) {
                                if (Inventory.getAllParts().get(i).getId() == partID) {
                                    modPartIndex = i;
                                    break;
                                }
                            }
                            Inventory.updatePart(modPartIndex, modPart);
                        }
                    }
                    Stage stage = (Stage) closeButton.getScene().getWindow();
                    stage.close();
                }



            }
        });

        // If add, change text labels to fit
        if (functionParam.equalsIgnoreCase("add")) {
            functionPartLabel.setText("Add Part");
            partIDText.setPromptText("Auto Gen - Enabled");
            partIDText.setMouseTransparent(true);
            partIDText.setFocusTraversable(false);
        }

        // If modify, change text labels to fit
        else if (functionParam.equalsIgnoreCase("modify")){
            // Default select In-house/Outsource radio button based on part instance
            if (partObject instanceof Outsourced){
                outsourcedRadio.setSelected(true);
            }
            functionPartLabel.setText("Modify Part");
            partIDText.setPromptText("Get Part ID");
            partIDText.setMouseTransparent(true);
            partIDText.setFocusTraversable(false);
        }
        functionPartLabel.setStyle("-fx-font-weight: bold");

        // Add Function Label and Radio Buttons
        partGrid.add(functionPartLabel,0,0);
        GridPane.setHalignment(insourcedRadio, HPos.RIGHT);
        partGrid.add(insourcedRadio, 1,0);
        partGrid.add(outsourcedRadio, 3,0);

        // Add Labels
        partGrid.add(partIDLabel, 0, 2);
        partGrid.add(partNameLabel, 0, 3);
        partGrid.add(partInvLabel, 0, 4);
        partGrid.add(partPriceLabel, 0, 5);
        partGrid.add(partMaxLabel, 0, 6);
        GridPane.setHalignment(partMinLabel, HPos.RIGHT);
        partGrid.add(partMinLabel, 2, 6);
        partGrid.add(partIDorCompanyLabel, 0, 7);

        // Add Text Forms
        partGrid.add(partIDText, 1, 2);
        partGrid.add(partNameText, 1, 3);
        partGrid.add(partInvText, 1, 4);
        partGrid.add(partPriceText, 1, 5);
        partGrid.add(partMaxText, 1, 6);
        partGrid.add(partMinText, 3, 6);
        partGrid.add(partIDorCompanyText, 1, 7);

        // Add Buttons
        partGrid.add(saveButton,2,9);
        partGrid.add(closeButton,3,9);

        Scene partScene = new Scene(partGrid, 550, 400);

        PartStage.setScene(partScene);
        PartStage.showAndWait();
    }

    /**
     * Check all inputs for type errors
     * @param selectedToggle selected Part type toggle object (In-House or Outsourced)
     * @param partNameText Part name text string
     * @param partInvText Part stock text string
     * @param partPriceText Part price text string
     * @param partMaxText Part max text string
     * @param partMinText Part min text string
     * @param partIDorCompanyText Part Machine ID or Company Name text string
     * @return boolean for whether the inputted strings in all text boxes are valid
     */
    static boolean checkInputs(RadioButton selectedToggle,
                            String partNameText,
                            String partInvText,
                            String partPriceText,
                            String partMaxText,
                            String partMinText,
                            String partIDorCompanyText){

        try{
            String partName = partNameText;
            int partInv = Integer.parseInt(partInvText);
            double partPrice = Double.parseDouble(partPriceText);
            int partMax = Integer.parseInt(partMaxText);
            int partMin = Integer.parseInt(partMinText);
            if (selectedToggle.getText().equals("In-House")) {
                int partIDorCompany = Integer.parseInt(partIDorCompanyText);
            }
            else{
                String partIDorCompany = partIDorCompanyText;
            }
            if (partMax < partMin){
                throw new DisplayParameterException("Max is less than Min");
            }
            return true;

        }catch (NumberFormatException e){
            Alert errorBox = new Alert(Alert.AlertType.ERROR);
            errorBox.setTitle("Error Box");
            errorBox.setContentText("An Input is not Valid");
            errorBox.showAndWait();
            return false;
        }catch (DisplayParameterException e){
            Alert errorBox = new Alert(Alert.AlertType.ERROR);
            errorBox.setTitle("Error Box");
            errorBox.setContentText(e.getMessage());
            errorBox.showAndWait();
            return false;

        }
    }
}
