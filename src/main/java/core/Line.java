package core;

import java.util.ArrayList;
import java.util.List;

public class Line implements Comparable<Line>
{
    private String number;
    private String name;
    private List<Station> stations;

    public Line(String number, String name)
    {
        this.number = number;
        this.name = name;
        stations = new ArrayList<>();
    }

    public String getNumber()
    {
        return number;
    }

    public String getName()
    {
        return name;
    }

    public void addStation(Station station)
    {
        stations.add(station);
    }

    public List<Station> getStations()
    {
        return stations;
    }

    @Override
    public int compareTo(Line line)
    {
        int numberComparison = Integer.compare(Integer.parseInt(number), Integer.parseInt(line.getNumber()));
        int nameComparison = name.compareToIgnoreCase(line.name);
        if(numberComparison != 0 &&  nameComparison != 0)
        {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj)
    {
        return compareTo((Line) obj) == 0;
    }

    @Override
    public int hashCode()
    {
        return Integer.parseInt(number);
    }
}