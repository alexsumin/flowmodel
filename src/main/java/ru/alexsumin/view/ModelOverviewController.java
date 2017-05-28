package ru.alexsumin.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.alexsumin.Main;
import ru.alexsumin.model.Data;
import ru.alexsumin.model.IdTypePair;
import ru.alexsumin.model.Material;
import ru.alexsumin.model.Result;
import ru.alexsumin.util.ReportGenerator;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.UnaryOperator;

/**
 * Created by alex on 15.03.17.
 */
public class ModelOverviewController {
    static final String STEP_VALUE = "0.1";
    @FXML
    TableView<Result> tableWithResult;

    static UnaryOperator<TextFormatter.Change> filter = t -> {
        DecimalFormatSymbols decimal = new DecimalFormatSymbols(Locale.getDefault());
        String sep = String.valueOf(decimal.getDecimalSeparator());

        if (t.isReplaced())
            if (t.getText().matches("[^0-9]"))
                t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));


        if (t.isAdded()) {
            if (t.getControlText().contains(sep)) {
                if (t.getText().matches("[^0-9]")) {
                    t.setText("");
                }
            } else if (t.getText().matches("[^0-9.]")) {
                t.setText("");
            }
        }

        return t;
    };
    @FXML
    EventHandler<KeyEvent> enterKeyEventHandler;
    @FXML
    private LineChart<Number, Number> viscosityChart;
    @FXML
    private LineChart<Number, Number> temperChart;
    @FXML
    private TableColumn<Result, Number> stepColumn;
    @FXML
    private TableColumn<Result, Number> temperatureColumn;
    @FXML
    private TableColumn<Result, Number> viscosityColumn;
    @FXML
    private Spinner stepField = new Spinner(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 10.6,
            Double.parseDouble(STEP_VALUE), 0.1));
    @FXML
    private TextField widthField, lengthField, depthField;
    @FXML
    private TextField densityField, capacityField, meltingTemperatureField;
    @FXML
    private TextField speedCoverField, coverTemperatureField, viscosityFactorField;
    @FXML
    private TextField reductionTemperatureField, indexOfMaterialField, emissionFactorField, consFactorWithReductionField;
    @FXML
    private TextField performField, lastTemperField, lastViscField;
    @FXML
    private ArrayList<TextField> fields, fieldsMaterial;
    @FXML
    private Button calculateButton, reportButton, editButton;
    @FXML
    private Label timeCalculate;
    @FXML
    private VBox vbox;
    private FileChooser fileChooser = new FileChooser();
    private ReportGenerator report = new ReportGenerator();
    @FXML
    private Button saveTemperChart, saveViscosChart;
    @FXML
    private NumberAxis xAis1, xAxis2;
    @FXML
    private NumberAxis yAxis1, yAxis2;
    private boolean isCalculated = false;
    private Data dt;
    private Main main = new Main();
    private ObservableList<Result> results = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<IdTypePair> choiceBox = new ChoiceBox<>();
    double[] dataMaterial = new double[8];
    Material material;
    public Stage stage;


    Stage prevStage;

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }


    public ModelOverviewController() {
    }

    public void refreshChoiceBox() {
        choiceBox.setItems(FXCollections.observableArrayList(material.getMaterialsFromDatabase()));
    }

    @FXML
    private void initialize() {
        stepField.setEditable(false);
        enterSpinner();


        material = new Material();

        refreshChoiceBox();



        choiceBox.getSelectionModel().selectFirst();


        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            choiceBox.getSelectionModel().select(newValue);
            material.setIdMaterial(choiceBox.getValue().getId());
            dataMaterial = material.getMaterialData();
            updateData();
        });


        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("Документы Microsoft Office Word", "*.docx");
        FileChooser.ExtensionFilter filter3 = new FileChooser.ExtensionFilter("Файлы PNG", "*.png");
        FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("Все файлы", "*.*");
        fileChooser.getExtensionFilters().addAll(filter1, filter2, filter3);


        initColumn();


        tableWithResult.setEditable(true);
        tableWithResult.setItems(results);



        for (TextField tf : fields) {
            createDefenceFromStupid(tf);

        }

        //createDefenceFromStupid(stepField, Double.parseDouble(lengthField.getText()));
        stepField.setTooltip(new Tooltip("Введите величину, не большую " + Double.parseDouble(lengthField.getText())));
        timeCalculate.setVisible(false);

        calculateButton.setTooltip(new Tooltip("Нажмите, чтобы произвести расчёт"));
        reportButton.setTooltip(new Tooltip("Нажмите, чтобы сгенерировать отчёт"));

        yAxis1.setAutoRanging(false);
        yAxis2.setAutoRanging(false);

        temperChart.getXAxis().setLabel("Координата по длине канала, м");
        temperChart.getYAxis().setLabel("Температура, °С");
        viscosityChart.getXAxis().setLabel("Координата по длине канала, м");
        viscosityChart.getYAxis().setLabel("Вязкость, Па∙с");


        if (Main.isAdmin()) {

        }


    }

    private void updateData() {
        for (int i = 0; i < fieldsMaterial.size(); i++) {
            fieldsMaterial.get(i).setText(String.valueOf(dataMaterial[i]));
        }
    }


    static void createDefenceFromStupid(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(filter));
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() || (newValue.equals("0.0")) || (newValue.equals("0")) || (newValue.equals("0."))) {
                textField.setText(oldValue);
            } else {
                try {
                    double value = Double.parseDouble(newValue);
                    textField.setText("" + value);

                } catch (NumberFormatException e) {
                    textField.setText(oldValue);
                }
            }
        });
    }



    @FXML
    private void initColumn() {
        stepColumn.setCellValueFactory(cellData -> cellData.getValue().stepProperty());
        temperatureColumn.setCellValueFactory(cellData -> cellData.getValue().temperatureProperty());
        viscosityColumn.setCellValueFactory(cellData -> cellData.getValue().viscosityProperty());
    }

    @FXML
    public void calculate(final ActionEvent event) {
        long startTime = System.currentTimeMillis();

        double usersData[] = new double[fields.size()];
        double step = Double.valueOf(stepField.getEditor().getText().replace(",", "."));

        for (int i = 0; i < usersData.length; i++) {
            usersData[i] = Double.parseDouble(fields.get(i).getText());
            System.out.println(usersData[i]);

        }


        dt = new Data(step, usersData);
        results = FXCollections.observableArrayList(dt.getResults());
        tableWithResult.setItems(results);
        performField.setText(String.valueOf((int) dt.getCanalPerformance()));
        lastTemperField.setText(String.valueOf(dt.getCurrentTemperature()));
        lastViscField.setText(String.valueOf((int) dt.getCurrentViscosity()));
        timeCalculate.setVisible(true);
        long totalTime = System.currentTimeMillis() - startTime;
        timeCalculate.setText("Время расчета: " + totalTime + "мс");
        isCalculated = true;


        temperChart.getData().clear();
        viscosityChart.getData().clear();
        drawTemperatureChart();
        drawViscosityChart();

        yAxis2.setUpperBound(Math.rint(results.get(0).getViscosity() * 1.03));
        yAxis2.setLowerBound(Math.rint(results.get(results.size() - 1).getViscosity() * 0.97));
        yAxis2.setTickUnit(Math.rint((yAxis2.getUpperBound() - yAxis2.getLowerBound()) / 10));

        yAxis1.setLowerBound(Math.rint(results.get(0).getTemperature() * 0.97));
        yAxis1.setUpperBound(Math.rint(results.get(results.size() - 1).getTemperature() * 1.03));
        yAxis1.setTickUnit(Math.rint((yAxis1.getUpperBound() - yAxis1.getLowerBound()) / 10));


    }

    private void plot(XYChart.Data[] points, LineChart lineChart) {
        XYChart.Series<Double, Double> series = new XYChart.Series();

        series.getData().addAll(points);
        lineChart.getData().addAll(series);
        lineChart.setLegendVisible(false);


        lineChart.setStyle(".default-color0.chart-series-line { -fx-stroke: #MAGENTA; }");


        for (XYChart.Data<Double, Double> s : series.getData()) {
            s.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                Tooltip tooltip = new Tooltip("x: " + s.getXValue() + "\ny: " + s.getYValue());
                Tooltip.install(s.getNode(), tooltip);

            });
        }
        points = null;

    }


    public void drawTemperatureChart() {
        int len = results.size();
        XYChart.Data[] resultsTemper = new XYChart.Data[len];
        Result tmp;
        for (int i = 0; i < len; i++) {
            tmp = results.get(i);
            resultsTemper[i] = new XYChart.Data(tmp.getStep(), tmp.getTemperature());
        }
        plot(resultsTemper, temperChart);


    }

    public void drawViscosityChart() {
        int len = results.size();
        XYChart.Data[] resultsTemper = new XYChart.Data[len];
        Result tmp;
        for (int i = 0; i < len; i++) {
            tmp = results.get(i);
            resultsTemper[i] = new XYChart.Data(tmp.getStep(), tmp.getViscosity());
        }
        plot(resultsTemper, viscosityChart);

    }

    public void clearTemperatureChart(final ActionEvent event) {
        temperChart.getData().clear();
    }

    public void clearViscosityChart(final ActionEvent event) {
        viscosityChart.getData().clear();
    }


    @FXML
    public void generateReport(final ActionEvent event) {


        if (isCalculated) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Microsoft Office Word (*.docx)", "*.docx");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {


                WritableImage image1 = temperChart.snapshot(new SnapshotParameters(), null);
                WritableImage image2 = viscosityChart.snapshot(new SnapshotParameters(), null);

                String namePic1 = "temperChart.png";
                String namePic2 = "viscosChart.png";

                File pic1 = new File(namePic1);
                File pic2 = new File(namePic2);
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image1, null),
                            "png", pic1);
                    ImageIO.write(SwingFXUtils.fromFXImage(image2, null),
                            "png", pic2);

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }


                report.setValues(dt.getValues());
                report.setListOfResults(results);
                report.setPics(namePic1, namePic2);
                report.setType(choiceBox.getValue().toString());
                report.create(file);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Отчёт");
                alert.setHeaderText("Отчёт успешно сохранён.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Предупреждение");
                alert.setHeaderText("Не указан путь для сохранения файла.\nОтчёт не был сохранён");
                alert.showAndWait();
                return;
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Сперва необходимо произвести расчёт!");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    private void saveTemperImage(ActionEvent event) {

        File file = fileChooser.showSaveDialog(null);

        if (file == null) return;

        WritableImage image = temperChart.snapshot(new SnapshotParameters(), null);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось сохранить изображение.");
            alert.showAndWait();
            return;
        }

    }


    private void enterSpinner() {
        enterKeyEventHandler = event -> {

            // handle users "enter key event"
            if (event.getCode() == KeyCode.ENTER) {

                try {
                    // yes, using exception for control is a bad solution ;-)
                    Double.valueOf(stepField.getEditor().textProperty().get().replace(",", "."));
                } catch (NumberFormatException e) {

                    // show message to user: "only numbers allowed"

                    // reset editor to INITIAL_VALUE
                    stepField.getEditor().textProperty().set(STEP_VALUE);
                }
            }
        };

        // note: use KeyEvent.KEY_PRESSED, because KeyEvent.KEY_TYPED is to late, spinners
        // SpinnerValueFactory reached new value before key released an SpinnerValueFactory will
        // throw an exception
        stepField.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, enterKeyEventHandler);
    }

    @FXML
    private void saveViscImage(ActionEvent event) {

        File file = fileChooser.showSaveDialog(null);

        if (file == null) return;

        WritableImage image = viscosityChart.snapshot(new SnapshotParameters(), null);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось сохранить изображение.");
            alert.showAndWait();
            return;
        }

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

        if (Main.isAdmin()) {

            try {
                main.initScene(new Stage());
                prevStage.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }


    }


    @FXML
    private void openAboutWindow(final ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setGraphic((Node) null);
        alert.setHeaderText("                                            FLOWMODEL" + "\n" +
                "Выполнили: студенты группы № 444: " +
                "\n                                            Анпилова А.В., " +
                "\n                                            Демьяненко А.В., \n" +
                "                                            Сумин А.П. \n" +
                        "Руководитель: кандидат технических наук, доцент кафедры САПРиУ СПбГТИ(ТУ) " +
                "\n                                    Полосин Андрей Николаевич\n\n" +
                        "                                            СПбГТИ(ТУ), 2017");
        alert.showAndWait();
    }


    @FXML
    private void exitProgram(final ActionEvent e) {
        System.exit(0);
    }

    @FXML
    private void openEditor(final ActionEvent event) {
        if (main.isAdmin()) {
            main.showDatabaseEditDialog(new Stage());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Недостаточно прав");
            alert.setContentText("Доступно только администратору!");

            alert.showAndWait();

        }
    }


    //HORRIBLE SOLUTION! TODO: use enum or something else!
    @FXML
    private void focusOnWidthField(final ActionEvent e) {
        widthField.requestFocus();

    }

    @FXML
    private void focusOnLengthField(final ActionEvent e) {
        lengthField.requestFocus();

    }

    @FXML
    private void focusOnDepthField(final ActionEvent e) {
        depthField.requestFocus();

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
    private void focusOnSpeedCoverField(final ActionEvent e) {
        speedCoverField.requestFocus();

    }

    @FXML
    private void focusOnCoverTemperatureField(final ActionEvent e) {
        coverTemperatureField.requestFocus();

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





