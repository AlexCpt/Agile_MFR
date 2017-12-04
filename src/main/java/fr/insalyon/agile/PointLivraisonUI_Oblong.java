package fr.insalyon.agile;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PointLivraisonUI_Oblong {

    private Circle mCircle1;
    private Circle mCircle2;
    private Point mPoint;
    private Button mButton;
    private Label mLabelHeure;
    private Label mLabelNomLivraison;
    private double m_xAffichage;
    private double m_yAffichage_RelocateLivraison;
    private double m_yAffichage_Relocate;
    private PointLivraisonUI.Type mType;
    private  Rectangle mRectangle;

    public enum Type {
        ENTREPOT_ARRIVEE,
        LIVRAISON,
        ENTREPOT_DEPART
    };

    final int radiusAffichageTimeline = 11;
    final Color colorEntrepot = Color.rgb(244,39,70);
    final Color colorLivraison = Color.rgb(56, 120, 244);
    final String popOverButtonStyle = "-fx-background-radius: 5em; " +
            "-fx-min-width: " + radiusAffichageTimeline*2 + "px; " +
            "-fx-min-height: " + radiusAffichageTimeline*2 + "px; " +
            "-fx-max-width: " + radiusAffichageTimeline*2 + "px; " +
            "-fx-max-height: " + radiusAffichageTimeline*2 + "px; " +
            "-fx-background-color: transparent;" +
            "-fx-background-insets: 0px; " +
            "-fx-padding: 0px;";


    public PointLivraisonUI_Oblong (double xAffichage, double yAffichage_RelocateLivraison, double yAffichage_Relocate, Rectangle rectangle, PointLivraisonUI.Type type, Label labelHeure, Label LabelNomLivraison){

        m_xAffichage = xAffichage;
        m_yAffichage_Relocate = yAffichage_Relocate;
        m_yAffichage_RelocateLivraison = yAffichage_RelocateLivraison;
        mLabelHeure = labelHeure;
        mLabelNomLivraison = LabelNomLivraison;
        mType = type;
        mRectangle = rectangle;

        //Circle 1
        mCircle1 = new Circle(radiusAffichageTimeline);
        if(mType == PointLivraisonUI.Type.ENTREPOT_DEPART || type == PointLivraisonUI.Type.ENTREPOT_ARRIVEE){
            mCircle1.setFill(colorEntrepot);
        }
        else if (mType == PointLivraisonUI.Type.LIVRAISON){
            mCircle1.setFill(colorLivraison);
        }
        mCircle1.relocate(m_xAffichage - radiusAffichageTimeline,m_yAffichage_RelocateLivraison - radiusAffichageTimeline);

        //Circle 2
        mCircle2 = new Circle(radiusAffichageTimeline);
        if(mType == PointLivraisonUI.Type.ENTREPOT_DEPART || type == PointLivraisonUI.Type.ENTREPOT_ARRIVEE){
            mCircle2.setFill(colorEntrepot);
        }
        else if (mType == PointLivraisonUI.Type.LIVRAISON){
            mCircle2.setFill(colorLivraison);
        }
        mCircle2.relocate(m_xAffichage - radiusAffichageTimeline,m_yAffichage_Relocate - radiusAffichageTimeline);


        //Button
        mButton = new Button();
        mButton.setStyle(popOverButtonStyle);
        mButton.relocate(m_xAffichage - radiusAffichageTimeline, m_yAffichage_RelocateLivraison - radiusAffichageTimeline);

        //Label
        mLabelHeure.setTextFill(Color.grayRgb(96));
        mLabelNomLivraison.setTextFill(Color.grayRgb(96));

        //rectangle
        mRectangle.setFill(colorLivraison);
    }

    public Button getButton(){ return mButton; }

    public void print (Pane pointPane, Pane labelPane){
        pointPane.getChildren().add(mCircle1);
        pointPane.getChildren().add(mCircle2);

        pointPane.getChildren().add(mButton);
        pointPane.getChildren().add(mRectangle);


        labelPane.getChildren().add(mLabelHeure);
        labelPane.getChildren().add(mLabelNomLivraison);
    }
}
