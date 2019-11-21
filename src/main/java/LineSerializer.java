import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import core.Line;

import java.lang.reflect.Type;

public class LineSerializer implements JsonSerializer<Line> {
    @Override
    public JsonElement serialize(Line src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject result = new JsonObject();
        result.addProperty("number", src.getNumber());
        result.addProperty("name", src.getName());
        return result;
    }
}
