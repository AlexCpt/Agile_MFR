package fr.insalyon.agile;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageviewExtended extends ImageView {

    PointLivraisonUI_Oblong mPointLivraisonUI_oblong;

    public ImageviewExtended(PointLivraisonUI_Oblong pointLivraisonUI_oblong, Image imageView){
        super(imageView);
        mPointLivraisonUI_oblong = pointLivraisonUI_oblong;
    }

    public PointLivraisonUI_Oblong getPointLivraisonUI_oblong() { return mPointLivraisonUI_oblong; }
}
