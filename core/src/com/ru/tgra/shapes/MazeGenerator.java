package com.ru.tgra.shapes;

import java.util.*;

public class MazeGenerator
{
    private List<Integer> visited;
    private List<String> nextDirection;

    private List<Walls> walls;
    private ArrayList<Integer> numbers;
    private Stack<Integer> wallStack;
    private Set<Integer> numbersToShuffle;

    private Random randDir = new Random();

    private Wall nWall;

    private Walls currBlock;
    private Walls wall;

    private int sizeOfMaze;
    private int row;
    private int col;
    private int currentBlockId;
    private int start;

    private int northwalls;
    private int southwalls;
    private int westwalls;
    private int eastwalls;

    private int up, down, left, right;
    private String directionFrom;

    private boolean newWall;
    private boolean justPopped;
    private boolean north, south, west, east;

    private float northWall = 0;
    private float southWall = -5;
    private float eastWall = 5;
    private float westWall = 0;

    private int wallCounter = 0;
    private Point3D startPoint;

    public MazeGenerator(int row, int col, Shader shader)
    {
        this.numbersToShuffle = new HashSet<Integer>();
        this.visited = new ArrayList<Integer>();
        this.nextDirection = new ArrayList<String>();
        this.numbers = new ArrayList<Integer>();
        this.wallStack = new Stack();
        this.walls = new ArrayList<Walls>();
        this.currBlock = new Walls();

        this.randDir = new Random();

        this.nWall = new Wall();

        this.row = row;
        this.col = col;
        this.sizeOfMaze = row * col;
        this.directionFrom = "";

        this.justPopped = false;
        this.north = false;
        this.south = false;
        this.west = false;
        this.east = false;

        northwalls = 0;
        southwalls = 0;
        westwalls = 0;
        eastwalls = 0;

        this.startPoint = new Point3D(0,0,0);

        fillBorderBlocks();
        getInitializePoint();
        generate();
        createCoordinates();
        fixDuplicates();
    }


    public void generate()
    {
        while(visited.size() < sizeOfMaze)
        {
            checkIfVisited();

            nextDirection.clear();

            getNeighborsBlock();

            north = false;
            south = false;
            west = false;
            east = false;

            checkValidDirection();

            breakDownWallFrom();

            chooseDirection();
        }
    }

    private void fillBorderBlocks()
    {
        for(int i = 0; i < row; i++)
        {
            numbers.add(i);
            numbers.add(row * i);
        }

        for(int i = 1; i < row; i++)
        {
            numbers.add((i * row) + (row-1));
            numbers.add( (row * row) - i );
        }

        numbersToShuffle.addAll(numbers);
        numbers.clear();
        numbers.addAll(numbersToShuffle);
    }

    private void getInitializePoint()
    {
        Collections.sort(numbers);
        Collections.shuffle(numbers);
        currentBlockId = numbers.get(0);

        start = currentBlockId;
    }

    public int getStart()
    {
        return start;
    }

    private void checkIfVisited()
    {
        if(!visited.contains(currentBlockId))
        {
            currBlock = new Walls(currentBlockId, true, true, true, true);
            visited.add(currentBlockId);
            wallStack.push(currentBlockId);
            newWall = true;
        }
        else
        {
            newWall = false;
            for (Walls wall : walls)
            {
                if(wall.getId() == currentBlockId)
                {
                    currBlock = wall;
                }
            }
        }
    }

    private void getNeighborsBlock()
    {
        up = currentBlockId - row;
        down = currentBlockId + row;
        left = currentBlockId -1;
        right = currentBlockId +1;
    }

    private void checkValidDirection()
    {
        // check up
        if(up >= 0 && !visited.contains(up))
        {
            north = true;
            this.nextDirection.add("up");
        }

        // check down
        if(down < sizeOfMaze && !visited.contains((down)))
        {
            south = true;
            this.nextDirection.add("down");
        }

        // check left
        if(currentBlockId % col != 0 && !visited.contains(left))
        {
            west = true;
            this.nextDirection.add("left");
        }

        // check right
        if( (currentBlockId+1) % col != 0 && !visited.contains(right))
        {
            east = true;
            this.nextDirection.add("right");
        }
    }

