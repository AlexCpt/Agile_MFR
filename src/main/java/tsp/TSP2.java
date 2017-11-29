package tsp;

import tsp.IteratorSeq;
import tsp.TemplateTSP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.IntStream;

public class TSP2 extends TemplateTSP {

    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
        return new IteratorSeq(nonVus, sommetCrt);
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
