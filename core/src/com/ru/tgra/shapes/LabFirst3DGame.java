package com.ru.tgra.shapes;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import java.util.*;
import java.nio.FloatBuffer;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;

import javax.swing.*;

public class LabFirst3DGame extends ApplicationAdapter implements InputProcessor {

	private float angle;

	Shader shader;
	private Camera cam;
	private Camera orthoCam;

	private float fov = 90.0f;

	//private ModelMatrix modelMatrix;

	@Override
	public void create ()
    {

	    randomMazeGenerator();
		shader = new Shader();

		Gdx.input.setInputProcessor(this);

		//COLOR IS SET HERE
		shader.setColor(0.7f, 0.2f, 0, 1);

		BoxGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SphereGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SincGraphic.create(shader.getVertexPointer());
		CoordFrameGraphic.create(shader.getVertexPointer());

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
//		ModelMatrix.main.setShaderMatrix(modelMatrixLoc);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		cam = new Camera();
		cam.look(new Point3D(0f, 0f, 4f), new Point3D(0,0,0),  new Vector3D(0,1,0));

		orthoCam = new Camera();
		orthoCam.orthographicProjection(-10, 10, -10, 10, 3.0f, 100);
	}

	private void input()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			cam.yaw(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			cam.yaw(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			cam.pitch(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			cam.pitch(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
			cam.roll(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			cam.roll(90.0f * deltaTime);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			cam.slide(-3.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			cam.slide(3.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			cam.slide(0,0, -3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			cam.slide(0,0, 3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.R)) {
			cam.slide(0,3.0f * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			cam.slide(0,-3.0f * deltaTime, 0);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.T)) {
			fov -= 30.0f * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			fov += 30.0f * deltaTime;
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			System.out.println("x: " + cam.getEye().x);
			System.out.println("y: " + cam.getEye().y);
			System.out.println("z: " + cam.getEye().z);
		}

	}
	
	private void update()
	{
		input();
//		float deltaTime = Gdx.graphics.getDeltaTime();

		//angle += 180.0f * deltaTime;

		//do all updates to the game
	}
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		shader.setColor(0.0f, 1f, 1f, 1.0f);

		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(0,3,-1);
		ModelMatrix.main.addScale(2,2,2);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(50,0,50);
		ModelMatrix.main.addScale(100,1,100);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();

		for(int viewNum = 0; viewNum < 2; viewNum++)
		{
			if(viewNum == 0)
			{
				Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				cam.perspectiveProjection(fov, 1.0f,0.1f,10000000.0f);
				shader.setViewMatrix(cam.getViewMatrix());
				shader.setProjectionMatrix(cam.getProjectionMatrix());
			}
			else
			{

				Gdx.gl.glViewport(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()/2);
				orthoCam.look(new Point3D(cam.eye.x, 20.0f, cam.eye.z), cam.eye, new Vector3D(0.0f,0.0f,-1.0f));
				shader.setViewMatrix(orthoCam.getViewMatrix());
				shader.setProjectionMatrix(orthoCam.getProjectionMatrix());

			}

			ModelMatrix.main.loadIdentityMatrix();
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(0,3,-1);
			ModelMatrix.main.addScale(2,2,2);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();

			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(50,0,50);
			ModelMatrix.main.addScale(100,1,100);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();

			int maxLevel = 4;
			
			ModelMatrix.main.pushMatrix();
			for(int level = 0; level < maxLevel; level++)
			{
				ModelMatrix.main.addTranslation(0.55f, 1.0f, -0.55f);
				ModelMatrix.main.pushMatrix();

				for(int i = 0; i < maxLevel - level; i++)
				{
					ModelMatrix.main.addTranslation(1.1f, 0f, 0f);
					ModelMatrix.main.pushMatrix();
					for(int j = 0; j < maxLevel-level; j++)
					{
						ModelMatrix.main.addTranslation(0f, 0f, -1.1f);
						ModelMatrix.main.pushMatrix();
						ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);

						shader.setModelMatrix(ModelMatrix.main.getMatrix());
						SphereGraphic.drawSolidSphere();
						ModelMatrix.main.popMatrix();
					}
					ModelMatrix.main.popMatrix();
				}
				ModelMatrix.main.popMatrix();

			}
			if(viewNum == 1)
			{
				shader.setColor(1.0f,0.3f,0.1f,1.0f);
				ModelMatrix.main.loadIdentityMatrix();
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(cam.eye.x, cam.eye.y, cam.eye.z);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube();
				ModelMatrix.main.popMatrix();
			}
			ModelMatrix.main.popMatrix();
		}
	}

	@Override
	public void render () {

		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();

	}

	public void randomMazeGenerator()
    {
        List<MazeGeneratorBool> walls = new ArrayList<MazeGeneratorBool>();
        Set<Integer> numbersToShuffle = new HashSet<Integer>();
        List<Integer> visited = new ArrayList<Integer>();
        List<Integer> stack = new ArrayList<Integer>();
        List<String> nextDirection = new ArrayList<String>();
        ArrayList<Integer> numbers = new ArrayList<Integer>();

        int row = 10;
        int col = 10;
        int count = 0;
        int sizeOfMaze = row * col;
        int visits = 0;
        int currentBlockId;
        int previousVisit;
        int end;
        String directionFrom = "";

        Random randDir = new Random();

        for(int i = 0; i < 10; i++)
        {
            numbers.add(i);
            numbers.add(90+i);
        }

        for(int i = 1; i < 10; i++)
        {
            numbers.add(i * 10);
            numbers.add((i * 10) + 9);
        }

        numbersToShuffle.addAll(numbers);
        numbers.clear();
        numbers.addAll(numbersToShuffle);

        Collections.sort(numbers);
        Collections.shuffle(numbers);
        //currentBlockId = numbers.get(1);
        visited.add(numbers.get(0));
        currentBlockId = numbers.get(0);

        while(visits < sizeOfMaze)
        {
//            System.out.println("Visit: " + (visits + 1));
//            System.out.println("Current: " + currentBlockId);
            MazeGeneratorBool currBlock = new MazeGeneratorBool(0, true, true, true, true);
            currBlock.setId(currentBlockId);
            nextDirection.clear();
            int up = currentBlockId - row;
            int down = currentBlockId + row;
            int left = currentBlockId -1;
            int right = currentBlockId +1;


            boolean north = false;
            boolean south = false;
            boolean west = false;
            boolean east = false;

            // check up
            if(up >= 0 && !visited.contains(up))
            {
                north = true;
                nextDirection.add("up");
                //currBlock.setNorth(true);
                //System.out.println("Up: " + up);

            }

            // check down
            if(down < sizeOfMaze && !visited.contains((down)))
            {
                south = true;
                nextDirection.add("down");
                //currBlock.setSouth(true);
                //System.out.println("Down: " + down);

            }

            // check left
            if(currentBlockId % col != 0 && !visited.contains(left))
            {
                west = true;
                nextDirection.add("left");
                //currBlock.setEast(true);
                //System.out.println("Left: " + left);
            }

            // check right
            if( (currentBlockId+1) % col != 0 && !visited.contains(right))
            {
                east = true;
                nextDirection.add("right");
                //currBlock.setWest(true);
                //System.out.println("Right: " + right);
            }

            if(!north && !south && !west && !east)
            {
                //System.out.println("Stack POP: " + stack.get(stack.size() - 1 ));

                //System.out.println("Stack POP: " + stack.get(stack.size() - 1 ));
                if(stack.size() > 0)
                {
                    stack.remove(stack.size() - 1 );
                    if(stack.size() > 0)
                    {
                        currentBlockId = stack.get(stack.size() - 1 );

//                        for(MazeGeneratorBool wall : walls)
//                        {
//                            if(wall.getId() == currentBlockId)
//                            {
//
//                            }
//                        }

                    }
                }
                else
                {
                    break;
                }


            }
            else
            {
                int randir = randDir.nextInt(nextDirection.size()) + 0;

                String direction = nextDirection.get(randir);
                //System.out.println("direction: " + direction);

                if(!direction.equals(""))
                {
//                    System.out.println("DirectionFrom: " + directionFrom);
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

                if (direction.equals("up"))
                {
//                    System.out.println("up");
                    visited.add(up);
                    stack.add(up);
                    currentBlockId = up;
                    directionFrom = "south";
                    currBlock.setNorth(false);

                }
                else if (direction.equals("down"))
                {
//                    System.out.println("down");
                    visited.add(down);
                    stack.add(down);
                    currentBlockId = down;
                    directionFrom = "north";
                    currBlock.setSouth(false);
                }
                else if (direction.equals("left"))
                {
//                    System.out.println("left");
                    visited.add(left);
                    stack.add(left);
                    currentBlockId = left;
                    directionFrom = "east";
                    currBlock.setWest(false);
                }
                else if (direction.equals("right"))
                {
//                    System.out.println("right");
                    visited.add(right);
                    stack.add(right);
                    currentBlockId = right;
                    directionFrom = "west";
                    currBlock.setEast(false);
                }


                walls.add(currBlock);

//                for(int i = 0; i < visited.size(); i++)
//                {
//                    System.out.println(i + ": " + visited.get(i));
//                }

//                System.out.println();

                visits++;
            }
        }
        int counter = 1;
        for(MazeGeneratorBool wall : walls)
        {
            System.out.println("NR: " + counter);
            System.out.println("Block nr: " + wall.getId());
            System.out.println("Walls to build: ");

            if(wall.getNorth())
            {
                System.out.println("North");
            }

            if(wall.getSouth())
            {
                System.out.println("South");
            }

            if(wall.getWest())
            {
                System.out.println("West");
            }

            if(wall.getEast())
            {
                System.out.println("East");
            }

            System.out.println();
            counter++;
        }
    }

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}


}