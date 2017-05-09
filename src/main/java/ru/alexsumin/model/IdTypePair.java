package ru.alexsumin.model;

/**
 * Created by alex on 09.05.17.
 */
public class IdTypePair {

    private int id;
    private String type;

    public IdTypePair(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return type;
    }
}


