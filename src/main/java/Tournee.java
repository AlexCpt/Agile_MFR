import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.MINUTES;

public class Tournee {

    List<Itineraire> mItineraires;
    DemandeDeLivraison mDemandeDeLivraison;
    List<Point> livraisons;
    Map<Point, LocalTime> margesLivraison;

    public Tournee(){

    }

    public Tournee(List<Itineraire> itineraires, DemandeDeLivraison demandeDeLivraison) {
        mItineraires = itineraires;
        mDemandeDeLivraison = demandeDeLivraison;
        livraisons = new ArrayList<>();
        margesLivraison = new HashMap<>();
    }

    public Map<Point, LocalTime> getMargesLivraison() {
        return margesLivraison;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tournee tournee = (Tournee) o;

        return mItineraires != null ? mItineraires.equals(tournee.mItineraires) : tournee.mItineraires == null;
    }

    public void print(Pane mapPane){
        for (Itineraire itineraire: mItineraires) {
            itineraire.print(mapPane);
        }
    }

    public void calculMargesPointsLivraison(){
        //Null pointer exception il y a un entrepot dans la liste de points
        for (Itineraire itineraire : mItineraires){
            if(itineraire.getTroncons().get(0).getOrigine().getType().equals(Point.Type.LIVRAISON)) {
                livraisons.add(itineraire.getTroncons().get(0).getOrigine());
                if (itineraire.getTroncons().get(itineraire.getTroncons().size() - 1).getDestination().getType().equals(Point.Type.LIVRAISON)) {
                    livraisons.add(itineraire.getTroncons().get(itineraire.getTroncons().size() - 1).getDestination());

                }
            }
        }

        for(Point point : livraisons)
        {
            LocalTime marge  = subLocalTime(point.getLivraison().getDateLivraison(), point.getLivraison().getDateArrivee());
            margesLivraison.put(point, marge);
        }
        Point derniereLivraison =  mItineraires.get(mItineraires.size()-1).getTroncons().get(0).getOrigine();
        LocalTime marge = subLocalTime(mDemandeDeLivraison.getFin(),sumLocalTime(sumLocalTime(derniereLivraison.getLivraison().getDateLivraison(), derniereLivraison.getLivraison().getDureeLivraison()), mItineraires.get(mItineraires.size()-1).getDuree()));

        margesLivraison.put(mDemandeDeLivraison.getEntrepot(), marge);
    }

    private LocalTime subLocalTime(LocalTime time1, LocalTime time2) {
         return time1.minusHours(time2.getHour()).minusMinutes(time2.getMinute()).minusSeconds(time2.getSecond());
    }

    public List<Boolean> modifierTournee (Point livraison)
    {
        this.calculMargesPointsLivraison();
        List<Boolean> result = new ArrayList<>();

        for(Itineraire itineraire : mItineraires)
        {
            Point origineItineraire = itineraire.getTroncons().get(0).getOrigine();
            Point arriveeItineraire = itineraire.getTroncons().get(itineraire.getTroncons().size()-1).getDestination();
            LocalTime tempsActuel = sumLocalTime(itineraire.getDuree(), margesLivraison.get(arriveeItineraire));
            LocalTime dureeAllee = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), origineItineraire, new Point[]{livraison} ).get(0).getDuree();
            LocalTime dureeRetour = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), livraison, new Point[]{arriveeItineraire}).get(0).getDuree();
            LocalTime nouvTemps = sumLocalTime(sumLocalTime(dureeAllee, livraison.getLivraison().getDureeLivraison()),dureeRetour);
            System.out.println("old : " + tempsActuel);
            System.out.println("new : " + nouvTemps);
            //faire tableau de booléen ou de temps
        }
        return result;
    }

    private LocalTime getDureeItineraire(Point a, Point b){
        LocalTime deb, fin;
        if (a.getType().equals(Point.Type.ENTREPOT)){
            deb = mDemandeDeLivraison.getDepart();
        }else
        {
            deb = sumLocalTime(a.getLivraison().getDateLivraison(), a.getLivraison().getDureeLivraison());
        }
        if (b.getType().equals(Point.Type.ENTREPOT)){
            fin = mDemandeDeLivraison.getFin();
        }else{
            fin = b.getLivraison().getDateLivraison();
        }
        int minutes = (int) MINUTES.between(deb, fin);
        return LocalTime.of(minutes/60, minutes % 60);
    }

    public LocalTime sumLocalTime(LocalTime time1, LocalTime time2)
    {
        return time1.plusHours(time2.getHour()).plusMinutes(time2.getMinute()).plusSeconds(time2.getSecond());
    }


}
