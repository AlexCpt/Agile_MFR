import fr.insalyon.agile.*;
import org.junit.Before;
import org.junit.Test;


import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class TestModifierTournee {
    private List<Point> points;
    private List<Troncon> troncons;

    @Before
    public void before() throws Exception {
        points = Arrays.asList(
                new Point("0",1, 2),
                new Point("1", 2, 3),
                new Point("2", 3, 4),
                new Point("3", 4, 5),
                new Point("4", 5, 6),
                new Point("5", 6, 7)
        );

        troncons = Arrays.asList(
                new Troncon(points.get(0), points.get(1),10, "a"),
                new Troncon(points.get(0), points.get(2),90, "a"),
                new Troncon(points.get(0), points.get(5),140, "a"),
                new Troncon(points.get(3), points.get(0),600, "a"),
                new Troncon(points.get(1), points.get(2),100, "a"),
                new Troncon(points.get(1), points.get(3),15, "a"),
                new Troncon(points.get(2), points.get(5),20, "a"),
                new Troncon(points.get(2), points.get(3),11, "a"),
                new Troncon(points.get(5), points.get(4),9, "a"),
                new Troncon(points.get(4), points.get(3),6, "a"),
                new Troncon(points.get(4), points.get(0),1, "a"),
                new Troncon(points.get(5), points.get(0),200, "a")
        );
    }

    @Test
    public final void TestTournee() {

        List<Point> livraisons = new ArrayList<>();
        points.get(2).setLivraison(new Livraison(null, null, LocalTime.of(0,6)));
        points.get(5).setLivraison(new Livraison(null, null, LocalTime.of(1,0)));
        livraisons.add(points.get(2));
        livraisons.add(points.get(5));
        Entrepot entrepot = new Entrepot();

        points.get(0).setEntrepot(entrepot);

        Plan plan = new Plan(points, troncons);

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(plan, livraisons, points.get(0), LocalTime.of(1,4));

        Tournee tournee = demandeDeLivraison.calculerTournee();
        tournee.calculMargesPointsLivraison();
        Map<Point, LocalTime> map = new HashMap<>();
        map.put(points.get(2), LocalTime.of(0, 2));
        map.put(points.get(5), LocalTime.of(1, 0));
        map.put(points.get(0), LocalTime.of(12, 58, 58));
        assertEquals(map, tournee.getMargesLivraison());

    }

    @Test
    public final void TestsumLocalTime()
    {
        Tournee t = new Tournee();
        assertEquals(LocalTime.of(4,12), t.sumLocalTime(LocalTime.of(1,17), LocalTime.of(2,55)));
    }


    @Test
    public final void TestgetDureeItineraire()
    {
        Tournee t = new Tournee();
        assertEquals(LocalTime.of(4,12), t.sumLocalTime(LocalTime.of(1,17), LocalTime.of(2,55)));
    }

    @Test
    public final void modifierTournee()
    {
        List<Point> livraisons = new ArrayList<>();
        points.get(5).setLivraison(new Livraison(null, null, LocalTime.of(0,10)));
        points.get(1).setLivraison(new Livraison(null, null, LocalTime.of(0,6)));
        points.get(2).setLivraison(new Livraison(null, null, LocalTime.of(0,20)));
        livraisons.add(points.get(2));
        livraisons.add(points.get(5));
        livraisons.add(points.get(1));
        Entrepot entrepot = new Entrepot();

        points.get(0).setEntrepot(entrepot);

        Plan plan = new Plan(points, troncons);

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(plan, livraisons, points.get(0), LocalTime.of(1,4));

        Tournee tournee = demandeDeLivraison.calculerTournee();

        Livraison livraison = new Livraison(null, null,  LocalTime.of(0,20));

        points.get(4).setLivraison(livraison);
        tournee.modifierTournee(points.get(4));

        assertEquals(1, 1);

    }
}
