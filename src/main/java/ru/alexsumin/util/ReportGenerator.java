package ru.alexsumin.util;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import ru.alexsumin.model.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 31.03.17.
 */
public class ReportGenerator {
    List<Double> values = new ArrayList<>();
    List<Result> listOfResults = new ArrayList<>();
    private long time;
    private File file;
    private String pic1;
    private String pic2;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    ReportGenerator(List values, List results, long time) {
        this.values = values;
        this.listOfResults = results;
        this.time = time;

    }

    public ReportGenerator() {

    }

    private static void widthCellsAcrossRow(XWPFTable table, int rowNum, int colNum, int width) {
        XWPFTableCell cell = table.getRow(rowNum).getCell(colNum);
        if (cell.getCTTc().getTcPr() == null)
            cell.getCTTc().addNewTcPr();
        if (cell.getCTTc().getTcPr().getTcW() == null)
            cell.getCTTc().getTcPr().addNewTcW();
        cell.getCTTc().getTcPr().getTcW().setW(BigInteger.valueOf((long) width));
    }

    private static void setRun(XWPFRun run, String fontFamily, int fontSize, String text) {
        run.setFontFamily(fontFamily);
        run.setFontSize(fontSize);
        run.setText(text);
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    public void setListOfResults(List<Result> listOfResults) {
        this.listOfResults = listOfResults;
    }


    //TODO: раз два три
    public void setTime(long time) {
        this.time = time;
    }

    public void create(File file) {
        try {
            // создаем модель docx документа,
            // к которой будем прикручивать наполнение (колонтитулы, текст)
            XWPFDocument docxModel = new XWPFDocument();
            CTSectPr ctSectPr = docxModel.getDocument().getBody().addNewSectPr();
            // получаем экземпляр XWPFHeaderFooterPolicy для работы с колонтитулами
            XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(docxModel, ctSectPr);


            XWPFParagraph bodyParagraph = docxModel.createParagraph();
            bodyParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun paragraphConfig = bodyParagraph.createRun();
            paragraphConfig.setItalic(false);
            paragraphConfig.setFontSize(12);
            paragraphConfig.setColor("000000");
            paragraphConfig.setFontSize(14);
            paragraphConfig.setFontFamily("Times New Roman");
            paragraphConfig.setText("Отчёт о моделировании");
            paragraphConfig.addBreak();

            XWPFParagraph bodyParagraph2 = docxModel.createParagraph();
            bodyParagraph2.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun paragraphConfig2 = bodyParagraph2.createRun();
            //paragraphConfig2.setItalic(true);
            paragraphConfig2.setFontSize(14);
            paragraphConfig2.setFontFamily("Times New Roman");
            paragraphConfig2.setText("Входные данные:");
            paragraphConfig2.addBreak();

            paragraphConfig2.setText("Тип материала: " + type);
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("1 Геометрические параметры канала");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("1.1 Ширина W = " + values.get(0) + " м");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("1.2 Длина L = " + values.get(1) + " м");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("1.3 Глубина H = " + values.get(2) + " м");
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("2 Параметры свойств материала");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("2.1 Плотность ρ = " + values.get(3) + " кг/м³");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("2.2 Удельная теплоёмкость c = " + values.get(4) + " Дж/(кг∙°С)");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("2.3 Температура плавления T₀ = " + values.get(5) + " °C");
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("3 Режимные параметры");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("3.1 Скорость движения крышки Vᵤ = " + values.get(6) + " м/с");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("3.2 Температура крышки Tᵤ = " + values.get(7) + " °C");
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("4 Эмпирические коэффициенты математической модели");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("4.1 Коэффициент консистенции материала µ₀ = " + values.get(8) + " Па∙сⁿ");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("4.2 Температурный коэффициент вязкости b = " + values.get(9) + " 1/°C");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("4.3 Температура приведения Tᵣ = " + values.get(10) + " °C");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("4.4 Индекс течения материала n = " + values.get(11));
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("4.5 Коэффициент теплоотдачи от крышки канала к материалу αᵤ = " + values.get(12) + " Вт/(м²∙°C)");
            paragraphConfig2.addBreak();
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("Выходные параметры:");
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("1 Температура продукта T = " + values.get(13) + " °С");
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("2 Вязкость продукта η = " + (int) Math.floor(values.get(14)) + " Па∙с");
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("3 Производительность канала Q = " + (int) Math.floor(values.get(15)) + " кг/ч");
            paragraphConfig2.addBreak(BreakType.PAGE);


            XWPFParagraph bodyParagraph3 = docxModel.createParagraph();
            XWPFRun run = bodyParagraph3.createRun();
            bodyParagraph3.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun paragraphConfig3 = bodyParagraph3.createRun();
            paragraphConfig3.setFontSize(12);
            paragraphConfig3.setFontFamily("Times New Roman");

            XWPFPicture picture1 = paragraphConfig3.addPicture(new FileInputStream(pic1), XWPFDocument.PICTURE_TYPE_PNG, pic1, Units.toEMU(315), Units.toEMU(302));
            paragraphConfig3.addBreak();
            paragraphConfig3.setText("Рисунок 1 - Зависимость температуры от координаты по длине канала");
            paragraphConfig3.addBreak();
            paragraphConfig3.addBreak();
            XWPFPicture picture2 = paragraphConfig3.addPicture(new FileInputStream(pic2), XWPFDocument.PICTURE_TYPE_PNG, pic2, Units.toEMU(315), Units.toEMU(302));
            paragraphConfig3.addBreak();
            paragraphConfig3.setText("Рисунок 2 - Зависимость вязкости от координаты по длине канала");
            paragraphConfig3.addBreak();
            paragraphConfig3.addBreak(BreakType.PAGE);


            XWPFParagraph bodyParagraph4 = docxModel.createParagraph();
            bodyParagraph4.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun paragraphConfig4 = bodyParagraph4.createRun();
            paragraphConfig4.setFontSize(12);
            paragraphConfig4.setFontFamily("Times New Roman");
            paragraphConfig4.setText("Таблица 1 - Текущие параметры состояния");


            //create table
            XWPFTable table = docxModel.createTable();


            XWPFTableRow tableRowOne = table.getRow(0);
            XWPFParagraph paragraphCell = tableRowOne.getCell(0).addParagraph();
            setRun(paragraphCell.createRun(), "Times New Roman", 11, "Шаг, м");
            tableRowOne.addNewTableCell();
            XWPFParagraph paragraphCell2 = tableRowOne.getCell(1).addParagraph();
            setRun(paragraphCell2.createRun(), "Times New Roman", 11, "Температура, °С");
            tableRowOne.addNewTableCell();
            XWPFParagraph paragraphCell3 = tableRowOne.getCell(2).addParagraph();
            setRun(paragraphCell3.createRun(), "Times New Roman", 11, "Вязкость, Па∙с");
            tableRowOne.setHeight(350);


            widthCellsAcrossRow(table, 0, 0, 1700);
            widthCellsAcrossRow(table, 0, 1, 1700);
            widthCellsAcrossRow(table, 0, 2, 1700);

            for (int i = 0; i < listOfResults.size(); i++) {
                XWPFTableRow tableRow = table.createRow();


                XWPFParagraph paragraphFirstCell = tableRow.getCell(0).addParagraph();
                setRun(paragraphFirstCell.createRun(), "Times New Roman", 10, String.valueOf(listOfResults.get(i).getStep()));
                XWPFParagraph paragraphSecondCell = tableRow.getCell(1).addParagraph();
                setRun(paragraphSecondCell.createRun(), "Times New Roman", 10, String.valueOf(listOfResults.get(i).getTemperature()));
                XWPFParagraph paragraphThirdCell = tableRow.getCell(2).addParagraph();
                setRun(paragraphThirdCell.createRun(), "Times New Roman", 10, String.valueOf(listOfResults.get(i).getViscosity()));


            }


            // сохраняем модель docx документа в файл


            FileOutputStream outputStream = new FileOutputStream(file);

            docxModel.write(outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Успешно записан в файл");
    }

    public void setPics(String file1, String file2) {
        pic1 = file1;
        pic2 = file2;
    }
}