/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db_3;

import java.sql.*;
import oracle.jdbc.pool.*;
/**
 *
 * @author Wladislaw
 */
public class connection {
    public static Connection connect() throws SQLException {
    String treiber;
    OracleDataSource ods = new OracleDataSource();
    treiber = "oracle.jdbc.driver.OracleDriver";
    Connection dbConnection = null;
 /* Treiber laden */
 try{
     Class.forName(treiber).newInstance();
 } 
 catch (Exception e){
     System.out.println("Fehler beim laden des Treibers: "+ e.getMessage());
 }
 /* Datenbank-Verbindung erstellen */
 try{
     
     ods.setURL("jdbc:oracle:thin:dbprak44/tin17@Schelling.nt.fh-koeln.de:1521:xe");
     dbConnection = ods.getConnection();
 }
 catch (SQLException e){
     System.out.println("Fehler beim Verbindungsaufbau zur Datenbank!");
     System.out.println(e.getMessage());
 }
 return dbConnection;
 }
    
    
}
