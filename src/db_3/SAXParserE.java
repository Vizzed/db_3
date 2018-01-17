package db_3;


/***************************************************************/
/* Verfasser: Prof. Dr. Gregor Büchel                          */
/* Source   : SAXParserE.java                                  */
/* Zweck    : Aufruf eines einfachen XML-Parsers vom Typ eines */
/*            SAX-Parsers, XML-Dateien auf Wohlgeformtheit und */
/*            Validitaet zu pruefen.                           */
/*            Datei zu parsen. Mit SQL-INSERT-Aufbau.          */
/* Aufruf   : mit Namen der XML-Datei als args[0] und der Vali-*/
/*            ditaetseinstellung als args[1] (true <=> Die Va- */
/*            liditaet wird geprueft, false <=> nur die Wohlge-*/
/*            formtheit wird geprueft). Beispiel: Die Datei    */
/*            tabelleMa.xml soll auf Validitaet geprueft werden*/
/*            java SAXParserE true                             */
/* Stand    : 06.10.2014                                       */
/***************************************************************/

import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;


public class SAXParserE{
  public static void saxpars(String filepath, boolean val)
    {
      String filename = filepath, info;

      if (val)
        info = "Validitaet";
      else
        {
          info = "Wohlgeformtheit nur";
        }
      System.out.println("SAXParserE: Jetzt wird der File " + filename + " auf " + info + " geparst.");
      MyContentHandlerE handler = new MyContentHandlerE();
      MyErrorHandlerE ehandler = new MyErrorHandlerE();
      parseXmlFile(filename, handler, ehandler, val);
    }

  public static class MyContentHandlerE implements ContentHandler
  {
    String aktwert;
    int eleanz = 0;

    public void startDocument()
      {
        System.out.println("Anfang des Parsens.");
      }

    public void endDocument()
      {
        System.out.println("Ende des Parsens: " + eleanz + " Elemente sind wohlgeformt.");
      }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
      {
        int i;
        String gVl = null;
        String gTy = null;
        String gNam = null;
        String gVl4;
        AttributesImpl a1 = new AttributesImpl(attributes);
        int l1 = a1.getLength();
        System.out.println("-A-> Anfang des Elements: " + qName + " Attributanzahl: " + l1);
        for (i = 0; i < l1; i++)
          {
            gVl = a1.getValue(i);
            gTy = a1.getType(i);
            gNam = a1.getQName(i);
            // System.out.println("++"+i+". Attribut: "+gNam+" ("+gTy+") : "+gVl);
          }
      }

    public void characters(char[] ch, int start, int length) throws SAXException
      {
        String h = null;
        h = new String(ch, start, length);
        // System.out.println("-> Start: "+start+" Laenge: "+length+" : "+h+" .");
        aktwert = h;
      }

    public void endElement(String uri, String localName, String qName) throws SAXException
      {
        System.out.println("-E-> Ende des Elements: " + qName + " .");
        eleanz++;
      }

    public void skippedEntity(String name) throws SAXException
      { //System.out.println("-S-> Skipped Entity: "+name+" .");
      }

    public void processingInstruction(String target, String data) throws SAXException
      { //System.out.println("-P-> Process_Instr: "+target+" "+data+" .");
      }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
      { //System.out.println("-W-> Whitespace: Pos: "+start+" Laenge: "+length+" .");
      }

    public void endPrefixMapping(String prefix) throws SAXException
      { //System.out.println("-Pr-> Praefix: "+prefix);
      }

    public void startPrefixMapping(String prefix, String uri) throws SAXException
      { //System.out.println("-PrS-> Praefix: "+prefix);
      }

    public void setDocumentLocator(Locator locator)
      { //System.out.println("-L-> Locator: "+locator.toString());
      }
  }

  public static class MyErrorHandlerE implements ErrorHandler
  {
    public void warning(SAXParseException ep) throws SAXException
      {
        System.out.println("Parser meldet WARNUNG: " + ep.toString());
        System.out.println("an der Entity        : " + ep.getPublicId());
        System.out.println("Zeile,Spalte         : " + ep.getLineNumber() + "," + ep.getColumnNumber());
      }

    public void error(SAXParseException ep) throws SAXException
      {
        System.out.println("Parser meldet FEHLER : " + ep.toString());
        System.out.println("an der Entity        : " + ep.getPublicId());
        System.out.println("Zeile,Spalte         : " + ep.getLineNumber() + "," + ep.getColumnNumber());
        //System.exit(1);
      }

    public void fatalError(SAXParseException ep) throws SAXException
      {
        System.out.println("Fataler FEHLER !!!   : " + ep.toString());
        System.out.println("an der Entity        : " + ep.getPublicId());
        System.out.println("Zeile,Spalte         : " + ep.getLineNumber() + "," + ep.getColumnNumber());
        System.exit(3);
      }
  }

  public static void parseXmlFile(String filename, MyContentHandlerE handler, MyErrorHandlerE ehandler, boolean val)
    {
      try
        {
          SAXParserFactory factory = SAXParserFactory.newInstance();
          factory.setValidating(val);
          SAXParser saxpars1 = factory.newSAXParser();
          XMLReader read1 = saxpars1.getXMLReader();
          read1.setContentHandler(handler);
          read1.setErrorHandler(ehandler);
          boolean w1 = saxpars1.isValidating();
          if(w1) System.out.println("---> Der Parser validiert.");
          String h1 = new File(filename).toURL().toString();
          System.out.println("URI = " + h1);
          read1.parse(new File(filename).toURL().toString());
        } catch (SAXParseException ep)
        {// A parsing error occurred; the xml input is not valid
          System.out.println("SAx-Parser-Ausnahme in " + filename + " :\n" + ep);
          System.out.println("Parser meldet FEHLER : " + ep.toString());
          System.out.println("an der Entity        : " + ep.getPublicId());
          System.out.println("Zeile,Spalte         : " + ep.getLineNumber() + "," + ep.getColumnNumber());
        } catch (SAXException e)
        {// A parsing error occurred; the xml input is not valid
          System.out.println("Da ist eine XML-Invaliditaet in " + filename + " :\n" + e);
        } catch (ParserConfigurationException e)
        {
          System.out.println("Ein Parser-Konfigurationsproblem.");
        } 
      catch (IOException e)
        {
          System.out.println("XML-File = " + filename + " konnte nicht geoeffnet werden");
        }
    }
}