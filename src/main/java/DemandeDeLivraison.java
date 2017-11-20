import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemandeDeLivraison {

    List<Point> mLivraisons;
    Point mEntrepot;
    LocalDateTime mDepart;

    public DemandeDeLivraison(List<Point> livraisons, Point entrepot, LocalDateTime depart) {
        this.mLivraisons = livraisons;
        this.mEntrepot = entrepot;
        this.mDepart = depart;
    }

    public Tournee calculerTournee(Plan plan){
        List<Itineraire> itineraires = new ArrayList<>();
        List<Point> points = new ArrayList<>(mLivraisons);
        points.add(mEntrepot);
        Point current = mEntrepot;
        Point successeur = mLivraisons.get(0);
        for(int i=1; i<mLivraisons.size(); i++)
        {
            itineraires.add(Dijkstra.dijkstra(plan, current, successeur));
            current = successeur;
            successeur = mLivraisons.get(i);
        }
        itineraires.add(Dijkstra.dijkstra(plan, current, successeur));
        itineraires.add(Dijkstra.dijkstra(plan, successeur, mEntrepot));

        return new Tournee(itineraires);
    }

}
