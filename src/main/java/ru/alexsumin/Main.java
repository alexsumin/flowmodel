package ru.alexsumin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Created by alex on 15.03.17.
 */
public class Main extends Application {

    private final String USER_LOGIN = "admin",
            USER_PASSWORD = "password";
    private boolean isAdmin;

    public static void main(String[] args) throws Exception {

        launch(args);
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

    private boolean openLoginDialog() {

        Dialog<LoginData> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Вход пользователя");


        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);


        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new LoginData(username.getText(), password.getText());
            } else {
                System.exit(0);
            }
            return null;
        });

        Optional<LoginData> result = dialog.showAndWait();

        LoginData ld = result.get();


        if (!(ld.login.equals(USER_LOGIN) && ld.password.equals(USER_PASSWORD))) return false;

        return true;
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
