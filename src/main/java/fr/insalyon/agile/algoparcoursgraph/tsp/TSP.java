package fr.insalyon.agile.algoparcoursgraph.tsp;

public interface TSP {
		
	/**
	 * @return true si chercheSolution() s'est terminee parce que la limite de temps avait ete atteinte, avant d'avoir pu explorer tout l'espace de recherche,
	 */
    Boolean getTempsLimiteAtteint();
	
	/**
	 * Cherche un circuit de duree minimale passant par chaque sommet (compris entre 0 et nbSommets-1)
	 * @param tpsLimite : limite (en millisecondes) sur le temps d'execution de chercheSolution
	 * @param nbSommets : nombre de sommets du graphe
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 &lt;= i &lt; nbSommets et 0 &lt;= j &lt; nbSommets
	 * @param duree : duree[i] = duree pour visiter le sommet i, avec 0 &lt;= i &lt; nbSommets
	 */
    void chercheSolution(int tpsLimite, int nbSommets, int[][] cout, int[] duree);
	
	/**
	 * @param i
	 * @return le sommet visite en i-eme position dans la solution calculee par chercheSolution
	 */
    Integer getMeilleureSolution(int i);
	
	/** 
	 * @return la duree de la solution calculee par chercheSolution
	 */
    int getCoutMeilleureSolution();
}
