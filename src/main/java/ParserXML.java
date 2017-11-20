import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ParserXML {
    public void parse(String fichier) {

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            /*
             * Etape 2 : création d'un parseur
             */
            final DocumentBuilder builder = factory.newDocumentBuilder();

	    /*
	     * Etape 3 : création d'un Document
	     */
            final Document document= builder.parse(new File(fichier));


	    /*
	     * Etape 4 : récupération de l'Element racine
	     */
            final Element racine = document.getDocumentElement();

            //Affichage de l'élément racine
            System.out.println("\n*************RACINE************");
            System.out.println(racine.getNodeName());

	    /*
	     * Etape 5 : récupération des personnes
	     */
            final NodeList racineNoeuds = racine.getChildNodes();
            final int nbRacineNoeuds = racineNoeuds.getLength();

            for (int i = 0; i<nbRacineNoeuds; i++) {
                if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    final Element personne = (Element) racineNoeuds.item(i);

                    //Affichage d'une personne
                    System.out.println(personne);
                    System.out.println("point : " + personne.getAttribute("x"));
                }
/*

                    final Element nom = (Element) personne.getElementsByTagName("nom").item(0);
                    final Element prenom = (Element) personne.getElementsByTagName("prenom").item(0);

                    //Affichage du nom et du prénom
                    System.out.println("nom : " + nom.getTextContent());
                    System.out.println("prénom : " + prenom.getTextContent());


                    final NodeList telephones = personne.getElementsByTagName("telephone");
                    final int nbTelephonesElements = telephones.getLength();

                    for(int j = 0; j<nbTelephonesElements; j++) {
                        final Element telephone = (Element) telephones.item(j);

                        //Affichage du téléphone
                        System.out.println(telephone.getAttribute("type") + " : " + telephone.getTextContent());
                    }
                }*/
            }
        }
        catch (final ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (final SAXException e) {
            e.printStackTrace();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }

    }
}
