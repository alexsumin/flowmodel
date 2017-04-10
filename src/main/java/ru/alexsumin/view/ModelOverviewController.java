package ru.alexsumin.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import ru.alexsumin.Main;
import ru.alexsumin.model.Data;
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
    @FXML
    TableView<Result> tableWithResult;
    UnaryOperator<TextFormatter.Change> filter = t -> {
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
    Label lbl;
    Data dt;
    private ObservableList<Result> results = FXCollections.observableArrayList();
    @FXML
    private LineChart<Number, Number> viscosityChart;
    @FXML
    private LineChart<Number, Number> tempChart;
    @FXML
    private TableColumn<Result, Number> stepColumn;
    @FXML
    private TableColumn<Result, Number> temperatureColumn;
    @FXML
    private TableColumn<Result, Number> viscosityColumn;
    @FXML
    private TextField stepField;
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
    private ArrayList<TextField> fields;
    @FXML
    private Button runChartTemp, clearTempChart, clearViscChart, runChartViscos;
    @FXML
    private Button reportButton;
    @FXML
    private Label timeCalculate;
    @FXML
    private VBox vbox;
    @FXML
    private ContextMenu table_context_menu, chart_context_menu;
    private FileChooser fileChooser = new FileChooser();
    private Main main;
    private ReportGenerator report = new ReportGenerator();
    @FXML
    private Button saveTemperChart, saveViscosChart;
    @FXML
    private NumberAxis xAis1, xAxis2;
    @FXML
    private NumberAxis yAxis1, yAxis2;
    private boolean isCalculated;

    public ModelOverviewController() {
    }


    @FXML
    public void openMenu(final MouseEvent event) {
        if (event.getButton() != MouseButton.SECONDARY) return;
        chart_context_menu.show(vbox.getScene().getWindow(), event.getScreenX(), event.getScreenY());
    }


    @FXML
    private void initialize() {


        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("Документы Microsoft Office Word", "*.docx");
        FileChooser.ExtensionFilter filter3 = new FileChooser.ExtensionFilter("Файлы PNG", "*.png");
        FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("Все файлы", "*.*");
        fileChooser.getExtensionFilters().addAll(filter1, filter2, filter3);


        initColumn();


        tableWithResult.setEditable(true);
        tableWithResult.setItems(results);


        initContextMenuChart();

        for (TextField tf :
                fields) {
            createDefenceFromStupid(tf);
        }

        createDefenceFromStupid(stepField, Double.parseDouble(lengthField.getText()));
        timeCalculate.setVisible(false);

        yAxis1.setAutoRanging(false);
        yAxis2.setAutoRanging(false);

        tempChart.getXAxis().setLabel("Координата по длине канала, м");
        tempChart.getYAxis().setLabel("Температура, °С");
        viscosityChart.getXAxis().setLabel("Координата по длине канала, м");
        viscosityChart.getYAxis().setLabel("Вязкость, Па∙с");


    }


    private void createDefenceFromStupid(TextField textField, double maxValue) {
        textField.setTextFormatter(new TextFormatter<>(filter));
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() || (newValue.equals("0.0")) || (newValue.equals("0")) || (newValue.equals("0."))
                    || (newValue.equals(".0")) || (Double.parseDouble(newValue) > maxValue)) {
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


    private void createDefenceFromStupid(TextField textField) {
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

    private void initContextMenuChart() {

        MenuItem save_chart = new MenuItem("Сохранить как изображение");
        MenuItem clear_chart = new MenuItem("Очистить");

        chart_context_menu = new ContextMenu(save_chart, clear_chart);

        clear_chart.setOnAction(e -> {
            tempChart.getData().clear();
        });

        save_chart.setOnAction(e -> {
            File file = fileChooser.showSaveDialog(null);

            if (file == null) return;

            WritableImage image = tempChart.snapshot(new SnapshotParameters(), null);

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException ex) {
                System.out.println("some error");
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

        for (int i = 0; i < usersData.length; i++) {
            usersData[i] = Double.parseDouble(fields.get(i).getText());
            System.out.println(usersData[i]);

        }


        dt = new Data(usersData);
        results = FXCollections.observableArrayList(dt.getResults());
        tableWithResult.setItems(results);
        performField.setText(String.valueOf((int) dt.getCanalPerformance()));
        lastTemperField.setText(String.valueOf(dt.getCurrentTemperature()));
        lastViscField.setText(String.valueOf((int) dt.getCurrentViscosity()));
        timeCalculate.setVisible(true);
        long totalTime = System.currentTimeMillis() - startTime;
        timeCalculate.setText("Время расчета: " + totalTime + "мс");
        isCalculated = true;


        tempChart.getData().clear();
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
        plot(resultsTemper, tempChart);


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
        tempChart.getData().clear();
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

                report.setValues(dt.getValues());
                report.setListOfResults(results);
                report.create(file);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Отчёт");
                alert.setHeaderText("Отчёт успешно сохранён.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Не указан путь для сохранения файла.\n Отчет не был сохранён");
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

        WritableImage image = tempChart.snapshot(new SnapshotParameters(), null);

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
    public void exitProgram(final ActionEvent e) {
        System.exit(0);
    }


}

