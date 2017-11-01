package toets;

import jdk.nashorn.internal.runtime.OptimisticReturnFilters;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
/*
#########################################################################
# Toetscode:     Praktische Opdracht kans 1 Bi5a-O Course 3a 2017-2018  #
# Datum:         01 november 2017                                       #
# Gemaakt door:  Alex Janse                                             #
# Studenten nr:  577754                                                 #
# Klas ID:       BIN-2B                                                 #
# School:        Hogeschool Arnhem en Nijmegen (HAN)                    #
# Opleiding:     Bio-Informatica                                        #
#-----------------------------------------------------------------------#
# Opmerking:    "Mocht mijn code lijken op die van andere dan           #
#               is dat te verklaren doordat ik veel heb samengewerkt    #
#               met andere studenten van leerjaar 2.                    #
#               Hierbij wil ik met name benadrukken dat ik dit          #
#               blok vooral Damian Bolwerk heb geholpen en met          #
#               hem de grootste kans op is dat de codes op elkaar       #
#               lijken."                                                #
# Opmerking2:   "In het programma heb ik op drie manieren commentaar    #
#               gegeven. 1: Boven elke methode voor de JavaDoc,         #
#               2: tussendoor met commentaar over opvolgende regels     #
#               en 3: Rechts naast sommige regels als de reden of       #
#               functie niet duidelijk genoegd is."                     #
# Opmerking3:   "Na het uitvoeren van het programma waarbij er          #
#               gebruik gemaakt is van de filechooser verschijnt        #
#               na het sluiten van het programma een error met          #
#               Exception while removing reference. Onderbergeleiding   #
#               van Martijn Liebrand heb ik de exception mogen          #
#               googlen maar kon helaas geen oplossing voor dit         #
#               probleem vinden. Ook kon ik deze exception niet         #
#               opvangen. Verder dan de melding heeft het geen          #
#               invloed op de performance van mijn programma."          #
#########################################################################
 */
/**
 * Class om de applicatie te visualiseren en methodes aan roepen.
 * @author Alex Janse
 * @since 01-11-2017
 * @version 1.00
 */
public class NucleoPie extends JFrame implements ActionListener{

    private JButton openButton, kiesBestandButton, percentageButton;
    private JTextField bestandTextField;
    private JTextArea headerArea, sequentieArea, statusArea, emotieArea, omschrijving;
    private JPanel piePanel, titelPanel, resultPanel,
            headerPanel, sequentiePanel, bestandPanel, statusPanel;
    private JScrollPane headerScrollPane, sequentieScrollPane, statusScrollPane;
    private JLabel titel;
    private Bestand bestand;
    private int statusCounter = 0;
    private Nucleotiden nucleotiden;

    /**
     * Roept de functies en methodes aan om de applicatie op te starten.
     */
    public static void main(String[] args) {
        NucleoPie frame = new NucleoPie();
        frame.setSize(600, 960);
        frame.setResizable(false);
        frame.setTitle("NucleoPie");

        // Exception handeling voor errors die ik niet heb opgevangen of had verwacht.
        try{
        frame.createGUI();
        } catch (Exception error){
            JOptionPane.showMessageDialog(null,"Er heeft een onbekende erro plaatst gevonden\n" +
                    "Neem contact op met de sofware developer Alex Janse voor oplossingen voor: "+error.toString());
        }
        frame.setVisible(true);
    }

