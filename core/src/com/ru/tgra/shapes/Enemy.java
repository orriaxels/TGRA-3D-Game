package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Enemy
{
    private Point3D startPosition;
    private Shader shader;
    private List<Walls> walls;
    private int start;
    private Walls wallBlock;
    private Random rand;
    private boolean moving;
    private int wayToGo;
    private float tempZ;
    private float tempX;
    private int lastDirection;

    private List<Integer> direction;

    float posX;
    float posY;
    float posZ;

    boolean north;
    boolean south;
    boolean west;
    boolean east;

    public Enemy(float x, float y, float z, List<Walls> walls, int startId)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.start = 0;
        shader = new Shader();
        this.walls = walls;
        wallBlock = walls.get(0);
        rand = new Random();
        start = 0;

        this.direction = new ArrayList<Integer>();
        moving = false;
        tempZ = 2.5f;
        tempX = -2.5f;
//        System.out.println("wALLE : " + wall.getId());
//        System.out.println(startId);
//        this.posX = wall.getNorthWall().getPosX() - 2.5f;
//        this.posZ = wall.getNorthWall().getPosZ() - 2.5f;
//        this.posY = 1.5f;
    }

    public void update(float deltaTime)
    {
        if(!moving)
        {
            wallBlock = walls.get(start);
//            int up = wallBlock.getId() + 10;
//            int south = wallBlock.getId() - 10;
//            int west = wallBlock.getId() - 1;
//            int east = wallBlock.getId() +1;

            if(!wallBlock.getNorth() && lastDirection != 1)
            {
                direction.add(1);
                System.out.println("GOING NORTH");
            }


            if(!(wallBlock.getId() + 10 > 99) && !walls.get(wallBlock.getId() + 10).getNorth() && lastDirection != 2)
            {
                direction.add(2);
                System.out.println("GOING SOUTH");
            }

            if(!wallBlock.getWest() && lastDirection != 3)
            {
                direction.add(3);
                System.out.println("GOING WEST");
            }

            if(!wallBlock.getEast() && !walls.get(wallBlock.getId() + 1).getWest() && lastDirection != 4)
            {
                direction.add(4);
                System.out.println("GOING EAST");
            }

            System.out.println(direction.size());

//            if(direction.size() == 2 && lastDirection != 4)
//            {
//                for(int i = 0; i < direction.size(); i++)
//                {
//                    System.out.println("forlykkja");
//                    if(direction.get(i) != lastDirection)
//                    {
//                        wayToGo = direction.get(i);
//                        System.out.println("Direction cho: " + direction.get(i));
//                    }
//                }
//            }
//            else
//            {
//                int choose = rand.nextInt(direction.size()) + 0;
//                System.out.println("Way to go: " + direction.get(choose));
//                wayToGo = direction.get(choose);
//                direction.clear();
//                tempX = this.posX;
//                tempZ = this.posZ;
//                moving = true;
//            }

            if(direction.size() == 0)
            {
                wayToGo = lastDirection;
            }
            else
            {
                Collections.shuffle(direction);
                wayToGo = direction.get(0);
            }

            direction.clear();
            tempX = this.posX;
            tempZ = this.posZ;
            moving = true;

        }



        if(moving)
        {
            if(wayToGo == 1 && tempX + 5 >= posX)
            {
                this.posX += 1 * deltaTime;
            }
            else if(wayToGo == 1 && posX >= tempX + 5)
            {
                moving = false;
                this.start -= 10;
                lastDirection = 2;
            }

            if(wayToGo == 2 && posX >= tempX - 5)
            {
                this.posX -= 1 * deltaTime;
            }
            else if(wayToGo == 2 && posX <= tempX - 5)
            {
                moving = false;
                this.start += 10;
                lastDirection = 1;
            }

            if(wayToGo == 3 && tempZ - 5 <= posZ)
            {
                this.posZ -= 1 * deltaTime;

            }
            else if(wayToGo == 3 && posZ <= tempZ - 5)
            {
                moving = false;
                this.start -= 1;
                lastDirection = 4;
            }

            if(wayToGo == 4 && tempZ + 5 >= posZ)
            {
                this.posZ += 1 * deltaTime;
            }
            else if(wayToGo == 4 && posZ >= tempZ + 5)
            {
                moving = false;
                this.start += 1;
                lastDirection = 3;
            }
        }



    }

    public void display()
    {
        shader.setMaterialDiffuse(1.0f, 0.0f, 0.0f, 1);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(posX, posY, posZ);
        ModelMatrix.main.addScale(1.5f, 6, 1.5f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube();
        ModelMatrix.main.popMatrix();
    }
}