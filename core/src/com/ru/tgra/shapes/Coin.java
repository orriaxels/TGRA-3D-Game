package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

public class Coin
{
    private float posX;
    private float posY;
    private float posZ;

    private Shader shader;
    public boolean boxMax;

    public Coin(float posX, float posY, float posZ)
    {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        shader = new Shader();
        boxMax = false;
    }

    public float getPosX()
    {
        return posX;
    }

    public float getPosZ()
    {
        return posZ;
    }

    public void update(float deltatime)
    {

        if(posY >= 3)
        {
            boxMax = true;
        }
        else if(posY <= 1)
        {
            boxMax = false;
        }

        if(boxMax)
        {
            posY -= 0.5 * deltatime;
        }
        if(!boxMax)
        {
            posY += 0.5 * deltatime;
        }


    }

    public void display()
    {
        shader.setMaterialDiffuse(1.0f, 0.0f, 0.0f, 1);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(posX, posY, posZ);
        ModelMatrix.main.addScale(1, 1, 1);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube();
        ModelMatrix.main.popMatrix();
    }
}
