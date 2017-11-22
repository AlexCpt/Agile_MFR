import javafx.util.Pair;

import java.util.*;

public class Dijkstra {

    public static Itineraire dijkstra(Plan plan, Point depart, Point arrive) {
        Map<Point, Double> parcourus = new HashMap<>();
        HashMap<Point, List<Troncon>> graph = plan.getGraph();
        Map<Point, Double> distances = new HashMap<>();
        PriorityQueue<Pair<Point, Double>> distanceQueue = new PriorityQueue<>(plan.getPoints().size(), Comparator.comparing(Pair::getValue));
        Map<Point, Point> predecesseurs = new HashMap<>();
        for(Point p : plan.getPoints()){
            distanceQueue.add(new Pair<>(p, Double.MAX_VALUE));
            distances.put(p, Double.MAX_VALUE);
        }

        distances.put(depart, (double) 0);
        distanceQueue.add(new Pair<>(depart, 0d));

        Pair<Point, Double> minPair;
        while(parcourus.size()<plan.getPoints().size())
        {
            do {
                minPair = distanceQueue.poll();
            } while (!distances.containsKey(minPair.getKey()));

            if(graph.containsKey(minPair.getKey()))
            {
                List<Troncon> troncons = graph.get(minPair.getKey());
                for(Troncon t : troncons)
                {
                    double newLongueur = minPair.getValue() + t.getLongueur();
                    if(distances.containsKey(t.getDestination()))
                    {
                        if(distances.get(t.getDestination())> newLongueur)
                        {
                            distances.put(t.getDestination(),newLongueur);
                            distanceQueue.add(new Pair<>(t.getDestination(), newLongueur));
                            predecesseurs.put(t.getDestination(),minPair.getKey());
                        }
                    }


                }
            }
            parcourus.put(minPair.getKey(), minPair.getValue());
            distances.remove(minPair.getKey());
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
                    break;
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
        //System.out.println(dijkstra(plan, points.get(0), points.get(3)));
    }
}

