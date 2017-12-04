import fr.insalyon.agile.*;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class TestDemandeDeLivraison {
    @Test
    public final void TestDemandeDeLivraison() {
        List<Point> points = Arrays.asList(
                new Point("0",1, 2),
                new Point("1", 2, 3),
                new Point("2", 3, 4),
                new Point("3", 4, 5),
                new Point("4", 5, 6),
                new Point("5", 6, 7)
        );

        List<Troncon> troncons = Arrays.asList(
                new Troncon(points.get(0), points.get(1),1, "a"),
                new Troncon(points.get(0), points.get(2),9, "a"),
                new Troncon(points.get(0), points.get(5),14, "a"),
                new Troncon(points.get(1), points.get(2),10, "a"),
                new Troncon(points.get(1), points.get(3),15, "a"),
                new Troncon(points.get(2), points.get(5),2, "a"),
                new Troncon(points.get(2), points.get(3),11, "a"),
                new Troncon(points.get(5), points.get(4),9, "a"),
                new Troncon(points.get(4), points.get(3),6, "a"),
                new Troncon(points.get(5), points.get(0),2, "a")
        );
        List<Point> livraisons = new ArrayList<>();
        points.get(2).setLivraison(new Livraison(null, null, Duration.ZERO));
        points.get(5).setLivraison(new Livraison(null, null,  Duration.ZERO));
        livraisons.add(points.get(2));
        livraisons.add(points.get(5));
        Entrepot entrepot = new Entrepot();

        points.get(0).setEntrepot(entrepot);

        Plan plan = new Plan(points, troncons);

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(plan, livraisons, points.get(0), LocalTime.of(0, 0, 0));

        Tournee tournee = demandeDeLivraison.calculerTournee();

        List<Itineraire> itineraires = new ArrayList<>();
        itineraires.add(new Itineraire(Arrays.asList(new Troncon(points.get(0), points.get(2), 9, "a"))));
        itineraires.add(new Itineraire(Arrays.asList(new Troncon(points.get(2), points.get(5), 2, "a"))));
        itineraires.add(new Itineraire(Arrays.asList(new Troncon(points.get(5), points.get(0), 2, "a"))));
        Tournee validTournee = new Tournee(itineraires, LocalTime.of(0, 0, 2), demandeDeLivraison);

        assertEquals(validTournee, tournee);
    }
}
