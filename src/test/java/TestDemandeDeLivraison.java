import org.junit.Test;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class TestDemandeDeLivraison {
    @Test
    public final void TestPlan() {
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
        points.get(2).setLivraison(new Livraison(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), 3));
        points.get(5).setLivraison(new Livraison(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), 3));
        livraisons.add(points.get(2));
        livraisons.add(points.get(5));
        Entrepot entrepot = new Entrepot();

        points.get(0).setEntrepot(entrepot);

        Plan plan = new Plan(points, troncons);

        HashMap<Point, List<Troncon>> graph = plan.getGraph();

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(livraisons, points.get(0), new Date());

        Tournee tournee = demandeDeLivraison.calculerTournee(plan);


        List<Itineraire> itineraires = new ArrayList<>();
        itineraires.add(new Itineraire(Arrays.asList(new Troncon(points.get(0), points.get(2), 9, "a"))));
        itineraires.add(new Itineraire(Arrays.asList(new Troncon(points.get(2), points.get(5), 2, "a"))));
        itineraires.add(new Itineraire(Arrays.asList(new Troncon(points.get(5), points.get(0), 2, "a"))));
        Tournee validTournee = new Tournee(itineraires);
        assertEquals(validTournee, tournee);
    }
}
