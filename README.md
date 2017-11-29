# Agile_MFR

Le but de ce projet est de : 
* Charger le plan d'une ville sous forme d'XML
* Afficher ce plan dans une interface graphique
* Charger une liste de demandes de livraisons
* **Calculer le trajet le plus efficace passant par tous ces points de livraison**
* Rajouter / supprimer des livraisons sur un trajet déjà calculé.

## Interface 
Nous utilisons JavaFX pour le tracé des routes et l'interface utilisateur.

## Calcul d'itinéraire
C'est le coeur de notre application. Cette implémentation du problème du voyageur de commerce se doit d'être optimisée pour pouvoir gérer un maximum de points de livraison dans un temps de calcul convenable.

Nous utilisons Ddfijkstra pour le calcul des distances entre les points, puis un algorithme de TSP

### Algorithmie
> À détailler.
