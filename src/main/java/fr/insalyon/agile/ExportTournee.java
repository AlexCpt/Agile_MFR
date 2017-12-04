package fr.insalyon.agile;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ExportTournee {
    private Tournee tournee;

    public String createFileText() {
        String text = "Liste des livraisons\n\n";

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
            
        }




        return text;
    }
}
