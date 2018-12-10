/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import model.Person;

/**
 *
 * @author idoia
 */
public class GestionListaEnMemoria {

    public static Connection konektatu() throws SQLException {
        Connection konexioa = null;
        String urla = "";

        konexioa = DriverManager.getConnection("jdbc:mysql://localhost/ikasleak", "root", "");

        return konexioa;
    }

    public static ObservableList<Person> observableJDBC() throws SQLException {
        ObservableList<Person> data = FXCollections.observableArrayList();

        Statement sententzia = null;
        ResultSet rs = null;
        Connection konekzioa = konektatu();
        try {
            sententzia = konekzioa.createStatement();
            rs = sententzia.executeQuery("SELECT * FROM ikasleak");

            while (rs.next()) {
                int id = rs.getInt("id");
                String izena = rs.getString("izena");
                String abizena = rs.getString("abizena");
                String korreoa = rs.getString("korreoa");
                Person ikasle = new Person(id, izena, abizena, korreoa);
                data.add(ikasle);
            }
        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        } finally {
            try {
                if (sententzia != null) {
                    sententzia.close();
                }
            } catch (SQLException sqle) {
            }
            return data;
        }
    }

    public static boolean personInsertJDBC(String addFirstName, String addLastName, String addEmail) throws SQLException {
        String sql = "INSERT INTO Ikasleak(izena, abizena, korreoa) VALUES(?,?,?)";

        try {
            Connection con = konektatu();
            PreparedStatement pStatement = con.prepareStatement(sql);

            pStatement.setString(1, addFirstName);
            pStatement.setString(2, addLastName);
            pStatement.setString(3, addEmail);

            pStatement.execute();

            return true;
        } catch (SQLException ex) {
            return false;
        }

    }

    public static void personDeleteJDBC(Person person) throws SQLException {
        String sql = "DELETE FROM Ikasleak WHERE id = " + person.getId();

        Connection con = konektatu();
        PreparedStatement pStatement = con.prepareStatement(sql);

        pStatement.executeUpdate();

    }

    public static void personUpdateJDBC(int i, String newValue, int id) throws SQLException {
        System.out.print(id);
        String sql = "";

        switch (i) {
            case 1:
                System.out.println(newValue + "Izena aldatu");
                sql = "UPDATE `ikasleak` SET `izena`= '" + newValue + "' WHERE `id` = " + id;
                break;
            case 2:
                System.out.println(newValue + "Abizena aldatu");
                sql = "UPDATE `ikasleak` SET `abizena`= '" + newValue + "'  WHERE `id` = " + id;       
                break;
            case 3:
                System.out.println(newValue + "korreoa aldatu");
                sql = "UPDATE `ikasleak` SET `korreoa`= '" + newValue + "'  WHERE `id` = " + id;
                break;
        }
                
        Connection con = konektatu();
        PreparedStatement pStatement = con.prepareStatement(sql);
        
        pStatement.execute();
        pStatement.close();
    }

}
