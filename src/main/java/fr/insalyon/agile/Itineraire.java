package fr.insalyon.agile;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/**
 * La classe Itineraire représente le chemin entre deux points
 */
public class Itineraire {

    private List<Troncon> mTroncons;
    private int mLongueur = 0;
    private Duration mDuree;

    public Itineraire(List<Troncon> troncons) {
        mTroncons =  troncons;
        for (Troncon t : mTroncons) {
            mLongueur += t.getLongueur();
        }

        mDuree = Duration.ofSeconds((int) ((mLongueur*(0.001)/15)*3600));

    }

    public List<Troncon> getTroncons() {
        return mTroncons;
    }

    public Duration getDuree() {
        return mDuree;
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

    /**
     * Affiche un itinéraire sur le plan
     * @param mapPane : Le plan sur lequel on affiche l'itinéraire
     */
    public void print(Pane mapPane){
        for (Troncon troncon: mTroncons) {
            troncon.print(mapPane, Color.YELLOW, Color.GREEN, 2);
        }
    }
}

