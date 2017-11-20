import org.junit.Test;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class TestDemandeDeLivraison {
    @Test
    public final void TestPlan() {
        Point[] points = {
                new Point(1, 2),
                new Point(2, 3),
                new Point(3, 4),
                new Point(4, 5),
                new Point(5, 6),
                new Point(6, 7)
        };

        Troncon[] troncons = {
                new Troncon(points[0], points[1],1),
                new Troncon(points[0], points[2],9),
                new Troncon(points[0], points[5],14),
                new Troncon(points[1], points[2],10),
                new Troncon(points[1], points[3],15),
                new Troncon(points[2], points[5],2),
                new Troncon(points[2], points[3],11),
                new Troncon(points[5], points[4],9),
                new Troncon(points[4], points[3],6),
                new Troncon(points[5], points[0],2)
        };
        List<Point> livraisons = new ArrayList<>();
        points[2].setLivraison(new Livraison(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), 3));
        points[5].setLivraison(new Livraison(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), 3));
        livraisons.add(points[2]);
        livraisons.add(points[5]);
        Entrepot entrepot = new Entrepot();

        points[0].setEntrepot(entrepot);

        Plan plan = new Plan(points, troncons);

        HashMap<Point, List<Troncon>> graph = plan.getGraph();

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(livraisons, points[0], LocalDateTime.now());

        Tournee tournee = demandeDeLivraison.calculerTournee(plan);


        List<Itineraire> itineraires = new ArrayList<>();
        itineraires.add(new Itineraire(Arrays.asList(new Troncon(points[0], points[2], 9))));
        itineraires.add(new Itineraire(Arrays.asList(new Troncon(points[2], points[5], 2))));
        itineraires.add(new Itineraire(Arrays.asList(new Troncon(points[5], points[0], 2))));
        Tournee validTournee = new Tournee(itineraires);
        assertEquals(validTournee, tournee);
    }
}
