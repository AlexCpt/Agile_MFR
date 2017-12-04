package fr.insalyon.agile;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.time.LocalTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Tournee {

    private LocalTime mDateArrivee;
    private List<Itineraire> mItineraires;
    private DemandeDeLivraison mDemandeDeLivraison;

    private List<Point> livraisons; //todo : never initialized
    private Map<Point, LocalTime> margesLivraison;
    private Itineraire dijkstraAllee;
    private Itineraire dijkstraRetour;

    public Tournee(){

    }

    public Tournee(List<Itineraire> itineraires, LocalTime dateArrivee, DemandeDeLivraison demandeDeLivraison) {
        mItineraires = itineraires;
        mDateArrivee = dateArrivee;
        mDemandeDeLivraison = demandeDeLivraison;
        livraisons = new ArrayList<>(demandeDeLivraison.getLivraisons());
        margesLivraison = new HashMap<>();
    }

    public DemandeDeLivraison getDemandeDeLivraison() {
        return mDemandeDeLivraison;
    }

    public List<Itineraire> getItineraires() {
        return mItineraires;
    }

    public DemandeDeLivraison getDemandeDeLivraison() {
        return mDemandeDeLivraison;
    }

    public Map<Point, LocalTime> getMargesLivraison() {
        if(margesLivraison.isEmpty())
        {
            calculMargesPointsLivraison();
        }
        return margesLivraison;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tournee tournee = (Tournee) o;

        if (mDateArrivee != null ? !mDateArrivee.equals(tournee.mDateArrivee) : tournee.mDateArrivee != null)
            return false;
        return mItineraires != null ? mItineraires.equals(tournee.mItineraires) : tournee.mItineraires == null;
    }

    public void print(Pane mapPane){
        for (Itineraire itineraire: mItineraires) {
            itineraire.print(mapPane);
        }
    }

    public void calculMargesPointsLivraison(){

        for(Point point : livraisons)
        {
            LocalTime marge  = subLocalTime(point.getLivraison().getDateLivraison(), point.getLivraison().getDateArrivee());
            margesLivraison.put(point, marge);
        }
        Point derniereLivraison =  mItineraires.get(mItineraires.size()-1).getTroncons().get(0).getOrigine();
        LocalTime marge = subLocalTime(mDemandeDeLivraison.getFin(),sumLocalTime(sumLocalTime(derniereLivraison.getLivraison().getDateLivraison(), derniereLivraison.getLivraison().getDureeLivraison()), mItineraires.get(mItineraires.size()-1).getDuree()));

        margesLivraison.put(mDemandeDeLivraison.getEntrepot(), marge);
    }


    public Boolean getItinerairesModifiable (Point livraison, Itineraire itineraire)
    {
        this.calculMargesPointsLivraison();
        Point origineItineraire = itineraire.getTroncons().get(0).getOrigine();
        Point arriveeItineraire = itineraire.getTroncons().get(itineraire.getTroncons().size()-1).getDestination();
        LocalTime tempsActuel = sumLocalTime(itineraire.getDuree(), margesLivraison.get(arriveeItineraire));
        dijkstraAllee = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), origineItineraire, new Point[]{livraison} ).get(0);
        LocalTime dureeAllee = dijkstraAllee.getDuree();
        dijkstraRetour = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), livraison, new Point[]{arriveeItineraire}).get(0);
        LocalTime dureeRetour = dijkstraRetour.getDuree();
        LocalTime nouvTemps = sumLocalTime(sumLocalTime(dureeAllee, livraison.getLivraison().getDureeLivraison()),dureeRetour);
        if(nouvTemps.isBefore(tempsActuel))
        {
            return true;
        }

        return false;
    }

    public void ajouterLivraison(Point livraison, Itineraire itineraire){
        if(getItinerairesModifiable(livraison, itineraire)){
            int index = mItineraires.indexOf(itineraire);
            mItineraires.remove(itineraire);
            LocalTime dateArrive = sumLocalTime(sumLocalTime(dijkstraAllee.getTroncons().get(0).getOrigine().getLivraison().getDateLivraison(), dijkstraAllee.getTroncons().get(0).getOrigine().getLivraison().getDureeLivraison()), dijkstraAllee.getDuree());
            livraison.getLivraison().setDateArrivee(dateArrive);
            if(dateArrive.isBefore(livraison.getLivraison().getDebutPlage()))
            {
                livraison.getLivraison().setDateLivraison(livraison.getLivraison().getDebutPlage());
            }
            else
            {
                livraison.getLivraison().setDateLivraison(dateArrive);
            }
            mItineraires.add(index, dijkstraAllee);
            mItineraires.add(index+1, dijkstraRetour);
            livraisons.add(livraison);
        }
    }

    public void supprimerLivraison(Point livraison){
        //Modifier la date d'arrivée du point qui suit la livraison à supprimer
        if(!livraison.getType().equals(Point.Type.LIVRAISON)){
            return;
        }

        Itineraire itiAlle, itiRetour;
        for(Itineraire itineraire : mItineraires)
        {
            if(itineraire.getTroncons().get(itineraire.getTroncons().size()-1).getDestination().equals(livraison)){
                itiAlle = itineraire;
                itiRetour = mItineraires.get(mItineraires.indexOf(itineraire)+1);
                int index = mItineraires.indexOf(itiAlle);
                //Ce dijkstra est déjà calcule peut etre pourrions nous avoir un cache des valeurs calculé par dijkstra
                Itineraire newItineraire = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), itiAlle.getTroncons().get(0).getOrigine(), new Point[]{itiRetour.getTroncons().get(itiRetour.getTroncons().size()-1).getDestination()}).get(0);
                mItineraires.remove(itiAlle);
                mItineraires.remove(itiRetour);
                mItineraires.add(index, newItineraire);
                break;
            }
        }
    }

    private LocalTime subLocalTime(LocalTime time1, LocalTime time2) {
        return time1.minusHours(time2.getHour()).minusMinutes(time2.getMinute()).minusSeconds(time2.getSecond());
    }

    public LocalTime sumLocalTime(LocalTime time1, LocalTime time2)
    {
        return time1.plusHours(time2.getHour()).plusMinutes(time2.getMinute()).plusSeconds(time2.getSecond());
    }


}
