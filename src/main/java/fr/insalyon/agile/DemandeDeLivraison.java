package fr.insalyon.agile;

import com.sun.tools.javac.util.BasicDiagnosticFormatter;
import fr.insalyon.agile.*;
import fr.insalyon.agile.tsp.TSP1;
import fr.insalyon.agile.tsp.TSP4;
import javafx.util.Pair;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
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

    public Point getEntrepot() {
        return mEntrepot;
    }

    public LocalTime getDepart() {
        return mDepart;
    }

    public LocalTime getFin() {
        return mFin;
    }

    public List<Point> getLivraisons() {
        return mLivraisons;
    }

    public Tournee calculerTournee(){

        HashMap<Pair<Point, Point>, Itineraire> itineraireHashMap = new HashMap<>();

        int nombreSommets = mLivraisons.size() + 1;

        int[][] couts = new int[nombreSommets][nombreSommets];
        int[] duree = new int[nombreSommets];

        for (int i = 0; i < mLivraisons.size(); i++) {
            duree[i+1] = (int)mLivraisons.get(i).getLivraison().getDureeLivraison().getSeconds();
        }

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
                    couts[i][j] = ((currentItineraires.get(j).getLongueur() * 3600) / 1000) / 15;
                }
            }
        }

        TSP4 tsp = new TSP4(mLivraisons, mDepart);
        tsp.chercheSolution(960000, nombreSommets, couts, duree);

        if (tsp.getTempsLimiteAtteint()) {
            System.out.println("TSP : Temps limite atteint");
            return null;
        }

        List<Itineraire> listeItineraires = new ArrayList<>();
        LocalTime tempsActuel = mDepart;
        for (int i = 0; i < nombreSommets - 1; i++) {
            int indexPoint1 = tsp.getMeilleureSolution(i);
            int indexPoint2 = tsp.getMeilleureSolution(i+1);
            listeItineraires.add(itineraireHashMap.get(new Pair<>(sommets[indexPoint1], sommets[indexPoint2])));

            tempsActuel = tempsActuel.plusSeconds(couts[indexPoint1][indexPoint2]);

            if (sommets[indexPoint2].getType() == Point.Type.LIVRAISON) {
                sommets[indexPoint2].getLivraison().setDateArrivee(tempsActuel);

                if (sommets[indexPoint2].getLivraison().getDebutPlage() != null &&
                        sommets[indexPoint2].getLivraison().getFinPlage() != null) {
                    if (tempsActuel.isBefore(sommets[indexPoint2].getLivraison().getDebutPlage())) {
                        tempsActuel = sommets[indexPoint2].getLivraison().getDebutPlage();
                    }
                }

                sommets[indexPoint2].getLivraison().setDateLivraison(tempsActuel);
            }

            tempsActuel = tempsActuel.plusSeconds(duree[indexPoint2]);
        }

        listeItineraires.add(itineraireHashMap.get(new Pair<>(sommets[tsp.getMeilleureSolution(nombreSommets - 1)], sommets[tsp.getMeilleureSolution(0)])));

        tempsActuel = tempsActuel.plusSeconds(couts[tsp.getMeilleureSolution(nombreSommets - 1)][tsp.getMeilleureSolution(0)] + duree[tsp.getMeilleureSolution(0)]);

        for (Point point : mLivraisons) {
            Livraison livraison = point.getLivraison();
            if (livraison.getDebutPlage() != null && livraison.getFinPlage() != null) {
                Duration marge = Duration.between(livraison.getDebutPlage(), livraison.getFinPlage());
                LocalTime finLivraison = livraison.getDateLivraison().plus(livraison.getDureeLivraison());

                if(marge.compareTo(Duration.ofHours(2)) > 0) {
                    if (livraison.getDureeLivraison().compareTo(Duration.ofHours(2)) > 0) {
                        livraison.setDebutPlage(livraison.getDateLivraison());
                        livraison.setFinPlage(finLivraison);
                    }
                    else {
                        if (livraison.getDateLivraison().minus(Duration.ofHours(1)).compareTo(livraison.getDebutPlage()) < 0) {
                            livraison.setFinPlage(livraison.getDebutPlage().plus(Duration.ofHours(2)));
                        }
                        else if (finLivraison.plus(Duration.ofHours(1)).compareTo(livraison.getFinPlage()) > 0) {
                            livraison.setDebutPlage(livraison.getFinPlage().minus(Duration.ofHours(2)));
                        }
                        else {
                            livraison.setDebutPlage(livraison.getDateLivraison().minus(Duration.ofHours(1)));
                            livraison.setFinPlage(livraison.getDateLivraison().plus(Duration.ofHours(1)));
                        }
                    }
                }
            }
        }

        return new Tournee(listeItineraires, tempsActuel, this);
    }




}
