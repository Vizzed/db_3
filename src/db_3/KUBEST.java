/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db_3;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Wladi
 */
public class KUBEST {
    int besnr,kdnr,artnr,bmenge,status,uet;
    double guts,rbet;
    Date bdat,ldat;
     


    public KUBEST(int besnr, int kdnr, int artnr, int bmenge,Date bdat,  Date ldat, int status, double rbet, int uet, double guts) {
        this.besnr = besnr;
        this.kdnr = kdnr;
        this.artnr = artnr;
        this.bmenge = bmenge;
        this.status = status;
        this.uet = uet;
        this.guts = guts;
        this.rbet = rbet;
        this.bdat = bdat;
        this.ldat = ldat;
    }

    public int getBesnr() {
        return besnr;
    }

    public int getKdnr() {
        return kdnr;
    }

    public int getArtnr() {
        return artnr;
    }

    public int getBmenge() {
        return bmenge;
    }

    public int getStatus() {
        return status;
    }

    public int getUet() {
        return uet;
    }

    public double getGuts() {
        return guts;
    }

    public double getRbet() {
        return rbet;
    }

    public Date getBdat() {
        return bdat;
    }

    public Date getLdat() {
        return ldat;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUet(int uet) {
        this.uet = uet;
    }

    public void setGuts(double guts) {
        this.guts = guts;
    }

    public void setLdat(Date ldat) {
        this.ldat = ldat;
    }
    
   
    
}
