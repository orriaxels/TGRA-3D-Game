package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;

public class Shader {

    private int renderingProgramID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private int positionLoc;
    private int normalLoc;

    private int modelMatrixLoc;
    private int viewMatrixLoc;
    private int projectionMatrixLoc;

    private int colorLoc;

    public Shader()
    {
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
        projectionMatrixLoc	= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

        colorLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_color");

        Gdx.gl.glUseProgram(renderingProgramID);
    }

    public void setColor(float red, float green, float blue, float alpha)
    {
        Gdx.gl.glUniform4f(colorLoc, red, green, blue, alpha);
    }

    public int getVertexPointer()
    {
        return positionLoc;
    }

    public int getNormalPointer()
    {
        return normalLoc;
    }

    public void setModelMatrix(FloatBuffer matrix)
    {
        Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, matrix);
    }

    public void setViewMatrix(FloatBuffer matrix)
    {
        Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrix);
    }

    public void setProjectionMatrix(FloatBuffer matrix)
    {
        Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrix);
    }
}
