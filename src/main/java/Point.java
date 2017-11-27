import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Point {
    private String mId;
    private int mX;
    private int mY;
    final double radiusAffichage = 3;

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

    public void setPoint() {
        mType = Type.POINT;
        mEntrepot = null;
        mLivraison = null;
    }

    public Type getType() {
        return mType;
    }

    public void setAdresse(String adresse) {
        mAdresse = adresse;
    }

    public Point(){};
    public Point(String id, int x, int y) {
        mType = Type.POINT;
        mId = id;
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

    public void print(Pane mapPane){

        Circle circle;
        //Color
        if(mType == Type.ENTREPOT){
            circle = new Circle(radiusAffichage, Color.RED);
        } else if (mType == Type.LIVRAISON) {
            circle = new Circle(radiusAffichage, Color.BLUE);
        }
        else {
            circle = new Circle(0, Color.BLACK);
        }

        //Position
        //System.out.println("prefw : " +mapPane.getPrefWidth());
        //System.out.println("prefH : " +mapPane.getPrefHeight());


        circle.relocate((((mX - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth()) - radiusAffichage,((mY-Plan.mPointYmin)/(double) (Plan.mPointYmax-Plan.mPointYmin)*mapPane.getPrefHeight()) - radiusAffichage);
        //System.out.println("x du point : " + ((mX - Plan.mPointXmin) / (double) (Plan.mPointXmax - Plan.mPointXmin)) * mapPane.getPrefWidth());
        //System.out.println("y du point : " + (mY-Plan.mPointYmin)/(double) (Plan.mPointYmax-Plan.mPointYmin)*mapPane.getPrefHeight());

        mapPane.getChildren().add(circle);
    }
}
