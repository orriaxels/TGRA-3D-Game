package com.ru.tgra.shapes;

public class MazeGeneratorBool
{
    private int id;
    private boolean north;
    private boolean south;
    private boolean west;
    private boolean east;

    public MazeGeneratorBool() {}

    public MazeGeneratorBool(int id, boolean north, boolean south, boolean west, boolean east)
    {
        this.id = id;
        this.north = north;
        this.south = south;
        this.west = west;
        this.east = east;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public boolean getNorth()
    {
        return north;
    }

    public void setNorth(boolean north)
    {
        this.north = north;
    }

    public boolean getSouth()
    {
        return south;
    }

    public void setSouth(boolean south)
    {
        this.south = south;
    }

    public boolean getWest()
    {
        return west;
    }

    public void setWest(boolean west)
    {
        this.west = west;
    }

    public boolean getEast()
    {
        return east;
    }

    public void setEast(boolean east)
    {
        this.east = east;
    }

}
