package fr.insalyon.agile;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageviewExtended extends ImageView {

    PointLivraisonUI_Oblong mPointLivraisonUI_oblong;
    TronconUI mTronconUIPrecedent;
    TronconUI mTronconUISuivant;

    public ImageviewExtended(PointLivraisonUI_Oblong pointLivraisonUI_oblong, Image imageView, TronconUI tronconUIPrecedent, TronconUI tronconUISuivant){
        super(imageView);
        mPointLivraisonUI_oblong = pointLivraisonUI_oblong;
        mTronconUIPrecedent = tronconUIPrecedent;
        mTronconUISuivant = tronconUISuivant;
    }

    public ImageviewExtended(PointLivraisonUI_Oblong pointLivraisonUI_oblong, Image imageView){
        super(imageView);
        mPointLivraisonUI_oblong = pointLivraisonUI_oblong;
    }

    public PointLivraisonUI_Oblong getPointLivraisonUI_oblong() { return mPointLivraisonUI_oblong; }

    public void setTronconUIPrecedent(TronconUI precedent) {mTronconUIPrecedent = precedent;}
    public void setTronconUISuivant(TronconUI suivant) {mTronconUISuivant = suivant;}

    public TronconUI getTronconUIPrecedent() {
        return mTronconUIPrecedent;
    }

    public TronconUI getTronconUISuivant() {
        return mTronconUISuivant;
    }
}
