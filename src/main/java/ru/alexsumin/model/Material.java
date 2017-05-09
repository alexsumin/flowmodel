package ru.alexsumin.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 10.05.17.
 */
public class Material {


    private int id_material;

    public Material() {
        connect();
    }

    public void setId_material(int id_material) {
        this.id_material = id_material;
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


    public List<IdTypePair> getMaterialsFromDatabase() {
        String material_type;
        int id_material;

        String sql = "SELECT id_material, material_type FROM Material";

        List<IdTypePair> materials = new ArrayList<>();

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                material_type = rs.getString("material_type");
                id_material = rs.getInt("id_material");
                IdTypePair pair = new IdTypePair(id_material, material_type);
//                System.out.println(material_type);
//                System.out.println(id_material);
//                System.out.println(pair);
                materials.add(pair);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
//        System.out.println(materials);
        return materials;
    }


    public double selectDensity() {
        String sql1 = "SELECT [MaterialObjCharact].charact_value\n" +
                "FROM (MaterialObjCharact INNER JOIN Material ON MaterialObjCharact.id_material = Material.id_material) INNER JOIN ObjCharact ON MaterialObjCharact.id_charact = ObjCharact.id_charact\n" +
                "WHERE ObjCharact.id_charact = 1 AND Material.id_material = " + id_material + ";";
        double density = 0;
        try (Connection conn1 = this.connect();
             Statement stmt1 = conn1.createStatement();
             ResultSet rs1 = stmt1.executeQuery(sql1)) {

            // loop through the result set
            while (rs1.next()) {
                density = rs1.getDouble("charact_value");
                System.out.println(density);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return density;
    }

    public double selectCapacity() {
        String sql2 = "SELECT [MaterialObjCharact].charact_value\n" +
                "FROM (MaterialObjCharact INNER JOIN Material ON MaterialObjCharact.id_material = Material.id_material) INNER JOIN ObjCharact ON MaterialObjCharact.id_charact = ObjCharact.id_charact\n" +
                "WHERE ObjCharact.id_charact = 2 AND Material.id_material = " + id_material + ";";
        double capacity = 0;
        try (Connection conn2 = this.connect();
             Statement stmt2 = conn2.createStatement();
             ResultSet rs2 = stmt2.executeQuery(sql2)) {

            // loop through the result set
            while (rs2.next()) {
                capacity = rs2.getDouble("charact_value");
                System.out.println(capacity);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return capacity;
    }

    public double selectMeltingTemperature() {
        String sql3 = "SELECT [MaterialObjCharact].charact_value\n" +
                "FROM (MaterialObjCharact INNER JOIN Material ON MaterialObjCharact.id_material = Material.id_material) INNER JOIN ObjCharact ON MaterialObjCharact.id_charact = ObjCharact.id_charact\n" +
                "WHERE ObjCharact.id_charact = 3 AND Material.id_material = " + id_material + ";";
        double meltingTemperature = 0;
        try (Connection conn3 = this.connect();
             Statement stmt3 = conn3.createStatement();
             ResultSet rs3 = stmt3.executeQuery(sql3)) {

            // loop through the result set
            while (rs3.next()) {
                meltingTemperature = rs3.getDouble("charact_value");
                System.out.println(meltingTemperature);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return meltingTemperature;
    }

    public double selectConsFactorWithReduction() {
        String sql4 = "SELECT [MaterialObjCharact].charact_value\n" +
                "FROM (MaterialObjCharact INNER JOIN Material ON MaterialObjCharact.id_material = Material.id_material) INNER JOIN ObjCharact ON MaterialObjCharact.id_charact = ObjCharact.id_charact\n" +
                "WHERE ObjCharact.id_charact = 4 AND Material.id_material = " + id_material + ";";
        double consFactorWithReduction = 0;
        try (Connection conn4 = this.connect();
             Statement stmt4 = conn4.createStatement();
             ResultSet rs4 = stmt4.executeQuery(sql4)) {

            // loop through the result set
            while (rs4.next()) {
                consFactorWithReduction = rs4.getDouble("charact_value");
                System.out.println(consFactorWithReduction);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return consFactorWithReduction;
    }

    public double selectViscosityFactor() {
        String sql5 = "SELECT [MaterialObjCharact].charact_value\n" +
                "FROM (MaterialObjCharact INNER JOIN Material ON MaterialObjCharact.id_material = Material.id_material) INNER JOIN ObjCharact ON MaterialObjCharact.id_charact = ObjCharact.id_charact\n" +
                "WHERE ObjCharact.id_charact = 5 AND Material.id_material = " + id_material + ";";
        double viscosityFactor = 0;
        try (Connection conn5 = this.connect();
             Statement stmt5 = conn5.createStatement();
             ResultSet rs5 = stmt5.executeQuery(sql5)) {

            // loop through the result set
            while (rs5.next()) {
                viscosityFactor = rs5.getDouble("charact_value");
                System.out.println(viscosityFactor);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return viscosityFactor;
    }

    public double selectReductionTemperature() {
        String sql6 = "SELECT [MaterialObjCharact].charact_value\n" +
                "FROM (MaterialObjCharact INNER JOIN Material ON MaterialObjCharact.id_material = Material.id_material) INNER JOIN ObjCharact ON MaterialObjCharact.id_charact = ObjCharact.id_charact\n" +
                "WHERE ObjCharact.id_charact = 6 AND Material.id_material = " + id_material + ";";
        double reductionTemperature = 0;
        try (Connection conn6 = this.connect();
             Statement stmt6 = conn6.createStatement();
             ResultSet rs6 = stmt6.executeQuery(sql6)) {

            // loop through the result set
            while (rs6.next()) {
                reductionTemperature = rs6.getDouble("charact_value");
                System.out.println(reductionTemperature);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reductionTemperature;
    }

    public double selectFlowIndex() {
        String sql7 = "SELECT [MaterialObjCharact].charact_value\n" +
                "FROM (MaterialObjCharact INNER JOIN Material ON MaterialObjCharact.id_material = Material.id_material) INNER JOIN ObjCharact ON MaterialObjCharact.id_charact = ObjCharact.id_charact\n" +
                "WHERE ObjCharact.id_charact = 7 AND Material.id_material = " + id_material + ";";
        double flowIndex = 0;
        try (Connection conn7 = this.connect();
             Statement stmt7 = conn7.createStatement();
             ResultSet rs7 = stmt7.executeQuery(sql7)) {

            // loop through the result set
            while (rs7.next()) {
                flowIndex = rs7.getDouble("charact_value");
                System.out.println(flowIndex);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return flowIndex;
    }

    public double selectEmissionFactor() {
        String sql8 = "SELECT [MaterialObjCharact].charact_value\n" +
                "FROM (MaterialObjCharact INNER JOIN Material ON MaterialObjCharact.id_material = Material.id_material) INNER JOIN ObjCharact ON MaterialObjCharact.id_charact = ObjCharact.id_charact\n" +
                "WHERE ObjCharact.id_charact = 8 AND Material.id_material = " + id_material + ";";
        double emissionFactor = 0;
        try (Connection conn8 = this.connect();
             Statement stmt8 = conn8.createStatement();
             ResultSet rs8 = stmt8.executeQuery(sql8)) {

            // loop through the result set
            while (rs8.next()) {
                emissionFactor = rs8.getDouble("charact_value");
                System.out.println(emissionFactor);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return emissionFactor;
    }


    private double[] getMaterialData(int id_material) {
        double[] data = new double[8];
        data[0] = selectDensity();
        data[1] = selectCapacity();
        data[2] = selectMeltingTemperature();
        data[3] = selectViscosityFactor();
        data[4] = selectReductionTemperature();
        data[5] = selectFlowIndex();
        data[6] = selectEmissionFactor();
        data[7] = selectConsFactorWithReduction();
        return data;
    }

}