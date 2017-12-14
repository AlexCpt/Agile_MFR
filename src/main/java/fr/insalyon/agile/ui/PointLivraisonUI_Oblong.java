package fr.insalyon.agile.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static fr.insalyon.agile.ui.MainWindow.centreRightPane;
import static fr.insalyon.agile.ui.MainWindow.widthLabelTime;

/**
 * Classe heritant de PointLivraisonUI permettant d'afficher sur la timeline une livraison possedant un temps de décharge.
 * Elle est donc représentée sur la timeline par un point oblong dont la longueur dépend de la durée de cette livraison.
 */
public class PointLivraisonUI_Oblong extends PointLivraisonUI {

    private Circle mCircle2;
    private double m_yAffichage_Relocate;
    private Rectangle mRectangle;
    private Label mLabelFin;

    protected String transparentButtonStyle = "-fx-background-color: transparent;";


    /**
     * Constructeur d'un PointLivraisonUI
     * @param xAffichage coordonnée x du point
     * @param yAffichage_RelocateLivraison coordonnée d'affichage y du point de début de livraison
     * @param yAffichage_Relocate coordonnée d'affichage y du point de fin de livraison
     * @param type type du point
     * @param labelHeureDebut label renseignant l'heure de début de la livraison
     * @param labelHeureFin label renseignant l'heure de fin de la livraison
     * @param LabelNomLivraison label renseignant le nom de la livraison
     */
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

    /**
     * Permet de recuperer le bouton du Point de livraison
     * @return bouton correspondant au Point sur la timeline
     */
    public Button getButton(){ return mButton; }

    /**
     * Permet de recuperer le rectangle du Point de livraison
     * @return rectangle correspondant au Point sur la timeline
     */
    public Rectangle getRectangle() {
        return mRectangle;
    }

    /**
     * Affichage des PointLivraisonUI_Oblong sur la Timeline
     * @param pointPane pane dans lequel sont contenus les Points
     * @param labelPane pane dans lequel sont contenus les labels
     * @param buttonPane pane dans lequel sont contenus les boutons
     */
    public void print (Pane pointPane, Pane labelPane, Pane buttonPane){
        super.print(pointPane, labelPane, buttonPane);
        pointPane.getChildren().add(mCircle2);
        labelPane.getChildren().add(mLabelFin);
        buttonPane.getChildren().add(mButton); // Mettre dans PLUI ?
        pointPane.getChildren().add(mRectangle);
    }
}
