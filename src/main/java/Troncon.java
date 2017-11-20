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

}
