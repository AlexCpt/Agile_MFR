package fr.insalyon.agile;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.PopOver;
import javafx.stage.Stage;


public class Point {
    private String mId;
    private int mX;
    private int mY;
    final double radiusAffichage = 4;
    private double coordX;
    private double coordY;
    private PopOver suppPopover;
    public String mAdresse;
    private Circle circle;

    public enum Type {
        POINT,
        LIVRAISON,
        ENTREPOT,
        VEHICULE
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

    public void setVehicule() {
        mType = Type.VEHICULE;
        mEntrepot = null;
        mLivraison = null;
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

    public void setX(int mX) {
        this.mX = mX;
    }

    public void setY(int mY) {
        this.mY = mY;
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
        double radius = mType == Type.VEHICULE ? (radiusAffichage * 2) : radiusAffichage;

        circle = new Circle(radius);
        coordX = ((mX - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth();
        coordY = ((mY - Plan.mPointYmin) / (double) (Plan.mPointYmax - Plan.mPointYmin) * mapPane.getPrefHeight());
        circle.relocate(coordX - radius, coordY - radius);

        if (mType == Type.ENTREPOT) {
            circle.setFill(Color.RED);
            mapPane.getChildren().add(circle);
        } else if (mType == Type.LIVRAISON) {
            circle.setFill(Color.BLUE);
            mapPane.getChildren().add(circle);
        } else if (mType == Type.VEHICULE) {
            circle.setFill(Color.GREEN);
            mapPane.getChildren().add(circle);
        }

    }

    public void printHover(Pane mapPane, Stage primaryStage, Button rndBtnPopover, String stringLabel){
        PopOver popOver = new PopOver();
        popOver.setAutoHide(true);
        popOver.setDetachable(false);
        Label label = new Label(stringLabel);
        label.setPadding(new Insets(6));
        popOver.setContentNode(label);
        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);

        popOver.setX(coordX + mapPane.getBoundsInParent().getMinX() + primaryStage.getX()-3);
        popOver.setY(coordY + mapPane.getBoundsInParent().getMinY() + primaryStage.getY() - 13);
        rndBtnPopover.setOnMouseEntered(e -> popOver.show(primaryStage));
        rndBtnPopover.setOnMouseExited(e -> popOver.hide());
    }

   public void printGlowHover(Pane mapPane, Stage primaryStage, Button rndBtnPopover, String label, Rectangle rectangle){
       PopOver popOver = new PopOver();
       popOver.setAutoHide(true);
       popOver.setDetachable(false);
       popOver.setContentNode(new Label(label));
       popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
       popOver.setX(coordX + mapPane.getBoundsInParent().getMinX() + primaryStage.getX()- 3);
       popOver.setY(coordY + mapPane.getBoundsInParent().getMinY() + primaryStage.getY() - 13);

       DropShadow borderGlow = new DropShadow();
       borderGlow.setColor(Color.BLUE);
       borderGlow.setWidth(80);
       borderGlow.setHeight(150);

       rndBtnPopover.setOnMouseEntered(e -> {
           popOver.show(primaryStage);
           rectangle.setEffect(borderGlow);
       });
       rndBtnPopover.setOnMouseExited(e -> {
           popOver.hide();
           rectangle.setEffect(null);
       });

   }

    public void move(Pane mapPane) {
        double radius = mType == Type.VEHICULE ? (radiusAffichage * 2) : radiusAffichage;

        coordX = ((mX - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth();
        coordY = ((mY - Plan.mPointYmin) / (double) (Plan.mPointYmax - Plan.mPointYmin) * mapPane.getPrefHeight());
        circle.relocate(coordX - radius, coordY - radius);
    }


    public void printSuppressButton(Pane mapPane, Stage primaryStage, Button btnSupprPopover, Button btnSupprValidate) {

        suppPopover = new PopOver();

        suppPopover.setAutoHide(false);
        suppPopover.setDetachable(false);
        suppPopover.setContentNode(btnSupprValidate);
        suppPopover.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);

        suppPopover.setX(mapPane.getBoundsInParent().getMaxX() + btnSupprPopover.getBoundsInParent().getMinX() + btnSupprPopover.getLayoutX());
        suppPopover.setY(mapPane.getBoundsInParent().getMinY() + btnSupprPopover.getBoundsInParent().getMinY() + btnSupprPopover.getMaxHeight() / 2);
        btnSupprPopover.setOnMouseClicked(e -> {
            if (suppPopover.isShowing()) suppPopover.hide();
            else
                suppPopover.show(primaryStage);
        });
    }


    public PopOver getSupprPopover(){return suppPopover;}

    public String getAdresse() {
        return mAdresse;
    }

    public String getId() {
        return mId;
    }

}
