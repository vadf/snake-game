import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GameField {
    final static Charset ENCODING      = StandardCharsets.UTF_8;

    private char[][]     field;
    private char         WALL          = '#';
    private int          effectiveSize = 0;

    public GameField(String filename) throws IOException {
        Path path = Paths.get(filename);
        List<String> tmpField = Files.readAllLines(path, ENCODING);
        field = new char[tmpField.size()][tmpField.get(0).length()];
        int i = 0;
        for (String line : tmpField) {
            for (int j = 0; j < line.length(); j++) {
                field[i][j] = line.charAt(j);
                if (field[i][j] != WALL) {
                    effectiveSize++;
                }
            }
            i++;
        }
    }

    public boolean isWall(TextPoint p) {
        return field[p.row][p.col] == WALL;
    }

    public int getEffectiveSize() {
        return effectiveSize;
    }

    protected char[][] getField() {
        return field;
    }

    public int getRowsNum() {
        return field.length;
    }

    public int getColsNum() {
        return field[0].length;
    }
}
