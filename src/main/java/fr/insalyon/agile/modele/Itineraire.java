package fr.insalyon.agile.modele;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.time.Duration;
import java.util.List;

/**
 * La classe Itineraire représente le chemin entre deux points
 */
public class Itineraire {

    private List<Troncon> mTroncons;
    private int mLongueur = 0;
    private Duration mDuree;

    /**
     * Constructeur d'un Itinéraire
     * @param troncons liste de troncons qui correspond à l'itineraire
     */
    public Itineraire(List<Troncon> troncons) {
        mTroncons =  troncons;
        for (Troncon t : mTroncons) {
            mLongueur += t.getLongueur();
        }

        mDuree = Duration.ofSeconds((int) ((mLongueur*(0.001)/15)*3600));

    }

    /**
     * Permet de récupérer l'ensemble des troncons associes à l'itineraire
     * @return liste de troncons
     */
    public List<Troncon> getTroncons() {
        return mTroncons;
    }

    /**
     * Permet de récupérer la duree associee a un itineraire
     * @return duree
     */
    public Duration getDuree() {
        return mDuree;
    }

    /**
     * Permet de récupérer la longueur associee a un itineraire
     * @return longueur en m
     */
    public int getLongueur() {
        return mLongueur;
    }


    /**
     * Permet d'afficher les caractéristiques d'un itineraire
     * @return string representant un itineraire
     */
    @Override
    public String toString() {
        return "I{" +
                " " + mTroncons +
                '}';
    }

    /**
     * Permet de comparer deux itineraires afin de savoir s'ils sont egaux
     * @param o deuxieme itineraire
     * @return boolean true si egaux false sinon
     */
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

