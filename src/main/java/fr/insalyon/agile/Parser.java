package fr.insalyon.agile;

import fr.insalyon.agile.DemandeDeLivraison;

public interface Parser {
    Plan parsePlan(String fichier);

    DemandeDeLivraison parseDemandeDeLivraison(String fichier);
}
