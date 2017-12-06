package fr.insalyon.agile;

import javafx.scene.layout.Pane;

import java.time.Duration;

public class CdeAjout implements  Commande{

    private Tournee tournee;
    private Point point;
    private Itineraire itineraire;
    private Duration dureeLivraison;
    private Pane mapPane;


    public CdeAjout(Tournee tournee, Point point, Duration dureeLivraison, Itineraire itineraire, Pane mapPane) {
        this.tournee = tournee;
        this.point = point;
        this.itineraire = itineraire;
        this.dureeLivraison = dureeLivraison;
        this.mapPane = mapPane;
    }

    @Override
    public void doCde() {
        tournee.ajouterLivraison(point, dureeLivraison, itineraire);

    }

    @Override
    public void undoCde() {

        tournee.supprimerLivraison(point, mapPane);

    }


}
