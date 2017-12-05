package fr.insalyon.agile;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.time.Duration;
import java.time.LocalTime;

import java.util.*;


public class Tournee {

    private LocalTime mDateArrivee;
    private List<Itineraire> mItineraires;
    private DemandeDeLivraison mDemandeDeLivraison;

    private List<Point> livraisons;
    private Map<Point, Duration> margesLivraison;

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

    public LocalTime getDateArrivee() {
        return mDateArrivee;
    }

    public List<Point> getLivraisons() {
        return livraisons;
    }

    public Map<Point, Duration> getMargesLivraison() {
        if(margesLivraison.isEmpty())
        {
            calculMargesPointsLivraison();
        }
        return margesLivraison;
    }

    public void setDateArrivee(LocalTime dateArrivee) {
        this.mDateArrivee = dateArrivee;
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
            Duration marge = Duration.between(point.getLivraison().getDateArrivee(), point.getLivraison().getDateLivraison());
            margesLivraison.put(point, marge);
        }
        Point derniereLivraison =  mItineraires.get(mItineraires.size()-1).getTroncons().get(0).getOrigine();
        Duration marge = Duration.between(derniereLivraison.getLivraison().getDateLivraison().plus(derniereLivraison.getLivraison().getDureeLivraison()).plus(mItineraires.get(mItineraires.size()-1).getDuree()), mDemandeDeLivraison.getFin());

        margesLivraison.put(mDemandeDeLivraison.getEntrepot(), marge);
    }


    public Boolean getItinerairesModifiable (Point livraison, Itineraire itineraire)
    {
        this.calculMargesPointsLivraison();
        Point origineItineraire = itineraire.getTroncons().get(0).getOrigine();
        Point arriveeItineraire = itineraire.getTroncons().get(itineraire.getTroncons().size()-1).getDestination();
        Duration tempsActuel = margesLivraison.get(arriveeItineraire).plus(itineraire.getDuree());
        Itineraire dijkstraAllee = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), origineItineraire, new Point[]{livraison} ).get(0);
        Duration dureeAllee = dijkstraAllee.getDuree();
        Itineraire dijkstraRetour = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), livraison, new Point[]{arriveeItineraire}).get(0);
        Duration dureeRetour = dijkstraRetour.getDuree();
        Duration nouvTemps = dureeAllee.plus(dureeRetour);
        if(nouvTemps.compareTo(tempsActuel) < 0)
        {
            return true;
        }

        return false;
    }

    public void ajouterLivraison(Point livraison, Itineraire itineraire){
        livraison.setLivraison(new Livraison(null, null, Duration.ZERO));
        Itineraire dijkstraAllee = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), itineraire.getTroncons().get(0).getOrigine(), new Point[]{livraison} ).get(0);
        Itineraire dijkstraRetour = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), livraison, new Point[]{itineraire.getTroncons().get(itineraire.getTroncons().size()-1).getDestination()}).get(0);
        int index = mItineraires.indexOf(itineraire);
        mItineraires.remove(itineraire);
        LocalTime dateArrivee;
        if(itineraire.getTroncons().get(0).getOrigine().getType().equals(Point.Type.ENTREPOT)){
            dateArrivee= mDemandeDeLivraison.getDepart().plus(dijkstraAllee.getDuree());
        }
        else
        {
            dateArrivee=itineraire.getTroncons().get(0).getOrigine().getLivraison().getDateLivraison().plus(itineraire.getTroncons().get(0).getOrigine().getLivraison().getDureeLivraison().plus(itineraire.getDuree()));
        }
        livraison.getLivraison().setDateArrivee(dateArrivee);
        livraison.getLivraison().setDateLivraison(dateArrivee);
        mItineraires.add(index, dijkstraAllee);
        mItineraires.add(index+1, dijkstraRetour);
        livraisons.add(livraison);
    }

    public Itineraire supprimerLivraison(Point livraison){
        Itineraire newItineraire = null;

        Itineraire itiAlle, itiRetour;
        for(Itineraire itineraire : mItineraires)
        {
            if(itineraire.getTroncons().get(itineraire.getTroncons().size()-1).getDestination().equals(livraison)){
                itiAlle = itineraire;
                itiRetour = mItineraires.get(mItineraires.indexOf(itineraire)+1);
                int index = mItineraires.indexOf(itiAlle);
                //Ce dijkstra est déjà calcule peut etre pourrions nous avoir un cache des valeurs calculé par dijkstra
                newItineraire = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), itiAlle.getTroncons().get(0).getOrigine(), new Point[]{itiRetour.getTroncons().get(itiRetour.getTroncons().size()-1).getDestination()}).get(0);
                mItineraires.remove(itiAlle);
                mItineraires.remove(itiRetour);
                mItineraires.add(index, newItineraire);
                LocalTime dateArrivee;
                LocalTime dateLivraison;


                if(newItineraire.getTroncons().get(newItineraire.getTroncons().size()-1).getDestination().getType().equals(Point.Type.ENTREPOT)){
                    this.setDateArrivee(newItineraire.getTroncons().get(0).getOrigine().getLivraison().getDateLivraison().plus(newItineraire.getTroncons().get(0).getOrigine().getLivraison().getDureeLivraison()).plus(newItineraire.getDuree()));
                }else
                {
                    if(newItineraire.getTroncons().get(0).getOrigine().getType().equals(Point.Type.ENTREPOT)){
                        dateArrivee = mDemandeDeLivraison.getDepart().plus(newItineraire.getDuree());

                    }else
                    {
                        dateArrivee = newItineraire.getTroncons().get(0).getOrigine().getLivraison().getDateLivraison().plus(newItineraire.getTroncons().get(0).getOrigine().getLivraison().getDureeLivraison()).plus(newItineraire.getDuree());

                    }
                    LocalTime dateDebPlage = newItineraire.getTroncons().get(newItineraire.getTroncons().size()-1).getDestination().getLivraison().getDebutPlage();
                    dateLivraison = dateArrivee;
                    if(dateDebPlage!=null){
                        if(dateArrivee.isBefore(dateDebPlage)){
                            dateLivraison=dateDebPlage;
                        }
                    }
                    newItineraire.getTroncons().get(newItineraire.getTroncons().size()-1).getDestination().getLivraison().setDateLivraison(dateLivraison);
                    newItineraire.getTroncons().get(newItineraire.getTroncons().size()-1).getDestination().getLivraison().setDateArrivee(dateArrivee);
                }
                 break;
            }
        }
        livraisons.remove(livraison);
        livraison.setPoint();
        return newItineraire;

    }
}
