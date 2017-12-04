package fr.insalyon.agile;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import static fr.insalyon.agile.MainWindow.centreRightPane;
import static fr.insalyon.agile.MainWindow.widthLabelTime;

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
    };

    protected final int radiusAffichageTimeline = 11;
    protected final Color mColorEntrepot = Color.rgb(244,39,70);
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

    public Button getButton(){ return mButton; }

    public void print (Pane pointPane, Pane labelPane){
        pointPane.getChildren().add(mCircle);

        labelPane.getChildren().add(mLabelHeure);
        labelPane.getChildren().add(mLabelNomLivraison);
        pointPane.getChildren().add(mButton);

    }


}
