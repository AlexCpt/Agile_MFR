import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.time.LocalTime;
import java.util.List;

public class Itineraire {

    private List<Troncon> mTroncons;
    private int mLongueur = 0;
    private LocalTime mDuree;

    public Itineraire(List<Troncon> troncons) {
        mTroncons =  troncons;
        for (Troncon t : mTroncons) {
            mLongueur += t.getLongueur();
        }

        mDuree = LocalTime.ofSecondOfDay((int) ((mLongueur*(0.001)/15)*3600));

    }

    public List<Troncon> getTroncons() {
        return mTroncons;
    }

    public LocalTime getDuree() {
        return mDuree;
    }

    public void setTroncons(List<Troncon> mTroncons) {
        this.mTroncons = mTroncons;
    }

    public int getLongueur() {
        return mLongueur;
    }

    @Override
    public String toString() {
        return "I{" +
                " " + mTroncons +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Itineraire that = (Itineraire) o;

        return mTroncons != null ? mTroncons.equals(that.mTroncons) : that.mTroncons == null;
    }

    public void print(Pane mapPane){
        for (Troncon troncon: mTroncons) {
            troncon.print(mapPane, Color.YELLOW,2);
        }
    }
}

