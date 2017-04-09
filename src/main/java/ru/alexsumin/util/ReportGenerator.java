package ru.alexsumin.util;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import ru.alexsumin.model.Result;

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
    long time;

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

    public void setTime(long time) {
        this.time = time;
    }

    public void create() {
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
            paragraphConfig.setText("Отчёт об исследовании.");
            paragraphConfig.addBreak();

            XWPFParagraph bodyParagraph2 = docxModel.createParagraph();
            bodyParagraph2.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun paragraphConfig2 = bodyParagraph2.createRun();
            paragraphConfig2.setItalic(true);
            paragraphConfig2.setFontSize(12);
            paragraphConfig2.setFontFamily("Times New Roman");
            paragraphConfig2.setText("Входные данные.");
            paragraphConfig2.addBreak();

            paragraphConfig2.setText("Тип материала: Полипропилен.");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("1. Геометрические параметры канала.");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("1.1 Ширина, м. W = " + values.get(0));
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("1.2 Длина, м. L = " + values.get(1));
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("1.3 Глубина, м. H = " + values.get(2));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("2. Параметры свойств материала.");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("2.1 Плотность, кг/м^3. ρ = " + values.get(3));
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("2.2 Удельная теплоёмкость, Дж/(кг*°C). c = " + values.get(4));
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("2.3 Температура плавления, °C. T0 = " + values.get(5));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("3. Режимные параметры.");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("3.1 Скорость движения крышки, м/с. Vu = " + values.get(6));
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("3.2 Температура крышки, °C. Tu = " + values.get(7));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("4. Эмпирические коэффициенты математической модели.");
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("4.1 Коэффициент консистенции материала, Па*с^n. µ0 = " + values.get(8));
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("4.2 Температурный коэффициент вязкости, 1/°C. b = " + values.get(9));
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("4.3 Температура приведения, °C. Tr = " + values.get(10));
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("4.4 Индекс течения материала, -. n = " + values.get(11));
            paragraphConfig2.addBreak();
            paragraphConfig2.addTab();
            paragraphConfig2.setText("4.5 Коэффициент теплоотдачи от крышки канала к материалу, Вт/(м^2*°C). αu = " + values.get(12));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("Температура продукта, °С. T = " + values.get(13));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("Вязкость продукта, Па∙с. V = " + values.get(14));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("Производительность канала, кг/ч. Q = " + values.get(15));
            paragraphConfig2.addBreak();
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("Таблица 1 - Текущие параметры состояния");


            //create table
            XWPFTable table = docxModel.createTable();


            XWPFTableRow tableRowOne = table.getRow(0);
            XWPFParagraph paragraphCell = tableRowOne.getCell(0).addParagraph();
            setRun(paragraphCell.createRun(), "Times New Roman", 11, "Шаг, м");
            tableRowOne.addNewTableCell();
            XWPFParagraph paragraphCell2 = tableRowOne.getCell(1).addParagraph();
            setRun(paragraphCell2.createRun(), "Times New Roman", 11, "Температура,°С");
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
            FileOutputStream outputStream = new FileOutputStream("Report.docx");
            docxModel.write(outputStream);
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Успешно записан в файл");
    }

}
