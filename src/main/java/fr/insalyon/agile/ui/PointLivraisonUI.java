package fr.insalyon.agile.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static fr.insalyon.agile.ui.MainWindow.centreRightPane;
import static fr.insalyon.agile.ui.MainWindow.widthLabelTime;

/**
 * Classe qui implemente l'affichage des points sur la timeline
 */
public class PointLivraisonUI {

    protected Circle mCircle;
    protected Button mButton;
    protected Label mLabelHeure;
    protected Label mLabelNomLivraison;
    protected double m_xAffichage;
    protected double m_yAffichage;
    protected Type mType;

    public enum Type {
        ENTREPOT_ARRIVEE,
        LIVRAISON,
        ENTREPOT_DEPART
    }

    protected final int radiusAffichageTimeline = 6;
    protected final Color mColorEntrepot = Color.RED;
    protected final Color mColorLivraison = Color.rgb(56, 120, 244);
    protected final  int decalageLabelLivraison = 25;
    protected final String popOverButtonStyle =
            "-fx-min-width: " + radiusAffichageTimeline*9 + "px; " +
            "-fx-min-height: " + radiusAffichageTimeline*4 + "px; " +
            "-fx-max-width: " + radiusAffichageTimeline*9 + "px; " +
            "-fx-max-height: " + radiusAffichageTimeline*4 + "px; " +
            "-fx-background-color: transparent;" +
            "-fx-background-insets: 0px; " +
            "-fx-padding: 0px;";

    public PointLivraisonUI(){
    }

    /**
     * Constructeur d'un PointLivraisonUI
     * @param xAffichage coordonnee x du point
     * @param yAffichage coordonnee y du point
     * @param type type du point
     * @param labelHeure label heure
     * @param LabelNomLivraison label nom de la livraison
     */
    public PointLivraisonUI(double xAffichage, double yAffichage, Type type, Label labelHeure, Label LabelNomLivraison){

        m_xAffichage = xAffichage;
        m_yAffichage = yAffichage;
        mLabelHeure = labelHeure;
        mLabelNomLivraison = LabelNomLivraison;
        mType = type;

        //Circle
        mCircle = new Circle(radiusAffichageTimeline);
        if(mType == Type.ENTREPOT_DEPART || type == Type.ENTREPOT_ARRIVEE){
            mCircle.setFill(mColorEntrepot);
        }
        else if (mType == Type.LIVRAISON){
            mCircle.setFill(mColorLivraison);
        }
        mCircle.relocate(m_xAffichage - radiusAffichageTimeline,m_yAffichage - radiusAffichageTimeline);

        //Button
        mButton = new Button();
        mButton.setStyle(popOverButtonStyle);
        mButton.relocate(m_xAffichage - radiusAffichageTimeline, m_yAffichage - radiusAffichageTimeline);

        //Label
        mLabelHeure.setTextFill(Color.grayRgb(96));
        mLabelHeure.setLayoutX(centreRightPane - widthLabelTime);

        mLabelNomLivraison.setTextFill(Color.grayRgb(96));
        mLabelNomLivraison.setLayoutX(centreRightPane + decalageLabelLivraison);
    }

    /**
     * Permet de recuperer le bouton du Point de livraison
     * @return bouton correspondant au Point sur la timeline
     */
    public Button getButton(){ return mButton; }

    /**
     * Affichage des points sur la Timeline
     * @param pointPane pane dans lequel sont contenus les Points
     * @param labelPane pane dans lequel sont contenus les labels
     * @param ButtonPane pane dans lequel sont contenus les boutons
     */
    public void print (Pane pointPane, Pane labelPane, Pane ButtonPane){
        pointPane.getChildren().add(mCircle);
        labelPane.getChildren().add(mLabelHeure);
        labelPane.getChildren().add(mLabelNomLivraison);
        pointPane.getChildren().add(mButton);
    }


}
