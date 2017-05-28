package ru.alexsumin.view;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import ru.alexsumin.Main;
import ru.alexsumin.model.ChangeMaterial;
import ru.alexsumin.model.IdTypePair;
import ru.alexsumin.model.Material;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    private FileChooser fileChooser = new FileChooser();

    private Stage editDatabaseStage;

    private String type;
    ChangeMaterial changer;
    Material material;
    boolean isNew = false;
    Main main = new Main();
    ModelOverviewController contrlr = new ModelOverviewController();

    Stage prevStage;

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }

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
        choiceBox.getItems().add(ADDNEW);


    }

    @FXML
    private void restoreDatabase(final ActionEvent event) {
        File src = new File("src/main/resources/database/Default/Material_Database.s3db");
        File target = new File("src/main/resources/database/Material_Database.s3db");


        try {
            Files.copy(src.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успешно");
            alert.setHeaderText("База данных успешно восстановлена!");
            alert.showAndWait();
            material = new Material();
            updateChoiceBox();
            changer = new ChangeMaterial();
            return;
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось восстановить базу данных!");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    private void copyDatabase(final ActionEvent event) {
        File src = new File("src/main/resources/database/Material_Database.s3db");

        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("SQLite база данных", "*.s3db");
        FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("Все файлы", "*.*");
        fileChooser.getExtensionFilters().addAll(filter1, filter2);

        File file = fileChooser.showSaveDialog(null);

        if (file == null) return;

        try {
            Files.copy(src.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успешно");
            alert.setHeaderText("База данных успешно экспортирована!");
            alert.showAndWait();
            return;
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось сделать резервную копию");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    private void importDatabase(final ActionEvent e) {
        File target = new File("src/main/resources/database/Material_Database.s3db");

        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("SQLite база данных", "*.s3db");
        FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("Все файлы", "*.*");
        fileChooser.getExtensionFilters().addAll(filter1, filter2);

        File file = fileChooser.showSaveDialog(null);

        if (file == null) return;

        try {
            Files.copy(file.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успешно");
            alert.setHeaderText("База данных успешно импортирована!");
            alert.showAndWait();
            material = new Material();
            updateChoiceBox();
            changer = new ChangeMaterial();
            return;
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось восстановить базу!");
            alert.showAndWait();
            return;
        }

    }


    @FXML
    private void openChangePasswordDialog(final ActionEvent event) {


        Dialog dialog = new Dialog();

        ButtonType changeButtonType = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 70, 10, 10));

        PasswordField oldpass = new PasswordField();
        oldpass.setPromptText("Пароль");
        PasswordField newpass = new PasswordField();
        newpass.setPromptText("Новый пароль");

        grid.add(new Label("Пароль:"), 0, 0);
        grid.add(oldpass, 1, 0);
        grid.add(new Label("Новый пароль:"), 0, 1);
        grid.add(newpass, 1, 1);


        Node changeButton = dialog.getDialogPane().lookupButton(changeButtonType);
        changeButton.setDisable(true);

        oldpass.textProperty().addListener((observable, oldValue, newValue) -> {
            changeButton.setDisable(newValue.trim().isEmpty());
            if (newValue.equals(Main.USER_PASSWORD)) {
                newpass.setEditable(true);
            }
        });


        dialog.getDialogPane().setContent(grid);
        dialog.show();


        changeButton.addEventFilter(ActionEvent.ACTION, ev -> {
            if (!newpass.getText().isEmpty()) {
                String tempPass = newpass.getText();
                System.out.println("считан пароль");
                File sourceFile = new File("src/main/resources/temp");
                File fileToCopy = new File(Main.CONFIG_FILE);

                try {
                    FileUtils.writeStringToFile(sourceFile, tempPass, "UTF8");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    FileUtils.copyFile(sourceFile, fileToCopy);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Main.USER_PASSWORD = tempPass;
                org.apache.commons.io.FileUtils.deleteQuietly(sourceFile);
            }

        });

    }

    @FXML
    private void openChangeUserDialog(final ActionEvent e) {
        if (!main.openLoginDialog()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка авторизации!");
            alert.setContentText("Вы ввели неверные данные пользователя!");

            alert.showAndWait();


        }
        try {
            main.loadUserScene(new Stage());
            prevStage.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }


    @FXML
    private void focusOnDensityField(final ActionEvent e) {
        densityField.requestFocus();

    }

    @FXML
    private void focusOnCapacityField(final ActionEvent e) {
        capacityField.requestFocus();

    }

    @FXML
    private void focusOnMeltingTemperatureField(final ActionEvent e) {
        meltingTemperatureField.requestFocus();

    }

    @FXML
    private void focusOnViscosityFactorField(final ActionEvent e) {
        viscosityFactorField.requestFocus();

    }

    @FXML
    private void focusOnReductionTemperatureField(final ActionEvent e) {
        reductionTemperatureField.requestFocus();

    }

    @FXML
    private void focusOnIndexOfMaterialField(final ActionEvent e) {
        indexOfMaterialField.requestFocus();

    }

    @FXML
    private void focusOnEmissionFactorField(final ActionEvent e) {
        emissionFactorField.requestFocus();

    }

    @FXML
    private void focusOnConsFactorWithReductionField(final ActionEvent e) {
        consFactorWithReductionField.requestFocus();

    }
}