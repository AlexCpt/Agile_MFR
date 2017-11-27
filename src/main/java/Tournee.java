import javafx.scene.layout.Pane;

import java.util.List;

public class Tournee {

    List<Itineraire> mItineraires;

    public Tournee(List<Itineraire> itineraires) {
        mItineraires = itineraires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tournee tournee = (Tournee) o;

        return mItineraires != null ? mItineraires.equals(tournee.mItineraires) : tournee.mItineraires == null;
    }

    public void print(Pane mapPane){
        for (Itineraire itineraire: mItineraires) {
            itineraire.print(mapPane);
        }
    }
}
