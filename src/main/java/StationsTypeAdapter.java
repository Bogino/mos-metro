import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import core.Line;
import core.Station;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StationsTypeAdapter extends TypeAdapter<List<Station>> {
    @Override
    public void write(JsonWriter out, List<Station> stations) throws IOException {

        

    }

    @Override
    public List<Station> read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
