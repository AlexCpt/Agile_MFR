package fr.insalyon.agile;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PointLivraisonUI_Oblong extends PointLivraisonUI {

    private Circle mCircle2;
    private double m_yAffichage_Relocate;
    private  Rectangle mRectangle;

    public PointLivraisonUI_Oblong (double xAffichage, double yAffichage_RelocateLivraison, double yAffichage_Relocate, PointLivraisonUI.Type type, Label labelHeure, Label LabelNomLivraison){

        super(xAffichage,yAffichage_RelocateLivraison, type,labelHeure,LabelNomLivraison);
        m_yAffichage_Relocate = yAffichage_Relocate;

        //Circle 2
        mCircle2 = new Circle(radiusAffichageTimeline);
        if(mType == PointLivraisonUI.Type.ENTREPOT_DEPART || type == PointLivraisonUI.Type.ENTREPOT_ARRIVEE){
            mCircle2.setFill(mColorEntrepot);
        }
        else if (mType == PointLivraisonUI.Type.LIVRAISON){
            mCircle2.setFill(mColorLivraison);
        }
        mCircle2.relocate(m_xAffichage - radiusAffichageTimeline,m_yAffichage_Relocate - radiusAffichageTimeline);

        //rectangle
        mRectangle = new Rectangle(radiusAffichageTimeline * 2, m_yAffichage - m_yAffichage_Relocate);
        mRectangle.relocate(m_xAffichage - radiusAffichageTimeline, m_yAffichage_Relocate);
        mRectangle.setFill(mColorLivraison);
        mRectangle.setWidth(2*radiusAffichageTimeline);
    }

    public Button getButton(){ return mButton; }

    public void print (Pane pointPane, Pane labelPane){
        super.print(pointPane, labelPane);
        pointPane.getChildren().add(mCircle2);

        pointPane.getChildren().add(mRectangle);
    }
}
