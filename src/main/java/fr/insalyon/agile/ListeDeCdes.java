package fr.insalyon.agile;

import java.util.LinkedList;

public class ListeDeCdes {

    private LinkedList<Commande> listeCdes;
    private int i;

    public ListeDeCdes() {
        this.listeCdes = new LinkedList<>();
        this.i = -1;
    }

    public void ajoute(Commande commande){
        if(i<listeCdes.size()-1){
            while(listeCdes.size()-1>i)
            {
                listeCdes.remove(listeCdes.size()-1);
            }
        }
        listeCdes.add(commande);
        commande.doCde();
        i++;
    }

    public void undo()
    {
        if(i>=0)
        {
            listeCdes.get(i--).undoCde();
        }
    }

    public void redo()
    {
        if(i<listeCdes.size()-1)
        {
            listeCdes.get(++i).doCde();
        }
    }
}
