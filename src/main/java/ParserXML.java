import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ParserXML {
    public Plan parse(String fichier) {

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document= builder.parse(new File(fichier));
            final Element racine = document.getDocumentElement();

            List<Point> listePoints = new ArrayList<>();
            List<Troncon> listeTroncons = new ArrayList<>();
            Map<String,Point> idMapToPoint = new HashMap<>();

            //Affichage de l'élément racine
            System.out.println("\n*************RACINE************");
            System.out.println(racine.getNodeName());

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
                        System.out.println(id);
                        System.out.println(x);
                        System.out.println(y);
                        Point unPoint = new Point(id,x,y);
                        listePoints.add(unPoint);
                        idMapToPoint.put(id,unPoint);
                    }

                    if(noeud.getTagName()=="troncon") {
                        destination = noeud.getAttribute("destination");
                        longueur = Double.valueOf(noeud.getAttribute("longueur"));
                        nomRue =  noeud.getAttribute("nomRue");
                        origine = noeud.getAttribute("origine");

                        System.out.println("destination: " + noeud.getAttribute("destination") +
                                "\nlongueur : " + noeud.getAttribute("longueur") +
                                "\nnom Rue: " + noeud.getAttribute("nomRue") +
                                "\norigine: " + noeud.getAttribute("origine"));

                        Point destinationPoint = idMapToPoint.get(destination);
                        Point originePoint = idMapToPoint.get(origine);
                        Troncon unTroncon = new Troncon(originePoint,destinationPoint,longueur,nomRue);
                        listeTroncons.add(unTroncon);
                    }
                }
            }
            System.out.println("longueur liste finale:"+listeTroncons.get(0).getOrigine().getX());
            Plan plan = new Plan(listePoints,listeTroncons);
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
}
