package ru.alexsumin.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 15.03.17. Edited by Anton on 18.03.17 and 26.03.17.
 */
public class Data {

    // геометрические параметры
    private double width; // ширина канала
    private double length; // длина канала
    private double depth; // глубина канала

    // параметры свойств материала
    private double density; // плотность материала
    private double capacity; // удельная теплоемкость материала
    private double meltingTemperature; // температура плавления материала

    // режимные параметры
    private double coverSpeed; // скорость движения крышки
    private double coverTemperature; // температура крышки

    // эмпирические коэффициенты математической модели
    private double consFactorWithReduction; // коэффициент консистенции при температуре приведения
    private double viscosityFactor; // температурный коэффициент вязкости
    private double reductionTemperature; // температура приведения
    private double flowIndex; // индекс течения материала
    private double emissionFactor; // коэффициент теплоотдачи от крышки к материалу

    private double lengthStep; // шаг по длине канала
    private double currentLength; // текущая длина канала

    // промежуточные и конечные данные
    private double geomFactor; // коэффициент геометрической формы канала
    private double volumeRate; // объемный расход потока через канал
    private double deformationSpeed; // скорость деформации сдвига
    private double viscosityFlux; // удельный тепловой поток, генерируемый в материале за счет работы сил вязкого трения
    private double fluxFromCover; // удельный тепловой поток от нагретой крышки канала к материалу
    private int numberOfIterations; // количество шагов в цикле для расчетов температуры и вязкости материала
    private double transcalencyFactor; // коэффициент теплопроводности материала
    private double currentTemperature; // текущая температура материала
    private double consistenceFactor; // коэффициент консистенции материала
    private double currentViscosity; // текущая вязкость материала
    private double canalPerformance; // производительность канала


    public Data(double step, double[] input) {
        this.lengthStep = step;
        this.width = input[0];
        this.length = input[1];
        this.depth = input[2];
        this.density = input[3];
        this.capacity = input[4];
        this.meltingTemperature = input[5];
        this.coverSpeed = input[6];
        this.coverTemperature = input[7];
        this.viscosityFactor = input[8];
        this.reductionTemperature = input[9];
        this.flowIndex = input[10];
        this.emissionFactor = input[11];
        this.consFactorWithReduction = input[12];
    }

    public static double roundToTwoPlaces(double d) {
        return ((long) (d < 0 ? d * 100 - 0.5 : d * 100 + 0.5)) / 100.0;
    }

    public void calcGeomFactor() {
        geomFactor = 0.125 * Math.pow((depth / width), 2) - 0.625 * (depth / width) + 1;
    }

    public void calcVolumeRate() {
        volumeRate = ((width * depth * coverSpeed) / 2) * geomFactor;
    }

    public void calcDeformationSpeed() {
        deformationSpeed = coverSpeed / depth;
    }

    public void calcViscosityFlux() {
        viscosityFlux = width * depth * consFactorWithReduction * Math.pow(deformationSpeed, flowIndex + 1);
    }

    public void calcFluxFromCover() {
        fluxFromCover = width * emissionFactor * ((1 / viscosityFactor) - coverTemperature + reductionTemperature);
    }

    public void calcNumberOfIterations() {
        numberOfIterations = (int) (length / lengthStep);
    }

    public void calcTranscalencyFactor(double currentLength) {
        transcalencyFactor = ((viscosityFactor * viscosityFlux + width * emissionFactor) / (viscosityFactor * fluxFromCover)) *
                (1 - Math.exp(-((viscosityFactor * fluxFromCover) / (density * capacity * volumeRate)) * currentLength)) +
                Math.exp(viscosityFactor * (meltingTemperature - reductionTemperature - (fluxFromCover / (density * capacity * volumeRate)) * currentLength));
    }

    public void calcCurrentTemperature() {
        currentTemperature = reductionTemperature + (1 / viscosityFactor) * Math.log(transcalencyFactor);
    }

    public void calcConsistenceFactor() {
        consistenceFactor = consFactorWithReduction * Math.exp(-viscosityFactor * (currentTemperature - reductionTemperature));
    }

    public void calcCurrentViscosity() {
        currentViscosity = consistenceFactor * Math.pow(deformationSpeed, flowIndex - 1);
    }

    public void calcCanalPerformance() {
        canalPerformance = 3600 * density * volumeRate;
    }

    public double getCurrentTemperature() {
        return Math.rint(this.currentTemperature * 10) / 10;
    }

    public double getCurrentViscosity() {
        return Math.rint(this.currentViscosity);
    }

    public double getCanalPerformance() {
        return Math.rint(this.canalPerformance);
    }

    public void calcIntermediate() {
        calcGeomFactor();
        calcVolumeRate();
        calcDeformationSpeed();
        calcViscosityFlux();
        calcFluxFromCover();
        calcNumberOfIterations();
    }

    public void calcOutput(double currentLength) {
        calcTranscalencyFactor(currentLength);
        calcCurrentTemperature();
        calcConsistenceFactor();
        calcCurrentViscosity();
        calcCanalPerformance();
    }

    public List getValues() {
        List<Double> values = new ArrayList<>();
        values.add(width);
        values.add(length);
        values.add(depth);
        values.add(density);
        values.add(capacity);
        values.add(meltingTemperature);
        values.add(coverSpeed);
        values.add(coverTemperature);
        values.add(consFactorWithReduction);
        values.add(viscosityFactor);
        values.add(reductionTemperature);
        values.add(flowIndex);
        values.add(emissionFactor);
        values.add(getCurrentTemperature());
        values.add(getCurrentViscosity());
        values.add(getCanalPerformance());
        return values;
    }

    public ArrayList getResults() {
        List<Result> results = new ArrayList<>();
        calcIntermediate();
        for (int i = 0; i <= numberOfIterations; i++) {
            calcOutput(currentLength);
            Result result = new Result(currentLength, currentTemperature, currentViscosity);
            results.add(result);
            this.currentLength += lengthStep;
        }
        boolean isContains = false;
        for (Result r : results) {
            if (r.getStep() == length)
                isContains = true;
        }
        if (!isContains) {
            calcOutput(length);
            Result lastResult = new Result(length, getCurrentTemperature(), getCurrentViscosity());
            results.add(lastResult);
        }


        //results.forEach(value -> System.out.println(value));
        return (ArrayList) results;
    }


}