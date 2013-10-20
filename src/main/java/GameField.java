import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameField {
    final static Charset    ENCODING      = StandardCharsets.UTF_8;

    private char            WALL          = '#';
    private int             effectiveSize = 0;

    private List<TextPoint> walls         = new ArrayList<TextPoint>();
    private int             rows;
    private int             cols;

    public GameField(String filename) throws IOException {
        Path path = Paths.get(filename);
        List<String> tmpField = Files.readAllLines(path, ENCODING);
        rows = tmpField.size();
        cols = tmpField.get(0).length();

        int i = 0;
        char ch;
        for (String line : tmpField) {
            for (int j = 0; j < line.length(); j++) {
                ch = line.charAt(j);
                if (ch == WALL) {
                    walls.add(new TextPoint(i, j));
                } else {
                    effectiveSize++;
                }
            }
            i++;
        }
    }

    public boolean isWall(TextPoint p) throws Exception {
        if (!isInField(p)) {
            throw new Exception("Point is out of game Field");
        }
        return walls.contains(p);
    }

    public int getEffectiveSize() {
        return effectiveSize;
    }

    public int getRowsNum() {
        return rows;
    }

    public int getColsNum() {
        return cols;
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
}
