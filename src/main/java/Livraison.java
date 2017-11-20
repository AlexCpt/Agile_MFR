import java.time.LocalDateTime;

public class Livraison {

    private LocalDateTime mDebutPlage;
    private LocalDateTime mFinPlage;
    private LocalDateTime mDateLivraison;
    private LocalDateTime mDateArrivee;
    private int mDureeLivraison;

    public Livraison(LocalDateTime mDebutPlage, LocalDateTime mFinPlage, LocalDateTime mDateLivraison, LocalDateTime mDateArrivee, int mDureeLivraison) {
        this.mDebutPlage = mDebutPlage;
        this.mFinPlage = mFinPlage;
        this.mDateLivraison = mDateLivraison;
        this.mDateArrivee = mDateArrivee;
        this.mDureeLivraison = mDureeLivraison;
    }


}
