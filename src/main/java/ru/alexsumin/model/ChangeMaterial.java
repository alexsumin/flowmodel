package ru.alexsumin.model;


import java.sql.*;

/**
 * Created by Anton on 14.05.17.
 */
public class ChangeMaterial {


    private double newDensity, newCapacity, newMeltingTemperature, newViscosityFactor, newReductionTemperature,
            newFlowIndex, newEmissionFactor, newConsFactorWithReduction;

    private int id_material;
    private String material_type;
    double[] data = new double[8];

    public ChangeMaterial() {
        connect();
    }

    public void setIdMaterial(int id) {
        id_material = id;
    }


    public void setMaterial(String s) {
        material_type = s;
    }


    public void setData(String type, int id, double[] data) {
        newDensity = data[0];
        newCapacity = data[1];
        newMeltingTemperature = data[2];
        newViscosityFactor = data[3];
        newReductionTemperature = data[4];
        newFlowIndex = data[5];
        newEmissionFactor = data[6];
        newConsFactorWithReduction = data[7];
        material_type = type;
        id_material = id;


    }

    private Connection connect() {

        String url = "jdbc:/sqlite:database/Material_Database.s3db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public int getMaxIdMaterialFromDatabase() {
        String maxIdSql = "SELECT MAX(id_material) FROM Material;";

        try (Connection maxIdConn = this.connect();
             Statement maxIdStmt = maxIdConn.createStatement();
             ResultSet maxIdRs = maxIdStmt.executeQuery(maxIdSql)) {

            while (maxIdRs.next()) {
                id_material = maxIdRs.getInt("MAX(id_material)");
                System.out.println(id_material);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ++id_material;
    }


    private void insertMaterial() {
        String upSql = "INSERT INTO Material (id_material, material_type)\n" +
                "VALUES (" + id_material + ", '" + material_type + "');";
        System.out.println(id_material);
        System.out.println(material_type);
        try (Connection upConn = this.connect();
             Statement upStmt = upConn.createStatement();
             ResultSet upRs = upStmt.executeQuery(upSql)) {
            while (upRs.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertCharactValues() {
        String upSql1 = "INSERT INTO MaterialObjCharact (id_material, id_charact, charact_value)\n" +
                "VALUES (" + id_material + ", 1, " + newDensity + "), (" + id_material + ", 2, " + newCapacity + "),\n" +
                "(" + id_material + ", 3, " + newMeltingTemperature + "), (" + id_material + ", 4, " + newConsFactorWithReduction + "),\n" +
                "(" + id_material + ", 5, " + newViscosityFactor + "), (" + id_material + ", 6, " + newReductionTemperature + "),\n" +
                "(" + id_material + ", 7, " + newFlowIndex + "), (" + id_material + ", 8, " + newEmissionFactor + ");";

        try (Connection upConn1 = this.connect();
             Statement upStmt1 = upConn1.createStatement();
             ResultSet upRs1 = upStmt1.executeQuery(upSql1)) {

            while (upRs1.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertDbRecords() {

        insertMaterial();
        insertCharactValues();
    }


    private void updateMaterial() {
        String updSql = "UPDATE Material SET material_type = '" + material_type + "'\n" +
                "WHERE id_material = " + id_material + ";";
        System.out.println(material_type);
        try (Connection updConn = this.connect();
             Statement updStmt = updConn.createStatement();
             ResultSet updRs = updStmt.executeQuery(updSql)) {
            while (updRs.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateDensity() {
        String updSql1 = "UPDATE MaterialObjCharact SET charact_value = " + newDensity + "\n" +
                "WHERE id_material = " + id_material + " AND id_charact = 1;";
        System.out.println(newDensity);
        try (Connection updConn1 = this.connect();
             Statement updStmt1 = updConn1.createStatement();
             ResultSet updRs1 = updStmt1.executeQuery(updSql1)) {
            while (updRs1.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateCapacity() {
        String updSql2 = "UPDATE MaterialObjCharact SET charact_value = " + newCapacity + "\n" +
                "WHERE id_material = " + id_material + " AND id_charact = 2;";
        System.out.println(newCapacity);
        try (Connection updConn2 = this.connect();
             Statement updStmt2 = updConn2.createStatement();
             ResultSet updRs2 = updStmt2.executeQuery(updSql2)) {
            while (updRs2.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateMeltingTemperature() {
        String updSql3 = "UPDATE MaterialObjCharact SET charact_value = " + newMeltingTemperature + "\n" +
                "WHERE id_material = " + id_material + " AND id_charact = 3;";
        System.out.println(newMeltingTemperature);
        try (Connection updConn3 = this.connect();
             Statement updStmt3 = updConn3.createStatement();
             ResultSet updRs3 = updStmt3.executeQuery(updSql3)) {
            while (updRs3.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateConsFactorWithReduction() {
        String updSql4 = "UPDATE MaterialObjCharact SET charact_value = " + newConsFactorWithReduction + "\n" +
                "WHERE id_material = " + id_material + " AND id_charact = 4;";
        System.out.println(newConsFactorWithReduction);
        try (Connection updConn4 = this.connect();
             Statement updStmt4 = updConn4.createStatement();
             ResultSet updRs4 = updStmt4.executeQuery(updSql4)) {
            while (updRs4.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateViscosityFactor() {
        String updSql5 = "UPDATE MaterialObjCharact SET charact_value = " + newViscosityFactor + "\n" +
                "WHERE id_material = " + id_material + " AND id_charact = 5;";
        System.out.println(newViscosityFactor);
        try (Connection updConn5 = this.connect();
             Statement updStmt5 = updConn5.createStatement();
             ResultSet updRs5 = updStmt5.executeQuery(updSql5)) {
            while (updRs5.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateReductionTemperature() {
        String updSql6 = "UPDATE MaterialObjCharact SET charact_value = " + newReductionTemperature + "\n" +
                "WHERE id_material = " + id_material + " AND id_charact = 6;";
        System.out.println(newReductionTemperature);
        try (Connection updConn6 = this.connect();
             Statement updStmt6 = updConn6.createStatement();
             ResultSet updRs6 = updStmt6.executeQuery(updSql6)) {
            while (updRs6.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateFlowIndex() {
        String updSql7 = "UPDATE MaterialObjCharact SET charact_value = " + newFlowIndex + "\n" +
                "WHERE id_material = " + id_material + " AND id_charact = 7;";
        System.out.println(newFlowIndex);
        try (Connection updConn7 = this.connect();
             Statement updStmt7 = updConn7.createStatement();
             ResultSet updRs7 = updStmt7.executeQuery(updSql7)) {
            while (updRs7.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateEmissionFactor() {
        String updSql8 = "UPDATE MaterialObjCharact SET charact_value = " + newEmissionFactor + "\n" +
                "WHERE id_material = " + id_material + " AND id_charact = 8;";
        System.out.println(newEmissionFactor);
        try (Connection updConn8 = this.connect();
             Statement updStmt8 = updConn8.createStatement();
             ResultSet updRs8 = updStmt8.executeQuery(updSql8)) {
            while (updRs8.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateCharactValues() {
        updateDensity();
        updateCapacity();
        updateMeltingTemperature();
        updateConsFactorWithReduction();
        updateViscosityFactor();
        updateReductionTemperature();
        updateFlowIndex();
        updateEmissionFactor();
    }

    public void updateDbRecords() {
        System.out.println(material_type);
        updateMaterial();
        updateCharactValues();

    }


    private void changeDbRecords() {
        setIdMaterial(0);
        if (id_material == 0) {
            insertDbRecords();
        } else {
            updateDbRecords();
        }
    }

    private int getIdMaterialFromDatabase() {
        String idSql = "SELECT id_material FROM Material\n" +
                "WHERE (material_type = '" + material_type + "');";

        try (Connection idConn = this.connect();
             Statement idStmt = idConn.createStatement();
             ResultSet idRs = idStmt.executeQuery(idSql)) {

            while (idRs.next()) {
                id_material = idRs.getInt("id_material");
                System.out.println(id_material);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id_material;
    }

    private void deleteCharactValues() {
        String delSql = "DELETE FROM MaterialObjCharact\n" +
                "WHERE (id_material = " + id_material + ");";
        System.out.println(id_material);
        try (Connection delConn = this.connect();
             Statement delStmt = delConn.createStatement();
             ResultSet delRs = delStmt.executeQuery(delSql)) {
            while (delRs.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteMaterial() {
        String delSql = "DELETE FROM Material\n" +
                "WHERE (id_material = " + id_material + ");";
        System.out.println(id_material);
        try (Connection delConn = this.connect();
             Statement delStmt = delConn.createStatement();
             ResultSet delRs = delStmt.executeQuery(delSql)) {
            while (delRs.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteDbRecords() {
        deleteCharactValues();
        deleteMaterial();
    }


}