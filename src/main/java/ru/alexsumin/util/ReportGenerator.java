package ru.alexsumin.util;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import ru.alexsumin.model.Result;

import java.io.FileOutputStream;
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
            paragraphConfig.setFontSize(14);
            paragraphConfig.setColor("000000");
            paragraphConfig.setFontSize(14);
            paragraphConfig.setFontFamily("Arial");
            paragraphConfig.setText("Отчёт об исследовании.");
            paragraphConfig.addBreak();

            XWPFParagraph bodyParagraph2 = docxModel.createParagraph();
            bodyParagraph2.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun paragraphConfig2 = bodyParagraph.createRun();
            paragraphConfig2.setItalic(true);
            paragraphConfig2.setFontSize(14);
            paragraphConfig2.setFontFamily("Arial");
            paragraphConfig2.setText("Входные данные.");
            paragraphConfig2.addBreak();
            //paragraphConfig2.addTab();
            paragraphConfig2.setText("Тип материала : Полипропилен.");
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("1. Геометрические параметры канала.");
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t1.1 Ширина, м. W = " + values.get(0));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t1.2 Длина, м. L =" + values.get(1));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t1.3 Глубина, м. H = " + values.get(2));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("2. Параметры свойств материала.");
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t2.1 Плотность, кг/м^3. ρ = " + values.get(3));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t2.2 Удельная теплоёмкость, Дж/(кг*°C). c =" + values.get(4));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t2.3 Температура плавления, °C. T0 =" + values.get(5));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("3. Режимные параметры.");
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t3.1 Скорость движения крышки, м/с. Vu = " + values.get(6));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t3.2 Температура крышки, °C. Tu =" + values.get(7));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("4. Эмпирические коэффициенты математической модели.");
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t4.1 Коэффициент консистенции материала, Па*с^n. µ0 = " + values.get(8));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t4.2 Температурный коэффициент вязкости, 1/°C. b = " + values.get(9));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t4.3 Температура приведения, °C. Tr = " + values.get(10));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t4.4 Индекс течения материала, -. n = " + values.get(11));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("\t4.5 Коэффициент теплоотдачи от крышки канала к материалу, Вт/(м^2*°C). αu = " + values.get(12));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("Температура продукта = " + values.get(13));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("Вязкость продукта =" + values.get(14));
            paragraphConfig2.addBreak();
            paragraphConfig2.setText("Производительность канала" + values.get(15));
            paragraphConfig2.addBreak();

            //create table
            XWPFTable table = docxModel.createTable();
            XWPFTableRow tableRowOne = table.getRow(0);
            tableRowOne.getCell(0).setText("Шаг");
            tableRowOne.addNewTableCell().setText("Температура");
            tableRowOne.addNewTableCell().setText("Вязкость");

            for (int i = 0; i < listOfResults.size(); i++) {

                XWPFTableRow tableRow = table.createRow();
                tableRow.getCell(0).setText(String.valueOf(listOfResults.get(i).getStep()));
                tableRow.getCell(1).setText(String.valueOf(listOfResults.get(i).getTemperature()));
                tableRow.getCell(2).setText(String.valueOf(listOfResults.get(i).getViscosity()));
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
