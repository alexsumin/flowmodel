package ru.alexsumin.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.alexsumin.model.ChangeMaterial;
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
    private TextField typeField, densityField, capacityField, meltingTemperatureField;
    @FXML
    private TextField consFactorWithReductionField, viscosityFactorField, reductionTemperatureField, indexOfMaterialField, emissionFactorField;
    double[] dataMaterial = new double[8];
    @FXML
    private ArrayList<TextField> fieldsMaterial;
    static final IdTypePair ADDNEW = new IdTypePair(-1, "Новая запись");

    private Stage editDatabaseStage;

    private String type;
    ChangeMaterial changer;
    Material material;
    boolean isNewExist = false;

    public EditorDbController() {

    }

    public void setEditDatabaseStage(Stage stage) {
        this.editDatabaseStage = stage;
    }

    @FXML
    private void initialize() {

        changer = new ChangeMaterial();

        material = new Material();

//        for (TextField tf : fieldsMaterial) {
//            ModelOverviewController.createDefenceFromStupid(tf);
//
//        }


        isNewExist = false;
        replaceChoiceBox();

//        choiceBox.setItems(FXCollections.observableArrayList(material.getMaterialsFromDatabase()));
//        boolean isNewExist = false;
//        if (!isNewExist) {
//
//            choiceBox.getItems().add(ADDNEW);
//            isNewExist = true;
//        }


        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            choiceBox.getSelectionModel().select(newValue);
            if (choiceBox.getValue() == ADDNEW) {
                createNewEntry();

            }
            if (choiceBox.getValue() == null) {

            } else {
                material.setIdMaterial(choiceBox.getValue().getId());
                dataMaterial = material.getMaterialData();
                typeField.setText(choiceBox.getValue().toString());
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
            tf.setText("");
        }
        typeField.setText("");

    }

    @FXML
    private void createNewEntry() {
        clearData();
        typeField.setText("Введите тип материала");

        for (int i = 0; i < dataMaterial.length; i++) {
            dataMaterial[i] = Double.parseDouble(fieldsMaterial.get(i).getText());
            System.out.println(dataMaterial[i]);

        }


    }

    private void updateChoiceBox() {
        choiceBox.setItems(null);
        choiceBox.setItems(FXCollections.observableArrayList(material.getMaterialsFromDatabase()));

        if (!isNewExist) {

            choiceBox.getItems().add(ADDNEW);
            isNewExist = true;
        }


    }

    @FXML
    private void updateEntry() {
        type = typeField.getText();
        System.out.println(type);
        int id = choiceBox.getValue().getId();
        System.out.println(id);

        for (int i = 0; i < dataMaterial.length; i++) {
            dataMaterial[i] = Double.parseDouble(fieldsMaterial.get(i).getText());
            System.out.println(dataMaterial[i]);

        }


        changer.setData(type, id, dataMaterial);

        changer.updateDbRecords();


        replaceChoiceBox();
        clearData();


    }


    @FXML
    private void deleteEntry() {

        //updateChoiceBox();

    }


    private void replaceChoiceBox() {
        choiceBox.getItems().clear();
        material = new Material();

        choiceBox.setItems(FXCollections.observableArrayList(material.getMaterialsFromDatabase()));
        if (!isNewExist) {

            choiceBox.getItems().add(ADDNEW);
            isNewExist = true;
        }
    }

}