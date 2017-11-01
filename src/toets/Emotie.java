package toets;

/**
 * Class om emoties te retourneren om succes te vieren en de pijn van errors te verzachten.
 * @author Alex Janse
 * @since 01-11-2017
 * @version 1.00
 */
public class Emotie {

    private static final String GELUKT = "  (\\__/)\n"+
                                         "  (^__^)\n"+
                                         "┌-0----0-┐\n"+
                                         "| Gelukt!|\n"+
                                         "└--------┘",
                                ERROR = "  (\\__/)\n"+
                                        "  (>__<)\n"+
                                        "┌-0----0-┐\n"+
                                        "| Error! |\n"+
                                        "└--------┘",
                                WELKOM = "  (\\__/)\n"+
                                        "  (^__^)\n"+
                                        "┌-0----0-┐\n"+
                                        "| Welkom!|\n"+
                                        "└--------┘";

    public static String getGelukt(){return GELUKT;}
    public static String getError(){return ERROR;}
    public static String getWelkom(){return WELKOM;}
}
