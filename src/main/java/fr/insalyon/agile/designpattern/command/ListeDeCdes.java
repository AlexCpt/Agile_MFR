package fr.insalyon.agile.designpattern.command;

import java.util.LinkedList;

/**
 * Liste de commandes qui nous permets de reproduire d'ancienne commandes sauvegardées
 */
public class ListeDeCdes {

    private LinkedList<Commande> listeCdes;
    private int i;

    /**
     * Constructeur de la liste de commande
     */
    public ListeDeCdes() {
        this.listeCdes = new LinkedList<>();
        this.i = -1;
    }

    /**
     * Permet d'ajouter une commande à la liste et de mettre à jour le pointeur sur la commande en cours
     * @param commande commande que l'on veut ajouter
     */
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

    /**
     * Permet de réexécuter la commande précédant la commande courante
     */
    public void undo()
    {
        if(i>=0)
        {
            listeCdes.get(i--).undoCde();
        }
    }

    /**
     * Permet de réexécuter la commande suivant la commande courante
     */
    public void redo()
    {
        if(i<listeCdes.size()-1)
        {
            listeCdes.get(++i).doCde();
        }
    }
}
