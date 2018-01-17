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
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

/**
 *
 * @author Wladislaw
 */
public class DBVerwaltung {

    Statement st;
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    ResultSet rs;
    String SQ;

    public DBVerwaltung() throws SQLException {
        this.st = connect().createStatement();
    }

    public void csvdat_eintragen() throws FileNotFoundException, IOException, SQLException {
        try (FileReader fr1 = new FileReader("ARTIKEL.txt")) {
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            String currentDate = null;
            Calendar calendar = Calendar.getInstance();
            currentDate = format.format(calendar.getTime());
            BufferedReader br1 = new BufferedReader(fr1);
            String zeile = br1.readLine();
            int z = 0;
            while (zeile != null) {
                String[] csv_parts = zeile.split(";");
                try {
                    Artikel a = new Artikel(Integer.parseInt(csv_parts[0]), csv_parts[1], Integer.parseInt(csv_parts[2]), Double.parseDouble(csv_parts[3]), Integer.parseInt(csv_parts[4]), currentDate);
                    SQ = "INSERT INTO ARTIKEL (artnr,artbez, mge, preis, steu, edat) VALUES (" + a.getArtnr() + ",'" + a.getArtbez() + "'," + a.getMge() + "," + a.getPreis() + "," + a.getSteu() + ",'" + a.getDatum() + "')";
                    int err = st.executeUpdate(SQ);
                    zeile = br1.readLine();
                    if (err == 1) {
                        System.out.println("Update erfolgreich!");
                        z += 1;
                    }
                } catch (NumberFormatException a) {
                    System.out.println("Update Fehlgeschlagen");
                    zeile = br1.readLine();
                } catch (ArrayIndexOutOfBoundsException a) {
                    System.out.println("Update fehlgeschlagen!");
                    zeile = br1.readLine();
                }
            }

            System.out.println("Es wurden " + z + " Eintraege in die Datenbank hinzugefuegt.");
            br1.close();
        }
    }

    public void artikeltab_Ausgabe() throws SQLException {
        SQ = "SELECT * FROM Artikel";
        rs = st.executeQuery(SQ);
        Artikel a;
        while (rs.next()) {
            a = new Artikel(rs.getInt("ARTNR"), rs.getString("ARTBEZ"), rs.getInt("MGE"), rs.getDouble("PREIS"), rs.getInt("STEU"), rs.getString("EDAT"));
            a.ausgabe();
        }
        rs.close();
    }

    public void lagertab_Ausgabe() throws SQLException {
        SQ = "SELECT * FROM Lager";
        rs = st.executeQuery(SQ);
        Lager l;
        while (rs.next()) {
            l = new Lager(rs.getInt("LNR"), rs.getInt("LPLZ"), rs.getString("LORT"));
            l.ausgabe();
        }

    }

    public void kundetab_Ausgabe() throws SQLException {
        SQ = "Select * FROM Kunde";
        rs = st.executeQuery(SQ);
        Kunde k;
        while (rs.next()) {
            k = new Kunde(rs.getInt("KDNR"), rs.getString("KNAME"), rs.getInt("PLZ"), rs.getString("ORT"), rs.getString("STRASSE"));
            k.ausgabe();
        }
        rs.close();
    }

    public int artikel_suche(int artnr) throws SQLException {
        Artikel a;
        Lager l;
        int gmenge = 0;
        String SQ1 = "Select ARTNR , ARTBEZ , PREIS, MGE , STEU , EDAT FROM Artikel WHERE ARTNR=" + artnr;
        String SQ2 = "SELECT LORT,LPLZ,MENGE FROM LAGER, Lagerbestand WHERE ARTNR=" + artnr + " AND lager.lnr=lagerbestand.lnr AND MENGE>0";
        String SQ3 = "SELECT SUM(MENGE) AS S1 FROM LAGERBESTAND WHERE ARTNR=" + artnr + " GROUP BY ARTNR";
        rs = st.executeQuery(SQ1);
        while (rs.next()) {
            a = new Artikel(rs.getInt("ARTNR"), rs.getString("ARTBEZ"), rs.getInt("MGE"), rs.getDouble("PREIS"), rs.getInt("STEU"), rs.getString("EDAT"));
            a.ausgabe();

        }
        rs.close();

        System.out.println("Lagerorte für diesen Artikel:");
        rs = st.executeQuery(SQ2);
        while (rs.next()) {
            System.out.println("Lagerort: " + rs.getString("LORT") + " LagerPLZ: " + rs.getInt("LPLZ") + " Menge: " + rs.getInt("MENGE"));
        }
        rs.close();
        rs = st.executeQuery(SQ3);
        while (rs.next()) {
            System.out.println("Gesamtmenge: " + rs.getInt("S1"));
            gmenge = rs.getInt("S1");
        }

        rs.close();

        return gmenge;
    }

