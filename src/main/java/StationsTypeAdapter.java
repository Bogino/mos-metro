import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import core.Line;
import core.Station;

import java.io.IOException;

public class StationsTypeAdapter extends TypeAdapter<Station> {


    @Override
    public void write(JsonWriter out, Station station) throws IOException {


    }

    @Override
    public Station read(JsonReader in) throws IOException {
        return null;
    }


}
