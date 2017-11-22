import java.time.LocalTime;

public class Livraison {

    private LocalTime mDebutPlage;
    private LocalTime mFinPlage;
    private LocalTime mDateLivraison;
    private LocalTime mDateArrivee;
    private int mDureeLivraison;

    public Livraison(LocalTime mDebutPlage, LocalTime mFinPlage, LocalTime mDateLivraison, LocalTime mDateArrivee, int mDureeLivraison) {
        this.mDebutPlage = mDebutPlage;
        this.mFinPlage = mFinPlage;
        this.mDateLivraison = mDateLivraison;
        this.mDateArrivee = mDateArrivee;
        this.mDureeLivraison = mDureeLivraison;
    }


}
