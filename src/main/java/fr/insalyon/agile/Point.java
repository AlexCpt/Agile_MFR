package fr.insalyon.agile;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.controlsfx.control.PopOver;
import javafx.stage.Stage;


public class Point {
    private String mId;
    private int mX;
    private int mY;
    final double radiusAffichage = 4;
    private double coordX;
    private double coordY;
    public String mAdresse;

    public enum Type {
        POINT,
        LIVRAISON,
        ENTREPOT
    };

    private Type mType;
    private Livraison mLivraison;
    private Entrepot mEntrepot;

    public void setEntrepot(Entrepot entrepot) {
        mType = Type.ENTREPOT;
        mLivraison = null;
        mEntrepot = entrepot;
    }

    public void setLivraison(Livraison livraison) {
        mType = Type.LIVRAISON;
        mEntrepot = null;
        mLivraison = livraison;
    }

    public void setPoint() {
        mType = Type.POINT;
        mEntrepot = null;
        mLivraison = null;
    }

    public Type getType() {
        return mType;
    }

    public Livraison getLivraison() {
        return mLivraison;
    }

    public void setAdresse(String adresse) {
        mAdresse = adresse;
    }

    public Point(){};
    public Point(String id, int x, int y) {
        mType = Type.POINT;
        mId = id;
        mX = x;
        mY = y;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    @Override
    public String toString() {
        return "P{" +
                " " + mX +
                ", " + mY +
                ", '" + mAdresse + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (mX != point.mX) return false;
        if (mY != point.mY) return false;
        if (mAdresse != null ? !mAdresse.equals(point.mAdresse) : point.mAdresse != null) return false;
        if (mType != point.mType) return false;
        if (mLivraison != null ? !mLivraison.equals(point.mLivraison) : point.mLivraison != null) return false;
        return mEntrepot != null ? mEntrepot.equals(point.mEntrepot) : point.mEntrepot == null;
    }

    public void print(Pane mapPane) {

        Circle circle = new Circle(radiusAffichage);
        coordX = ((mX - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth();
        coordY = ((mY - Plan.mPointYmin) / (double) (Plan.mPointYmax - Plan.mPointYmin) * mapPane.getPrefHeight());
        circle.relocate(coordX - radiusAffichage, coordY - radiusAffichage);

        if (mType == Type.ENTREPOT) {
            circle.setFill(Color.RED);
            mapPane.getChildren().add(circle);

        } else if (mType == Type.LIVRAISON) {
            circle.setFill(Color.BLUE);
            mapPane.getChildren().add(circle);
        }

    }

    public void printHover(Pane mapPane, Stage primaryStage, Button rndBtnPopover, String label){
        PopOver popOver = new PopOver();
        popOver.setAutoHide(true);
        popOver.setDetachable(false);
        popOver.setContentNode(new Label(label));
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);

        popOver.setX(coordX + mapPane.getBoundsInParent().getMinX() + primaryStage.getX());
        popOver.setY(coordY + mapPane.getBoundsInParent().getMinY() + primaryStage.getY() - 10);
        rndBtnPopover.setOnMouseEntered(e -> popOver.show(primaryStage));
        rndBtnPopover.setOnMouseExited(e -> popOver.hide());
    }

    public String getAdresse() {
        return mAdresse;
    }
}
