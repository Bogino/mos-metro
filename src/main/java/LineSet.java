import core.Line;

import java.util.HashSet;
import java.util.Set;

public class LineSet {

    HashSet<Line> lines = new HashSet<>();

    public HashSet<Line> getLines() {
        return lines;
    }

    public void addLines(HashSet<Line> set) {
        lines.addAll(set);
    }
}
