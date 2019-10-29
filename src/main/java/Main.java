import core.Line;
import core.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.rmi.server.LogStream.log;

public class Main
{

    private static String dataFile = "data/webpage/index.html";


    public static void main(String[] args)
    {


        try {
            String htmlFile = parseFile(dataFile);
            Document doc = Jsoup.parse(htmlFile);
            Elements titles = doc.select("table > tbody > tr > td:has(span) > span");

            for (Element el : titles) {

                System.out.println(el.getElementsByAttribute("title").attr("title"));
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