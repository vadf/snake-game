import java.util.ArrayList;
import java.util.List;

public class Stars {
    List<TextPoint> stars = new ArrayList<TextPoint>();

    public boolean add(TextPoint point) {
        if (stars.contains(point)) {
            return false;
        }
        return stars.add(point);
    }

    public boolean remove(TextPoint point) {
        return stars.remove(point);
    }

    public int getNumOfStars() {
        return stars.size();
    }

    public boolean isStar(TextPoint point) {
        return stars.contains(point);
    }

    public List<TextPoint> getStars() {
        return new ArrayList<TextPoint>(stars);
    }
}
