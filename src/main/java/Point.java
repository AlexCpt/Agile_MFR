public class Point {
    private String mId;
    private int mX;
    private int mY;

    public String mAdresse;

    public void setAdresse(String adresse) {
        mAdresse = adresse;
    }

    public Point(String id, int x, int y) {
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


}
