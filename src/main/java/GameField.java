import java.awt.Point;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GameField {
    final static Charset ENCODING = StandardCharsets.UTF_8;

    private char[][]     field;
    private char         WALL     = '#';

    public GameField(String filename) throws IOException {
        Path path = Paths.get(filename);
        List<String> tmpField = Files.readAllLines(path, ENCODING);
        field = new char[tmpField.get(0).length()][tmpField.size()];
        int j = 0;
        for (String line : tmpField) {
            for (int i = 0; i < line.length(); i++) {
                field[i][j] = line.charAt(i);
            }
            j++;
        }

    }

    public boolean isWall(Point p) {
        return field[p.x][p.y] == WALL;
    }
}
