import java.util.*;

import static java.lang.Math.min;

public class Dijkstra {

    //Possibilité d'utiliser un heap pour améliorer la recherche de la distance minimale
    private static Point getMinKey(Map<Point, Double> map) {
        Point minKey = null;
        double minValue = Integer.MAX_VALUE;
        for(Point key : map.keySet()) {
            double value = map.get(key);
            if(value < minValue) {
                minValue = value;
                minKey = key;
            }
        }
        return minKey;
    }

    public static Itineraire dijkstra(Plan plan, Point depart, Point arrive) {
        Map<Point, Double> parcourus = new HashMap<>();
        HashMap<Point, List<Troncon>> graph = plan.getGraph();
        Map<Point, Double> distances = new HashMap<>();
        Map<Point, Point> predecesseurs = new HashMap<>();
        for(Point p : plan.getPoints()){
            distances.put(p, (double) 10000);
        }
        distances.put(depart, (double) 0);
        predecesseurs.put(depart, depart);
        while(parcourus.size()<plan.getPoints().size())
        {
            Point minDistances = getMinKey(distances);

            if(graph.containsKey(minDistances))
            {
                List<Troncon> troncons = graph.get(minDistances);
                for(Troncon t : troncons)
                {
                    double newLongueur = distances.get(minDistances) + t.getLongueur();
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
        List<Point> points = Arrays.asList(
                new Point("0",1, 2),
                new Point("1", 2, 3),
                new Point("2", 3, 4),
                new Point("3", 4, 5),
                new Point("4", 5, 6),
                new Point("5", 6, 7)
        );

        List<Troncon> troncons = Arrays.asList(
                new Troncon(points.get(0), points.get(1),1, "a"),
                new Troncon(points.get(0), points.get(2),9, "a"),
                new Troncon(points.get(0), points.get(5),14, "a"),
                new Troncon(points.get(1), points.get(2),10, "a"),
                new Troncon(points.get(1), points.get(3),15, "a"),
                new Troncon(points.get(2), points.get(5),2, "a"),
                new Troncon(points.get(2), points.get(3),11, "a"),
                new Troncon(points.get(5), points.get(4),9, "a"),
                new Troncon(points.get(4), points.get(3),6, "a"),
                new Troncon(points.get(5), points.get(0),2, "a")
        );

        Plan plan = new Plan(points, troncons);
        System.out.println(dijkstra(plan, points.get(0), points.get(3)));
    }
    }

