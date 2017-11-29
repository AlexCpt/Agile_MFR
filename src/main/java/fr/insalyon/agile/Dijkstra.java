package fr.insalyon.agile;

import javafx.util.Pair;

import java.util.*;

public class Dijkstra {

    public static List<Itineraire> dijkstra(Plan plan, Point depart, Point[] arrives) {
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

        List<Itineraire> itineraires = new ArrayList<>(arrives.length);
        for (Point arrive : arrives) {
            List<Troncon> itineraire = new ArrayList<>();
            Point current = arrive;
            Point predecesseurCurrent;
            while (!current.equals(depart)) {
                predecesseurCurrent = predecesseurs.get(current);
                for (Troncon t : plan.getGraph().get(predecesseurCurrent)) {
                    if (t.getDestination().equals(current)) {

                        itineraire.add(0, t);
                        break;
                    }
                }
                current = predecesseurCurrent;

            }

            itineraires.add(new Itineraire(itineraire));
        }

        return itineraires;
    }
}

