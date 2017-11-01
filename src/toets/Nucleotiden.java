package toets;

import java.util.Arrays;

/**
 * Class om nucleotides te controleren en bij te houden hoeveel er van elke nucleotide is gevonden
 * @author Alex Janse
 * @version 1.00
 * @since 01-11-2017
 */
public class Nucleotiden {

    private final String[] BESTAANDE_NUCLEOTIDEN = {"A", "C", "G", "T"};
    private Integer[] aantallen = new Integer[4];
    private int totaal = 0;

    // Constructoor om er voor te zorgen dat de integer vector gevuld is met 0.
    public Nucleotiden() {
        Arrays.fill(aantallen, 0);
    }


    /**
     * Methode om de opgegeven nucleotide te controleren of die bestaat en eventueel optellen.
     * @param nucleotide een character met daarin de nucleotide die gecontroleerd moet worden.
     *                   Gekozen voor character omdat je dan 1 letter verplicht.
     * @return true als de necleotide bestaat.
     * @throws NotANucleotide wordt opgegooid als de nucleotide niet voorkomt in de bestaande nucleotide.
     */
    public boolean controleerNucleotide(Character nucleotide) throws NotANucleotide {
        String checkNucleotide = Character.toString(nucleotide);
        String vergelijkMet;

        for (int counter = 0; counter < BESTAANDE_NUCLEOTIDEN.length; counter++) {
            vergelijkMet = BESTAANDE_NUCLEOTIDEN[counter];
            if (checkNucleotide.equals(vergelijkMet)) {
                totaal++;
                switch (vergelijkMet) {

                    case "A":
                        aantallen[0]++;
                        break;

                    case "C":
                        aantallen[1]++;
                        break;

                    case "G":
                        aantallen[2]++;
                        break;

                    case "T":
                        aantallen[3]++;
                        break;

                    default:
                        break;
                }
                return true;
            }
        }
        throw new NotANucleotide(checkNucleotide);
    }

    public Integer[] getAantallen() {return aantallen;}
    public int getTotaal(){return totaal;}

}

/**
 * Exception voorals er een niet bekend nucleotide wordt gecontroleerd
 */
class NotANucleotide extends Exception{

    public NotANucleotide(){
        super();
    }

    public NotANucleotide(String fouteNucleotide){
        super("Onbekende nucleotide gevonden: "+fouteNucleotide);
    }
}
