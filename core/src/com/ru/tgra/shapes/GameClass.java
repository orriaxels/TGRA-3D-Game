package com.ru.tgra.shapes;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

import java.lang.reflect.Array;
import java.util.*;

public class GameClass extends ApplicationAdapter implements InputProcessor {


	private Random rand;
	private boolean noClip;
	private boolean enemyCollision;

	Shader shader;

	Vector3D vec = new Vector3D(0,0,0);
	private Camera fpsCam;
	private Camera orthoCam;
	private Camera hudCamera;

	MazeGenerator maze;

	private float fov = 90.0f;
	private int row = 10;
	private int col = 10;
	private int start;
	private int enemyStart = 0;


	private List<Walls> allWalls;
	private List<Point3D> allWallsPos;
	private Point3D startpoint;

	//private List<Float> randCoinsPos;
	private List<Coin> coins;

    private List<Walls> removeWalls;

    private Enemy enemy;

	@Override
	public void create ()
    {
		shader = new Shader();
        rand = new Random();

		startGame();

		Gdx.input.setInputProcessor(this);

		BoxGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SphereGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SincGraphic.create(shader.getVertexPointer());
		CoordFrameGraphic.create(shader.getVertexPointer());

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		hudCamera = new Camera();
		hudCamera.orthographicProjection(-5, 5, -5, 5, 3.0f, 100);

		orthoCam = new Camera();
		orthoCam.orthographicProjection(-10, 10, -10, 10, 3.0f, 100);

		Gdx.input.setCursorCatched(true);
	}

	private void startGame()
	{
		maze = new MazeGenerator(row, col, shader);

		allWalls = maze.getWalls();
		allWallsPos = new ArrayList<Point3D>();
		removeWalls = new ArrayList<Walls>();
		List<Float> randCoinsPos = new ArrayList<Float>();
		coins = new ArrayList<Coin>();
		startpoint = maze.getStartPoint();
		start = maze.getStart();
		enemy = new Enemy(-2.5f, 3, 2.5f, maze.getWalls(), enemyStart);


		do
		{
			enemyStart = rand.nextInt(99) + 0;
		} while( enemyStart == 0 || enemyStart == start);


		for(int i = 1; i < (row*2); i++)
		{
			if(i % 2 != 0)
			{
				randCoinsPos.add(i * 2.5f);
			}
		}
		int count = 0;
		boolean match;
		do
		{
			match = false;
			float x = -randCoinsPos.get(rand.nextInt(randCoinsPos.size()) + 0);
			float z =  randCoinsPos.get(rand.nextInt(randCoinsPos.size()) + 0);

			if(coins.size() == 0)
			{
				Coin c = new Coin(x, 1.0f, z);
				coins.add(c);
				count++;
			}
			else
			{
				for(Coin coin: coins)
				{
					if(coin.getPosX() == x && coin.getPosZ() == z)
					{
						match = true;
						break;
					}
				}

				if(!match)
				{
					Coin c = new Coin(x, 1.0f, z);
					coins.add(c);
					count++;
				}
			}
        }while(count < row);

		fpsCam = new Camera();
		fpsCam.look(startpoint, new Point3D(0,2,0), new Vector3D(0,1,0));

	}

	private void restart()
	{
		enemy = null;
		maze = null;
		allWalls.clear();
		coins.clear();
		allWallsPos.clear();
	}

	private void input()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			fpsCam.yaw(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			fpsCam.yaw(90.0f * deltaTime);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			fpsCam.slide(-8.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			fpsCam.slide(8.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			fpsCam.slide(0,0, -8.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			fpsCam.slide(0,0, 8.0f * deltaTime);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.T)) {
			fov -= 30.0f * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			fov += 30.0f * deltaTime;
		}

		fpsCam.roll(-0.1f * Gdx.input.getDeltaX());
		fpsCam.pitch(-0.1f * Gdx.input.getDeltaY());


		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			System.out.println("fps-x: " + fpsCam.getEye().x);
			System.out.println("fps-y: " + fpsCam.getEye().y);
			System.out.println("fps-z: " + fpsCam.getEye().z);
			System.out.println();
			System.out.println("-------------------------");
			if(noClip)
			    noClip = false;
			else
			    noClip = true;
		}

	}
	
