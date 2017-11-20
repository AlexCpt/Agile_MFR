import java.util.List;

public class Itineraire {

    private List<Troncon> mTroncons;

    public Itineraire(List<Troncon> troncons) {
        mTroncons =  troncons;
    }

    public List<Troncon> getTroncons() {
        return mTroncons;
    }

    public void setTroncons(List<Troncon> mTroncons) {
        this.mTroncons = mTroncons;
    }

    @Override
    public String toString() {
        return "I{" +
                " " + mTroncons +
                '}';
    }
}
