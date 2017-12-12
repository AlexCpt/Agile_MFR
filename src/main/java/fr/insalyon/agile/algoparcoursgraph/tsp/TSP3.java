package fr.insalyon.agile.algoparcoursgraph.tsp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class TSP3 extends TemplateTSP {

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


}
