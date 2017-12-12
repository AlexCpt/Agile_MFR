package fr.insalyon.agile.metier;

import javafx.event.EventHandler;
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

/**
 * Cette classe représente un point sur une carte (identifié par ses coordonnées, son adresse...)
 * Un point peut être associé à une livraison ou un entrepôt
 */
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

    private Type mType;
    private Livraison mLivraison;
    private Entrepot mEntrepot;
    private EventHandler<javafx.scene.input.MouseEvent> eventHandler;


    public enum Type {
        POINT,
        LIVRAISON,
        ENTREPOT,
        VEHICULE
    }

    public Point(){}

    /**
     * Constructeur d'un point
     * @param id id unique permettant d'identifier un point sur la carte
     * @param x coordonnee x
     * @param y coordonnee y
     */
    public Point(String id, int x, int y) {
        mType = Type.POINT;
        mId = id;
        mX = x;
        mY = y;
    }

    public void setButtonEventHandler(EventHandler<javafx.scene.input.MouseEvent> eventHandler) {
        this.eventHandler = eventHandler;
        circle.setOnMouseClicked(eventHandler);
    }

    /**
     * Permet d'associer au point un entrepot
     * @param entrepot que l'on veut associer au point courant
     */
    public void setEntrepot(Entrepot entrepot) {
        mType = Type.ENTREPOT;
        mLivraison = null;
        mEntrepot = entrepot;
    }

    /**
     * Permet d'associer au point une livraison
     * @param livraison que l'on veut associer au point courant
     */
    public void setLivraison(Livraison livraison) {
        mType = Type.LIVRAISON;
        mEntrepot = null;
        mLivraison = livraison;
    }

    /**
     * Permet d'associer au point un vehicule
     */
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

    /**
     * Permet de récupérer le type du point courant (Vehicule, Entrepot, Livraison ou Point)
     * @return Type du point
     */
    public Type getType() {
        return mType;
    }

    /**
     * Permet de récupérer la livraison associée au point
     * @return Livraison associée au point
     */
    public Livraison getLivraison() {
        return mLivraison;
    }

    /**
     * Permet de récupérer la coordonnée x du point courant
     * @return coordonnée x
     */
    public int getX() {
        return mX;
    }

    /**
     * Permet de récupérer la coordonnée y du point courant
     * @return coordonnée y
     */
    public int getY() {
        return mY;
    }

    /**
     * Permet de récupérer l'id du point courant
     * @return id du point courant
     */
    public String getId() {
        return mId;
    }

    /**
     * Permet de modifier la coordonnée x du point courant
     * @param mX nouvelle coordonnée x
     */
    public void setX(int mX) {
        this.mX = mX;
    }

    /**
     * Permet de modifier la coordonnée y du point courant
     * @param mY nouvelle coordonnée y
     */
    public void setY(int mY) {
        this.mY = mY;
    }

    /**
     * Permet d'afficher les caractéristiques d'un Point
     * @return string representant un Point
     */
    @Override
    public String toString() {
        return "P{" +
                " " + mX +
                ", " + mY +
                ", '" + mAdresse + '\'' +
                '}';
    }


    /**
     * Permet de comparer deux point afin de savoir s'ils sont egaux
     * @param o deuxieme point
     * @return boolean true si egaux false sinon
     */
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

    /**
     * Permet de dessiner un Point dans la pane passée en paramètre
     * @param mapPane pane dans laquelle on veut dessiner le point
     */
    public void print(Pane mapPane) {
        double radius = mType == Type.VEHICULE ? (radiusAffichage * 2) : radiusAffichage;

        circle = new Circle(radius);
        coordX = ((mX - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth();
        coordY = ((mY - Plan.mPointYmin) / (double) (Plan.mPointYmax - Plan.mPointYmin) * mapPane.getPrefHeight());
        circle.relocate(coordX - radius, coordY - radius);

        circle.setOnMouseClicked(eventHandler);

        if (mType == Type.ENTREPOT) {
            circle.setFill(Color.RED);
        } else if (mType == Type.LIVRAISON) {
            circle.setFill(Color.BLUE);
        } else if (mType == Type.VEHICULE) {
            circle.setFill(Color.GREEN);
        } else {
            circle.setFill(Color.TRANSPARENT);
        }

        mapPane.getChildren().add(circle);

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

   public void printGlowHover(Pane mapPane, Stage primaryStage, Button rndBtnPopover, String stringLabel, Rectangle rectangle){
       PopOver popOver = new PopOver();
       popOver.setAutoHide(true);
       popOver.setDetachable(false);
       Label label = new Label(stringLabel);
       popOver.setContentNode(label);
       label.setPadding(new Insets(6));
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

        suppPopover.setAutoHide(true);
        suppPopover.setDetachable(false);
        suppPopover.setContentNode(btnSupprValidate);
        suppPopover.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);

        suppPopover.setX(mapPane.getBoundsInParent().getMaxX() + btnSupprPopover.getBoundsInParent().getMinX() + btnSupprPopover.getLayoutX() -35);
        suppPopover.setY(mapPane.getBoundsInParent().getMinY() + btnSupprPopover.getBoundsInParent().getMinY() + btnSupprPopover.getMaxHeight() / 2 + 30);
        btnSupprPopover.setOnMouseClicked(e -> {
            if (suppPopover.isShowing()) suppPopover.hide();
            else
                suppPopover.show(primaryStage);
        });
    }


    public PopOver getSupprPopover(){return suppPopover;}

}
