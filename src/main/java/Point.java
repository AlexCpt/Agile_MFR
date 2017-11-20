import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Point {
    private int mX;
    private int mY;

    public String mAdresse;

    enum Type {
        POINT,
        LIVRAISON,
        ENTREPOT
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

    public Type getType() {
        return mType;
    }

    public void setAdresse(String adresse) {
        mAdresse = adresse;
    }

    public Point(int x, int y) {
        mType = Type.POINT;
        mX = x;
        mY = y;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    @Override
    public String toString() {
        return "P{" +
                " " + mX +
                ", " + mY +
                ", '" + mAdresse + '\'' +
                '}';
    }

    public void print(Pane mapPane){

        Circle circle;
        //Color
        if(mType == Type.ENTREPOT){
            circle = new Circle(3, Color.RED);
        } else if (mType == Type.LIVRAISON) {
            circle = new Circle(3, Color.BLUE);
        }
        else {
            circle = new Circle(3, Color.GREEN);
        }

        //Position
        circle.relocate((mX-Plan.mPointXmin)/(Plan.mPointXmax-Plan.mPointXmin)*mapPane.getPrefWidth(),(mY-Plan.mPointYmin)/(Plan.mPointYmax-Plan.mPointYmin)*mapPane.getPrefWidth());

        mapPane.getChildren().add(circle);
    }
}