	private void update()
	{
		if(coins.isEmpty())
		{
			restart();
			startGame();
		}

		if( (enemy.posX - 0.75f <= fpsCam.eye.x + 1f && enemy.posZ + 0.75f >= fpsCam.eye.z - 1) && (enemy.posX + 0.75f >= fpsCam.eye.x - 1 && enemy.posZ - 0.75f <= fpsCam.eye.z + 1) )
		{
			restart();
			startGame();
		}


        float deltaTime = Gdx.graphics.getDeltaTime();
		input();
		enemy.update(deltaTime);


		for(int i = 0; i < coins.size(); i++)
		{
		    coins.get(i).update(deltaTime);
			if( (fpsCam.eye.z <= coins.get(i).getPosZ() + 1f && fpsCam.eye.z >= coins.get(i).getPosZ() - 1f) && (fpsCam.eye.x <= coins.get(i).getPosX() + 1f && fpsCam.eye.x >= coins.get(i).getPosX() - 1f))
			{
				coins.remove(coins.get(i));
			}
		}
		if(!noClip)
		{
			for (Walls wall: allWalls)
			{
				if (wall.getNorth())
				{
					if ((fpsCam.eye.z <= wall.getNorthWall().getPosZ() + 2.5f && fpsCam.eye.z >= wall.getNorthWall().getPosZ() - 2.5f) && (fpsCam.eye.x - 1 <= wall.getNorthWall().getPosX() + 0.25f && fpsCam.eye.x + 1 >= wall.getNorthWall().getPosX() - 0.25f))
					{
						if ((fpsCam.eye.x) < wall.getNorthWall().getPosX())
						{
							fpsCam.eye.x -= (fpsCam.eye.x + 1) - (wall.getNorthWall().getPosX() - 0.25);
						}

						if ((fpsCam.eye.x) > wall.getNorthWall().getPosX())
						{
							fpsCam.eye.x -= (fpsCam.eye.x - 1) - (wall.getNorthWall().getPosX() + 0.25);
						}
					}
					if ((fpsCam.eye.x <= wall.getNorthWall().getPosX() + 0.25f && fpsCam.eye.x >= wall.getNorthWall().getPosX() - 0.25f) && (fpsCam.eye.z - 1 <= wall.getNorthWall().getPosZ() + 2.75f && fpsCam.eye.z + 1 >= wall.getNorthWall().getPosZ() - 2.75f))
					{
						if ((fpsCam.eye.z) < wall.getNorthWall().getPosZ())
						{
							fpsCam.eye.z -= (fpsCam.eye.z + 1) - (wall.getNorthWall().getPosZ() - 2.75f);
						}

						if ((fpsCam.eye.z) > wall.getNorthWall().getPosZ())
						{
							fpsCam.eye.z -= (fpsCam.eye.z - 1) - (wall.getNorthWall().getPosZ() + 2.75f);
						}
					}
				}

				if (wall.getWest())
				{
					if ((fpsCam.eye.z - 1 <= wall.getWestWall().getPosZ() + 0.25f && fpsCam.eye.z + 1 >= wall.getWestWall().getPosZ() - 0.25f) && (fpsCam.eye.x <= wall.getWestWall().getPosX() + 2.5f && fpsCam.eye.x >= wall.getWestWall().getPosX() - 2.5f))
					{
						if ((fpsCam.eye.z) < wall.getWestWall().getPosZ())
						{
							fpsCam.eye.z -= (fpsCam.eye.z + 1) - (wall.getWestWall().getPosZ() - 0.25);
						}

						if ((fpsCam.eye.z) > wall.getWestWall().getPosZ())
						{
							fpsCam.eye.z -= (fpsCam.eye.z - 1) - (wall.getWestWall().getPosZ() + 0.25);
						}
					}

					if ((fpsCam.eye.z <= wall.getWestWall().getPosZ() + 0.25 && fpsCam.eye.z >= wall.getWestWall().getPosZ() - 0.25f) && (fpsCam.eye.x - 1 <= wall.getWestWall().getPosX() + 2.5 && fpsCam.eye.x + 1 >= wall.getWestWall().getPosX() - 2.5f))
					{
						if (fpsCam.eye.x > wall.getWestWall().getPosX())
						{
							fpsCam.eye.x -= (fpsCam.eye.x - 1) - (wall.getWestWall().getPosX() + 2.5f);
						}

						if (fpsCam.eye.x < wall.getWestWall().getPosX())
						{
							fpsCam.eye.x -= (fpsCam.eye.x + 1) - (wall.getWestWall().getPosX() - 2.5f);
						}
					}
				}

				if (wall.getSouth())
				{
					if ((fpsCam.eye.z <= wall.getSouthWall().getPosZ() + 2.5f && fpsCam.eye.z >= wall.getSouthWall().getPosZ() - 2.5f) && (fpsCam.eye.x - 1 <= wall.getSouthWall().getPosX() + 0.25f && fpsCam.eye.x + 1 >= wall.getSouthWall().getPosX() - 0.25f))
					{
						if ((fpsCam.eye.x) < wall.getSouthWall().getPosX())
						{
							fpsCam.eye.x -= (fpsCam.eye.x + 1) - (wall.getSouthWall().getPosX() - 0.25);
						}

						if ((fpsCam.eye.x) > wall.getSouthWall().getPosX())
						{
							fpsCam.eye.x -= (fpsCam.eye.x - 1) - (wall.getSouthWall().getPosX() + 0.25);
						}
					}
				}


				if (wall.getEast())
				{
					if ((fpsCam.eye.z - 1 <= wall.getEastWall().getPosZ() + 0.25f && fpsCam.eye.z + 1 >= wall.getEastWall().getPosZ() - 0.25f) && (fpsCam.eye.x <= wall.getEastWall().getPosX() + 2.5f && fpsCam.eye.x >= wall.getEastWall().getPosX() - 2.5f))
					{
						if ((fpsCam.eye.z) < wall.getEastWall().getPosZ())
						{
							fpsCam.eye.z -= (fpsCam.eye.z + 1) - (wall.getEastWall().getPosZ() - 0.25);
						}

						if ((fpsCam.eye.z) > wall.getEastWall().getPosZ())
						{
							fpsCam.eye.z -= (fpsCam.eye.z - 1) - (wall.getEastWall().getPosZ() + 0.25);
						}
					}
				}
			}
		}

    }
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		for(int viewNum = 0; viewNum < 3; viewNum++)
		{
			if(viewNum == 0)
			{
				Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				fpsCam.perspectiveProjection(fov, 1.0f,0.1f,10000000.0f);
				shader.setViewMatrix(fpsCam.getViewMatrix());
				shader.setProjectionMatrix(fpsCam.getProjectionMatrix());
			}
			else if(viewNum == 2)
			{
				Gdx.gl.glViewport((Gdx.graphics.getWidth() / 2) + 250, Gdx.graphics.getHeight() - 30, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 20);
				hudCamera.look(new Point3D(25, 40.0f, 25), new Point3D(25, 0.0f, 25), new Vector3D(0.0f,0.0f,-1.0f));
				shader.setViewMatrix(hudCamera.getViewMatrix());
				shader.setProjectionMatrix(hudCamera.getProjectionMatrix());
			}
			else
			{
				Gdx.gl.glViewport((Gdx.graphics.getWidth() / 2) + 250, Gdx.graphics.getHeight() / 2 - 30, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 - 6);
				orthoCam.look(new Point3D(fpsCam.eye.x, 30.0f, fpsCam.eye.z), fpsCam.eye, new Vector3D(0.0f,0.0f,-1.0f));
				shader.setViewMatrix(orthoCam.getViewMatrix());
				shader.setProjectionMatrix(orthoCam.getProjectionMatrix());
			}

			maze.drawMaze();

			enemy.display();

			// Light 1
			shader.setLightPosition(fpsCam.eye.x, 10,fpsCam.eye.z, 1.0f);
			shader.setLightColor(1f, 1f, 1f, 1.0f);

			// Light 2
			shader.setLightPosition2(24, 20, 24, 1.0f);
			shader.setLightColor2(1f, 1f, 1f, 1.0f);

			// Light 3
			shader.setLightPosition3(0, 10.0f, 100, 1.0f);
			shader.setLightColor3(1f, 1f, 1f, 1.0f);


			shader.setGlobalAmbient(0.2f, 0.2f, 0.2f, 1);
			shader.setMaterialEmission(1.0f, 0f, 0f, 1.0f);
			shader.setMaterialDiffuse(0, 0, 0, 1);
			shader.setMaterialSpecular(0, 0, 0, 1);

			shader.setMaterialDiffuse(0.3f, 0.3f, 0.7f, 1.0f);
			shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setMaterialEmission(0, 0, 0, 1);
			shader.setShininess(10.0f);

			// Draw the floor
			shader.setMaterialDiffuse(0.5f, 0.5f, 0.5f, 1.0f);
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(-25,0,25);
			ModelMatrix.main.addScale(200,1,200);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();

			for(int i = 0; i < coins.size(); i++)
			{
                coins.get(i).display();
				shader.setMaterialDiffuse(1f, 0f, 0f, 1.0f);
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(20.5f + i, 5, 25);
				ModelMatrix.main.addScale(0.4f, 1.5f, 2f);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube();
				ModelMatrix.main.popMatrix();
			}

			if(viewNum == 1 || viewNum == 2)
			{
				shader.setMaterialDiffuse(1f, 1f, 0f, 1.0f);
				ModelMatrix.main.loadIdentityMatrix();
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(fpsCam.eye.x, fpsCam.eye.y, fpsCam.eye.z);
				ModelMatrix.main.addScale(0.8f, 0.8f, 0.8f);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				SphereGraphic.drawSolidSphere();
				ModelMatrix.main.popMatrix();

			}
        }
	}

	@Override
	public void render () {

		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();

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