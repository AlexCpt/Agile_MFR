package fr.insalyon.agile;

public interface Parser {
    Plan parsePlan(String fichier);

    DemandeDeLivraison parseDemandeDeLivraison(String fichier);
}
