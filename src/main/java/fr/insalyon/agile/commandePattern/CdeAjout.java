package fr.insalyon.agile.commandePattern;

import fr.insalyon.agile.metier.Itineraire;
import fr.insalyon.agile.metier.Point;
import fr.insalyon.agile.metier.Tournee;
import javafx.scene.layout.Pane;

import java.time.Duration;

/**
 * Classe représentant une commande permettant d'ajouter une livraison dans un itineraire
 */
public class CdeAjout implements Commande {

    private Tournee tournee;
    private Point point;
    private Itineraire itineraire;
    private Duration dureeLivraison;
    private Pane mapPane;


    /**
     * Constructeur d'une CdeAjout
     * @param tournee tournee que l'on veut modifier
     * @param point livraison que l'on veut ajouter a la tournee
     * @param dureeLivraison duree de la livraison que l'on veut ajouter
     * @param itineraire itineraire qui sera modifier dans la tournee
     * @param mapPane pane associe a la tournee que l'on veut modifier
     */
    public CdeAjout(Tournee tournee, Point point, Duration dureeLivraison, Itineraire itineraire, Pane mapPane) {
        this.tournee = tournee;
        this.point = point;
        this.itineraire = itineraire;
        this.dureeLivraison = dureeLivraison;
        this.mapPane = mapPane;
    }

    /**
     * Permet d'ajouter la livraison
     */
    @Override
    public void doCde() {
        tournee.ajouterLivraison(point, dureeLivraison, itineraire);

    }

    /**
     * Permet d'annuler le dernier ajout de livraison
     */
    @Override
    public void undoCde() {

        tournee.supprimerLivraison(point, mapPane);

    }


}
