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
            Duration marge = Duration.between(point.getLivraison().getDateLivraison(), point.getLivraison().getDateArrivee());
            margesLivraison.put(point, marge);
        }
        Point derniereLivraison =  mItineraires.get(mItineraires.size()-1).getTroncons().get(0).getOrigine();
        Duration marge = Duration.between(mDemandeDeLivraison.getFin(), derniereLivraison.getLivraison().getDateLivraison().plus(derniereLivraison.getLivraison().getDureeLivraison()).plus(mItineraires.get(mItineraires.size()-1).getDuree()));

        margesLivraison.put(mDemandeDeLivraison.getEntrepot(), marge);
    }


    public Boolean getItinerairesModifiable (Point livraison, Itineraire itineraire)
    {
        this.calculMargesPointsLivraison();
        Point origineItineraire = itineraire.getTroncons().get(0).getOrigine();
        Point arriveeItineraire = itineraire.getTroncons().get(itineraire.getTroncons().size()-1).getDestination();
        Duration tempsActuel = margesLivraison.get(arriveeItineraire).plus(itineraire.getDuree());
        dijkstraAllee = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), origineItineraire, new Point[]{livraison} ).get(0);
        Duration dureeAllee = dijkstraAllee.getDuree();
        dijkstraRetour = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), livraison, new Point[]{arriveeItineraire}).get(0);
        Duration dureeRetour = dijkstraRetour.getDuree();
        Duration nouvTemps = dureeAllee.plus(livraison.getLivraison().getDureeLivraison()).plus(dureeRetour);
        if(nouvTemps.compareTo(tempsActuel) < 0)
        {
            return true;
        }

        return false;
    }
    //Test si entrepot

    public void ajouterLivraison(Point livraison, Itineraire itineraire){
        if(getItinerairesModifiable(livraison, itineraire)){
            int index = mItineraires.indexOf(itineraire);
            mItineraires.remove(itineraire);
            LocalTime dateArrive = dijkstraAllee.getTroncons().get(0).getOrigine().getLivraison().getDateLivraison().plus(dijkstraAllee.getTroncons().get(0).getOrigine().getLivraison().getDureeLivraison()).plus(dijkstraAllee.getDuree());
            livraison.getLivraison().setDateArrivee(dateArrive);
            livraison.getLivraison().setDateLivraison(dateArrive);
            if(livraison.getLivraison().getDebutPlage() !=null){
                if(dateArrive.isBefore(livraison.getLivraison().getDebutPlage()))
                {
                    livraison.getLivraison().setDateLivraison(livraison.getLivraison().getDebutPlage());
                }
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
                LocalTime dateArrivee;
                LocalTime dateLivraison;


                if(newItineraire.getTroncons().get(newItineraire.getTroncons().size()-1).getDestination().getType().equals(Point.Type.ENTREPOT)){
                    this.setDateArrivee(newItineraire.getTroncons().get(0).getOrigine().getLivraison().getDateLivraison().plus(newItineraire.getTroncons().get(0).getOrigine().getLivraison().getDureeLivraison()));
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
                    newItineraire.getTroncons().get(newItineraire.getTroncons().size()-1).getDestination().getLivraison().setDateLivraison(dateArrivee);
                }

                 break;
            }
        }
        livraisons.remove(livraison);

    }

    public List<Point> getLivraisons() {
        return livraisons;
    }

    public LocalTime getDateArrivee() {
        return mDateArrivee;
    }
}
