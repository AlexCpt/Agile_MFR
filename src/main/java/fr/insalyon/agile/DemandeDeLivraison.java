package fr.insalyon.agile;

import fr.insalyon.agile.*;
import fr.insalyon.agile.tsp.TSP1;
import javafx.util.Pair;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DemandeDeLivraison {

    private List<Point> mLivraisons;
    private Point mEntrepot;
    private LocalTime mDepart;
    private LocalTime mFin = LocalTime.of(18,0);
    private Plan mPlan;

    public DemandeDeLivraison(Plan plan, List<Point> livraisons, Point entrepot, LocalTime depart) {
        this.mPlan = plan;
        this.mLivraisons = livraisons;
        this.mEntrepot = entrepot;
        this.mDepart = depart;
    }

    public Plan getPlan() {
        return mPlan;
    }

    public List<Point> getLivraisons() {
        return mLivraisons;
    }

    public Point getEntrepot() {
        return mEntrepot;
    }

    public LocalTime getDepart() {
        return mDepart;
    }

    public LocalTime getFin() {
        return mFin;
    }

    public Tournee calculerTournee(){

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
            currentItineraires = Dijkstra.dijkstra(mPlan, sommets[i], sommets);
            for (int j = 0; j < sommets.length; j++) {
                if (i != j) {
                    itineraireHashMap.put(new Pair<>(sommets[i], sommets[j]), currentItineraires.get(j));
                    couts[i][j] = currentItineraires.get(j).getLongueur();
                }
            }
        }

        TSP1 tsp1 = new TSP1();
        tsp1.chercheSolution(1000, nombreSommets, couts, duree);

        if (tsp1.getTempsLimiteAtteint()) {
            System.out.println("TSP : Temps limite atteint");
            return null;
        }

        List<Itineraire> listeItineraires = new ArrayList<>();
        for (int i = 0; i < nombreSommets - 1; i++) {
            int indexPoint1 = tsp1.getMeilleureSolution(i);
            int indexPoint2 = tsp1.getMeilleureSolution(i+1);
            listeItineraires.add(itineraireHashMap.get(new Pair<>(sommets[indexPoint1], sommets[indexPoint2])));
        }

        listeItineraires.add(itineraireHashMap.get(new Pair<>(sommets[tsp1.getMeilleureSolution(nombreSommets - 1)], sommets[tsp1.getMeilleureSolution(0)])));

        return new Tournee(listeItineraires, this);
    }




}
