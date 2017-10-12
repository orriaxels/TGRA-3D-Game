package com.ru.tgra.shapes;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Shader {

    private int renderingProgramID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private int positionLoc;
    private int normalLoc;

    private int modelMatrixLoc;
    private int viewMatrixLoc;
    private int projectionMatrixLoc;

    private int globalAmbient;

    private int lightPos;
    private int lightPos2;
    private int lightPos3;
    private int boxLight;
    private int lightBoxColor;
    private int lightColor;
    private int lightColor2;
    private int lightColor3;
    private int materialDiffusion;
    private int matSpecLoc;
    private int materialShine;
    private int materialEmission;


    public Shader() {

        String vertexShaderString;
        String fragmentShaderString;

        vertexShaderString = Gdx.files.internal("shaders/simple3D.vert").readString();
        fragmentShaderString =  Gdx.files.internal("shaders/simple3D.frag").readString();

        vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
        fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
        Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);

        Gdx.gl.glCompileShader(vertexShaderID);
        Gdx.gl.glCompileShader(fragmentShaderID);

        renderingProgramID = Gdx.gl.glCreateProgram();

        Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
        Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);

        Gdx.gl.glLinkProgram(renderingProgramID);

        positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
        Gdx.gl.glEnableVertexAttribArray(positionLoc);

        normalLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
        Gdx.gl.glEnableVertexAttribArray(normalLoc);

        modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
        viewMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
        projectionMatrixLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");


        globalAmbient           = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_globalAmbient");
        lightPos				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightPos");
        lightPos2			    = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightPos2");
        lightPos3			    = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightPos3");
        boxLight			    = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightBoxPos");
        lightColor		    	= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightColor");
        lightColor2	    		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightColor2");
        lightColor3 			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightColor3");
        lightBoxColor           = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightBoxColor");
        materialDiffusion       = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialDiffuse");
        matSpecLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSpecular");
        materialShine           = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialShininess");
        materialEmission        = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialEmission");

        Gdx.gl.glUseProgram(renderingProgramID);
    }



    public void setGlobalAmbient(float r, float g, float b, float a){

        Gdx.gl.glUniform4f(globalAmbient, r, g, b, a);
    }

    public void setLightPosition(float x, float y, float z, float w){

        Gdx.gl.glUniform4f(lightPos, x, y, z, w);
    }

    public void setLightPosition2(float x, float y, float z, float w){

        Gdx.gl.glUniform4f(lightPos2, x, y, z, w);
    }

    public void setLightPosition3(float x, float y, float z, float w){

        Gdx.gl.glUniform4f(lightPos3, x, y, z, w);
    }

    public void setLightColor(float r, float g, float b, float a){

        Gdx.gl.glUniform4f(lightColor, r, g, b, a);
    }

    public void setLightColor2(float r, float g, float b, float a){

        Gdx.gl.glUniform4f(lightColor2, r, g, b, a);
    }

    public void setLightColor3(float r, float g, float b, float a){

        Gdx.gl.glUniform4f(lightColor3, r, g, b, a);
    }

    public void setLightPositionBoxLight(float x, float y, float z, float w){

        Gdx.gl.glUniform4f(boxLight, x, y, z, w);
    }

    public void setLightBoxColor(float x, float y, float z, float w)
    {
        Gdx.gl.glUniform4f(lightBoxColor, x, y, z, w);
    }

    public void setMaterialDiffuse(float r, float g, float b, float a){

        Gdx.gl.glUniform4f(materialDiffusion, r, g, b, a);
    }

    public void setMaterialSpecular(float r, float g, float b, float a){

        Gdx.gl.glUniform4f(matSpecLoc, r, g, b, a);
    }

    public void setMaterialEmission(float r, float g, float b, float a){

        Gdx.gl.glUniform4f(materialEmission, r, g, b, a);
    }

    public void setShininess(float shine){

        Gdx.gl.glUniform1f(materialShine, shine);
    }

    public int getVertexPointer() {

        return positionLoc;
    }

    public int getNormalPointer() {

        return normalLoc;
    }

    public void setModelMatrix(FloatBuffer matrix) {

        Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, matrix);
    }

    public void setViewMatrix(FloatBuffer matrix) {

        Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrix);
    }

    public void setProjectionMatrix(FloatBuffer matrix) {

        Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrix);
    }
}










