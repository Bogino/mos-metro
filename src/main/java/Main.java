
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.reflect.TypeToken;
import core.Line;
import core.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Main
{
    public static void main(String[] args)
    {
        LineSet lineSet = Subway.createLines();
//        Type linesSetType = new TypeToken<Set<Line>>(){}.getType();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LineSet.class, new LinesTypeAdapter())
                .create();
        String json = gson.toJson(lineSet);
        //System.out.println(json);

    }

}