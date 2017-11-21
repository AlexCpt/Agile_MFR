import java.util.ArrayList;
import java.util.List;

public class Main  {

    public static void main(String[] args) {

        ParserXML parse = new ParserXML();
        parse.parse("fichiersXML/planLyonPetit.xml");
        parse.chargerLivraison("fichiersXML/DLpetit3.xml");
    }
}
