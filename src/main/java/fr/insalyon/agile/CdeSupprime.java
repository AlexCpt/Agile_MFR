package fr.insalyon.agile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;import javafx.scene.layout.Pane;


public class CdeSupprime implements  Commande{

    private Tournee tournee;
    private Point point;
    private Itineraire itineraire;
    private LocalTime debPlage;
    private LocalTime finPlage;
    private Duration dureeLivraison;
    private Pane mapPane;


    public CdeSupprime(Tournee tournee, Point point, Pane mapPane) {
        this.tournee = tournee;
        this.point = point;
        this.debPlage = point.getLivraison().getDebutPlage();
        this.finPlage = point.getLivraison().getFinPlage();
        this.dureeLivraison = point.getLivraison().getDureeLivraison();
        this.mapPane = mapPane;

    }

    @Override
    public void doCde() {
        this.itineraire=tournee.supprimerLivraison(point, mapPane);
    }

    @Override
    public void undoCde() {
        point.setLivraison(new Livraison(debPlage, finPlage, dureeLivraison));
        tournee.ajouterLivraison(point, dureeLivraison, itineraire);
    }


}
