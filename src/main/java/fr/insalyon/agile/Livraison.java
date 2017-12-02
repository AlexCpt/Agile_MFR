package fr.insalyon.agile;

import java.time.LocalTime;

public class Livraison {

    private LocalTime mDebutPlage;
    private LocalTime mFinPlage;
    private LocalTime mDateLivraison;
    private LocalTime mDateArrivee;
    private LocalTime mDureeLivraison;

    public Livraison(LocalTime mDebutPlage, LocalTime mFinPlage, LocalTime mDureeLivraison) {
        this.mDebutPlage = mDebutPlage;
        this.mFinPlage = mFinPlage;
        this.mDureeLivraison = mDureeLivraison;
    }

    public LocalTime getDebutPlage() {
        return mDebutPlage;
    }

    public LocalTime getFinPlage() {
        return mFinPlage;
    }

    public LocalTime getDateLivraison() {
        return mDateLivraison;
    }

    public LocalTime getDateArrivee() {
        return mDateArrivee;
    }

    public LocalTime getDureeLivraison() {
        return mDureeLivraison;
    }

    public void setDateLivraison(LocalTime mDateLivraison) {
        this.mDateLivraison = mDateLivraison;
    }

    public void setDateArrivee(LocalTime mDateArrivee) {
        this.mDateArrivee = mDateArrivee;
    }
}
