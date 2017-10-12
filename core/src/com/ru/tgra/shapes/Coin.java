package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;

public class Coin
{
    private float posX;
    private float posY;
    private float posZ;

    private float rotation;

    private Shader shader;
    public boolean boxMax;

    public Coin(float posX, float posY, float posZ)
    {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        shader = new Shader();
        boxMax = false;
        this.rotation = 0.0f;
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

        this.rotation += 90* Gdx.graphics.getDeltaTime();

    }

    public void display()
    {
        // BoxLight
        //shader.setLightPositionBoxLight(posX, posY,posZ, 1.0f);
        //shader.setLightColor(1.0f, 0.0f, 0.0f, 1.0f);

        shader.setMaterialDiffuse(1.0f, 0.0f, 0.0f, 1);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(posX, posY, posZ);
        ModelMatrix.main.addScale(1, 1, 1);
        ModelMatrix.main.addRotationZ(rotation);
        ModelMatrix.main.addRotationX(rotation);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube();
        ModelMatrix.main.popMatrix();
    }
}
