package fr.insalyon.agile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ExportTournee {
    private Tournee mTournee;

    public ExportTournee (Tournee tournee) {
        mTournee = tournee;
    }

    public String createFileText() {
        String text = "Liste des livraisons\n\n\n";

        // Entrepot

        Troncon tronconEntrepot = mTournee.getItineraires().get(0).getTroncons().get(0);
        Point entrepot = tronconEntrepot.getOrigine();

        text += "Départ de l'entrepôt :\n\n"
                + "Adresse : " + entrepot.getId() + " " + tronconEntrepot.getNomRue() + "\n"
                + "Heure de départ : " + mTournee.getDemandeDeLivraison().getDepart() + "\n\n\n\n"
        ;



        // Livraisons

        for (Itineraire itineraire : mTournee.getItineraires()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            Troncon tronconActuel = itineraire.getTroncons().get(itineraire.getTroncons().size() - 1);
            Point point = tronconActuel.getDestination();

            if (point.getType() == Point.Type.LIVRAISON)
            {
                Livraison livraison = point.getLivraison();
                LocalTime heureDepart = livraison.getDateLivraison().plus(livraison.getDureeLivraison());
                int index = mTournee.getLivraisons().indexOf(point) + 1;


                text += "Livraison n°" + index + " :\n\n"
                        + "Adresse de livraison : " + point.getId() + " " + tronconActuel.getNomRue() + "\n"
                        + "Heure d'arrivée : " + livraison.getDateArrivee().format(formatter) + "\n"
                        + "Heure de départ : " + heureDepart.getHour() + ":" + heureDepart.getMinute() + "\n"
                ;
            }
            else if (point.getType() == Point.Type.ENTREPOT) {
                text += "Retour à l'entrepôt : " + mTournee.getDateArrivee().format(formatter) + "\n"
                        + "Adresse : " + point.getId() + " " + tronconActuel.getNomRue() + "\n"
                ;
            }

            text += "Itinéraire à suivre : \n";

            String rueActuelle = "";
            for (Troncon troncon : itineraire.getTroncons()) {
                if (!troncon.getNomRue().equals(rueActuelle)) {
                    text += "\t" + troncon.getNomRue() + "\n";
                    rueActuelle = troncon.getNomRue();
                }

            }

            text += "\n\n\n";
        }

        return text;
    }

    public void exportFile (String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("exportTournee/" + fileName));
            String text = createFileText();
            writer.write(text);

            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
