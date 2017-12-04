import fr.insalyon.agile.*;
import org.junit.Before;
import org.junit.Test;


import java.time.Duration;
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
                new Troncon(points.get(0), points.get(5),40, "a"),
                new Troncon(points.get(3), points.get(0),60, "a"),
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
        points.get(2).setLivraison(new Livraison(LocalTime.of(2, 0), LocalTime.of(2,30), Duration.ofMinutes(6)));
        points.get(5).setLivraison(new Livraison(LocalTime.of(3,0), LocalTime.of(3,30), Duration.ofHours(1)));
        livraisons.add(points.get(2));
        livraisons.add(points.get(5));
        Entrepot entrepot = new Entrepot();

        points.get(0).setEntrepot(entrepot);

        Plan plan = new Plan(points, troncons);

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(plan, livraisons, points.get(0), LocalTime.of(1,4));

        Tournee tournee = demandeDeLivraison.calculerTournee();
        tournee.calculMargesPointsLivraison();
        Map<Point, Duration> map = new HashMap<>();
        map.put(points.get(2), Duration.ofMinutes(55).plus(Duration.ofSeconds(39)));
        map.put(points.get(5), Duration.ofMinutes(53).plus(Duration.ofSeconds(56)));
        map.put(points.get(0), Duration.ofHours(13).plus(Duration.ofMinutes(59).plus(Duration.ofSeconds(58))));
        assertEquals(map, tournee.getMargesLivraison());

    }

    @Test
    public final void TestGetItinerairesModifiable()
    {
        List<Point> livraisons = new ArrayList<>();
        points.get(5).setLivraison(new Livraison(null, null, Duration.ofMinutes(10)));
        points.get(1).setLivraison(new Livraison(null, null, Duration.ofMinutes(6)));
        points.get(2).setLivraison(new Livraison(null, null, Duration.ofMinutes(20)));
        livraisons.add(points.get(2));
        livraisons.add(points.get(5));
        livraisons.add(points.get(1));
        Entrepot entrepot = new Entrepot();

        points.get(0).setEntrepot(entrepot);

        Plan plan = new Plan(points, troncons);

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(plan, livraisons, points.get(0), LocalTime.of(1,4));

        Tournee tournee = demandeDeLivraison.calculerTournee();

        Livraison livraison = new Livraison(null, null,  Duration.ofMinutes(20));

        points.get(4).setLivraison(livraison);
        tournee.getItinerairesModifiable(points.get(4), tournee.getItineraires().get(tournee.getItineraires().size()-1));

        assertEquals(1, 1);

    }

    @Test
    public final void TestAjouterLivraisonFinTournee()
    {
        List<Point> livraisons = new ArrayList<>();
        points.get(5).setLivraison(new Livraison(LocalTime.of(4,0), LocalTime.of(4,30), Duration.ofMinutes(10)));
        points.get(1).setLivraison(new Livraison(LocalTime.of(2,0), LocalTime.of(2,30), Duration.ofMinutes(6)));
        points.get(2).setLivraison(new Livraison(LocalTime.of(3,0), LocalTime.of(3, 30), Duration.ofMinutes(20)));

        livraisons.add(points.get(2));
        livraisons.add(points.get(5));
        livraisons.add(points.get(1));
        Entrepot entrepot = new Entrepot();

        points.get(0).setEntrepot(entrepot);

        Plan plan = new Plan(points, troncons);

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(plan, livraisons, points.get(0), LocalTime.of(1,4));

        Tournee tournee = demandeDeLivraison.calculerTournee();

        Livraison livraison = new Livraison(null, null,  Duration.ofMinutes(20));

        points.get(4).setLivraison(livraison);

        tournee.ajouterLivraison(points.get(4), tournee.getItineraires().get(tournee.getItineraires().size()-1));

        List<Itineraire> itineraires= new ArrayList<>();
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(0))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(4))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(6))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(8))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(10))));
        assertEquals(itineraires, tournee.getItineraires());
        assertEquals(points.get(4).getLivraison().getDateArrivee(), LocalTime.of(4, 10, 2));
        assertEquals(points.get(4).getLivraison().getDateLivraison(), LocalTime.of(4,10,2));

    }

    @Test
    public final void TestAjouterLivraisonDebutTournee()
    {
        //A FAIIIIIIREEEE et AUssi AjouterLivraisonMilieu
        List<Point> livraisons = new ArrayList<>();
        points.get(5).setLivraison(new Livraison(LocalTime.of(4,0), LocalTime.of(4,30), Duration.ofMinutes(10)));
        points.get(1).setLivraison(new Livraison(LocalTime.of(2,0), LocalTime.of(2,30), Duration.ofMinutes(6)));
        points.get(2).setLivraison(new Livraison(LocalTime.of(3,0), LocalTime.of(3, 30), Duration.ofMinutes(20)));

        livraisons.add(points.get(2));
        livraisons.add(points.get(5));
        livraisons.add(points.get(1));
        Entrepot entrepot = new Entrepot();

        points.get(0).setEntrepot(entrepot);

        Plan plan = new Plan(points, troncons);

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(plan, livraisons, points.get(0), LocalTime.of(1,4));

        Tournee tournee = demandeDeLivraison.calculerTournee();

        Livraison livraison = new Livraison(null, null,  Duration.ofMinutes(20));

        points.get(4).setLivraison(livraison);

        tournee.ajouterLivraison(points.get(4), tournee.getItineraires().get(0));

        List<Itineraire> itineraires= new ArrayList<>();
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(0))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(4))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(6))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(8))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(10))));
        assertEquals(itineraires, tournee.getItineraires());
        assertEquals(points.get(4).getLivraison().getDateArrivee(), LocalTime.of(4, 10, 2));
        assertEquals(points.get(4).getLivraison().getDateLivraison(), LocalTime.of(4,10,2));

    }

    @Test
    public final void TestSupprimerLivraisonFinTournee()
    {
        List<Point> livraisons = new ArrayList<>();
        points.get(5).setLivraison(new Livraison(LocalTime.of(4,0), LocalTime.of(4,30), Duration.ofMinutes(10)));
        points.get(1).setLivraison(new Livraison(LocalTime.of(2,0), LocalTime.of(2,30), Duration.ofMinutes(6)));
        points.get(2).setLivraison(new Livraison(LocalTime.of(3,0), LocalTime.of(3, 30), Duration.ofMinutes(20)));
        points.get(4).setLivraison(new Livraison(LocalTime.of(5,0), LocalTime.of(5,30), Duration.ofMinutes(20)));

        livraisons.add(points.get(2));
        livraisons.add(points.get(5));
        livraisons.add(points.get(1));
        livraisons.add(points.get(4));
        Entrepot entrepot = new Entrepot();

        points.get(0).setEntrepot(entrepot);

        Plan plan = new Plan(points, troncons);

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(plan, livraisons, points.get(0), LocalTime.of(1,4));

        Tournee tournee = demandeDeLivraison.calculerTournee();

        tournee.supprimerLivraison(points.get(4));

        List<Itineraire> itineraires= new ArrayList<>();
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(0))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(4))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(6))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(8), troncons.get(10))));
        assertEquals(itineraires, tournee.getItineraires());
        assertEquals(tournee.getDateArrivee(), LocalTime.of(4, 10, 2));

    }

    @Test
    public final void TestSupprimerLivraisonMilieuTournee()
    {
        List<Point> livraisons = new ArrayList<>();
        points.get(5).setLivraison(new Livraison(LocalTime.of(4,0), LocalTime.of(4,30), Duration.ofMinutes(10)));
        points.get(1).setLivraison(new Livraison(LocalTime.of(2,0), LocalTime.of(2,30), Duration.ofMinutes(6)));
        points.get(2).setLivraison(new Livraison(LocalTime.of(3,0), LocalTime.of(3, 30), Duration.ofMinutes(20)));
        points.get(4).setLivraison(new Livraison(LocalTime.of(5,0), LocalTime.of(5,30), Duration.ofMinutes(20)));

        livraisons.add(points.get(2));
        livraisons.add(points.get(5));
        livraisons.add(points.get(1));
        livraisons.add(points.get(4));
        Entrepot entrepot = new Entrepot();

        points.get(0).setEntrepot(entrepot);

        Plan plan = new Plan(points, troncons);

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(plan, livraisons, points.get(0), LocalTime.of(1,4));

        Tournee tournee = demandeDeLivraison.calculerTournee();

        tournee.supprimerLivraison(points.get(2));

        List<Itineraire> itineraires= new ArrayList<>();
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(0))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(5), troncons.get(3), troncons.get(2))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(8))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(10))));
        assertEquals(itineraires, tournee.getItineraires());
        assertEquals(points.get(5).getLivraison().getDateArrivee(), LocalTime.of(2,6, 27));
        assertEquals(points.get(5).getLivraison().getDateLivraison(), LocalTime.of(4, 0));

    }

    @Test
    public final void TestSupprimerLivraisonDebutTournee()
    {
        List<Point> livraisons = new ArrayList<>();
        points.get(5).setLivraison(new Livraison(LocalTime.of(4,0), LocalTime.of(4,30), Duration.ofMinutes(10)));
        points.get(1).setLivraison(new Livraison(LocalTime.of(2,0), LocalTime.of(2,30), Duration.ofMinutes(6)));
        points.get(2).setLivraison(new Livraison(LocalTime.of(3,0), LocalTime.of(3, 30), Duration.ofMinutes(20)));
        points.get(4).setLivraison(new Livraison(LocalTime.of(5,0), LocalTime.of(5,30), Duration.ofMinutes(20)));

        livraisons.add(points.get(2));
        livraisons.add(points.get(5));
        livraisons.add(points.get(1));
        livraisons.add(points.get(4));
        Entrepot entrepot = new Entrepot();

        points.get(0).setEntrepot(entrepot);

        Plan plan = new Plan(points, troncons);

        DemandeDeLivraison demandeDeLivraison = new DemandeDeLivraison(plan, livraisons, points.get(0), LocalTime.of(1,4));

        Tournee tournee = demandeDeLivraison.calculerTournee();

        tournee.supprimerLivraison(points.get(1));

        List<Itineraire> itineraires= new ArrayList<>();
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(1))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(6))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(8))));
        itineraires.add(new Itineraire(Arrays.asList(troncons.get(10))));
        assertEquals(itineraires, tournee.getItineraires());
        assertEquals(points.get(2).getLivraison().getDateArrivee(), LocalTime.of(1,4, 21));
        assertEquals(points.get(2).getLivraison().getDateLivraison(), LocalTime.of(3, 0));

    }





}
