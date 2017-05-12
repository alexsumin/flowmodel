package ru.alexsumin;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by alex on 15.03.17.
 */
public class Main extends Application {

    private final String USER_PASSWORD = "password";
    private static boolean isAdmin;

    public static boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        if (!openLoginDialog()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка авторизации!");
            alert.setContentText("Вы ввели неверные данные пользователя! ");

            alert.showAndWait();

            return;
        }

        Parent root = FXMLLoader.load(getClass().getResource("/view/ModelOverview.fxml"));
        primaryStage.setTitle("Flowmodel");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
    }

    public boolean openLoginDialog() {

        Dialog<LoginData> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Вход пользователя");


        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setVisible(false);
        password.setTooltip(new Tooltip("Введите пароль"));
        Label lbl = new Label("Password:");
        lbl.setVisible(false);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);


        final ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("Исследователь", "Администратор"));
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.equals(0)) {
                    lbl.setVisible(false);
                    password.setVisible(false);
                    loginButton.setDisable(false);
                } else {
                    loginButton.setDisable(true);
                    lbl.setVisible(true);
                    password.setVisible(true);
                    password.textProperty().addListener((ObservableValue<? extends String> obv, String oldVal, String newVal) -> {
                        loginButton.setDisable(newVal.trim().isEmpty());
                    });
                }


            }
        });
        cb.setTooltip(new Tooltip("Выберите категорию пользователя"));
        cb.setValue("Исследователь");


        grid.add(new Label("Username:"), 0, 0);
        grid.add(cb, 1, 0);
        grid.add(lbl, 0, 1);
        grid.add(password, 1, 1);


        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new LoginData((String) cb.getValue(), password.getText());
            } else {
                System.exit(0);
            }
            return null;
        });

        Optional<LoginData> result = dialog.showAndWait();

        LoginData ld = result.get();


        if (ld.login.equals("Исследователь")) return true;
        else if ((ld.login == "Администратор") && ld.password.equals(USER_PASSWORD)) {
            isAdmin = true;
            return true;
        }

        return false;
    }


    public void showDatabaseEditDialog(Stage primaryStage) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/view/EditorDb.fxml"));
            primaryStage.setTitle("Редактирование базы данных");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class LoginData {
    public String login;
    public String password;


    public LoginData(String login, String password) {
        this.login = login;
        this.password = password;
    }
}