public interface Parser {
    Plan parsePlan(String fichier);

    DemandeDeLivraison parseDemandeDeLivraison(String fichier);
}
