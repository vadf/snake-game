import java.util.Objects;

public class TextPoint {

    public int row;
    public int col;

    public TextPoint(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public TextPoint(TextPoint point) {
        this(point.row, point.col);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof TextPoint)) return false;
        TextPoint other = (TextPoint) obj;
        if ((other.col == this.col) && (other.row == this.row))
            return true;
        return false;
    }
    
    @Override
    public String toString() {
        return "[row=" + row + ",col=" + col + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
