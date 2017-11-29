package fr.insalyon.agile;

import javafx.scene.layout.Pane;

import java.time.LocalTime;
import java.util.List;

public class Tournee {

    LocalTime mDateArrivee;
    List<Itineraire> mItineraires;

    public Tournee(List<Itineraire> itineraires, LocalTime dateArrivee) {
        mItineraires = itineraires;
        mDateArrivee = dateArrivee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tournee tournee = (Tournee) o;

        if (mDateArrivee != null ? !mDateArrivee.equals(tournee.mDateArrivee) : tournee.mDateArrivee != null)
            return false;
        return mItineraires != null ? mItineraires.equals(tournee.mItineraires) : tournee.mItineraires == null;
    }

    public void print(Pane mapPane){
        for (Itineraire itineraire: mItineraires) {
            itineraire.print(mapPane);
        }
    }
}