    public void lagerbestand_hinzufuegen(String s) throws SQLException {
        try {
            SQ = "INSERT INTO LAGERBESTAND VALUES " + s;

            st.executeUpdate(SQ);

        } catch (SQLSyntaxErrorException e) {
            System.out.println("ungültige Eingabe");
        }
    }

    public void lagerbestand_update(int bstnr, int menge) throws SQLException {

        try {
            SQ = "UPDATE Lagerbestand SET menge=" + menge + "WHERE BSTNR=" + bstnr;
            st.executeUpdate(SQ);
        } catch (NumberFormatException e) {
            System.out.println("ungültige Eingabe");
        }
    }

    public void aufgabe6(int artnr, int kdnr, int bmenge, int gmenge) throws SQLException, IOException {
        ArrayList<Lagerbestand> lbliste = new ArrayList<Lagerbestand>();
        Lagerbestand lb;
        int newmenge = 0;
        int puff=bmenge;
        double preis = 0;
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = null;
        String lieferDate = null;
        Calendar calendar = Calendar.getInstance();
        currentDate = format.format(calendar.getTime());
        calendar.add(Calendar.DATE, 14);
        lieferDate = format.format(calendar.getTime());
        
        if (bmenge <= gmenge) {

            SQ = "Select PREIS FROM Artikel WHERE ARTNR=" + artnr;
            rs = st.executeQuery(SQ);
            while (rs.next()) {
                preis = rs.getDouble("Preis");
            }
            rs.close();
            String s = "(0," + kdnr + "," + artnr + "," + bmenge + ",'" + currentDate + "','" + lieferDate + "'," + 1 + "," + bmenge * preis + ")";
            SQ = "INSERT INTO kubest(BESNR,KDNR,ARTNR,BMENGE,BDAT,LDAT,STATUS,RBET) values" + s;
            try {
                st.executeUpdate(SQ);

                SQ = "Select * From Lagerbestand Where artnr=" + artnr + "And Menge >0";

                rs = st.executeQuery(SQ);
                while (rs.next()) {
                    lb = new Lagerbestand(rs.getInt("BSTNR"), rs.getInt("ARTNR"), rs.getInt("LNR"), rs.getInt("menge"));

                    lbliste.add(lb);

                }
                rs.close();
                for (int i = 0; i < lbliste.size(); i++) {
                    if (bmenge > lbliste.get(i).getMenge()) {
                        bmenge -= lbliste.get(i).getMenge();
                        SQ = "Update Lagerbestand SET menge=0 WHERE bstnr=" + lbliste.get(i).getBstnr();
                        st.executeUpdate(SQ);
                    } else {
                        newmenge = lbliste.get(i).getMenge() - bmenge;
                        SQ = "Update Lagerbestand SET menge=" + newmenge + "WHERE bstnr=" + lbliste.get(i).getBstnr();
                        st.executeUpdate(SQ);
                        break;
                    }
                }

                Kunde k = null;
                SQ = "Select * From Kunde Where kdnr=" + kdnr;
                rs = st.executeQuery(SQ);
                while (rs.next()) {
                    k = new Kunde(rs.getInt("KDNR"), rs.getString("KNAME"), rs.getInt("PLZ"), rs.getString("ORT"), rs.getString("STRASSE"));
                }
                rs.close();
                SQ = "Select besnr from kubest WHere besnr=(Select max(besnr) from kubest)";
                int abs = 0;
                rs = st.executeQuery(SQ);
                while (rs.next()) {
                    abs = rs.getInt("besnr");
                }
                rs.close();
                FileWriter fw1 = new FileWriter(kdnr + "_" + abs + ".txt", true);
                PrintWriter pw1 = new PrintWriter(fw1);
                pw1.println("Kundenanschrift:");
                pw1.println(k.getKname());
                pw1.println(k.getPlz());
                pw1.println(k.getOrt());
                pw1.println(k.getStrasse());
                pw1.println("Spaetestes Lieferdatum: " + lieferDate);
                pw1.println("Bestellmenge: " + puff);
                pw1.println("Gesamtpreis: " + puff * preis);
                pw1.close();
                fw1.close();
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("Kunde nicht gefunden");
            }
        } else {
            System.out.println(" nicht genug Artikel: " + (puff - gmenge));
        }
    }

