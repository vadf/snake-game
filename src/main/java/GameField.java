import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameField {
    private static final Charset    ENCODING = StandardCharsets.UTF_8;
    private List<TextPoint> walls    = new ArrayList<TextPoint>();
    private int             rows;
    private int             cols;

    public GameField(String filename) throws IOException, FieldInitException {
        Path path = Paths.get(filename);
        List<String> tmpField = Files.readAllLines(path, ENCODING);
        rows = tmpField.size();
        cols = tmpField.get(0).length();

        int i = 0;
        char ch;
        for (String line : tmpField) {
            if (cols != line.length()) {
                throw new FieldInitException("Game Field is not rectangle.");
            }
            for (int j = 0; j < line.length(); j++) {
                ch = line.charAt(j);
                if (ch != ' ') {
                    walls.add(new TextPoint(i, j));
                }
            }
            i++;
        }
    }

    public boolean isWall(TextPoint p) throws OutOfFieldException {
        if (!isInField(p)) {
            throw new OutOfFieldException("Point is out of game Field: " + p);
        }
        return walls.contains(p);
    }

    public int getEffectiveSize() {
        return rows * cols - walls.size();
    }

    public List<TextPoint> getWalls() {
        return new ArrayList<TextPoint>(walls);
    }

    public boolean isInField(TextPoint p) {
        if (p.row >= 0 && p.row < rows && p.col >= 0 && p.col < cols) {
            return true;
        }
        return false;
    }

    public boolean addWall(TextPoint p) throws OutOfFieldException {
        if (isWall(p)) {
            return false;
        }
        walls.add(p);
        return true;
    }

    public boolean removeWall(TextPoint p) throws OutOfFieldException {
        if (!isWall(p)) {
            return false;
        }
        walls.remove(p);
        return true;

    }

    public TextPoint getFieldSize() {
        return new TextPoint(rows, cols);
    }
}

class OutOfFieldException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 6911686971633228894L;

    public OutOfFieldException() {
        super();
    }

    public OutOfFieldException(String message) {
        super(message);
    }
}

class FieldInitException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 4667666295077915400L;

    public FieldInitException() {
        super();
    }

    public FieldInitException(String message) {
        super(message);
    }
}