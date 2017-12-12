package fr.insalyon.agile.parser;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import fr.insalyon.agile.metier.Plan;
import fr.insalyon.agile.metier.Point;
import fr.insalyon.agile.metier.Troncon;
import fr.insalyon.agile.metier.DemandeDeLivraison;
import fr.insalyon.agile.metier.Entrepot;
import fr.insalyon.agile.metier.Livraison;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * La classe ParserXML permet de récupérer
 * les informations contenues dans un fichier XML
 */
public class ParserXML implements Parser {

    private Map<String, Point> idMapToPoint;
    private Plan plan;

    /**
     * Construit un plan à partir d'un fichier XML formmaté correctement
     * @param fichier : Le nom du fichier à charger
     * @return Une instance de Plan, construite à partir des
     * informations contenues dans le fichier passé en paramètre
     */
    @Override
    public Plan parsePlan(String fichier) {
        if (idMapToPoint != null) {
            idMapToPoint.clear();
        }

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.parse(new File(fichier));
            final Element racine = document.getDocumentElement();

            List<Point> listePoints = new ArrayList<>();
            List<Troncon> listeTroncons = new ArrayList<>();
            idMapToPoint = new HashMap<>();


            final NodeList racineNoeuds = racine.getChildNodes();
            final int nbRacineNoeuds = racineNoeuds.getLength();
            String id;
            int x;
            int y;

            String destination;
            double longueur;
            String nomRue;
            String origine;

            for (int i = 0; i<nbRacineNoeuds; i++) {
                if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    final Element noeud = (Element) racineNoeuds.item(i);
                    if(noeud.getTagName()=="noeud") {
                        id = noeud.getAttribute("id");
                        x = Integer.valueOf(noeud.getAttribute("x"));
                        y = Integer.valueOf(noeud.getAttribute("y"));
                        Point unPoint = new Point(id,y,-x);
                        listePoints.add(unPoint);
                        idMapToPoint.put(id,unPoint);
                    }

                    if(noeud.getTagName()=="troncon") {
                        destination = noeud.getAttribute("destination");
                        longueur = Double.valueOf(noeud.getAttribute("longueur"));
                        nomRue =  noeud.getAttribute("nomRue");
                        origine = noeud.getAttribute("origine");

                        Point destinationPoint = idMapToPoint.get(destination);
                        Point originePoint = idMapToPoint.get(origine);
                        Troncon unTroncon = new Troncon(originePoint,destinationPoint,longueur,nomRue);
                        listeTroncons.add(unTroncon);
                    }
                }
            }
            plan = new Plan(listePoints,listeTroncons);
            return plan;
        }

        catch (final ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        catch (final SAXException e) {
            e.printStackTrace();
            return null;
        }
        catch (final IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Construit une demande de livraison à partir d'un fichier XML formmaté correctement
     * @param fichier : Le nom du fichier à charger
     * @return Une instance de DemandeDeLivraison, construite à partir des
     * informations contenues dans le fichier passé en paramètre
     */
    public DemandeDeLivraison parseDemandeDeLivraison(String fichier) {

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.parse(new File(fichier));
            final Element racine = document.getDocumentElement();

            final NodeList racineNoeuds = racine.getChildNodes();
            final int nbRacineNoeuds = racineNoeuds.getLength();

            List<Point> livraisons=new ArrayList<>();
            LocalTime depart = null;
            LocalTime debutPlage = null;
            LocalTime finPlage = null;
            Duration duree = null;
            String idEntrepot;
            Point entrepot=new Point();


            for (int i = 0; i < nbRacineNoeuds; i++) {
                if (racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    final Element noeud = (Element) racineNoeuds.item(i);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m:s");
                    if (noeud.getTagName() == "entrepot") {
                        idEntrepot=noeud.getAttribute("adresse");

                        String departString = noeud.getAttribute("heureDepart");
                        depart = LocalTime.parse(departString, formatter);

                        entrepot=idMapToPoint.get(idEntrepot);
                        entrepot.setEntrepot(new Entrepot());
                    }
                    if (noeud.getTagName() == "livraison") {
                        String idLivraison=noeud.getAttribute("adresse");
                        Point livraison=idMapToPoint.get(idLivraison);
                        livraisons.add(livraison);

                        String dureeString = noeud.getAttribute("duree");
                        duree = Duration.ofSeconds(Integer.parseInt(dureeString));

                        if (noeud.hasAttribute("debutPlage")) {
                            String debutPlageString = noeud.getAttribute("debutPlage");
                            debutPlage = LocalTime.parse(debutPlageString, formatter);
                        } else {
                            debutPlage = null;
                        }

                        if (noeud.hasAttribute("finPlage")) {
                            String finPlageString = noeud.getAttribute("finPlage");
                            finPlage = LocalTime.parse(finPlageString, formatter);
                        } else {
                            finPlage = null;
                        }

                        livraison.setLivraison(new Livraison(debutPlage, finPlage, duree));
                    }
                }
            }

            return new DemandeDeLivraison(plan, livraisons,entrepot,depart);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        }
    }
}