import static java.lang.Thread.sleep;

public class Profiling {
    public static void main(String[] args) {
        ParserXML parser = new ParserXML();

        Plan plan = parser.parsePlan("fichiersXML/planLyonGrand.xml");

        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DemandeDeLivraison ddl = parser.parseDemandeDeLivraison("fichiersXML/DLgrand10.xml");
        Tournee tournee = ddl.calculerTournee(plan);
    }
}
