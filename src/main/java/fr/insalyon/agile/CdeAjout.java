package fr.insalyon.agile;

public class CdeAjout implements  Commande{

    private Tournee tournee;
    private Point point;
    private Itineraire itineraire;


    public CdeAjout(Tournee tournee, Point point, Itineraire itineraire) {
        this.tournee = tournee;
        this.point = point;
        this.itineraire = itineraire;
    }

    @Override
    public void doCde() {
        tournee.ajouterLivraison(point,itineraire);

    }

    @Override
    public void undoCde() {

        tournee.supprimerLivraison(point);

    }


}
