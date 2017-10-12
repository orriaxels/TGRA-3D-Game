package com.ru.tgra.shapes;

public class Walls implements Comparable
{
    private int id;
    private boolean north;
    private boolean south;
    private boolean west;
    private boolean east;

    private float posX;
    private float posY;
    private float posZ;

    private float sizeX;
    private float sizeY;
    private float sizeZ;

    private Wall northWall;
    private Wall southWall;
    private Wall westWall;
    private Wall eastWall;

    private Shader shader;

    public Walls() {}

    public Walls(int id, boolean north, boolean south, boolean west, boolean east)
    {
        this.id = id;
        this.north = north;
        this.south = south;
        this.west = west;
        this.east = east;

        this.northWall = new Wall();
        this.southWall = new Wall();
        this.westWall = new Wall();
        this.eastWall = new Wall();

        shader = new Shader();
    }

    public int getId()
    {
        return id;
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

    public void setPosition(float posX, float posY, float posZ)
    {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public void setScale(float sizeX, float sizeY, float sizeZ)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    public Wall getNorthWall()
    {
        return northWall;
    }

    public void setNorthWall(Wall northWall)
    {
        this.northWall = northWall;
    }

    public Wall getSouthWall()
    {
        return southWall;
    }

    public void setSouthWall(Wall southWall)
    {
        this.southWall = southWall;
    }

    public Wall getWestWall()
    {
        return westWall;
    }

    public void setWestWall(Wall westWall)
    {
        this.westWall = westWall;
    }

    public Wall getEastWall()
    {
        return eastWall;
    }

    public void setEastWall(Wall eastWall)
    {
        this.eastWall = eastWall;
    }

    @Override
    public int compareTo(Object o)
    {
        Walls m = (Walls) o;
        if(this.getId() > m.getId())
        {
            return 1;
        }
        else if(this.getId() < m.getId())
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    public void display()
    {
        if(getNorth())
        {
            shader.setMaterialDiffuse(1, 0, 0, 1);
            northWall.display();
        }
        if(getSouth())
        {
            shader.setMaterialDiffuse(0, 1, 0, 1);
            southWall.display();
        }
        if(getWest())
        {
            shader.setMaterialDiffuse(0, 0, 1, 1);
            westWall.display();
        }
        if(getEast())
        {
            shader.setMaterialDiffuse(1, 0, 1, 1);
            eastWall.display();
        }
    }
}
