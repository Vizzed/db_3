/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db_3;


import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Artikel {

	private String artbez,datum;
	private int artnr, mge;
        private double preis, steu;
       // BigDecimal preis;
 
        
         public Artikel(int anr,String abez,int mge,double preis, double steu,String datum){
             this.artbez=abez;
             this.artnr=anr;
             this.mge=mge;
             this.preis=preis;
             this.steu=steu;
             this.datum=datum;
             
         }
	
		

	public String getArtbez() {
		return artbez;
	}

	public int getArtnr() {
		return artnr;
	}

	public int getMge() {
		return mge;
	}

	public double getPreis() {
		return preis;
	}

	public double getSteu() {
		return steu;
	}

    public String getDatum() {
        return datum;
    }


	

	public void ausgabe() {
		System.out.println("Artikelbezeichnung: " + artbez);
		System.out.println("Artikelnummer: " + artnr);
		System.out.println("Mengeneinheit: " + mge);
		System.out.println("Preis: " + preis);
		System.out.println("Steuersatz: " + steu);
		System.out.println();
	}

}