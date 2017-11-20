import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.min;

public class Dijkstra {

    //Possibilité d'utiliser un heap pour améliorer la recherche de la distance minimale
    private static Point getMinKey(Map<Point, Integer> map) {
        Point minKey = null;
        int minValue = Integer.MAX_VALUE;
        for(Point key : map.keySet()) {
            int value = map.get(key);
            if(value < minValue) {
                minValue = value;
                minKey = key;
            }
        }
        return minKey;
    }

    public static Itineraire dijkstra(Plan plan, Point depart, Point arrive) {
        Map<Point, Integer> parcourus = new HashMap<>();
        HashMap<Point, List<Troncon>> graph = plan.getGraph();
        Map<Point, Integer> distances = new HashMap<>();
        Map<Point, Point> predecesseurs = new HashMap<>();
        for(Point p : plan.getPoints()){
            distances.put(p, 10000);
        }
        distances.put(depart, 0);
        predecesseurs.put(depart, depart);
        while(parcourus.size()<plan.getPoints().length)
        {
            Point minDistances = getMinKey(distances);

            if(graph.containsKey(minDistances))
            {
                List<Troncon> troncons = graph.get(minDistances);
                for(Troncon t : troncons)
                {
                    int newLongueur = distances.get(minDistances) + t.getLongueur();
                    if(distances.containsKey(t.getDestination()))
                    {
                        if(distances.get(t.getDestination())> newLongueur)
                        {
                            distances.put(t.getDestination(),newLongueur);
                            predecesseurs.put(t.getDestination(),minDistances);
                        }
                    }


                }
            }
            parcourus.put(minDistances, distances.get(minDistances));
            distances.remove(minDistances);

        }
        List<Troncon> itineraire = new ArrayList<>();
        Point current = arrive;
        Point predecesseurCurrent;
        while(!current.equals(depart))
        {
            predecesseurCurrent = predecesseurs.get(current);
            for(Troncon t : plan.getGraph().get(predecesseurCurrent)){
                if(t.getDestination().equals(current)){

                    itineraire.add(0, t);
                }
            }
            current = predecesseurCurrent;

        }

        return new Itineraire(itineraire);
    }

    public static void main (String[] args){
        Point[] points = {
                new Point(1, 2),
                new Point(2, 3),
                new Point(3, 4),
                new Point(4, 5),
                new Point(5, 6),
                new Point(6, 7)
        };

        Troncon[] troncons = {
                new Troncon(points[0], points[1],1),
                new Troncon(points[0], points[2],9),
                new Troncon(points[0], points[5],14),
                new Troncon(points[1], points[2],10),
                new Troncon(points[1], points[3],15),
                new Troncon(points[2], points[5],2),
                new Troncon(points[2], points[3],11),
                new Troncon(points[5], points[4],9),
                new Troncon(points[4], points[3],6)
        };

        Plan plan = new Plan(points, troncons);
        System.out.println(dijkstra(plan, points[0], points[3]));
    }
}
