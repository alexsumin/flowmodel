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
    boolean isNewExistInChoiceBox = false;
    boolean isNew = false;

    public EditorDbController() {

    }

    public void setEditDatabaseStage(Stage stage) {
        this.editDatabaseStage = stage;
    }

    @FXML
    private void initialize() {

        changer = new ChangeMaterial();
        material = new Material();


        updateChoiceBox();


        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            choiceBox.getSelectionModel().select(newValue);
            if (choiceBox.getValue() == ADDNEW) {
                isNew = true;
                typeField.setText("Введите тип материала");
                clearData();


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
        typeField.setText("");
        for (TextField tf : fieldsMaterial) {
            tf.setText("");
        }


    }


    @FXML
    private void createNewEntry() {

        for (int i = 0; i < dataMaterial.length; i++) {
            dataMaterial[i] = Double.parseDouble(fieldsMaterial.get(i).getText());
            System.out.println(dataMaterial[i]);

        }

        changer.setData(typeField.getText(), changer.getMaxIdMaterialFromDatabase(), dataMaterial);
        changer.insertDbRecords();
        isNew = false;
        isNewExistInChoiceBox = false;


    }


    @FXML
    private void updateEntry() {
        if (isNew) {
            createNewEntry();
        } else {
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

        }
        updateChoiceBox();
        clearData();


    }


    @FXML
    private void deleteEntry() {
        changer.setIdMaterial(choiceBox.getValue().getId());
        changer.deleteDbRecords();
        clearData();
        updateChoiceBox();

    }


    private void updateChoiceBox() {
        choiceBox.getItems().clear();
        material = new Material();

        choiceBox.setItems(FXCollections.observableArrayList(material.getMaterialsFromDatabase()));
        //if (!isNewExistInChoiceBox)
        {

            choiceBox.getItems().add(ADDNEW);
            isNewExistInChoiceBox = true;
        }
    }

}