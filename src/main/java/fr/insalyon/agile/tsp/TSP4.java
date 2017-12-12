package fr.insalyon.agile.tsp;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import fr.insalyon.agile.metier.Point;

import static java.time.temporal.ChronoUnit.SECONDS;


public class TSP4 extends TemplateTSP {
    private List<Point> mLivraisons;
    private LocalTime mTempsActuel;

    public TSP4(List<Point> livraisons, LocalTime depart) {
        mLivraisons = livraisons;
        mTempsActuel = depart;
    }

    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
        ArrayList<Integer> coutsDepuisSommet = new ArrayList<>(nonVus.size());
        for (int i = 0; i < nonVus.size(); i++) {
            coutsDepuisSommet.add(nonVus.get(i));
        }

        coutsDepuisSommet.sort(Comparator.comparing(o -> Integer.valueOf(cout[sommetCrt][o])));

        return coutsDepuisSommet.iterator();
    }

    @Override
    protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
        int somme = 0;

        int coutMin = Integer.MAX_VALUE;
        for (int i = 0; i < nonVus.size(); i++) {
            if (cout[sommetCourant][nonVus.get(i)] < coutMin) {
                coutMin = cout[sommetCourant][nonVus.get(i)];
            }
        }

        somme += coutMin;

        for (int i = 0; i < nonVus.size(); i++) {
            coutMin = cout[nonVus.get(i)][0];
            for (int j = 0; j < nonVus.size(); j++) {
                if (i != j) {
                    if (cout[nonVus.get(i)][nonVus.get(j)] < coutMin) {
                        coutMin = cout[nonVus.get(i)][nonVus.get(j)];
                    }
                }
            }

            somme += coutMin + duree[nonVus.get(i)];
        }

        return somme;
    }

    @Override
    void branchAndBound(int sommetCrt, ArrayList<Integer> nonVus, ArrayList<Integer> vus, int coutVus, int[][] cout, int[] duree, long tpsDebut, int tpsLimite) {
        if (System.currentTimeMillis() - tpsDebut > tpsLimite){
            tempsLimiteAtteint = true;
            return;
        }
        if (nonVus.size() == 0){ // tous les sommets ont ete visites
            coutVus += cout[sommetCrt][0];
            if (coutVus < coutMeilleureSolution){ // on a trouve une solution meilleure que meilleureSolution
                vus.toArray(meilleureSolution);
                coutMeilleureSolution = coutVus;
            }
        } else if (coutVus + bound(sommetCrt, nonVus, cout, duree) < coutMeilleureSolution){
            Iterator<Integer> it = iterator(sommetCrt, nonVus, cout, duree);
            while (it.hasNext()){
                Integer prochainSommet = it.next();

                Integer coutAdditionnel = 0;

                LocalTime previousTime = mTempsActuel;

                mTempsActuel = mTempsActuel.plusSeconds(cout[sommetCrt][prochainSommet] + duree[prochainSommet]);

                if (mLivraisons.get(prochainSommet - 1).getType() == Point.Type.LIVRAISON) {
                    LocalTime debut = mLivraisons.get(prochainSommet - 1).getLivraison().getDebutPlage();
                    LocalTime fin = mLivraisons.get(prochainSommet - 1).getLivraison().getFinPlage();

                    if (debut != null && fin != null) {
                        if (mTempsActuel.isAfter(fin)) {
                            mTempsActuel = previousTime;
                            continue;
                        }

                        if (mTempsActuel.isBefore(debut)) {
                            coutAdditionnel += (int)SECONDS.between(mTempsActuel, debut);
                        }
                    }
                }

                mTempsActuel = mTempsActuel.plusSeconds(coutAdditionnel);

                vus.add(prochainSommet);
                nonVus.remove(prochainSommet);
                branchAndBound(prochainSommet, nonVus, vus, coutVus + cout[sommetCrt][prochainSommet] + duree[prochainSommet] + coutAdditionnel, cout, duree, tpsDebut, tpsLimite);
                vus.remove(prochainSommet);
                nonVus.add(prochainSommet);

                mTempsActuel = previousTime;
            }
        }
    }


}
