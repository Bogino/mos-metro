import core.Line;
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

    public static LineSet createLines()
    {
        LineSet lineSet = new LineSet();

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

                if (title.contains("линия"))
                {
                    String[] fragments = title.split("\\s+");
                    if (fragments.length == 3)
                    {
                        Line line = new Line(fragments[2].replaceAll("[^0-9]+",""),fragments[0]);
                        if (!lines.contains(line))
                        {
                            System.out.println(lines.size());
                            lines.add(line);
                        }
                    }
                }
            }
            lineSet.addLines(lines);
            System.out.println(lines.size() + " " + "размер");
            System.out.println(lineSet.getLines().size());

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

