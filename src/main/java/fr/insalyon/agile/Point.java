package fr.insalyon.agile;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
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
    final double popoverButtonRadius = 12;
    private double mXscaled;
    private double mYscaled;


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

    public void print(Pane mapPane, Stage primaryStage){

        mXscaled = (((mX - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth());
        mYscaled = ((mY-Plan.mPointYmin)/(double) (Plan.mPointYmax-Plan.mPointYmin)*mapPane.getPrefHeight());

        Circle circle = new Circle(radiusAffichage);
        Button rndBtnPopover = new Button();
        rndBtnPopover.setLayoutX(mXscaled - popoverButtonRadius);
        rndBtnPopover.setLayoutY(mYscaled - popoverButtonRadius);
        circle.relocate(mXscaled - radiusAffichage,mYscaled - radiusAffichage);
        rndBtnPopover.setStyle(
                "-fx-background-radius: 5em; " +
                        "-fx-min-width: "+popoverButtonRadius*2+"px; " +
                        "-fx-min-height: "+popoverButtonRadius*2+"px; " +
                        "-fx-max-width: "+popoverButtonRadius*2+"px; " +
                        "-fx-max-height: "+popoverButtonRadius*2+"px; " +
                        "-fx-background-color: transparent;"+
                        "-fx-background-insets: 0px; " +
                        "-fx-padding: 0px;"
        );

        //Popover
        Label lblLivraisonPopover = new Label("Livraison 1 - 10h30");
        Bounds boundsInScreen = lblLivraisonPopover.localToScreen(lblLivraisonPopover.getBoundsInLocal());
        lblLivraisonPopover.setPadding(new Insets(10,10,10,10));
        PopOver popOver = new PopOver();
        popOver.setContentNode(lblLivraisonPopover);
        //popOver.setX(boundsInScreen.getMinX());
        //popOver.setY(boundsInScreen.getMinY());
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
        popOver.setDetachable(false);


        rndBtnPopover.setOnMouseEntered(e -> popOver.show(primaryStage));
        rndBtnPopover.setOnMouseExited(e -> popOver.hide());

        if(mType == Type.ENTREPOT){
            circle.setFill(Color.RED);
            mapPane.getChildren().add(circle);
            mapPane.getChildren().add(rndBtnPopover);
        } else if (mType == Type.LIVRAISON) {
            circle.setFill(Color.BLUE);
            mapPane.getChildren().add(circle);
            mapPane.getChildren().add(rndBtnPopover);
        }
        else {
        }
    }
}
