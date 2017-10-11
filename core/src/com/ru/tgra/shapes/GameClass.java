package com.ru.tgra.shapes;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

import java.lang.reflect.Array;
import java.util.*;

public class GameClass extends ApplicationAdapter implements InputProcessor {

	private boolean fps;

	private Random rand;

	Shader shader;

	Vector3D vec = new Vector3D(0,0,0);
	private Camera fpsCam;
	private Camera thirdPersonCam;
	private Camera orthoCam;
	private Camera hudCamera;

	MazeGenerator maze;

	private float fov = 90.0f;
	private float angle;
	private int row = 10;
	private int col = 10;

	private List<Walls> allWalls;
	private List<Point3D> allWallsPos;

	//private List<Float> randCoinsPos;
	private List<Coin> coins;

    private List<Walls> removeWalls;

	@Override
	public void create ()
    {
		fps = true;
		angle = 0;
		shader = new Shader();
        rand = new Random();
		maze = new MazeGenerator(row, col, shader);

		allWalls = maze.getWalls();
		allWallsPos = new ArrayList<Point3D>();
		removeWalls = new ArrayList<Walls>();
		List<Float> randCoinsPos = new ArrayList<Float>();
		coins = new ArrayList<Coin>();

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
                Coin c = new Coin(x, 1f, z);
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
                    Coin c = new Coin(x, 1f, z);
                    coins.add(c);
                    count++;
                }
            }
        }while( count < row);

		Gdx.input.setInputProcessor(this);

		//COLOR IS SET HERE

		BoxGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SphereGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SincGraphic.create(shader.getVertexPointer());
		CoordFrameGraphic.create(shader.getVertexPointer());

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		fpsCam = new Camera();
		fpsCam.look(new Point3D(-4f, 2f, 4f), new Point3D(0,2,0), new Vector3D(0,1,0));

		thirdPersonCam = new Camera();
		hudCamera = new Camera();
		hudCamera.orthographicProjection(-5, 5, -5, 5, 3.0f, 100);


		orthoCam = new Camera();
		orthoCam.orthographicProjection(-15, 15, -15, 15, 3.0f, 100);

		Gdx.input.setCursorCatched(true);
	}

	private void input()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();

		angle += 180.0f * deltaTime;

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

		if(Gdx.input.isKeyJustPressed(Input.Keys.V)) {
			if(fps)
				fps = false;
			else
				fps = true;
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			System.out.println("3rd-x: " + thirdPersonCam.getEye().x);
			System.out.println("3rd-y: " + thirdPersonCam.getEye().y);
			System.out.println("3rd-z: " + thirdPersonCam.getEye().z);
			System.out.println();
			System.out.println("fps-x: " + fpsCam.getEye().x);
			System.out.println("fps-y: " + fpsCam.getEye().y);
			System.out.println("fps-z: " + fpsCam.getEye().z);
			System.out.println("fps: " + fps);
			System.out.println();
			System.out.println("Delta-x: " + Gdx.input.getDeltaX());
			System.out.println("Delta-y: " + Gdx.input.getDeltaY());
			System.out.println("-------------------------");
		}

	}
	
	private void update()
	{
        float deltaTime = Gdx.graphics.getDeltaTime();
		input();

		for(int i = 0; i < coins.size(); i++)
		{
			if( (fpsCam.eye.z <= coins.get(i).getPosZ() + 1f && fpsCam.eye.z >= coins.get(i).getPosZ() - 1f) && (fpsCam.eye.x <= coins.get(i).getPosX() + 1f && fpsCam.eye.x >= coins.get(i).getPosX() - 1f))
			{
				coins.remove(coins.get(i));
			}
		}

        for (Walls wall: allWalls)
        {
            if(wall.getNorth())
            {
                if( (fpsCam.eye.z <= wall.getNorthWall().getPosZ() + 2.5f && fpsCam.eye.z >= wall.getNorthWall().getPosZ() - 2.5f) && (fpsCam.eye.x - 1 <= wall.getNorthWall().getPosX() + 0.25f && fpsCam.eye.x + 1 >= wall.getNorthWall().getPosX() - 0.25f))
                {
                    if( (fpsCam.eye.x) < wall.getNorthWall().getPosX() )
                    {
                        fpsCam.eye.x -= 0.1;
                    }

                    if( (fpsCam.eye.x) > wall.getNorthWall().getPosX() )
                    {
                        fpsCam.eye.x += 0.1;
                    }
                }
            }

            if(wall.getSouth())
            {
                if( (fpsCam.eye.z <= wall.getSouthWall().getPosZ() + 2.5f && fpsCam.eye.z >= wall.getSouthWall().getPosZ() - 2.5f) && (fpsCam.eye.x - 1 <= wall.getSouthWall().getPosX() + 0.25f && fpsCam.eye.x + 1 >= wall.getSouthWall().getPosX() - 0.25f))
                {
                    if( (fpsCam.eye.x) < wall.getSouthWall().getPosX() )
                    {
                        fpsCam.eye.x -= 0.1;
                    }

                    if( (fpsCam.eye.x) > wall.getSouthWall().getPosX() )
                    {
                        fpsCam.eye.x += 0.1;
                    }
                }
            }

            if(wall.getWest())
            {
                if( (fpsCam.eye.z - 1 <= wall.getWestWall().getPosZ() + 0.25f && fpsCam.eye.z + 1 >= wall.getWestWall().getPosZ() - 0.25f) && (fpsCam.eye.x <= wall.getWestWall().getPosX() + 2.5f && fpsCam.eye.x >= wall.getWestWall().getPosX() - 2.5f))
                {
                    if( (fpsCam.eye.z) < wall.getWestWall().getPosZ() )
                    {
                        fpsCam.eye.z -= 0.1;
                    }

                    if( (fpsCam.eye.z) > wall.getWestWall().getPosZ() )
                    {
                        fpsCam.eye.z += 0.1;
                    }
                }
            }
            if(wall.getEast())
            {
                if( (fpsCam.eye.z - 1 <= wall.getEastWall().getPosZ() + 0.25f && fpsCam.eye.z + 1 >= wall.getEastWall().getPosZ() - 0.25f) && (fpsCam.eye.x <= wall.getEastWall().getPosX() + 2.5f && fpsCam.eye.x >= wall.getEastWall().getPosX() - 2.5f))
                {
                    if( (fpsCam.eye.z) < wall.getEastWall().getPosZ() )
                    {
                        fpsCam.eye.z -= 0.1;
                    }

                    if( (fpsCam.eye.z) > wall.getEastWall().getPosZ() )
                    {
                        fpsCam.eye.z += 0.1;
                    }
                }
            }
        }

    }
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		vec.set(fpsCam.getN().x, fpsCam.getN().y, fpsCam.getN().z);
		vec.normalize();

		thirdPersonCam.look(new Point3D(fpsCam.eye.x + vec.x, 2, fpsCam.eye.z + vec.z), fpsCam.eye, new Vector3D(0,1,0));


		for(int viewNum = 0; viewNum < 3; viewNum++)
		{
			if(viewNum == 0)
			{
				Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				getCurrentCam().perspectiveProjection(fov, 1.0f,0.1f,10000000.0f);
				shader.setViewMatrix(getCurrentCam().getViewMatrix());
				shader.setProjectionMatrix(getCurrentCam().getProjectionMatrix());
			}
			else if(viewNum == 2)
			{
				Gdx.gl.glViewport((Gdx.graphics.getWidth() / 2) + 250, Gdx.graphics.getHeight() - 50, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 12);
				hudCamera.look(new Point3D(25, 40.0f, 25), new Point3D(25, 0.0f, 25), new Vector3D(0.0f,0.0f,-1.0f));
				shader.setViewMatrix(hudCamera.getViewMatrix());
				shader.setProjectionMatrix(hudCamera.getProjectionMatrix());
			}
			else
			{
				Gdx.gl.glViewport((Gdx.graphics.getWidth() / 2) + 250, Gdx.graphics.getHeight() / 2 - 50, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 - 6);
				orthoCam.look(new Point3D(fpsCam.eye.x, 40.0f, fpsCam.eye.z), fpsCam.eye, new Vector3D(0.0f,0.0f,-1.0f));
				shader.setViewMatrix(orthoCam.getViewMatrix());
				shader.setProjectionMatrix(orthoCam.getProjectionMatrix());

			}

			maze.drawMaze();

			// Light 1
			shader.setLightPosition(0, 10f,0, 1.0f);
			shader.setLightColor(1f, 1f, 1f, 1.0f);

			// Light 2
			shader.setLightPosition2(-100, 10, 100, 1.0f);
			shader.setLightColor2(1f, 1f, 1f, 1.0f);

//			// Light 3
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
				shader.setMaterialDiffuse(1f, 1f, 0f, 1.0f);
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(20.5f + i, 5, 24);
				ModelMatrix.main.addScale(0.2f, 0.5f, 1f);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				SphereGraphic.drawSolidSphere();
				ModelMatrix.main.popMatrix();
			}

			if(viewNum == 1 || viewNum == 2)
			{
				shader.setMaterialDiffuse(1f, 1f, 0f, 1.0f);
				ModelMatrix.main.loadIdentityMatrix();
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(fpsCam.eye.x, fpsCam.eye.y, fpsCam.eye.z);
				ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				SphereGraphic.drawSolidSphere();
				ModelMatrix.main.popMatrix();

			}

            for (Coin coin: coins)
            {
                coin.display();

            }
        }
	}

	@Override
	public void render () {

		//put the code inside the update and display methods, depending on the nature of the code

		update();
		display();

	}

	public Camera getCurrentCam()
	{
		if(fps)
			return fpsCam;
		else
			return thirdPersonCam;
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