package fr.insalyon.agile;

public class CdeSupprime implements  Commande{

    private Tournee tournee;
    private Point point;
    private Itineraire itineraire;


    public CdeSupprime(Tournee tournee, Point point) {
        this.tournee = tournee;
        this.point = point;

    }

    @Override
    public void doCde() {

        this.itineraire=tournee.supprimerLivraison(point);

    }

    @Override
    public void undoCde() {

        tournee.ajouterLivraison(point,itineraire);

    }


}
