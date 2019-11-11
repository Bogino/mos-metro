
import com.google.gson.internal.$Gson$Preconditions;
import core.Line;
import core.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Main
{

    private static String dataFile = "data/webpage/index.html";


    public static void main(String[] args)
    {
        ArrayList<Station> stations = new ArrayList<>();
        Set<Line> lines = new HashSet<>();


        try {
            String htmlFile = parseFile(dataFile);
            Document doc = Jsoup.parse(htmlFile);
            //Document doc = Jsoup.connect("https://ru.wikipedia.org/wiki/Список_станций_Московского_метрополитена").maxBodySize(0).get();
            Elements titles = doc.select("table[class][style]:contains(Список может быть отсортирован по названиям станций в алфавитном порядке, " +
                    "а также по их характеристикам. Интерактивную карту можно вызвать нажатием на ссылку в графе «Координаты».) > tbody > tr > td[data-sort-value][style], td[data-sort-value][style] ~ td:has(span) > span > a");



            for (Element el : titles)
            {
                String title = el.getElementsByAttribute("title").attr("title") + " " + el.
                        getElementsByAttribute("data-sort-value").attr("data-sort-value");

               // System.out.println(title);
                if (title.contains("линия"))
                {
                    String[] fragments = title.split("\\s+");
                    if (fragments.length == 3)
                    {
                       // System.out.println(title);

                        Line line = new Line(fragments[2].replaceAll("[^0-9]+",""),fragments[0]);
                        if (!lines.contains(line))
                        {
                            lines.add(line);
                            System.out.println(line.getName()+ " " + line.getNumber());
                        }
                    }
                }
            }
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        //lines.stream()
          //      .map(line -> line.getName())
            //    .forEach(System.out::println);
        System.out.println(lines.size());


    }


    public static String parseFile(String path)
    {
        StringBuilder builder = new StringBuilder();

        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            lines.forEach(line -> builder.append(line + "\n"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

}