    /**
     * Methode om de GUI op te bouwen.
     */
    private void createGUI() throws Exception{

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Sluit het programma als de gebruiker op X klikt

        // Maak de achtergrond van de GUI aan.
        Container window = getContentPane();
        window.setBackground(Color.black);
        window.setLayout(new FlowLayout() );


        // Maak de panel aan met de titel en omschrijving van de applicatie.
        titelPanel = new JPanel();
        titelPanel.setPreferredSize(new Dimension(600,100));
        titelPanel.setBackground(Color.black);
        window.add(titelPanel);

        titel = new JLabel("NucleoPie");
        titel.setForeground(Color.red);
        titel.setBackground(Color.black);
        titel.setFont(new Font("TimesRoman",Font.BOLD,50));
        titelPanel.add(titel);

        omschrijving = new JTextArea("Dit programma leest fasta bestanden en maakt een piechart van je sequentie!.");
        omschrijving.setForeground(Color.white);
        omschrijving.setBackground(Color.black);
        omschrijving.setFont(new Font("TimesRoman",Font.BOLD,12));
        titelPanel.add(omschrijving);


        // Maakt de panel aan waarin de errors en successen in komen te staan.
        statusPanel = new JPanel();
        statusPanel.setBackground(Color.black);
        statusPanel.setPreferredSize(new Dimension(580,130));
        statusPanel.setLayout(new FlowLayout());
        statusPanel.setBorder(BorderFactory.createTitledBorder(
                null,"Status", TitledBorder.CENTER,
                TitledBorder.TOP,new Font("Ariel",Font.BOLD,20),Color.white));
        window.add(statusPanel);

        emotieArea = new JTextArea(Emotie.getWelkom());
        emotieArea.setFont(new Font("Courier new",Font.BOLD,15));
        emotieArea.setBackground(Color.black);
        emotieArea.setForeground(Color.cyan);
        statusPanel.add(emotieArea);

        statusArea = new JTextArea("Welkom "+System.getProperty("user.name")+"!"+
                "\nKies een bestand of voer een nucleotide sequentie in.");
        statusArea.setFont(new Font("Courier new",Font.BOLD,15));
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);                                      // Dit zorgt ervoor dat als de regel dat wordt ingevoerd langer is dan de grote van de textarea het dan doorgaat op de volgende regel
        statusArea.setBackground(Color.black);
        statusArea.setForeground(Color.cyan);

        statusScrollPane = new JScrollPane(statusArea);
        statusScrollPane.setPreferredSize(new Dimension(300,80));
        statusScrollPane.setBorder(null);                                       // Verwijderd de rand om de scrollPane
        statusPanel.add(statusScrollPane);


        // Maakt de panel aan waarin er gekozen kan worden voor een bestand te openen.
        bestandPanel = new JPanel();
        bestandPanel.setBackground(Color.black);
        bestandPanel.setPreferredSize(new Dimension(580,100));
        bestandPanel.setLayout(new GridLayout(3,1));
        window.add(bestandPanel);

        bestandTextField = new JTextField("",30);
        bestandTextField.setEnabled(false);                                     // Voorkomt dat de gebruiker de pathway kan veranderen om zo de kans op errors te verminderen.
        bestandTextField.setFont(new Font("Ariel",Font.BOLD,14));
        bestandTextField.setBorder(null);
        bestandTextField.setBackground(Color.black);
        bestandTextField.setForeground(Color.white);
        bestandPanel.add(bestandTextField);

        kiesBestandButton = new JButton("Kies een bestand");
        kiesBestandButton.setBackground(Color.cyan);
        kiesBestandButton.setForeground(Color.black);
        kiesBestandButton.addActionListener(this);
        bestandPanel.add(kiesBestandButton);
        
        openButton = new JButton("Open");
        openButton.setBackground(Color.green);
        openButton.setForeground(Color.black);
        openButton.addActionListener(this);
        bestandPanel.add(openButton);

        bestandPanel.setBorder(BorderFactory.createTitledBorder(null,"Bestand:", TitledBorder.CENTER,
                TitledBorder.TOP,new Font("Ariel",Font.BOLD,20),Color.white));


