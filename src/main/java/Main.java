import com.google.gson.GsonBuilder;
import core.Line;
import core.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;

public class Main
{

    private static String dataFile = "data/webpage/index.html";
    private static Line line;

    public static void main(String[] args)
    {

        //Document doc = Jsoup.connect("https://ru.wikipedia.org/wiki/Список_станций_Московского_метрополитена").maxBodySize(0).get();
//            Elements titles = doc.select("table[class][style]:contains(Список может быть отсортирован по названиям станций в алфавитном порядке, " +
//                    "а также по их характеристикам. Интерактивную карту можно вызвать нажатием на ссылку в графе «Координаты».) > tbody > tr > td[data-sort-value][style], td[data-sort-value][style] ~ td:has(span) > span > a");
        int INDEX_LINE_NAME = 0;
        int INDEX_LINE_NUMBER = 2;
        int i = 0;
        List<Line> lines = new ArrayList<>();
        Map<String, List<String>> station = new HashMap<>();

        try {
            String htmlFile = parseFile(dataFile);
            Document doc = Jsoup.parse(htmlFile);
            //Document doc = Jsoup.connect("https://ru.wikipedia.org/wiki/Список_станций_Московского_метрополитена").maxBodySize(0).get();
//            Elements titles = doc.select("table[class][style]:contains(Список может быть отсортирован по названиям станций в алфавитном порядке, " +
//                    "а также по их характеристикам. Интерактивную карту можно вызвать нажатием на ссылку в графе «Координаты».) > tbody > tr > td[data-sort-value][style], td[data-sort-value][style] ~ td:has(span) > span > a");
            Elements titles = doc.select("table[class][style]:contains(Список может быть отсортирован по названиям станций в алфавитном порядке, " +
                    "а также по их характеристикам. Интерактивную карту можно вызвать нажатием на ссылку в графе «Координаты».) > tbody > tr > td:has(span), td[data-sort-value][style] + td:has(a)");
            for (Element el : titles)
            {
                String title = el.getElementsByAttribute("title").attr("title") + " " + el.
                        getElementsByAttribute("data-sort-value").attr("data-sort-value");

                if (title.contains("линия"))
                {
                    String[] fragments = title.split("\\s+");
                    if (fragments.length == 3)
                    {
                        line = new Line(fragments[INDEX_LINE_NUMBER].replaceAll("[^0-9]+","").trim(),fragments[INDEX_LINE_NAME].trim());
                        //System.out.println(line.getName() + " " + line.getNumber());
                        if (!lines.contains(line))
                        {
                            lines.add(line);
                        }
                    }
                }
                if (title.contains("станция метро"))
                {
                    i++;
                    //System.out.println(title);
                    String stationName = title.trim().substring(0,title.indexOf("(станция")).trim();
                    Station stationOfLines = new Station(stationName,line);
                    for (Line line : lines)
                    {
                        if (line.getNumber().equals(stationOfLines.getLine().getNumber()))
                        {
                            line.addNameStation(stationName);
                        }
                    }
                    //System.out.println(station.getName() + " " + station.getLine().getName());
                }

            }

            for (Line line : lines)
            {
                station.put(line.getNumber(), line.getStations());
            }
//            for (Line line : lines)
//            {
//                System.out.println(line.getNumber());
//                for (String name : line.getStations())
//                {
//                    System.out.println("\t" + name);
//                }
//            }



        }catch (Exception ex)
        {
            ex.getMessage();
        }

        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setStations(station);
        jsonResponse.setLines(lines);

        //переводим объект в JSON (в красивом pretty виде)
        String json =
                new GsonBuilder()
                        .setPrettyPrinting()
                        .disableHtmlEscaping()
                        .create()
                        .toJson(jsonResponse);

        System.out.println(json);

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