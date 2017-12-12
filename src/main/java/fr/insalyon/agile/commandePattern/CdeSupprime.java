package fr.insalyon.agile.commandePattern;

import java.time.Duration;
import java.time.LocalTime;

import fr.insalyon.agile.metier.Itineraire;
import fr.insalyon.agile.metier.Livraison;
import fr.insalyon.agile.metier.Point;
import fr.insalyon.agile.metier.Tournee;
import javafx.scene.layout.Pane;

/**
 * Classe représentant une commande permettant de supprimer une livraison dans un itineraire
 */
public class CdeSupprime implements Commande {

    private Tournee tournee;
    private Point point;
    private Itineraire itineraire;
    private LocalTime debPlage;
    private LocalTime finPlage;
    private Duration dureeLivraison;
    private Pane mapPane;


    /**
     * Constructeur d'une CdeSupprime
     * @param tournee tournee que l'on veut modifier
     * @param point livraison que l'on veut supprimer
     * @param mapPane pane associe a la tournee
     */
    public CdeSupprime(Tournee tournee, Point point, Pane mapPane) {
        this.tournee = tournee;
        this.point = point;
        this.debPlage = point.getLivraison().getDebutPlage();
        this.finPlage = point.getLivraison().getFinPlage();
        this.dureeLivraison = point.getLivraison().getDureeLivraison();
        this.mapPane = mapPane;

    }

    /**
     * Permet de supprimer une livraison
     */
    @Override
    public void doCde() {
        this.itineraire=tournee.supprimerLivraison(point, mapPane);
    }

    /**
     * Permet d'annuler la dernière suppression
     */
    @Override
    public void undoCde() {
        point.setLivraison(new Livraison(debPlage, finPlage, dureeLivraison));
        tournee.ajouterLivraison(point, dureeLivraison, itineraire);
    }


}
