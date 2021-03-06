package fr.insalyon.agile.export;

import fr.insalyon.agile.modele.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * La classe ExportTournee permet de créer un fichier
 * contenant les informations relatives à une tournée
 */
public class ExportTournee {
    private Tournee mTournee;

    /**
     * Construit un ExportTournee à partir d'une tournée
     * @param tournee : La tournée que l'on veut exporter
     */
    public ExportTournee (Tournee tournee) {
        mTournee = tournee;
    }

    /**
     * Crée le texte d'un fichier d'export de tournée
     * @return Un String, qui représente le texte écrit dans le fichier exporté
     */
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

            int longueurTroncon = 0;
            for (int i = 0; i < itineraire.getTroncons().size(); i++) {
                Troncon troncon = itineraire.getTroncons().get(i);
                if (i != (itineraire.getTroncons().size() - 1)) {
                    Troncon nextTroncon = itineraire.getTroncons().get(i + 1);
                    if (!troncon.getNomRue().equals(nextTroncon.getNomRue())) {
                        longueurTroncon += troncon.getLongueur();
                        text += "\tPrendre " + troncon.getNomRue() + " sur " + longueurTroncon + "m\n";
                        text += "\t - Puis ";

                        double angle = Math.toDegrees(troncon.angleWith(nextTroncon));
                        if (angle == 0) {
                            text += "faire demi-tour";
                        } else if (angle > 0 && angle < 160) {
                            text += "tourner à droite";
                        } else if (angle >= 160 || angle <= -160) {
                            text += "continuer tout droit";
                        } else if (angle > -160 && angle < 0) {
                            text += "tourner à gauche";
                        }
                        text += "\n";

                        longueurTroncon = 0;
                    }
                    else {
                        longueurTroncon += troncon.getLongueur();
                    }
                }
                else {
                    longueurTroncon += troncon.getLongueur();
                    text += "\tPrendre " + troncon.getNomRue() + " sur " + longueurTroncon + "m\n";
                }



            }

            text += "\n\n\n";
        }

        return text;
    }

    /**
     * Exporte un fichier qui contient les informations relatives à une tournée
     * @param fileName : Le nom du fichier que l'on veut exporter
     * @throws Exception : Propage une exception si le fichier n'arrive pas à être créé
     */
    public void exportFile (String fileName) throws Exception {
        try {
            if (fileName == null) {
                throw new IOException("Aucun fichier sélectionné");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            String text = createFileText();
            writer.write(text);

            writer.close();
        } catch (Exception e) {
            throw e;
        }

    }
}
