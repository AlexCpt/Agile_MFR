public class Troncon {

    private Point mOrigine;
    private Point mDestination;
    private int mLongueur;

    public Troncon(Point origine, Point destination, int longueur) {
        mOrigine = origine;
        mDestination = destination;
        mLongueur = longueur;
    }

    public Point getOrigine() {
        return mOrigine;
    }

    public Point getDestination() {
        return mDestination;
    }

    public int getLongueur() { return mLongueur; }

    @Override
    public String toString() {
        return "T{" +
                " " + mOrigine +
                ", " + mDestination +
                ", " + mLongueur +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Troncon troncon = (Troncon) o;

        if (mLongueur != troncon.mLongueur) return false;
        if (mOrigine != null ? !mOrigine.equals(troncon.mOrigine) : troncon.mOrigine != null) return false;
        return mDestination != null ? mDestination.equals(troncon.mDestination) : troncon.mDestination == null;
    }
}
