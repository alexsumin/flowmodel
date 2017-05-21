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

    public void setIdMaterial(int index) {
        switch (index) {
            case 0:
                getIdMaterialFromDatabase();
                break;
            case 1:
                getMaxIdMaterialFromDatabase();
        }
    }


    /*
    public void setDensity() {
        Scanner Density = new Scanner(System.in);
        System.out.println("\nВведите плотность материала:");
        newDensity = Density.nextDouble();
        //newDensity = 1200;
    }

    public void setCapacity() {
        Scanner Capacity = new Scanner(System.in);
        System.out.println("\nВведите удельную теплоёмкость материала:");
        newCapacity = Capacity.nextDouble();
        //newCapacity = 2100;
    }

    public void setMeltingTemperarure() {
        Scanner MeltingTemperature = new Scanner(System.in);
        System.out.println("\nВведите температуру плавления материала:");
        newMeltingTemperature = MeltingTemperature.nextDouble();
        //newMeltingTemperature = 140;
    }

    public void setConsFactorWithReduction() {
        Scanner ConsFactorWithReduction = new Scanner(System.in);
        System.out.println("\nВведите коэффициент консистенции материала при температуре приведения:");
        newConsFactorWithReduction = ConsFactorWithReduction.nextDouble();
        //newConsFactorWithReduction = 10000;
    }

    public void setViscosityFactor() {
        Scanner ViscosityFactor = new Scanner(System.in);
        System.out.println("\nВведите температурный коэффициент вязкости материала:");
        newViscosityFactor = ViscosityFactor.nextDouble();
        //newViscosityFactor = 0.04;
    }

    public void setReductionTemperature() {
        Scanner ReductionTemperature = new Scanner(System.in);
        System.out.println("\nВведите температуру приведения материала:");
        newReductionTemperature = ReductionTemperature.nextDouble();
        //newReductionTemperature = 170;
    }

    public void setFlowIndex() {
        Scanner FlowIndex = new Scanner(System.in);
        System.out.println("\nВведите индекс течения материала:");
        newFlowIndex = FlowIndex.nextDouble();
        //newFlowIndex = 0.3;
    }

    public void setEmissionFactor() {
        Scanner EmissionFactor = new Scanner(System.in);
        System.out.println("\nВведите коэффициент теплоотдачи от крышки канала к материалу:");
        newEmissionFactor = EmissionFactor.nextDouble();
        //newEmissionFactor = 450;
    }



    public void setCharactValues() {
        setDensity();
        setCapacity();
        setMeltingTemperarure();
        setConsFactorWithReduction();
        setViscosityFactor();
        setReductionTemperature();
        setFlowIndex();
        setEmissionFactor();
    }


    public void setMaterial() {
        setMaterialType();
        setCharactValues();
    }

    */

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
        // SQLite connection string
        String url = "jdbc:sqlite:src/main/resources/database/Material_Database.s3db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    private int getMaxIdMaterialFromDatabase() {
        String maxIdSql = "SELECT MAX(id_material) FROM Material;";

        try (Connection maxIdConn = this.connect();
             Statement maxIdStmt = maxIdConn.createStatement();
             ResultSet maxIdRs = maxIdStmt.executeQuery(maxIdSql)) {

            // loop through the result set
            while (maxIdRs.next()) {
                id_material = maxIdRs.getInt("MAX(id_material)");
                System.out.println(id_material);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id_material++;
    }


    //вставка значений

    private void insertMaterial() {
        String upSql = "INSERT INTO Material (id_material, material_type)\n" +
                "VALUES (" + id_material + ", '" + material_type + "');";
        System.out.println(id_material);
        System.out.println(material_type);
        try (Connection upConn = this.connect();
             Statement upStmt = upConn.createStatement();
             ResultSet upRs = upStmt.executeQuery(upSql)) {
            // loop through the result set
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
            // loop through the result set
            while (upRs1.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertDbRecords() {
        setIdMaterial(1);
        insertMaterial();
        insertCharactValues();
    }


    //методы обновляющие по одной записи в бд

    private void updateMaterial() {
        String updSql = "UPDATE Material SET material_type = '" + material_type + "'\n" +
                "WHERE id_material = " + id_material + ";";
        System.out.println(material_type);
        try (Connection updConn = this.connect();
             Statement updStmt = updConn.createStatement();
             ResultSet updRs = updStmt.executeQuery(updSql)) {
            // loop through the result set
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
            // loop through the result set
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
            // loop through the result set
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
            // loop through the result set
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
            // loop through the result set
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
            // loop through the result set
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
            // loop through the result set
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
            // loop through the result set
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
            // loop through the result set
            while (updRs8.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //метод вызывает все методы на обновление записей

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
        //setMaterial();
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

            // loop through the result set
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
            // loop through the result set
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
            // loop through the result set
            while (delRs.next()) {
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

//    private void deleteDbRecords() {
//        setMaterialType();
//        setIdMaterial(0);
//        deleteCharactValues();
//        deleteMaterial();
//    }


}