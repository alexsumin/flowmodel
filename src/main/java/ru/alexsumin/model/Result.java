package ru.alexsumin.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by alex on 15.03.17. Edited by Anton on 26.03.17.
 */
public class Result {
    private double step;
    private double temperature;
    private double viscosity;

    public Result(double step, double temperature, double viscosity) {
        this.step = Math.rint(step * 10) / 10;
        this.temperature = Math.rint(temperature * 10) / 10;
        this.viscosity = Math.rint(viscosity);
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getViscosity() {
        return viscosity;
    }

    public void setViscosity(int viscosity) {
        this.viscosity = viscosity;
    }

    public DoubleProperty stepProperty() {
        DoubleProperty pr = new SimpleDoubleProperty(step);
        return pr;
    }

    public DoubleProperty temperatureProperty() {
        DoubleProperty pr = new SimpleDoubleProperty(temperature);
        return pr;
    }

    public IntegerProperty viscosityProperty() {
        IntegerProperty pr = new SimpleIntegerProperty((int) viscosity);
        return pr;
    }

    public double get(int index) {
        switch (index) {
            case 0:
                return step;
            case 1:
                return temperature;
            default:
                return viscosity;
        }
    }

    @Override
    public String toString() {

        return "Result{" +
                "currentLength=" + step +
                ", temperature=" + temperature +
                ", viscosity=" + (int) viscosity +
                '}';
    }
}