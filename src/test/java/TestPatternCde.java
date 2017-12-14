import fr.insalyon.agile.designpattern.command.CdeAjout;
import fr.insalyon.agile.designpattern.command.CdeSupprime;
import fr.insalyon.agile.designpattern.command.ListeDeCdes;
import fr.insalyon.agile.modele.*;
import javafx.scene.layout.Pane;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class TestPatternCde {
    private List<Point> points;
    private List<Troncon> troncons;
    private Plan plan;

    @Before
    public void before() throws Exception {
        points = Arrays.asList(
                new Point("0", 1, 2),
                new Point("1", 2, 3),
                new Point("2", 3, 4),
                new Point("3", 4, 5),
                new Point("4", 5, 6),
                new Point("5", 6, 7)
        );

        troncons = Arrays.asList(
                new Troncon(points.get(0), points.get(1), 10, "a"),
                new Troncon(points.get(0), points.get(2), 90, "a"),
                new Troncon(points.get(0), points.get(5), 40, "a"),
                new Troncon(points.get(3), points.get(0), 60, "a"),
                new Troncon(points.get(1), points.get(2), 100, "a"),
                new Troncon(points.get(1), points.get(3), 15, "a"),
                new Troncon(points.get(2), points.get(5), 20, "a"),
                new Troncon(points.get(2), points.get(3), 11, "a"),
                new Troncon(points.get(5), points.get(4), 9, "a"),
                new Troncon(points.get(4), points.get(3), 6, "a"),
                new Troncon(points.get(4), points.get(0), 1, "a"),
                new Troncon(points.get(5), points.get(0), 200, "a")
        );

        points.get(0).setEntrepot(new Entrepot());

        plan = new Plan(points, troncons);
    }

    @Test
    public void TestPatternCde() {

        List<Point> livraisons = new ArrayList<>();
        points.get(5).setLivraison(new Livraison(LocalTime.of(4, 0), LocalTime.of(4, 30), Duration.ofMinutes(10)));
        points.get(1).setLivraison(new Livraison(LocalTime.of(2, 0), LocalTime.of(2, 30), Duration.ofMinutes(6)));
        points.get(2).setLivraison(new Livraison(LocalTime.of(3, 0), LocalTime.of(3, 30), Duration.ofMinutes(20)));
        livraisons.add(points.get(2));
        livraisons.add(points.get(5));
        livraisons.add(points.get(1));

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(plan, livraisons, points.get(0), LocalTime.of(1, 4));

        Tournee tournee = demandeDeLivraison.calculerTournee();

        ListeDeCdes listeDeCdes=new ListeDeCdes();
        if(tournee.getItinerairesModifiable(points.get(4),Duration.ofMinutes(20),tournee.getItineraires().get(tournee.getItineraires().size()-1))) {

            listeDeCdes.ajoute(new CdeAjout(tournee, points.get(4),Duration.ofMinutes(20), tournee.getItineraires().get(tournee.getItineraires().size()-1), new Pane()));

        }


        List<Itineraire> itineraires= new ArrayList<>();
        itineraires.add(new Itineraire(Collections.singletonList(troncons.get(0))));
        itineraires.add(new Itineraire(Collections.singletonList(troncons.get(4))));
        itineraires.add(new Itineraire(Collections.singletonList(troncons.get(6))));
        itineraires.add(new Itineraire(Collections.singletonList(troncons.get(8))));
        itineraires.add(new Itineraire(Collections.singletonList(troncons.get(10))));
        assertEquals(itineraires, tournee.getItineraires());


        listeDeCdes.ajoute(new CdeSupprime(tournee, points.get(1), null));

        List<Itineraire> itineraires2= new ArrayList<>();
        itineraires2.add(new Itineraire(Collections.singletonList(troncons.get(1))));
        itineraires2.add(new Itineraire(Collections.singletonList(troncons.get(6))));
        itineraires2.add(new Itineraire(Collections.singletonList(troncons.get(8))));
        itineraires2.add(new Itineraire(Collections.singletonList(troncons.get(10))));

        assertEquals(itineraires2, tournee.getItineraires());

        listeDeCdes.undo();

        assertEquals(itineraires, tournee.getItineraires());

        listeDeCdes.redo();

        assertEquals(itineraires2, tournee.getItineraires());
    }

    @Test
    public void TestPatternCde2() {

        List<Point> livraisons = new ArrayList<>();
        points.get(1).setLivraison(new Livraison(LocalTime.of(2, 0), LocalTime.of(2, 30), Duration.ofMinutes(6)));
        livraisons.add(points.get(1));

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(plan, livraisons, points.get(0), LocalTime.of(1, 4));

        Tournee tournee = demandeDeLivraison.calculerTournee();

        ListeDeCdes listeDeCdes=new ListeDeCdes();
        if(tournee.getItinerairesModifiable(points.get(2),Duration.ofMinutes(20), tournee.getItineraires().get(tournee.getItineraires().size()-1))) {

            listeDeCdes.ajoute(new CdeAjout(tournee, points.get(2), Duration.ofMinutes(20), tournee.getItineraires().get(tournee.getItineraires().size()-1), new Pane()));

        }

        if(tournee.getItinerairesModifiable(points.get(5),Duration.ofMinutes(20), tournee.getItineraires().get(tournee.getItineraires().size()-1))) {

            listeDeCdes.ajoute(new CdeAjout(tournee, points.get(5), Duration.ofMinutes(20), tournee.getItineraires().get(tournee.getItineraires().size()-1), null));

        }

        if(tournee.getItinerairesModifiable(points.get(4), Duration.ofMinutes(20), tournee.getItineraires().get(tournee.getItineraires().size()-1))) {

            listeDeCdes.ajoute(new CdeAjout(tournee, points.get(4), Duration.ofMinutes(20), tournee.getItineraires().get(tournee.getItineraires().size()-1), null));

        }

        listeDeCdes.undo();
        listeDeCdes.undo();

        if(tournee.getItinerairesModifiable(points.get(3), Duration.ofMinutes(20), tournee.getItineraires().get(tournee.getItineraires().size()-1))) {

            listeDeCdes.ajoute(new CdeAjout(tournee, points.get(3), Duration.ofMinutes(20), tournee.getItineraires().get(tournee.getItineraires().size()-1), null));

        }

        List<Itineraire> itineraires= new ArrayList<>();
        itineraires.add(new Itineraire(Collections.singletonList(troncons.get(0))));
        itineraires.add(new Itineraire(Collections.singletonList(troncons.get(4))));
        itineraires.add(new Itineraire(Collections.singletonList(troncons.get(7))));
        itineraires.add(new Itineraire(Collections.singletonList(troncons.get(3))));


        assertEquals(itineraires, tournee.getItineraires());
    }
}
