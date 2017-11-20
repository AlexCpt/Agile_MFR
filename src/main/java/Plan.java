import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class Plan {
    private Point[] mPoints;
    private Troncon[] mTroncons;
    public static int mPointXmin;
    public static int mPointYmin;
    public static int mPointXmax;
    public static int mPointYmax;


    public HashMap<Point, List<Troncon>> getGraph() {
        return mGraph;
    }

    private HashMap<Point, List<Troncon>> mGraph;

    public Plan(Point[] points, Troncon[] troncons) {
        mPoints = points;
        mTroncons = troncons;

        mGraph = new HashMap<>();

        // FIXME: Réduire le nombre d'allocations en faisant un premier passage pour compter le nombre de troncons par
        // points, pour ensuite allouer avec le nombre exact de troncons
        // FIXME: peut-être utiliser un HashSet au lieu de la LinkedList vu que l'ordre ne nous importe pas et qu'on peut être amenés
        // à vérifier l'existence d'un tronçon à partir d'un point.
        for (Troncon t : troncons) {
            if (!mGraph.containsKey(t.getOrigine())) {
                mGraph.put(t.getOrigine(), new LinkedList<>());
            }

            mGraph.get(t.getOrigine()).add(t);
        }

        calculEchelle();

    }

    public Point[] getPoints() {
        return mPoints;
    }

    public Troncon[] getTroncons() {
        return mTroncons;
    }

    public void calculEchelle()
    {
        for(Point point : mPoints)
        {
            if(point.getX() > mPointXmax){
                mPointXmax = point.getX();
            }
            if(point.getX() < mPointXmin){
                mPointXmin = point.getX();
            }
            if(point.getY() > mPointYmax){
                mPointYmax = point.getY();
            }
            if(point.getY() < mPointYmin){
                mPointYmin = point.getY();
            }
        }

        System.out.println(mPointXmin + " " + mPointXmax + " " + mPointYmin + " " + mPointYmax );
    }

    public void print(Pane mapPane) {

        for (Troncon troncon: mTroncons) {
            troncon.print((mapPane));
        }

        for (Point point: mPoints) {
            point.print(mapPane);
        }
    }
}

//to merge