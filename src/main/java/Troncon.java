import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class Troncon {

    private Point mOrigine;
    private Point mDestination;
    private double mLongueur;
    private String mNomRue;

    public Troncon(Point origine, Point destination, double longueur, String nomRue) {
        mOrigine = origine;
        mDestination = destination;
        mLongueur = longueur;
        mNomRue = nomRue;
    }

    public Point getOrigine() {
        return mOrigine;
    }

    public Point getDestination() {
        return mDestination;
    }

    public double getLongueur() { return mLongueur; }

    @Override
    public String toString() {
        return "T{" +
                " " + mOrigine +
                ", " + mDestination +
                ", " + mLongueur +
                '}';
    }

    public void print(Pane mapPane, javafx.scene.paint.Color color){

        Line line = new Line();
        line.setStroke(color);
        line.setStartX(((this.getOrigine().getX() - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth());
        line.setStartY(((this.getOrigine().getY() - Plan.mPointYmin) / (double) (Plan.mPointYmax - Plan.mPointYmin)) * mapPane.getPrefHeight());
        line.setEndX(((this.getDestination().getX() - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth());
        line.setEndY(((this.getDestination().getY() - Plan.mPointYmin) / (double) (Plan.mPointYmax - Plan.mPointYmin)) * mapPane.getPrefHeight());
        mapPane.getChildren().add(line);
    }
} //to merge