    public void versandrueckmeldung(java.util.Date vdate, int besnr) throws SQLException {
        KUBEST kb = null;
        SQ = "Select * FROM KUBEST WHERE besnr=" + besnr;
        rs = st.executeQuery(SQ);
        while (rs.next()) {
            kb = new KUBEST(rs.getInt("BESNR"), rs.getInt("KDNR"), rs.getInt("ARTNR"), rs.getInt("BMENGE"), rs.getDate("BDAT"), rs.getDate("LDAT"), rs.getInt("status"), rs.getDouble("rbet"), rs.getInt("uet"), rs.getDouble("guts"));
        }
        rs.close();
        System.out.println("Lieferdattum: " + kb.getLdat());
        System.out.println("geliefert am: " + vdate);
        Calendar cal1, cal2;
        cal1 = Calendar.getInstance();
        cal2 = Calendar.getInstance();
        cal1.setTime(vdate);
        cal2.setTime(kb.getLdat());
        int j2, m2, t2;
        j2 = cal1.get(Calendar.YEAR);
        m2 = cal1.get(Calendar.MONTH) + 1;
        t2 = cal1.get(Calendar.DAY_OF_MONTH);
        /*   System.out.println(cal2.getTime());
         System.out.println(cal2.get(Calendar.MONTH)+1);
        
         */
        System.out.println(t2 + "." + m2 + "." + j2);
        System.out.println(vdate.getTime());
        System.out.println(kb.getLdat().getTime());
        if (vdate.getTime() <= kb.getLdat().getTime()) {
            System.out.println("Ich bin drin");
            kb.setStatus(2);

            kb.setLdat(vdate);
            System.out.println(kb.getLdat());
            SQ = "Update KUBEST SET ldat=TO_DATE('" + t2 + "." + m2 + "." + j2 + "') Where besnr=" + besnr;
            st.executeUpdate(SQ);
            SQ = "Update KUBEST SET status=2 Where besnr=" + besnr;
            st.executeUpdate(SQ);
        }
        if (vdate.getTime() > kb.getLdat().getTime()) {
            long l = vdate.getTime() - kb.getLdat().getTime();
            int tage = (int) (l / (1000 * 60 * 60 * 24));
            kb.setUet(tage);
            kb.setGuts(kb.getRbet() * 0.05 * kb.getUet() / 365);
            kb.setLdat(vdate);
            SQ = "Update KUBEST SET ldat=TO_DATE('" + t2 + "." + m2 + "." + j2 + "') Where besnr=" + besnr;
            st.executeUpdate(SQ);
            SQ = "Update KUBEST SET uet=" + kb.getUet() + "Where besnr=" + besnr;
            st.executeUpdate(SQ);
            SQ = "Update KUBEST SET guts=" + kb.getGuts() + "Where besnr=" + besnr;
            st.executeUpdate(SQ);
            SQ = "Update KUBEST SET status=2 Where besnr=" + besnr;
            st.executeUpdate(SQ);
        }

    }

