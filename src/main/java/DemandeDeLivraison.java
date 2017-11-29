import javafx.util.Pair;
import tsp.TSP3;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DemandeDeLivraison {

    List<Point> mLivraisons;
    Point mEntrepot;
    LocalTime mDepart;

    public DemandeDeLivraison(List<Point> livraisons, Point entrepot, LocalTime depart) {
        this.mLivraisons = livraisons;
        this.mEntrepot = entrepot;
        this.mDepart = depart;
    }

    public Tournee calculerTournee(Plan plan){
        HashMap<Pair<Point, Point>, Itineraire> itineraireHashMap = new HashMap<>();

        int nombreSommets = mLivraisons.size() + 1;

        int[][] couts = new int[nombreSommets][nombreSommets];
        int[] duree = new int[nombreSommets];

        Point[] sommets = new Point[nombreSommets];
        sommets[0] = mEntrepot;
        for (int i = 0; i < mLivraisons.size(); i++) {
            sommets[i+1] = mLivraisons.get(i);
        }

        List<Itineraire> currentItineraires;
        for (int i = 0; i < sommets.length; i++) {
            currentItineraires = Dijkstra.dijkstra(plan, sommets[i], sommets);
            for (int j = 0; j < sommets.length; j++) {
                if (i != j) {
                    itineraireHashMap.put(new Pair<>(sommets[i], sommets[j]), currentItineraires.get(j));
                    couts[i][j] = currentItineraires.get(j).getLongueur();
                }
            }
        }

        TSP3 tsp = new TSP3();
        tsp.chercheSolution(240000, nombreSommets, couts, duree);

        if (tsp.getTempsLimiteAtteint()) {
            System.out.println("TSP : Temps limite atteint");
            return null;
        }

        List<Itineraire> listeItineraires = new ArrayList<>();
        for (int i = 0; i < nombreSommets - 1; i++) {
            int indexPoint1 = tsp.getMeilleureSolution(i);
            int indexPoint2 = tsp.getMeilleureSolution(i+1);
            listeItineraires.add(itineraireHashMap.get(new Pair<>(sommets[indexPoint1], sommets[indexPoint2])));
        }

        listeItineraires.add(itineraireHashMap.get(new Pair<>(sommets[tsp.getMeilleureSolution(nombreSommets - 1)], sommets[tsp.getMeilleureSolution(0)])));

        return new Tournee(listeItineraires);
    }


}
