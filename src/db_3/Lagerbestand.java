/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db_3;

/**
 *
 * @author Wladi
 */
public class Lagerbestand {
    private int bstnr,artnr,lnr,menge;
 
    public Lagerbestand(int bstnr,int artnr,int lnr,int menge) {
        this.bstnr=bstnr;
        this.artnr=artnr;
        this.lnr=lnr;
        this.menge=menge;
    
}

    public void setMenge(int menge) {
        this.menge = menge;
    }

    public int getBstnr() {
        return bstnr;
    }

    public int getArtnr() {
        return artnr;
    }

    public int getLnr() {
        return lnr;
    }

    public int getMenge() {
        return menge;
    }
    
}
