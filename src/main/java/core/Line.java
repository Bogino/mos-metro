package core;

import java.util.ArrayList;
import java.util.List;

public class Line implements Comparable<Line>
{
    private String number;
    private String name;
    private List<String> nameOfStations;


    public Line(String number, String name)
    {
        this.number = number;
        this.name = name;
        nameOfStations = new ArrayList<>();
    }

    public String getNumber()
    {
        return number;
    }

    public String getName()
    {
        return name;
    }

    public void addNameStation(String name)
    {
        nameOfStations.add(name);
    }

    public List<String> getStations()
    {
        return nameOfStations;
    }

    @Override
    public int compareTo(Line line)
    {
        Integer nameComparison = name.compareToIgnoreCase(line.name);
        Integer numberComparison = Integer.compare(Integer.parseInt(number), Integer.parseInt(line.number));
        if (numberComparison != 0 && nameComparison != 0)
        {
            return nameComparison;
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