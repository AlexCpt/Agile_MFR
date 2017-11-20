public class Point {
    private int mX;
    private int mY;

    public String mAdresse;

    public void setAdresse(String adresse) {
        mAdresse = adresse;
    }

    public Point(int x, int y) {
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
}
