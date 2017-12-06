package fr.insalyon.agile;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import org.controlsfx.control.PopOver;

public class Troncon {

    private Point mOrigine;
    private Point mDestination;
    private double mLongueur;
    private String mNomRue;
    private double mLongueurParcourue;
    private Line line;
    private Line line2;

    public Troncon(Point origine, Point destination, double longueur, String nomRue) {
        mOrigine = origine;
        mDestination = destination;
        mLongueur = longueur;
        mNomRue = nomRue;
        mLongueurParcourue = 0;
    }

    public Point getOrigine() {
        return mOrigine;
    }

    public Point getDestination() {
        return mDestination;
    }

    public double getLongueur() { return mLongueur; }

    public String getNomRue() { return mNomRue; }

    public double angleWith(Troncon other) {
        double a_x = mDestination.getX() - mOrigine.getX();
        double a_y = mDestination.getY() - mOrigine.getY();
        double b_x = other.getDestination().getX() - other.getOrigine().getX();
        double b_y = other.getDestination().getY() - other.getOrigine().getY();
        return Math.atan2( a_x*b_y - a_y*b_x, a_x*b_x + a_y*b_y );
    }

    @Override
    public String toString() {
        return "T{" +
                " " + mOrigine +
                ", " + mDestination +
                ", " + mLongueur +
                '}';
    }

    public void setLongueurParcourue(Pane mapPane, double longueurParcourue) {
        this.mLongueurParcourue = longueurParcourue;

        double midPointX = ((((mLongueurParcourue / mLongueur) * (this.getDestination().getX() - this.getOrigine().getX()) + this.getOrigine().getX()) - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth();
        double midPointY = ((((mLongueurParcourue / mLongueur) * (this.getDestination().getY() - this.getOrigine().getY()) + this.getOrigine().getY()) - Plan.mPointYmin) / (double) (Plan.mPointYmax - Plan.mPointYmin)) * mapPane.getPrefHeight();

        line.setStartX(midPointX);
        line.setStartY(midPointY);

        line2.setEndX(midPointX);
        line2.setEndY(midPointY);

        if (mLongueurParcourue == mLongueur) {
            line.setVisible(false);
            line2.setVisible(true);
        } else if (mLongueurParcourue == 0) {
            line.setVisible(true);
            line2.setVisible(false);
        } else {
            line.setVisible(true);
            line2.setVisible(true);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Troncon troncon = (Troncon) o;

        if (mLongueur != troncon.mLongueur) return false;
        if (mOrigine != null ? !mOrigine.equals(troncon.mOrigine) : troncon.mOrigine != null) return false;
        return mDestination != null ? mDestination.equals(troncon.mDestination) : troncon.mDestination == null;
    }

    public void print(Pane mapPane, javafx.scene.paint.Color color, javafx.scene.paint.Color color2, int epaisseur){
        double midPointX = ((((mLongueurParcourue / mLongueur) * (this.getDestination().getX() - this.getOrigine().getX()) + this.getOrigine().getX()) - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth();
        double midPointY = ((((mLongueurParcourue / mLongueur) * (this.getDestination().getY() - this.getOrigine().getY()) + this.getOrigine().getY()) - Plan.mPointYmin) / (double) (Plan.mPointYmax - Plan.mPointYmin)) * mapPane.getPrefHeight();

        line = new Line();
        line.setStroke(color);
        line.setStrokeWidth(epaisseur);
        line.setStartX(midPointX);
        line.setStartY(midPointY);
        line.setEndX(((this.getDestination().getX() - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth());
        line.setEndY(((this.getDestination().getY() - Plan.mPointYmin) / (double) (Plan.mPointYmax - Plan.mPointYmin)) * mapPane.getPrefHeight());
        mapPane.getChildren().add(line);

        line2 = new Line();
        line2.setStroke(color2);
        line2.setStrokeWidth(epaisseur);
        line2.setStartX(((this.getOrigine().getX() - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth());
        line2.setStartY(((this.getOrigine().getY() - Plan.mPointYmin) / (double) (Plan.mPointYmax - Plan.mPointYmin)) * mapPane.getPrefHeight());
        line2.setEndX(midPointX);
        line2.setEndY(midPointY);
        mapPane.getChildren().add(line2);

        if (mLongueurParcourue == mLongueur) {
            line.setVisible(false);
            line2.setVisible(true);
        } else if (mLongueurParcourue == 0) {
            line.setVisible(true);
            line2.setVisible(false);
        } else {
            line.setVisible(true);
            line2.setVisible(true);
        }
    }
}
