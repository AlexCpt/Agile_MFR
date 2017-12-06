package fr.insalyon.agile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CdeSupprime implements  Commande{

    private Tournee tournee;
    private Point point;
    private Itineraire itineraire;
    private LocalTime debPlage;
    private LocalTime finPlage;
    private Duration dureeLivraison;


    public CdeSupprime(Tournee tournee, Point point) {
        this.tournee = tournee;
        this.point = point;
        this.debPlage = point.getLivraison().getDebutPlage();
        this.finPlage = point.getLivraison().getFinPlage();
        this.dureeLivraison = point.getLivraison().getDureeLivraison();

    }

    @Override
    public void doCde() {
        this.itineraire=tournee.supprimerLivraison(point);
    }

    @Override
    public void undoCde() {
        point.setLivraison(new Livraison(debPlage, finPlage, dureeLivraison));
        tournee.ajouterLivraison(point, dureeLivraison, itineraire);
    }


}