    private void breakDownWallFrom()
    {
        if(!directionFrom.equals("") && !justPopped)
        {
            if(directionFrom.equals("south"))
            {
                currBlock.setSouth(false);
            }
            else if(directionFrom.equals("north"))
            {
                currBlock.setNorth(false);
            }
            else if(directionFrom.equals("west"))
            {
                currBlock.setWest(false);
            }
            else if(directionFrom.equals("east"))
            {
                currBlock.setEast(false);
            }
        }
    }

    public void chooseDirection()
    {
        if(!north && !south && !west && !east)
        {
            if (wallStack.size() > 0)
            {

                currentBlockId = wallStack.pop();
                justPopped = true;
            }
            else if (wallStack.size() == 0)
            {
                System.out.println("Wall stack finito");
            }
        }
        else
        {
            justPopped = false;
            int randir = randDir.nextInt(this.nextDirection.size()) + 0;

            String direction = nextDirection.get(randir);

            if (direction.equals("up"))
            {
                currentBlockId = up;
                directionFrom = "south";
                currBlock.setNorth(false);
            }
            else if (direction.equals("down"))
            {
                currentBlockId = down;
                directionFrom = "north";
                currBlock.setSouth(false);
            }
            else if (direction.equals("left"))
            {
                currentBlockId = left;
                directionFrom = "east";
                currBlock.setWest(false);
            }
            else if (direction.equals("right"))
            {
                currentBlockId = right;
                directionFrom = "west";
                currBlock.setEast(false);
            }
        }

        if(newWall)
        {
            walls.add(currBlock);
        }
    }

    public List<Walls> getWalls()
    {
        return walls;
    }

    public Point3D getStartPoint()
    {
        return startPoint;
    }


    private void fixDuplicates()
    {

        for (Walls wallBlock: walls)
        {
            for(Walls wallb: walls)
            {

                if(wallBlock.getNorth() && wallb.getSouth())
                {
                    if(wallBlock.getNorthWall().getPosX() == wallb.getSouthWall().getPosX() && wallBlock.getNorthWall().getPosZ() == wallb.getSouthWall().getPosZ())
                    {
                        wallb.setSouth(false);
                        southwalls--;

                    }
                }

                if(wallBlock.getWest() && wallb.getEast())
                {
                    if(wallBlock.getWestWall().getPosX() == wallb.getEastWall().getPosX() && wallBlock.getWestWall().getPosZ() == wallb.getEastWall().getPosZ())
                    {
                        wallb.setEast(false);
                        eastwalls--;
                    }
                }

            }
        }
    }

    private void createCoordinates()
    {
        Collections.sort(walls);
        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < row; j++)
            {
                wall = walls.get(wallCounter);

                if(wall.getNorth())
                {
                    wall.getNorthWall().setWallPos(northWall, 2.5f, eastWall - 2.5f);
                    wall.getNorthWall().setWallScale(0.5f, 5, 5.5f);

                    northwalls++;
                }

                if(wall.getSouth())
                {
                    wall.getSouthWall().setWallPos(southWall, 2.5f, eastWall - 2.5f);
                    wall.getSouthWall().setWallScale(0.5f, 5, 5.5f);

                    southwalls++;
                }

                if(wall.getWest())
                {
                    wall.getWestWall().setWallPos(northWall - 2.5f, 2.5f, westWall);
                    wall.getWestWall().setWallScale(5.0f, 5, 0.5f);
                    westwalls++;
                }

                if(wall.getEast())
                {
                    wall.getEastWall().setWallPos(northWall - 2.5f, 2.5f, eastWall);
                    wall.getEastWall().setWallScale(5.0f, 5, 0.5f);
                    eastwalls++;
                }


                wallCounter++;
                eastWall += 5;
                westWall += 5;
            }
            northWall -= 5;
            southWall -= 5;
            eastWall = 5;
            westWall = 0;
        }

        wallCounter = 0;
    }


    public void drawMaze()
    {
        Collections.sort(walls);
        for (Walls drawWall : walls)
        {
            drawWall.display();
        }
    }
}
