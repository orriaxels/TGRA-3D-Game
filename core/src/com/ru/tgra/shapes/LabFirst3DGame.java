package com.ru.tgra.shapes;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import java.util.*;

public class LabFirst3DGame extends ApplicationAdapter implements InputProcessor {

	private boolean fps;

	Shader shader;

	Vector3D vec = new Vector3D(0,0,0);
	private Camera fpsCam;
	private Camera thirdPersonCam;
	private Camera orthoCam;

	MazeGenerator maze;

	private float fov = 90.0f;
	private float angle;

	//private ModelMatrix modelMatrix;

	@Override
	public void create ()
    {
		fps = true;
		angle = 0;
		shader = new Shader();

		maze = new MazeGenerator(10, 10, shader);

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
		fpsCam.look(new Point3D(0f, 2f, 4f), new Point3D(0,2,0), new Vector3D(0,1,0));

		thirdPersonCam = new Camera();

		orthoCam = new Camera();
		orthoCam.orthographicProjection(-10, 10, -10, 10, 3.0f, 100);

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
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			fpsCam.pitch(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			fpsCam.pitch(90.0f * deltaTime);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			fpsCam.slide(-3.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			fpsCam.slide(3.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			fpsCam.slide(0,0, -3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			fpsCam.slide(0,0, 3.0f * deltaTime);
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
		input();
	}
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		vec.set(fpsCam.getN().x, fpsCam.getN().y, fpsCam.getN().z);
		vec.normalize();

		thirdPersonCam.look(new Point3D(fpsCam.eye.x + vec.x, 2, fpsCam.eye.z + vec.z), fpsCam.eye, new Vector3D(0,1,0));

		for(int viewNum = 0; viewNum < 2; viewNum++)
		{
			if(viewNum == 0)
			{
				Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
				getCurrentCam().perspectiveProjection(fov, 1.0f,0.1f,10000000.0f);
				shader.setViewMatrix(getCurrentCam().getViewMatrix());
				shader.setProjectionMatrix(getCurrentCam().getProjectionMatrix());
			}
			else
			{
				Gdx.gl.glViewport(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
				orthoCam.look(new Point3D(fpsCam.eye.x, 40.0f, fpsCam.eye.z), fpsCam.eye, new Vector3D(0.0f,0.0f,-1.0f));
				shader.setViewMatrix(orthoCam.getViewMatrix());
				shader.setProjectionMatrix(orthoCam.getProjectionMatrix());
			}

			float s = (float)Math.sin(angle * Math.PI / 180.0);
			float c = (float)Math.cos(angle * Math.PI / 180.0);

			shader.setLightPosition(0.0f, 40.0f, 0.0f, 1);
			shader.setLightDiffuse(1, 1, 1, 1);

			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(0.0f,2.0f,0.0f);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			SphereGraphic.drawSolidSphere();
			ModelMatrix.main.popMatrix();

			maze.drawMaze();

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
			if(viewNum == 1 || !fps) {
				shader.setMaterialDiffuse(1f, 1f, 0f, 1.0f);
				ModelMatrix.main.loadIdentityMatrix();
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(fpsCam.eye.x, fpsCam.eye.y, fpsCam.eye.z);
				ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				SphereGraphic.drawSolidSphere();
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