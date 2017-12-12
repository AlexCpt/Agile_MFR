package fr.insalyon.agile.metier;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Une Livraison a plusieurs caractéristiques temporelles (Plage de livraison, Date d'arrivée, Date de Livraison
 * et une Durée de Livraison)
 */
public class Livraison {

    private LocalTime mDebutPlage;
    private LocalTime mFinPlage;
    private LocalTime mDateLivraison;
    private LocalTime mDateArrivee;
    private Duration mDureeLivraison;

    /**
     * Constructeur d'une Livraison
     * @param mDebutPlage Début de la plage horraire associée à la livraison
     * @param mFinPlage Fin de la plage horraire associée à la livraison
     * @param mDureeLivraison Durée de la Livraison
     */
    public Livraison(LocalTime mDebutPlage, LocalTime mFinPlage, Duration mDureeLivraison) {
        this.mDebutPlage = mDebutPlage;
        this.mFinPlage = mFinPlage;
        this.mDureeLivraison = mDureeLivraison;
    }

    /**
     * Permet de récupérer le début de la plage associé à la Livraison courante
     * @return début de la plage associé à la Livraison courante
     */
    public LocalTime getDebutPlage() {
        return mDebutPlage;
    }

    /**
     * Permet de récupérer la fin de la plage associé à la Livraison courante
     * @return fin de la plage associée à la Livraison courante
     */
    public LocalTime getFinPlage() {
        return mFinPlage;
    }

    /**
     * Permet de récupérer la date de livraison associé à la Livraison courante
     * @return date de livraison associée à la Livraison courante
     */
    public LocalTime getDateLivraison() {
        return mDateLivraison;
    }

    /**
     * Permet de récupérer la date d'arrivée associé à la Livraison courante
     * @return la date d'arrivée associée à la Livraison courante
     */
    public LocalTime getDateArrivee() {
        return mDateArrivee;
    }

    /**
     * Permet de récupérer la durée de la Livraison courante
     * @return durée de la Livraison courante
     */
    public Duration getDureeLivraison() {
        return mDureeLivraison;
    }

    /**
     * Permet de modifier la date de Livraison de la Livraison courante
     * @param mDateLivraison nouvelle date de Livraison
     */
    public void setDateLivraison(LocalTime mDateLivraison) {
        this.mDateLivraison = mDateLivraison;
    }

    /**
     * Permet de modifier la date d'arrivée de la Livraison courante
     * @param mDateArrivee nouvelle date d'arrivée
     */
    public void setDateArrivee(LocalTime mDateArrivee) {
        this.mDateArrivee = mDateArrivee;
    }

    /**
     * Permet de modifier le début de la plage de la Livraison courante
     * @param mDebutPlage  début de la plage de la Livraison
     */
    public void setDebutPlage(LocalTime mDebutPlage) {
        this.mDebutPlage = mDebutPlage;
    }

    /**
     * Permet de modifier la fin de la plage de la Livraison courante
     * @param mFinPlage fin de la plage de Livraison
     */
    public void setFinPlage(LocalTime mFinPlage) {
        this.mFinPlage = mFinPlage;
    }
}
