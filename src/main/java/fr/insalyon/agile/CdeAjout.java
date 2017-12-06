package fr.insalyon.agile;

import java.time.Duration;

public class CdeAjout implements  Commande{

    private Tournee tournee;
    private Point point;
    private Itineraire itineraire;
    private Duration dureeLivraison;


    public CdeAjout(Tournee tournee, Point point, Duration dureeLivraison, Itineraire itineraire) {
        this.tournee = tournee;
        this.point = point;
        this.itineraire = itineraire;
        this.dureeLivraison = dureeLivraison;
    }

    @Override
    public void doCde() {
        tournee.ajouterLivraison(point, dureeLivraison, itineraire);

    }

    @Override
    public void undoCde() {

        tournee.supprimerLivraison(point);

    }


}
