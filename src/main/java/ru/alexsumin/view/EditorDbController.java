package ru.alexsumin.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.alexsumin.model.IdTypePair;
import ru.alexsumin.model.Material;

import java.util.ArrayList;


/**
 * Created by alex on 11.05.17.
 */
public class EditorDbController {

    @FXML
    Button updateButton, deleteButton;
    @FXML
    private ChoiceBox<IdTypePair> choiceBox = new ChoiceBox<>();
    @FXML
    private TextField densityField, capacityField, meltingTemperatureField;
    @FXML
    private TextField consFactorWithReductionField, viscosityFactorField, reductionTemperatureField, indexOfMaterialField, emissionFactorField;
    double[] dataMaterial = new double[8];
    @FXML
    private ArrayList<TextField> fieldsMaterial;
    static final IdTypePair ADDNEW = new IdTypePair(-1, "Новая запись");

    private Stage editDatabaseStage;

    public EditorDbController() {

    }

    public void setEditDatabaseStage(Stage stage) {
        this.editDatabaseStage = stage;
    }

    @FXML
    private void initialize() {

        Material material = new Material();

//        for (TextField tf : fieldsMaterial) {
//            ModelOverviewController.createDefenceFromStupid(tf);
//
//        }


        choiceBox.setItems(FXCollections.observableArrayList(material.getMaterialsFromDatabase()));

        boolean isNewExist = false;
        if (!isNewExist) {

            choiceBox.getItems().add(ADDNEW);
        }

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            choiceBox.getSelectionModel().select(newValue);
            if (choiceBox.getValue() == ADDNEW) {
                System.out.println("что-то происходит вообще?");
                clearData();

            } else {
                material.setIdMaterial(choiceBox.getValue().getId());
                dataMaterial = material.getMaterialData();
                updateData();
            }


        });

    }


    private void updateData() {
        for (int i = 0; i < fieldsMaterial.size(); i++) {
            fieldsMaterial.get(i).setText(String.valueOf(dataMaterial[i]));
        }
    }

    private void clearData() {
        for (TextField tf : fieldsMaterial) {
            tf.setText("0");
        }

    }

    @FXML
    private void createNewEntry() {


    }

    @FXML
    private void updateEntry() {

    }

    @FXML
    private void deleteEntry() {

    }

}
