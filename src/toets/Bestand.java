package toets;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

/**
 * Class om bestanden te openen, op te slaan en te lezen.
 * @author Alex Janse
 * @since 01-11-2017
 * @version 1.00
 */
public class Bestand extends JFileChooser{

    private File bestand;
    private String naamBestand, pathBestand;
    private BufferedReader inBstand;

    /**
     * Bij het aanroepen van deze methode zal er een openDialog van JFileChooser verschijnen waarin de gebruiker een bestand kan kiezen.
     * @param typeBestand String met daarin het type bestand dat de gebruiker bij voorkeur kan gebruiken.
     *                    Hierbij wordt zowel .fileType als fileType geaccepteerd.
     */
    public void kiesBestand(String typeBestand) throws NotFileTypeError{
        int reply;
        if (typeBestand.substring(0,1).equals(".")){
            typeBestand = typeBestand.substring(1,typeBestand.length());
        }

        /*
         * Ik had kunnen kiezen om de optie van AllFiles uit te zetten
         * maar in de opdracht staat dat er een error wordt verwacht
         * als er een niet fasta bestand wordt geopend dus vandaar dat ik dit niet heb gedaan.
         */
        FileNameExtensionFilter filter = new FileNameExtensionFilter(typeBestand,typeBestand);
        setFileFilter(filter);

        reply = showOpenDialog(this); // retourneert een 0 als de gebruiker een bestand heeft gekozen
        if (reply == APPROVE_OPTION) {
            bestand = getSelectedFile();
            naamBestand = bestand.getName();
            pathBestand = bestand.getAbsolutePath();
            if (!(naamBestand.endsWith(typeBestand))){
                bestand = null;
                throw new NotFileTypeError();
            }
        }
    }

    /**
     * Methode om het bestand in te lezen en de inhoud te retourneren
     * @return String met de inhoud van het bestand. Hierbij zijn de regels gescheden met een \n.
     * @throws NoFileInObject Wordt aangeroepen als de functie wordt aangeroepen als er nog geen bestand in het object staat.
     * @throws FileNotFoundException Wordt aangeroepen als het bestand in het object niet gevonden kan worden.
     * @throws IOException Wordt aangeroepen als het inhoud van het bestand niet gelezen kan worden.
     */
    public String leesBestand() throws NoFileInObject, FileNotFoundException, IOException{
        if (bestand == null){
            throw new NoFileInObject();
        } else {
            StringBuilder inhoud = new StringBuilder();
            String regel;
            inBstand = new BufferedReader(new FileReader(bestand));
            regel = inBstand.readLine();
            while (regel != null){
                inhoud.append(regel+"\n");
                regel = inBstand.readLine();
            }
            return inhoud.toString();
        }
    }

    public String getNaamBestand() {return naamBestand;}
    public File getBestand(){ return bestand; }
    public String getPathBestand(){return pathBestand;}

}

/**
 * Exception voorals er een methode wordt aangeroepen terwijl er geen bestand aanwezig is om te gebruiken.
 */
class NoFileInObject extends Exception{

    public NoFileInObject(){

        super("Error: There is no file to use in called method.");
    }

}

/**
 * Exception voorals er een bestand is gekozen die niet past bij het gekozen bestandstype.
 */
class NotFileTypeError extends Exception{

    public NotFileTypeError(){
        super("Gekozen bestand is geen fasta bestand.");
    }
}
