package com.ru.tgra.shapes;

public class Coin
{
    private float posX;
    private float posY;
    private float posZ;

    private boolean pickedUp;

    private Shader shader;

    public Coin(float posX, float posY, float posZ)
    {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        shader = new Shader();
        pickedUp = false;
    }

    public float getPosX()
    {
        return posX;
    }

    public float getPosZ()
    {
        return posZ;
    }

    public void display()
    {
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(posX, posY, posZ);
        ModelMatrix.main.addScale(2, 2, 2);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube();
        ModelMatrix.main.popMatrix();
    }
}
