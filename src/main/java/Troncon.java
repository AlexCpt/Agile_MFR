import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class Troncon {

    private Point mOrigine;
    private Point mDestination;
    private int mLongueur;

    public Troncon(Point origine, Point destination, int longueur) {
        mOrigine = origine;
        mDestination = destination;
        mLongueur = longueur;
    }

    public Point getOrigine() {
        return mOrigine;
    }

    public Point getDestination() {
        return mDestination;
    }

    public int getLongueur() { return mLongueur; }

    @Override
    public String toString() {
        return "T{" +
                " " + mOrigine +
                ", " + mDestination +
                ", " + mLongueur +
                '}';
    }

    public void print(Pane mapPane){
        Line line = new Line();
        line.setStartX((this.getOrigine().getX()-Plan.mPointXmin)/(Plan.mPointXmax-Plan.mPointXmin)*mapPane.getPrefWidth());
        line.setStartY((this.getOrigine().getY()-Plan.mPointYmin)/(Plan.mPointYmax-Plan.mPointYmin)*mapPane.getPrefHeight());
        line.setEndX((this.getDestination().getX()-Plan.mPointXmin)/(Plan.mPointXmax-Plan.mPointXmin)*mapPane.getPrefWidth());
        line.setEndY((this.getDestination().getY()-Plan.mPointYmin)/(Plan.mPointYmax-Plan.mPointYmin)*mapPane.getPrefHeight());
        mapPane.getChildren().add(line);
    }
} //to merge