        // Maakt de panel aan waarin de header van het bestand kan worden weergegeven en worden aangepast.
        headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createTitledBorder(null,"Header:", TitledBorder.CENTER,
                TitledBorder.TOP,new Font("Ariel",Font.BOLD,20),Color.white));
        headerPanel.setPreferredSize(new Dimension(580,100));
        headerPanel.setBackground(Color.black);
        window.add(headerPanel);

        headerArea = new JTextArea(2,30);
        headerArea.setBackground(Color.black);
        headerArea.setForeground(Color.white);
        headerArea.setFont(new Font("Ariel",Font.BOLD,14));
        headerArea.setLineWrap(true);
        headerArea.setWrapStyleWord(true);
        headerScrollPane = new JScrollPane(headerArea);
        headerScrollPane.setPreferredSize(new Dimension(550,50));
        headerPanel.add(headerScrollPane);

        // Maakt de panel aan waarin de sequentie van het bestand kan worden weergegeven en worden aangepast.
        sequentiePanel = new JPanel();
        sequentiePanel.setBorder(BorderFactory.createTitledBorder(null,"Sequentie:", TitledBorder.CENTER,
                TitledBorder.TOP,new Font("Ariel",Font.BOLD,20),Color.white));
        sequentiePanel.setPreferredSize(new Dimension(580,150));
        sequentiePanel.setBackground(Color.black);
        window.add(sequentiePanel);
        
        sequentieArea = new JTextArea(3,30);
        sequentieArea.setBackground(Color.black);
        sequentieArea.setForeground(Color.white);
        sequentieArea.setFont(new Font("Ariel",Font.BOLD,14));
        sequentieArea.setLineWrap(true);
        sequentieArea.setWrapStyleWord(true);
        sequentieScrollPane = new JScrollPane(sequentieArea);
        sequentieScrollPane.setPreferredSize(new Dimension(550,75));
        sequentiePanel.add(sequentieScrollPane);

        percentageButton = new JButton("Bereken percentage");
        percentageButton.addActionListener(this);
        percentageButton.setBackground(Color.YELLOW);
        sequentiePanel.add(percentageButton);


        // Maakt de panel aan waarin de resultaten worden weergegeven
        resultPanel = new JPanel();
        resultPanel.setPreferredSize(new Dimension(580,310));
        resultPanel.setBackground(Color.black);
        window.add(resultPanel);

        piePanel = new JPanel();
        piePanel.setBackground(Color.black);
        piePanel.setPreferredSize(new Dimension(570,260));
        resultPanel.add(piePanel);

        resultPanel.setBorder(BorderFactory.createTitledBorder(null,"Resultaat:", TitledBorder.CENTER,
                TitledBorder.TOP,new Font("Ariel",Font.BOLD,20),Color.white));

        // Dit zorgt ervoor dat de applicatie in het midden van het scherm opent.
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

    }

    /**
     * Methode die andere methodes aangeroept als de gebruiker een knop indrukt
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        // Exception handeling voor errors die ik niet heb opgevangen of had verwacht.
        try {
            if (source == kiesBestandButton) {
                openBestand();
            } else if (source == openButton) {
                leesBestand();
            } else if (source == percentageButton &&
                    sequentieArea.getText() != null &&
                    controleer()) {
                tekenPieChart();
            } // Analyseer alleen als er iets in de sequentie veld staat en er gecontroleerd is dat het alleen nucleotiden bevat.

        } catch (Exception error){
            JOptionPane.showMessageDialog(null,"Er heeft een onbekende erro plaatst gevonden\n" +
                    "Neem contact op met de sofware developer Alex Janse voor oplossingen voor: "+error.toString());
        }
    }

    /**
     * Methode om de Bestand class te gebruiken om bestanden te openen en eventuele errors op te vangen
     */
    private void openBestand()throws Exception{
        bestand = new Bestand();
        try {
            bestand.kiesBestand("fasta");
        } catch (NotFileTypeError error){
            setStatusKleur(true);
            statusArea.append("\n*****************************" +
                    "\n"+(++statusCounter)+". "+error.toString());
        }
        bestandTextField.setText(bestand.getPathBestand()); // Toont de pathway naar het bestand in de applicatie
        if (bestand.getBestand() != null) {
            setStatusKleur(false);
            statusArea.append("\n*****************************" +
                    "\n" + (++statusCounter) + ". Het bestand is gevonden."); // Vertel de gebruiker dat het is gelukt
        }
    }

    /**
     * Methode om de inhoud van het bestand op te halen en te plaatsen in de header en sequentie textarea. 
     */
    private void leesBestand()throws Exception{
        String regel = "",
                inhoud = "";
        String[] inhoudArray;

        if (bestand.getBestand() != null) {
            try {
                inhoud = bestand.leesBestand();

                // Om de voorkomen dat het programma's niet pakt waar per ongeluk een \n in het begin staat.
                while (inhoud.startsWith("\n")){
                    inhoud = inhoud.substring(1,inhoud.length());
                }

                if (inhoud.startsWith(">")){
                    inhoudArray = inhoud.split("\n");
                    headerArea.setText(inhoudArray[0]);

                    for (int index = 1; index < inhoudArray.length; index++){
                        regel = inhoudArray[index];
                        sequentieArea.append(regel);
                    }
                    setStatusKleur(false);
                    statusArea.append("\n*****************************" +
                            "\n"+(++statusCounter)+". Het bestand is geopend en doorgelezen."); // Vertel de gebruiker dat het is gelukt
                } else {
                    setStatusKleur(true);
                    statusArea.append("\n*****************************" +
                            "\n"+(++statusCounter)+". Het bestand bevat geen header." +
                            "\nControleer uw bestand en probeer opnieuw."); // Vertel de gebruiker dat het is gelukt
                }
                
            }
            catch (NoFileInObject error) {
                setStatusKleur(true);
                statusArea.append("\n*****************************" +
                        "\n"+(++statusCounter)+". "+error.toString());
            } catch (FileNotFoundException error) {
                setStatusKleur(true);
                statusArea.append("\n*****************************" +
                        "\n"+(++statusCounter)+". Helaas is uw bestand niet gevonden.");
            } catch (IOException error) {
                setStatusKleur(true);
                statusArea.append("\n*****************************" +
                        "\n"+(++statusCounter)+".Er is een error ontstaan bij het verwerken van uw bestand." +
                        "\n Controleer de inhoud en probeer opnieuw.");
            }
        }
    }

    /**
     * Methode om te controleren of er alleen nucleotiden in de sequentieArea staan.
     * @return een boolean of de sequentie wel of geen vreemde tekens bevat.
     */
    private boolean controleer()throws Exception{
        char[] sequentie = sequentieArea.getText().toUpperCase().toCharArray();
        nucleotiden = new Nucleotiden();

        try {
            for(char nucleotide : sequentie){
                nucleotiden.controleerNucleotide(nucleotide);
            }
            return true;
        } catch (NotANucleotide error){
            setStatusKleur(true);
            statusArea.append("\n*****************************" +
                    "\n"+(++statusCounter)+". "+error.toString());
        }
        return false;
    }


    /**
     * Methode om de percentage van de nucleotiden te berekenen en weer te geven in een piechart.
     */
    private void tekenPieChart()throws Exception{
        int totaal = nucleotiden.getTotaal(),
                hoek  = 0,
                x = 50, y = 25, width = 200, height = 200, startAngle = 100,
                legendaX = 300, legendaY = 50;
        double percentage = 0.0;
        String legendaNucleotide = "";
        Integer[] aantallen = nucleotiden.getAantallen();


        Graphics paper = piePanel.getGraphics();

        // Vlak overeen tekenen voor als er een nieuwe piechart getekent moet worden tijdens de zelfde sessie.
        paper.setColor(Color.black);
        paper.fillRect(0,0,piePanel.getWidth(),piePanel.getHeight());

        // Legenda start opmaak
        paper.setColor(Color.white);
        paper.setFont(new Font("Ariel", Font.BOLD,18));
        paper.drawString("Legenda:",legendaX,legendaY);
        legendaY += 19; // Y naar benenden zodat de onderderdelen onder de titel komen te staan
        paper.setFont(new Font("Ariel", Font.BOLD,16));

        // Tijdens deze loop worden de percentages en hoek graden berekent en getekend in de piePanel.
        for (int counter = 0; counter < aantallen.length; counter++){
            percentage = (double) aantallen[counter]/totaal;
            hoek = (int)Math.round(percentage*360);
            switch (counter){

                case 0:
                    paper.setColor(Color.green);
                    legendaNucleotide = "A";
                    break;

                case 1:
                    paper.setColor(Color.red);
                    legendaNucleotide = "C";
                    break;

                case 2:
                    paper.setColor(Color.blue);
                    legendaNucleotide = "G";
                    break;

                case 3:
                    paper.setColor(Color.yellow);
                    legendaNucleotide = "T";
                    break;
            }
            paper.fillArc(x,y,width,height,startAngle,hoek);
            startAngle += hoek;

            paper.fillRect( legendaX,legendaY-18,20,20);

            paper.setColor(Color.white);
            paper.drawString(legendaNucleotide+": "+(int)Math.round(percentage*100)+"%",legendaX+22,legendaY);
            legendaY += 20;

        }
        paper.drawString("Lengte sequentie: "+totaal,legendaX+22,legendaY);
    }



    /**
     * Methode om de kleur van de emotie en status tekst te veranderen aan de hand of het een error of een succes is
     * @param error : een boolean waarin staat of de als laatst een error bevat
     */
    private void setStatusKleur(Boolean error)throws Exception{
        if (error){
            emotieArea.setText(Emotie.getError());
            emotieArea.setForeground(Color.white);
            emotieArea.setBackground(Color.red);
            statusArea.setForeground(Color.red);
            statusArea.setCaretPosition(statusArea.getDocument().getLength()); //Zorgt ervoor dat de tekst area automatisch naar beneden scrollt.
        } else {
            emotieArea.setText(Emotie.getGelukt());
            emotieArea.setForeground(Color.green);
            emotieArea.setBackground(Color.black);
            statusArea.setForeground(Color.green);
            statusArea.setCaretPosition(statusArea.getDocument().getLength());
        }
    }
}


