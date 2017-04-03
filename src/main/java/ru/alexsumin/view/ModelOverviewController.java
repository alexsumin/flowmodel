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
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alex on 15.03.17.
 */
public class ModelOverviewController {
    @FXML
    TableView<Result> tableWithResult;
    UnaryOperator<TextFormatter.Change> filter = t -> {

        if (t.isReplaced())
            if (t.getText().matches("[^0-9]"))
                t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));


        if (t.isAdded()) {
            if (t.getControlText().contains(".")) {
                if (t.getText().matches("[^0-9]")) {
                    t.setText("");
                }
            } else if (t.getText().matches("[^0-9.]")) {
                t.setText("");
            }
        }

        return t;
    };
    private ObservableList<Result> results = FXCollections.observableArrayList();
    @FXML
    private LineChart viscosityChart;
    @FXML
    private LineChart tempChart;
    @FXML
    private TableColumn<Result, Number> stepColumn;
    @FXML
    private TableColumn<Result, Number> temperatureColumn;
    @FXML
    private TableColumn<Result, Number> viscosityColumn;
    @FXML
    private TextField stepField, widthField, lengthField, depthField;
    @FXML
    private TextField densityField, capacityField, meltingTemperatureField;
    @FXML
    private TextField speedCoverField, coverTemperatureField, viscosityFactorField;
    @FXML
    private TextField reductionTemperatureField, indexOfMaterialField, emissionFactorField, consFactorWithReductionField;
    @FXML
    private TextField performField, lastTemperField, lastViscField;
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
    private double stepForLegend;
    @FXML
    private Button saveTemperChart, saveViscosChart;
    @FXML
    private NumberAxis xAis1, xAxis2;
    @FXML
    private NumberAxis yAxis1, yAxis2;

    public ModelOverviewController() {
    }

    private void initContextMenuTable() {

        MenuItem add_row = new MenuItem("Добавить строку");
        MenuItem remove_row = new MenuItem("Удалить строку");
        MenuItem clear_table = new MenuItem("Очистить таблицу");

        table_context_menu = new ContextMenu(add_row, remove_row, clear_table);

        add_row.setOnAction(e -> {
            results.add(new Result(0, 0, 0));
        });

        clear_table.setOnAction(e -> {
            results.clear();
        });

        remove_row.setOnAction(e -> {
            results.remove(tableWithResult.getSelectionModel().getSelectedIndex());
        });

    }

    @FXML
    private void initialize() {
        initColumn();

        tableWithResult.setEditable(true);
        tableWithResult.setItems(results);

        initContextMenuTable();

        //initContextMenuChart();
        createDefenceFromStupid(stepField);
        timeCalculate.setVisible(false);

        yAxis1.setAutoRanging(false);
        yAxis2.setAutoRanging(false);

        /*
        yAxis.setUpperBound(2100);
        yAxis.setLowerBound(1200;
        yAxis.setTickUnit(100);
        */
        tempChart.getXAxis().setLabel("Длина канала, м");
        tempChart.getYAxis().setLabel("Температура, °С");
        viscosityChart.getXAxis().setLabel("Длина канала, м");
        viscosityChart.getYAxis().setLabel("Вязкость, Па∙с");



    }

    private void createDefenceFromStupid(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(filter));

    }
    /*
    private void createDefenceFromStupid(TextField textField) {
        final Pattern pattern = Pattern.compile("[0-9]{1,}[\\.,]{0,1}[0-9]{0,1}");

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            Matcher matcher = pattern.matcher(newValue);
            if (!newValue.isEmpty()) {
                if ((!matcher.matches()) || (newValue == "0.0")) {
                    textField.setText(oldValue);
                } else {
                    try {
                        // тут можете парсить строку как захотите
                        double value = Double.parseDouble(newValue);
                        textField.setText("" + value);

                    } catch (NumberFormatException e) {
                        textField.setText(oldValue);
                    }
                }
            }
        });


    }

    */




    /*
    private void createDefenceFromStupid(TextField textField) {
    textField.textProperty().addListener(( observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }
    );}
    */

    /*
    private void createDefenceFromStupid(TextField textField) {
    textField.textProperty().addListener(( observable, oldValue, newValue) -> {
            if (newValue.matches("\\d+(\\.\\d{2})?|\\.\\d{2}")) {
                textField.setText(newValue);
            }
            else {
                textField.setText(oldValue);
            }
        }
    );}
    */


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

        double usersData[] = new double[14];

        //TODO: переделать с использование  цикла
        //в цикле выкидывает nullPointerException, мб textfield не имплементирует iterable?
        usersData[0] = Double.parseDouble(stepField.getText());
        stepForLegend = Double.parseDouble(stepField.getText());
        usersData[1] = Double.parseDouble(widthField.getText());
        usersData[2] = Double.parseDouble(lengthField.getText());
        usersData[3] = Double.parseDouble(depthField.getText());
        usersData[4] = Double.parseDouble(densityField.getText());
        usersData[5] = Double.parseDouble(capacityField.getText());
        usersData[6] = Double.parseDouble(meltingTemperatureField.getText());
        usersData[7] = Double.parseDouble(speedCoverField.getText());
        usersData[8] = Double.parseDouble(coverTemperatureField.getText());
        usersData[9] = Double.parseDouble(viscosityFactorField.getText());
        usersData[10] = Double.parseDouble(reductionTemperatureField.getText());
        usersData[11] = Double.parseDouble(indexOfMaterialField.getText());
        usersData[12] = Double.parseDouble(emissionFactorField.getText());
        usersData[13] = Double.parseDouble(consFactorWithReductionField.getText());

        /*
        double usersData[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14};
        for (int i = 0; i < fields.length; i++) {
            //usersData[i] = (Double.parseDouble(fields[i].getText()));
            System.out.println(Double.parseDouble(fields[i].getText()));

        }
        */

        Data dt = new Data(usersData);
        results = FXCollections.observableArrayList(dt.getResults());
        tableWithResult.setItems(results);
        performField.setText(String.valueOf((int) dt.getCanalPerformance()));
        lastTemperField.setText(String.valueOf(dt.getCurrentTemperature()));
        lastViscField.setText(String.valueOf((int) dt.getCurrentViscosity()));
        timeCalculate.setVisible(true);
        long totalTime = System.currentTimeMillis() - startTime;
        timeCalculate.setText("Время расчета: " + totalTime + "мс");
        report.setValues(dt.getValues());
        report.setListOfResults(results);


        yAxis2.setUpperBound(Math.rint(results.get(0).getViscosity() * 1.03));
        yAxis2.setLowerBound(Math.rint(results.get(results.size() - 1).getViscosity() * 0.97));
        yAxis2.setTickUnit(Math.rint((yAxis2.getUpperBound() - yAxis2.getLowerBound()) / 10));

        yAxis1.setLowerBound(Math.rint(results.get(0).getTemperature() * 0.97));
        yAxis1.setUpperBound(Math.rint(results.get(results.size() - 1).getTemperature() * 1.03));
        yAxis1.setTickUnit(Math.rint((yAxis1.getUpperBound() - yAxis1.getLowerBound()) / 10));



    }

    private void plot(XYChart.Data[] points, String name, LineChart lineChart) {
        XYChart.Series series = new XYChart.Series();
        series.getData().addAll(points);
        series.setName(name);
        lineChart.getData().addAll(series);
        points = null;
    }
    /*
    public void activateTemperatureChart(){
        int len = results.size();
        XYChart.Data[] resultsTemper = new XYChart.Data[len];
        Result tmp;
        for(int i = 0; i < len;i++){
            tmp = results.get(i);
            resultsTemper[i] = new XYChart.Data(tmp.get(0),tmp.get(1));
        }
        plot(resultsTemper, "temperatureChart");

    }*/

    public void drawTemperatureChart(final ActionEvent e) {
        int len = results.size();
        XYChart.Data[] resultsTemper = new XYChart.Data[len];
        Result tmp;
        for (int i = 0; i < len; i++) {
            tmp = results.get(i);
            resultsTemper[i] = new XYChart.Data(tmp.getStep(), tmp.getTemperature());
        }
        plot(resultsTemper, "При шаге " + stepForLegend, tempChart);

    }

    public void drawViscosityChart(final ActionEvent e) {
        int len = results.size();
        XYChart.Data[] resultsTemper = new XYChart.Data[len];
        Result tmp;
        for (int i = 0; i < len; i++) {
            tmp = results.get(i);
            resultsTemper[i] = new XYChart.Data(tmp.getStep(), tmp.getViscosity());
        }
        plot(resultsTemper, "При шаге " + stepForLegend, viscosityChart);

    }

    public void clearTemperatureChart(final ActionEvent event) {
        tempChart.getData().clear();
    }

    public void clearViscosityChart(final ActionEvent event) {
        viscosityChart.getData().clear();
    }

    @FXML
    public void addRow(final MouseEvent event) {
        if (event.getButton() != MouseButton.SECONDARY) return;
        table_context_menu.show(vbox.getScene().getWindow(), event.getScreenX(), event.getScreenY());
    }

    @FXML
    public void generateReport(final ActionEvent event) {
        report.create();

    }

    @FXML
    public void exitProgram(final ActionEvent e) {
        System.exit(0);
    }
}


