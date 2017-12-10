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

    /**
     * Construit une tournée
     *
     * @param itineraires : La liste des itinéraires composant la tournée
     * @param dateArrivee : La date d'arrivée de la tournée
     * @param demandeDeLivraison : La demande de livraison associée à la tournée
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tournee tournee = (Tournee) o;

        if (mDateArrivee != null ? !mDateArrivee.equals(tournee.mDateArrivee) : tournee.mDateArrivee != null)
            return false;
        return mItineraires != null ? mItineraires.equals(tournee.mItineraires) : tournee.mItineraires == null;
    }

    /**
     * Affiche une tournée sur le plan
     * @param mapPane : Le plan sur lequel on veut afficher la tournée
     */
    public void print(Pane mapPane){
        for (Itineraire itineraire: mItineraires) {
            itineraire.print(mapPane);
        }
    }

    /**
     * Calcule pour chaque livraison, la marge disponible entre l'heure
     * d'arrivée du livreur à son point de livraison, et l'heure à laquelle
     * il commence sa livraison
     */
    public void calculMargesPointsLivraison(){
        margesLivraison.clear();
        for(Point point : livraisons)
        {
            Duration marge = Duration.between(point.getLivraison().getDateArrivee(), point.getLivraison().getDateLivraison());
            margesLivraison.put(point, marge);
        }
        Point derniereLivraison =  mItineraires.get(mItineraires.size()-1).getTroncons().get(0).getOrigine();
        Duration marge = Duration.between(derniereLivraison.getLivraison().getDateLivraison().plus(derniereLivraison.getLivraison().getDureeLivraison()).plus(mItineraires.get(mItineraires.size()-1).getDuree()), mDemandeDeLivraison.getFin());

        margesLivraison.put(mDemandeDeLivraison.getEntrepot(), marge);
    }

    /**
     * Vérifie si une livraison peut être ajoutée sur un itinéraire,
     * sans décaler les heures des livraisons suivantes
     *
     * @param livraison : La livraison que l'on cherche à ajouter
     * @param duree : La durée de la livraison que l'on veut ajouter
     * @param itineraire : L'itinéraire sur lequel on veut ajouter la livraison
     * @return Un booléen qui indique si la livraison peut être ajouté ou pas sur l'itinéraire
     */
    public Boolean getItinerairesModifiable (Point livraison, Duration duree, Itineraire itineraire)
    {
        this.calculMargesPointsLivraison();
        Point origineItineraire = itineraire.getTroncons().get(0).getOrigine();
        Point arriveeItineraire = itineraire.getTroncons().get(itineraire.getTroncons().size()-1).getDestination();
        Duration tempsActuel = margesLivraison.get(arriveeItineraire).plus(itineraire.getDuree());
        Itineraire dijkstraAllee = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), origineItineraire, new Point[]{livraison} ).get(0);
        Duration dureeAllee = dijkstraAllee.getDuree();
        Itineraire dijkstraRetour = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), livraison, new Point[]{arriveeItineraire}).get(0);
        Duration dureeRetour = dijkstraRetour.getDuree();
        Duration nouvTemps = dureeAllee.plus(dureeRetour).plus(duree);
        return nouvTemps.compareTo(tempsActuel) < 0;

    }

    /**
     * Ajoute une livraison dans la tournée actuellement affichée sur le plan
     *
     * @param livraison : La livraison que l'on veut ajouter
     * @param dureeLivraison : La durée de la livraison à ajouter
     * @param itineraire : L'itinéraire sur lequel on veut ajouter la livraison
     */
    public void ajouterLivraison(Point livraison, Duration dureeLivraison, Itineraire itineraire){
        if(!livraison.getType().equals(Point.Type.LIVRAISON)){
            livraison.setLivraison(new Livraison(null, null, dureeLivraison));
        }

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
        calculMargesPointsLivraison();
    }

    /**
     * Supprime une livraison de la tournée actuellement affichée sur le plan
     *
     * @param livraison : La livraison à supprimer
     * @param mapPane : Le plan sur lequel est affichée la livraison
     * @return Une instance d'Itineraire, représentant le nouvel itinéraire
     * qui remplace celui de la livraison supprimée
     */
    public Itineraire supprimerLivraison(Point livraison, Pane mapPane){
        Itineraire newItineraire = null;

        Itineraire itiAlle, itiRetour;
        for(Itineraire itineraire : mItineraires)
        {
            if(itineraire.getTroncons().get(itineraire.getTroncons().size()-1).getDestination().equals(livraison)){
                itiAlle = itineraire;
                itiRetour = mItineraires.get(mItineraires.indexOf(itineraire)+1);
                int index = mItineraires.indexOf(itiAlle);

                for (Troncon troncon:itiAlle.getTroncons()) { //Retirer point verts restant quand suppr
                    troncon.setLongueurParcourue(mapPane, 0);
                }

                for (Troncon troncon:itiRetour.getTroncons()) { //Retirer point verts restant quand suppr
                    troncon.setLongueurParcourue(mapPane, 0);
                }

                newItineraire = Dijkstra.dijkstra(mDemandeDeLivraison.getPlan(), itiAlle.getTroncons().get(0).getOrigine(), new Point[]{itiRetour.getTroncons().get(itiRetour.getTroncons().size()-1).getDestination()}).get(0);
                mItineraires.remove(itiAlle);
                mItineraires.remove(itiRetour);
                mItineraires.add(index, newItineraire);
                break;
            }
        }
        livraisons.remove(livraison);
        livraison.setPoint();
        calculMargesPointsLivraison();
        return newItineraire;

    }

}
