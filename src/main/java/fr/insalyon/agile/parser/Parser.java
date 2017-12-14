package fr.insalyon.agile.parser;

import fr.insalyon.agile.modele.Plan;
import fr.insalyon.agile.modele.DemandeDeLivraison;

public interface Parser {
    Plan parsePlan(String fichier);

    DemandeDeLivraison parseDemandeDeLivraison(String fichier);
}
