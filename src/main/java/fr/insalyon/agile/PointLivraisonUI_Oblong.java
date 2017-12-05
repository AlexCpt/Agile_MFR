package fr.insalyon.agile;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static fr.insalyon.agile.MainWindow.centreRightPane;
import static fr.insalyon.agile.MainWindow.widthLabelTime;

public class PointLivraisonUI_Oblong extends PointLivraisonUI {

    private Circle mCircle2;
    private double m_yAffichage_Relocate;
    private Rectangle mRectangle;
    private Label mLabelFin;
    private Label mLabelArrivee;

    protected String transparentButtonStyle = "-fx-background-color: transparent;";

    public PointLivraisonUI_Oblong (double xAffichage, double yAffichage_RelocateLivraison, double yAffichage_Relocate, PointLivraisonUI.Type type, Label labelHeureDebut, Label labelHeureFin,Label LabelNomLivraison){

        super(xAffichage,yAffichage_RelocateLivraison, type,labelHeureDebut,LabelNomLivraison);
        m_yAffichage_Relocate = yAffichage_Relocate;
        mLabelFin = labelHeureFin;

        //Circle 2
        mCircle2 = new Circle(radiusAffichageTimeline);
        if(mType == PointLivraisonUI.Type.ENTREPOT_DEPART || type == PointLivraisonUI.Type.ENTREPOT_ARRIVEE){
            mCircle2.setFill(mColorEntrepot);
        }
        else if (mType == PointLivraisonUI.Type.LIVRAISON){
            mCircle2.setFill(mColorLivraison);
        }
        mCircle2.relocate(m_xAffichage - radiusAffichageTimeline,m_yAffichage_Relocate - radiusAffichageTimeline);

        mLabelArrivee = new Label(mLabelNomLivraison.getText());
        mLabelArrivee.setTextFill(Color.grayRgb(96));
        mLabelArrivee.relocate(centreRightPane + decalageLabelLivraison,m_yAffichage - radiusAffichageTimeline);

        mLabelFin.setTextFill(Color.grayRgb(96));
        mLabelFin.relocate(centreRightPane - widthLabelTime, m_yAffichage - radiusAffichageTimeline);

        //rectangle
        mRectangle = new Rectangle(radiusAffichageTimeline * 2, m_yAffichage - m_yAffichage_Relocate);
        mRectangle.relocate(m_xAffichage - radiusAffichageTimeline, m_yAffichage_Relocate);
        mRectangle.setFill(mColorLivraison);
        mRectangle.setWidth(2*radiusAffichageTimeline);

        //Button
        mButton = new Button();
        mButton.setStyle(transparentButtonStyle);
        mButton.relocate(m_xAffichage - radiusAffichageTimeline*2, m_yAffichage_Relocate - radiusAffichageTimeline); //TODO : lié ligne dessous
        mButton.setPrefSize(radiusAffichageTimeline*4,m_yAffichage - m_yAffichage_Relocate);
    }

    public Button getButton(){ return mButton; }

    public void setTranslateY(double y){
        mCircle.setTranslateY(y);
        mCircle2.setTranslateY(y);
        mRectangle.setTranslateY(y);
        mLabelFin.setTranslateY(y);
        mLabelArrivee.setTranslateY(y);
        mLabelHeure.setTranslateY(y);
        mLabelNomLivraison.setTranslateY(y);
    }

    public void print (Pane pointPane, Pane labelPane, Pane buttonPane){
        super.print(pointPane, labelPane, buttonPane);
        pointPane.getChildren().add(mCircle2);
        labelPane.getChildren().add(mLabelFin);
        labelPane.getChildren().add(mLabelArrivee);
        buttonPane.getChildren().add(mButton); // Mettre dans PLUI ?

        pointPane.getChildren().add(mRectangle);
    }
}
