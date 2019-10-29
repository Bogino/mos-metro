
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;




public class Main
{

    private static String dataFile = "data/webpage/index.html";


    public static void main(String[] args)
    {


        List<String> stations = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        List<String> connections = new ArrayList<>();


        try {
            String htmlFile = parseFile(dataFile);
            Document doc = Jsoup.parse(htmlFile);
            //Document doc = Jsoup.connect("https://ru.wikipedia.org/wiki/Список_станций_Московского_метрополитена").maxBodySize(0).get();
            Elements titles = doc.select("table[class][style]:contains(Список может быть отсортирован по названиям станций в алфавитном порядке, " +
                    "а также по их характеристикам. Интерактивную карту можно вызвать нажатием на ссылку в графе «Координаты».) > tbody > tr > td[data-sort-value][style]," +
                    " td[data-sort-value][style] + td:has(span) > span");

            for (Element el : titles) {



                System.out.println(el.getElementsByAttribute("title").attr("title"));
                System.out.println(el.getElementsByAttribute("data-sort-value").attr("data-sort-value"));

            }
        } catch (Exception ex)
        {
            ex.getMessage();
        }


    }





    private static void createStationIndex()
    {
//        stationIndex = new StationIndex();
//        try
//        {
//            JSONParser parser = new JSONParser();
//            JSONObject jsonData = (JSONObject) parser.parse(getJsonFile());
//
//            JSONArray linesArray = (JSONArray) jsonData.get("lines");
//            parseLines(linesArray);
//
//            JSONObject stationsObject = (JSONObject) jsonData.get("stations");
//            parseStations(stationsObject);
//
//            JSONArray connectionsArray = (JSONArray) jsonData.get("connections");
//            parseConnections(connectionsArray);
//        }
//        catch(Exception ex) {
//            ex.printStackTrace();
//        }
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