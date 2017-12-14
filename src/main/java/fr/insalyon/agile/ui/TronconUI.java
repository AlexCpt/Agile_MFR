package fr.insalyon.agile.ui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;


/**
 * La classe TronconUI est utilisée pour la réprésentation temporelle d'un itinéraire dans la timeline. Il prend la forme
 * d'une ligne et correspond au trajet entre deux livraisons. Sa taille dépend de la durée de l'itinéraire. Sa couleur va
 * du vert au rouge et indique si le livreur arrive avant la livraison (il a du temps : vert), ou juste à l'heure (rouge).
 */
public class TronconUI {
    private Line line;
    private Color lineColor;

    /**
     * Constructeur d'un TronconUI
     * @param x abscisse du TronconUI (une seule valeur car c'est une ligne verticale
     * @param startY ordonnée de départ du TronconUI
     * @param endY abscisse d'arrivée TronconUI
     * @param marge marge associée à l'itinéraire représenté
     * @param margeMax Valeur correspondant à un temps de flexibilité maximal (vert total)
     */
    public TronconUI (double x, double startY, double endY, double marge, double margeMax){
        lineColor = Color.RED.interpolate(Color.GREEN, marge / margeMax);

        line = new Line();
        line.setStroke(lineColor);
        line.setStrokeWidth(2);
        line.setStartX(x);
        line.setStartY(startY);
        line.setEndX(x);
        line.setEndY(endY);
    }

    /**
     * Affiche le TronconUI sur la timeline
     * @param pane : Le pane de la timeline sur laquelle on affiche le TronconUI
     */
    public void print(Pane pane){
        pane.getChildren().add(line);
    }
}
