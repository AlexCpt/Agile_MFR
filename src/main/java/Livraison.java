import java.time.LocalTime;

public class Livraison {

    private LocalTime mDebutPlage;
    private LocalTime mFinPlage;
    private LocalTime mDateLivraison;
    private LocalTime mDateArrivee;
    private LocalTime mDureeLivraison;

    public Livraison(LocalTime mDebutPlage, LocalTime mFinPlage, LocalTime mDateLivraison, LocalTime mDateArrivee, LocalTime mDureeLivraison) {
        this.mDebutPlage = mDebutPlage;
        this.mFinPlage = mFinPlage;
        this.mDateLivraison = mDateLivraison;
        this.mDateArrivee = mDateArrivee;
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

}
