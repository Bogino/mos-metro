import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.Line;

public class JsonResponse {

    private Map<String, List<String>> stations = new HashMap<>();
    private List<Line> lines = new ArrayList<>();

    public Map<String, List<String>> getStations() {
        return stations;
    }

    public void setStations(Map<String, List<String>> stations) {
        this.stations = stations;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }
}
