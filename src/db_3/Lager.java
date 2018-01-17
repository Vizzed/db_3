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
public class Lager {
    private int lnr,plz;
    private String lname;
    public Lager(int lnr,int plz, String lname){
        this.lnr=lnr;
        this.plz=plz;
        this.lname=lname;
    }

    public int getLnr() {
        return lnr;
    }

    public int getPlz() {
        return plz;
    }

    public String getLname() {
        return lname;
    }
    
    public void ausgabe() {
		System.out.println("Lagernummer: " + lnr);
		System.out.println("Lagerort: " + lname);
		System.out.println("Postleitzahl: " + plz);
		System.out.println();
    
}
}
