/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db_3;

import static db_3.connection.connect;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.time.Instant;
import java.util.*;

/**
 *
 * @author Wladislaw
 */
public class Benutzer_Anw {

    public static void main(String[] args)throws IOException, SQLException {
      DBVerwaltung verwaltung=new DBVerwaltung();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String buffer;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
     
       
        
	
        int besnr;
        int artnr;
        int kdnr;
        int bmenge;
        int temp;
        int gmenge;
        boolean schleife=true;
        while(schleife==true){
        System.out.println("Benutzer Eingabe:");
        System.out.println("Um Artikel aus CSV in TAbelle einzutragen druecken Sie '0'.");
        System.out.println("Um alle Artikel auzugeben drücken Sie '1'.");
        System.out.println("Um alle Lager auzugeben drücken Sie '2'");
        System.out.println("Um alle Kunden auzugeben drücken Sie '3'");
        System.out.println("Drücken Sie '4' um nach einem Artikel zu suchen und deren Lagerorte und Mengen ausgeben");
        System.out.println("Um einen neuen Lagerbestand eines Artikels zu erfassen drücken Sie '5'");
        System.out.println("Drücken Sie '6' um die Menge eines Lagerstandes upzudaten");
        System.out.println("Druecken Sie '7' um eine Bestellung aufzunehmen: ");
        System.out.println("Druecken Sie 8 fuer die Versandrueckmeldung.");
        System.out.println("Druekcen sie 9 um Rechnung zu erstellen");
        System.out.println("Druecken Sie 10 um eine XML Datei zu erstellen");
        System.out.println("Drücken Sie 11 um das Programm zu  beenden.");
       
        String select=in.readLine();
        switch(select){
            case "0":
                verwaltung.csvdat_eintragen();
                System.out.println("Druecken Sie eine Taste um fortzufahren.");
                System.in.read();
                
                break;
                
            case "1":
                verwaltung.artikeltab_Ausgabe();
                System.out.println("Druecken Sie eine Taste um fortzufahren.");
                System.in.read();
                break;
            case "2":
                verwaltung.lagertab_Ausgabe();
                System.out.println("Druecken Sie eine Taste um fortzufahren.");
                System.in.read();
                break;
            case "3":
                verwaltung.kundetab_Ausgabe(); 
                System.out.println("Druecken Sie eine Taste um fortzufahren.");
                System.in.read();
                break;
            case "4" :
                System.out.println("Geben Sie die Artikelnummer vom gewünschten Artikel ein: ");
                artnr=Integer.parseInt(in.readLine());
                verwaltung.artikel_suche(artnr);
                System.out.println("Druecken Sie eine Taste um fortzufahren.");
                System.in.read();
                break;
            case "5":
                System.out.println(" Geben Sie einzufügendes Lagebrstand in folgendem Datensatz ein: (BSTNR,ARTNR,LNR,MENGE) ");
                buffer=in.readLine();
                verwaltung.lagerbestand_hinzufuegen(buffer);
                System.out.println("Druecken Sie eine Taste um fortzufahren.");
                System.in.read();
                break;
            case "6":
                try{
                System.out.println(" Geben Sie die BSTNR ein:");
                int bstnr=Integer.parseInt(in.readLine());
                System.out.println("Auf Welchen Wert soll die Menge des Artikels geandert werden?:");
                int menge=Integer.parseInt(in.readLine());
                verwaltung.lagerbestand_update(bstnr, menge);
                }
                catch(NumberFormatException e){
                    System.out.println("ungültige EIngabe");
                }
                System.out.println("Druecken Sie eine Taste um fortzufahren.");
                System.in.read();
                
                break;
            case "7":
                 System.out.println("Geben Sie eine Artkilnummer ein: ");
                 artnr=Integer.parseInt(in.readLine());
                 gmenge=verwaltung.artikel_suche(artnr);
                 System.out.println("Geben Sie eine Kundennumer ein: ");
                 kdnr=Integer.parseInt(in.readLine());
                 System.out.println("Geben Sie eine BestellMenge ein: ");
                 bmenge=Integer.parseInt(in.readLine());
                 verwaltung.aufgabe6(artnr, kdnr,bmenge,gmenge);
                 System.out.println("Druecken Sie eine Taste um fortzufahren.");
                 System.in.read();
                 break;
            case "8":
                System.out.println("Geben Sie die Bestellnummer ein:");
               besnr=Integer.parseInt(in.readLine());
                System.out.println("Geben Sie VDat in folgendem datensatz an:YYYY-MM-DD");
                String string=in.readLine();
            
                java.sql.Date vdate= java.sql.Date.valueOf(string);
             verwaltung.versandrueckmeldung(vdate, besnr);
             System.out.println("Druecken Sie eine Taste um fortzufahren.");
              in.readLine();
                break;
            case "9":
                System.out.println("Geben Sie die BESNR ein: ");
                besnr=Integer.parseInt(in.readLine());
                verwaltung.rechnungerstellung(besnr);
                System.out.println("Druecken Sie eine Taste um fortzufahren.");
                in.readLine();
                
                break;
            case "10":
                System.out.println("Geben Sie eine Bestellnr ein");
                besnr=Integer.parseInt(in.readLine());
                verwaltung.xmlRechnung(besnr);
                 System.out.println("Soll die Datei auf Validität(1) oder nur Wohlgeformtheit(2) geprüft werden?");
                 temp=Integer.parseInt(in.readLine());
                verwaltung.saxParsing(temp, besnr);
                System.out.println("Druecken Sie eine Taste um fortzufahren.");
              in.readLine();
                break;
            case "11":
                System.out.println("Das Programm wird beendet.");
                schleife=false;
                break;
            default: 
                System.out.println("Sie haben eine ungültige Eingabe betätigt.");
                in.readLine();
                break;
                      
                
                 
                 
                 
                 
                
        }
       
        }  
    }
    
}
