package com.ru.tgra.shapes;

public class Wall
{
    private float posX;

    private float posY;
    private float posZ;
    private float scaleX;

    private float scaleY;
    private float scaleZ;
    private Shader shader;

    public Wall()
    {
        shader = new Shader();
    }

    public void setWallPos(float posX, float posY, float posZ)
    {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public void setWallScale(float scaleX, float scaleY, float scaleZ)
    {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
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
        shader.setMaterialDiffuse(0.5f, 0.5f, 0.5f, 1.0f);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(posX, posY, posZ);
        ModelMatrix.main.addScale(scaleX, scaleY, scaleZ);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube();
        ModelMatrix.main.popMatrix();
    }


}
