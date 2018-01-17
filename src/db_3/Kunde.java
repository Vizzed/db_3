/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db_3;

/**
 *
 * @author Wladislaw
 */
public class Kunde {
   private int knr,plz;
   private String kname,ort,strasse;
   public Kunde(int knr,String name, int plz,String ort,String strasse){
       this.kname=name;
       this.knr=knr;
       this.ort=ort;
       this.plz=plz;
       this.strasse=strasse;
   }

    public int getKnr() {
        return knr;
    }

    public int getPlz() {
        return plz;
    }

    public String getKname() {
        return kname;
    }

    public String getOrt() {
        return ort;
    }

    public String getStrasse() {
        return strasse;
    }
   public void ausgabe(){
		System.out.println("Kundennummer: " + knr);
		System.out.println("Name: " + kname);
		System.out.println("Ort: " + ort);
		System.out.println("Plz: " + plz);
		System.out.println("Strasse: " + strasse);
		System.out.println();
   }
    
}
