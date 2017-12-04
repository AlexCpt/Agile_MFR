package fr.insalyon.agile;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ExportTournee {
    private Tournee tournee;

    public String createFileText() {
        String text = "Liste des livraisons\n\n";

        // Entrepot

        Point entrepot = tournee.getItineraires().get(0).getTroncons().get(0).getOrigine();

        text += "Départ de l'entrepôt :\n"
                + "Adresse : " + entrepot.getAdresse()
                + "Heure de départ : " + tournee.getDemandeDeLivraison().getDepart()
        ;



        // Livraisons

        for (Itineraire itineraire : tournee.getItineraires()) {

            Point point = itineraire.getTroncons().get(itineraire.getTroncons().size()).getDestination();
            Livraison livraison = point.getLivraison();

            LocalTime heureDepart = livraison.getDateLivraison().plus(livraison.getDureeLivraison());
            text += "Livraison n°" + tournee.getLivraisons().indexOf(point) + 1 + ":\n"
                    + "Adresse de livraison : " + point.getAdresse() + "\n"
                    + "Heure d'arrivée : " + livraison.getDateArrivee() + "\n"
                    + "Heure de départ : " + heureDepart + "\n"
                    + "Itinéraire à suivre : \n"
            ;

            for (Troncon troncon : itineraire.getTroncons()) {
                text += troncon.getNomRue() + "\n";
            }

            text += "\n";
        }




        return text;
    }
}
