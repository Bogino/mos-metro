import core.Line;
import core.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Subway {

    private static String dataFile = "data/webpage/index.html";
    private static HashSet<Line> lines = new HashSet<>();
    private static Line line;

    public static LineSet createLines()
    {
        LineSet lineSet = new LineSet();
        int INDEX_LINE_NAME = 0;
        int INDEX_LINE_NUMBER = 2;

        try {
            String htmlFile = parseFile(dataFile);
            Document doc = Jsoup.parse(htmlFile);
            //Document doc = Jsoup.connect("https://ru.wikipedia.org/wiki/Список_станций_Московского_метрополитена").maxBodySize(0).get();
//            Elements titles = doc.select("table[class][style]:contains(Список может быть отсортирован по названиям станций в алфавитном порядке, " +
//                    "а также по их характеристикам. Интерактивную карту можно вызвать нажатием на ссылку в графе «Координаты».) > tbody > tr > td[data-sort-value][style], td[data-sort-value][style] ~ td:has(span) > span > a");
            Elements titles = doc.select("table[class][style]:contains(Список может быть отсортирован по названиям станций в алфавитном порядке, " +
                    "а также по их характеристикам. Интерактивную карту можно вызвать нажатием на ссылку в графе «Координаты».) > tbody > tr > td:has(span), td[data-sort-value][style] + td:has(a)");

//            for (Element el : komunarka)
//            {
//             String kom = el.getElementsByAttribute("title").attr("title") + " " + el.
//                    getElementsByAttribute("data-sort-value").attr("data-sort-value");;
//                System.out.println(kom);
//            }

            for (Element el : titles)
            {
                String title = el.getElementsByAttribute("title").attr("title") + " " + el.
                        getElementsByAttribute("data-sort-value").attr("data-sort-value");

                //System.out.println(title);
                if (title.contains("линия"))
                {
                    String[] fragments = title.split("\\s+");
                    if (fragments.length == 3)
                    {
                        line = new Line(fragments[INDEX_LINE_NUMBER].replaceAll("[^0-9]+",""),fragments[INDEX_LINE_NAME]);
                        if (!lines.contains(line))
                        {
                            lines.add(line);
                        }
                    }
                }
                if (title.contains("станция метро"))
                {
                    //System.out.println(title);
                    String name = title.trim().substring(0,title.indexOf("(станция")).trim();
                    //System.out.println(name);
                    Station station = new Station(name, line);
                    System.out.println(station.getName() + " " + station.getLine().getName());

                }
            }
            lineSet.addLines(lines);

        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return lineSet;
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

