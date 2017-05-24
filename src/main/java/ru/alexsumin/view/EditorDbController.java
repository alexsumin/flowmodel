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

//    @FXML
//    private void changePassword(){
//
//
//        Dialog<String> dialog = new Dialog<>();
//        dialog.setTitle("Change password dialog");
//        dialog.setHeaderText("Смена пароля");
//
//
//        ButtonType changeButton= new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
//
//        dialog.getDialogPane().getButtonTypes().addAll(changeButton);
//        //dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
//
//
//
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//        PasswordField oldPassword = new PasswordField();
//        oldPassword.setPromptText("Password");
//        oldPassword.setTooltip(new Tooltip("Введите старый пароль"));
//        Label lbl = new Label("Старый пароль");
//        lbl.setVisible(false);
//
////        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
////        loginButton.setDisable(true);
//
//
//        dialog.getDialogPane().getButtonTypes().addAll(changeButton);
//        //dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
//
//
//
//
//
//
//        dialog.getDialogPane().setContent(grid);
//
//
//
//        File file = new File("src/main/resources/", "config");
//        try {
//            try(BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/config"))){
//                USER_PASSWORD = reader.readLine();
//            }
//        } catch (IOException e) {
//            USER_PASSWORD = "password";
//            try {
//                try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/config"))) {
//                    try {
//                        writer.write(USER_PASSWORD);
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        } ;
//
//    }


//    @FXML
//    private void openChangePasswordDialog() {
//        //final PasswordPolicy passwordPolicy;
//
//        Dialog<String> dialog = new Dialog<>();
//        dialog.setTitle("Change password dialog");
//        dialog.setHeaderText("Смена пароля");
//
//
//        dialog.showAndWait();
//
//        final PasswordField oldPasswordField = new PasswordField();
//        final PasswordField newPasswordField = new PasswordField();
//        final PasswordField confirmPasswordField = new PasswordField();
//        final Label errorLabel = new Label();
//        final Label guidelineLabel;
//
//        final DialogPane dialogPane = dialog.getDialogPane();
//        dialogPane.getButtonTypes().addAll(
//                ButtonType.CANCEL, ButtonType.OK
//        );
//
//
//        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
//
//
//        VBox guidelineHolder = new VBox();
//
//        oldPasswordField.setPrefWidth(300);
//        newPasswordField.setPrefWidth(300);
//        confirmPasswordField.setPrefWidth(300);
//
//        Platform.runLater(oldPasswordField::requestFocus);
//
//        GridPane gridPane = new GridPane();
//        gridPane.setHgap(10);
//        gridPane.setVgap(10);
//        gridPane.addRow(0,new
//
//        Label("Old Password"),oldPasswordField);
//        gridPane.addRow(1,new
//
//        Label("New Password"),newPasswordField);
//        gridPane.addRow(2,new
//
//        Label("Confirm New Password"),confirmPasswordField);
//        gridPane.addRow(3,errorLabel);
//        gridPane.addRow(4,guidelineHolder);
//        GridPane.setColumnSpan(errorLabel,2);
//        GridPane.setColumnSpan(guidelineHolder,2);
//        guidelineLabel = new Label();
//
//    //this.passwordPolicy = passwordPolicy;
//
//        errorLabel.setStyle("-fx-text-fill: firebrick");
//        Hyperlink showGuidelines = new Hyperlink("Show password guidelines");
//        guidelineHolder.getChildren().add(showGuidelines);
//        showGuidelines.setOnAction(event -> {
//            if (!guidelineHolder.getChildren().contains(guidelineLabel)) {
//                showGuidelines.setText("Hide password guidelines");
//                guidelineHolder.getChildren().add(
//                        guidelineLabel
//                );
//            } else {
//                showGuidelines.setText("Show password guidelines");
//                guidelineHolder.getChildren().remove(
//                        guidelineLabel
//                );
//            }
//
//            guidelineHolder.getScene().getWindow().sizeToScene();
//        });
//
//        dialogPane.setContent(
//                gridPane
//        );
//
//
//
//    }


    @FXML
    private void openChangePasswordDialog(final ActionEvent event) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Login Dialog");


// Set the button types.
        ButtonType loginButtonType = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        PasswordField oldpass = new PasswordField();
        oldpass.setPromptText("Username");
        PasswordField newpass = new PasswordField();
        newpass.setPromptText("Password");
        PasswordField confirmpass = new PasswordField();

        grid.add(new Label("Старый пароль:"), 0, 0);
        grid.add(oldpass, 1, 0);
        grid.add(new Label("Новый пароль:"), 0, 1);
        grid.add(newpass, 1, 1);
        grid.add(new Label("Подтвердите пароль:"), 0, 2);
        grid.add(confirmpass, 1, 2);


// Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
        oldpass.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
            if (newValue == Main.USER_PASSWORD) {

            }
        });


        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();

// Request focus on the username field by default.
        //Platform.runLater(() -> username.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
//        dialog.setResultConverter(dialogButton -> {
//            if (dialogButton == loginButtonType) {
//                return new Pair<>(username.getText(), password.getText());
//            }
//            return null;
//        });
//
//        Optional<Pair<String, String>> result = dialog.showAndWait();
//
//        result.ifPresent(usernamePassword -> {
//            System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
//        });

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