import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import core.Line;

import java.io.IOException;
import java.util.Set;

public class LinesTypeAdapter extends TypeAdapter<Set<Line>> {

    @Override
    public void write(JsonWriter out, Set<Line> lines) throws IOException {

        out.beginArray();

        for (Line line : lines)
        {
            out.beginObject();
            out.name("number").value(line.getNumber());
            out.name("name").value(line.getName());
            out.endObject();
        }
        out.endArray();
    }

    @Override
    public Set<Line> read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
