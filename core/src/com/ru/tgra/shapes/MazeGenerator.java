package com.ru.tgra.shapes;

import java.util.*;

public class MazeGenerator
{
    private List<Integer> visited;
    private List<String> nextDirection;

    private List<MazeGeneratorBool> walls;
    private ArrayList<Integer> numbers;
    private Stack<Integer> wallStack;
    private Set<Integer> numbersToShuffle;

    private Random randDir = new Random();

    private MazeGeneratorBool currBlock;
    private MazeGeneratorBool wall;

    private int sizeOfMaze;
    private int row;
    private int col;
    private int currentBlockId;
    private int startPoint;

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
    private Shader shader;

    public MazeGenerator(int row, int col, Shader shader)
    {
        this.numbersToShuffle = new HashSet<Integer>();
        this.visited = new ArrayList<Integer>();
        this.nextDirection = new ArrayList<String>();
        this.numbers = new ArrayList<Integer>();
        this.wallStack = new Stack();
        this.walls = new ArrayList<MazeGeneratorBool>();
        this.currBlock = new MazeGeneratorBool();

        this.randDir = new Random();

        this.row = row;
        this.col = col;
        this.sizeOfMaze = row * col;
        this.directionFrom = "";

        this.justPopped = false;
        this.north = false;
        this.south = false;
        this.west = false;
        this.east = false;

        this.shader = shader;

        fillBorderBlocks();
        getInitializePoint();
        generate();
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

        startPoint = currentBlockId;
    }

    private void checkIfVisited()
    {
        if(!visited.contains(currentBlockId))
        {
            currBlock = new MazeGeneratorBool(currentBlockId, true, true, true, true);
            visited.add(currentBlockId);
            wallStack.push(currentBlockId);
            newWall = true;
        }
        else
        {
            newWall = false;
            for (MazeGeneratorBool wall : walls)
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

    public List<MazeGeneratorBool> getWalls()
    {
        return walls;
    }

    public int getStartPoint()
    {
        return startPoint;
    }

    public void drawMaze()
    {
        //        walls = maze.getWalls();
        Collections.sort(walls);

        ModelMatrix.main.pushMatrix();
        for(int i = 0; i < row; i++)
        {
            ModelMatrix.main.pushMatrix();
            for(int j = 0; j < row; j++)
            {
                shader.setLightDiffuse(1, 1, 1, 1);
                wall = walls.get(wallCounter);
                if(wall.getNorth())
                {

                    ModelMatrix.main.pushMatrix();
                    ModelMatrix.main.addTranslation(northWall, 2.5f, eastWall - 2.5f );
                    ModelMatrix.main.addScale(0.5f, 5, 5);
                    shader.setModelMatrix(ModelMatrix.main.getMatrix());
                    BoxGraphic.drawSolidCube();
                    ModelMatrix.main.popMatrix();
                }

                if(wall.getSouth())
                {
                    ModelMatrix.main.pushMatrix();
                    ModelMatrix.main.addTranslation(southWall, 2.5f, eastWall - 2.5f);
                    ModelMatrix.main.addScale(0.5f, 5, 5);
                    shader.setModelMatrix(ModelMatrix.main.getMatrix());
                    BoxGraphic.drawSolidCube();
                    ModelMatrix.main.popMatrix();
                }

                if(wall.getWest())
                {
                    ModelMatrix.main.pushMatrix();
                    ModelMatrix.main.addTranslation(northWall - 2.5f, 2.5f, westWall);
                    ModelMatrix.main.addScale(5, 5, 0.5f);
                    shader.setModelMatrix(ModelMatrix.main.getMatrix());
                    BoxGraphic.drawSolidCube();
                    ModelMatrix.main.popMatrix();
                }

                if(wall.getEast())
                {
                    ModelMatrix.main.pushMatrix();
                    ModelMatrix.main.addTranslation(northWall - 2.5f , 2.5f, eastWall);
                    ModelMatrix.main.addScale(5, 5, 0.5f);
                    shader.setModelMatrix(ModelMatrix.main.getMatrix());
                    BoxGraphic.drawSolidCube();
                    ModelMatrix.main.popMatrix();
                }


                wallCounter++;
                eastWall += 5;
                westWall += 5;
            }
            ModelMatrix.main.popMatrix();

            northWall -= 5;
            southWall -= 5;
            eastWall = 5;
            westWall = 0;

        }
        ModelMatrix.main.popMatrix();
        wallCounter = 0;

        northWall = 0;
        southWall = -5;
        eastWall = 5;
        westWall = 0;
    }
}
