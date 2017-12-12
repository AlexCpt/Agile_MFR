package fr.insalyon.agile.parser;

import fr.insalyon.agile.metier.Plan;
import fr.insalyon.agile.metier.DemandeDeLivraison;

public interface Parser {
    Plan parsePlan(String fichier);

    DemandeDeLivraison parseDemandeDeLivraison(String fichier);
}