    public void rechnungerstellung(int besnr) throws SQLException, IOException {
        SQ = "Select Status From KUBEST WHERE besnr=" + besnr;
        rs = st.executeQuery(SQ);
        int status = 0;
        while (rs.next()) {
            status = rs.getInt("status");
        }
        rs.close();
        if (status == 2) {
            KUBEST kb = null;
            Artikel a = null;
            Kunde k = null;
            String renr;
            java.sql.Date rdat;
            double erbet;

            SQ = "Select * FROM KUBEST WHERE besnr=" + besnr;
            rs = st.executeQuery(SQ);
            while (rs.next()) {
                kb = new KUBEST(rs.getInt("BESNR"), rs.getInt("KDNR"), rs.getInt("ARTNR"), rs.getInt("BMENGE"), rs.getDate("BDAT"), rs.getDate("LDAT"), rs.getInt("status"), rs.getDouble("rbet"), rs.getInt("uet"), rs.getDouble("guts"));
            }
            rs.close();
            SQ = "Select * FROM Kunde WHERE kdnr=" + kb.getKdnr();
            rs = st.executeQuery(SQ);
            while (rs.next()) {

                k = new Kunde(rs.getInt("KDNR"), rs.getString("KNAME"), rs.getInt("PLZ"), rs.getString("ORT"), rs.getString("STRASSE"));
            }

            rs.close();
            SQ = "Select * FROM Artikel WHERE artnr=" + kb.getArtnr();
            rs = st.executeQuery(SQ);
            while (rs.next()) {
                a = new Artikel(rs.getInt("ARTNR"), rs.getString("ARTBEZ"), rs.getInt("MGE"), rs.getDouble("PREIS"), rs.getInt("STEU"), rs.getString("EDAT"));
            }
            rs.close();
            renr = String.valueOf(kb.getBesnr()) + "R";
            Calendar cal = Calendar.getInstance();
            cal.setTime(kb.getLdat());
            cal.add(Calendar.DAY_OF_MONTH, 14);
            int t, m, j;
            j = cal.get(Calendar.YEAR);
            m = cal.get(Calendar.MONTH) + 1;
            t = cal.get(Calendar.DAY_OF_MONTH);
            rdat = java.sql.Date.valueOf(j + "-" + m + "-" + t);
            erbet = kb.getRbet() - kb.getGuts();
            String v = "("+besnr+",'" + t + "." + m + "." + j + "'," + erbet + ")";
            try{
            SQ = "INSERT INTO RECHNUNG(BENR,RDAT,ERBET) values" + v;
            st.executeUpdate(SQ);
            }
            catch (SQLIntegrityConstraintViolationException e){
                System.out.println("Es Gibt bereits eine Rechnung fuer die Bestellnummer");}
            
            FileWriter fw1 = new FileWriter("./TXTFile/RECH" + besnr+ ".txt", true);
            PrintWriter pw1 = new PrintWriter(fw1);
            pw1.println("Rechnungsnummer: "+renr);
            pw1.println("Rechnungsdatum: "+t+"."+m+"."+j);
            pw1.println("Kundeninformationen:");
            pw1.println("Kundennummer: "+k.getKnr());
            pw1.println(k.getKname());
            pw1.println(k.getPlz());
            pw1.println(k.getOrt());
            pw1.println(k.getStrasse());
            pw1.println("Informaton zum Artikel:");
            pw1.println("Artikelnummer: "+a.getArtnr());
            pw1.println("Artikelbezeichnung: "+a.getArtbez());
            pw1.println("Bestellmenge: " +kb.getBmenge());
            pw1.println("endgueltiger Rechnungsbetrag: "+erbet);
        
            pw1.close();
            fw1.close();
        } else {
            System.out.println("Status ist nicht 2");

        }
    }
    public void xmlRechnung(int besnr) throws SQLException, IOException {
    
      
        KUBEST kb = null;
        Artikel a = null; 
        Kunde k = null;
        String renr;
        java.sql.Date rdat;
        double erbet;
        
        SQ = "Select * FROM KUBEST WHERE besnr=" + besnr;
            rs = st.executeQuery(SQ);
            while (rs.next()) {
                kb = new KUBEST(rs.getInt("BESNR"), rs.getInt("KDNR"), rs.getInt("ARTNR"), rs.getInt("BMENGE"), rs.getDate("BDAT"), rs.getDate("LDAT"), rs.getInt("status"), rs.getDouble("rbet"), rs.getInt("uet"), rs.getDouble("guts"));
            }
            rs.close();
            SQ = "Select * FROM Kunde WHERE kdnr=" + kb.getKdnr();
            rs = st.executeQuery(SQ);
            while (rs.next()) {

                k = new Kunde(rs.getInt("KDNR"), rs.getString("KNAME"), rs.getInt("PLZ"), rs.getString("ORT"), rs.getString("STRASSE"));
            }

            rs.close();
            SQ = "Select * FROM Artikel WHERE artnr=" + kb.getArtnr();
            rs = st.executeQuery(SQ);
            while (rs.next()) {
                a = new Artikel(rs.getInt("ARTNR"), rs.getString("ARTBEZ"), rs.getInt("MGE"), rs.getDouble("PREIS"), rs.getInt("STEU"), rs.getString("EDAT"));
            }
            rs.close();
            renr = String.valueOf(kb.getBesnr()) + "R";
            Calendar cal = Calendar.getInstance();
            cal.setTime(kb.getLdat());
            cal.add(Calendar.DAY_OF_MONTH, 14);
            int t, m, j;
            j = cal.get(Calendar.YEAR);
            m = cal.get(Calendar.MONTH) + 1;
            t = cal.get(Calendar.DAY_OF_MONTH);
            rdat = java.sql.Date.valueOf(j + "-" + m + "-" + t);
            erbet = kb.getRbet() - kb.getGuts();
            String v = "(0,'" + t + "." + m + "." + j + "'," + erbet + ")";
      try
        {
            
          DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
          Document doc = docBuilder.newDocument();

          // DTD implementieren
          DOMImplementation doimp = doc.getImplementation();
          DocumentType doctype = doimp.createDocumentType("doctype", "-//SYSTEM//EN", "RECHNUNGEN.dtd");
          TransformerFactory transformerFactory = TransformerFactory.newInstance();
          Transformer transformer = transformerFactory.newTransformer();
          transformer.setOutputProperty(OutputKeys.INDENT, "yes");
          transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());

          doc.appendChild(doctype);

          // WurzelElement anfügen
          Element rootElement = doc.createElement("RECH");
          doc.appendChild(rootElement);

          // RENR
          Element RENR = doc.createElement("RENR");
          RENR.appendChild(doc.createTextNode(String.valueOf(renr)));
          rootElement.appendChild(RENR);

          // RDAT
          Element RDAT = doc.createElement("RDAT");
          RDAT.appendChild(doc.createTextNode(String.valueOf(rdat)));
          rootElement.appendChild(RDAT);

          // KNR
          Element KNR = doc.createElement("KNR");
          KNR.appendChild(doc.createTextNode(String.valueOf(k.getKnr())));
          rootElement.appendChild(KNR);

          // KNAME
          Element KNAME = doc.createElement("KNAME");
          KNAME.appendChild(doc.createTextNode(k.getKname()));
          rootElement.appendChild(KNAME);

          // PLZ
          Element PLZ = doc.createElement("PLZ");
          PLZ.appendChild(doc.createTextNode(String.valueOf(k.getPlz())));
          rootElement.appendChild(PLZ);

          // ORT
          Element ORT = doc.createElement("ORT");
          ORT.appendChild(doc.createTextNode(k.getOrt()));
          rootElement.appendChild(ORT);

          // STRASSE
          Element STRASSE = doc.createElement("STRASSE");
          STRASSE.appendChild(doc.createTextNode(k.getStrasse()));
          rootElement.appendChild(STRASSE);

          // ARTNR
          Element ARTNR = doc.createElement("ARTNR");
          ARTNR.appendChild(doc.createTextNode(String.valueOf(a.getArtnr())));
          rootElement.appendChild(ARTNR);

          // ARTBEZ
          Element ARTBEZ = doc.createElement("ARTBEZ");
          ARTBEZ.appendChild(doc.createTextNode(a.getArtbez()));
          rootElement.appendChild(ARTBEZ);

          // BMENGE
          Element BEMENGE = doc.createElement("BMENGE");
          BEMENGE.appendChild(doc.createTextNode(String.valueOf(kb.getBmenge())));
          rootElement.appendChild(BEMENGE);

          // ERBET
          Element ERBET = doc.createElement("ERBET");
          ERBET.appendChild(doc.createTextNode(String.valueOf(erbet)));
          rootElement.appendChild(ERBET);

          // als XML schreiben

          DOMSource source = new DOMSource(doc);
          StreamResult result = new StreamResult(new File("./XMLFile/RECH" + besnr+ ".xml"));
          transformer.transform(source, result);

        } catch (ParserConfigurationException pce)
        {
          pce.printStackTrace();
        } catch (TransformerException tfe)
        {
          tfe.printStackTrace();
        }
      
    }
      void saxParsing(int choice,int besnr)
    {
      String s = "XMLFile/RECH" + besnr+ ".xml";
      if (choice == 1)
        SAXParserE.saxpars(s, true);
      else
        {
          SAXParserE.saxpars(s, false);
        }

    }
}
