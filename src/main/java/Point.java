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
}
