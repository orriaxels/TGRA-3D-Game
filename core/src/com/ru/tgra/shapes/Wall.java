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

    private boolean addedToList;

    public Wall()
    {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        shader = new Shader();
        this.addedToList = false;
    }

    public void setAdded(boolean added)
    {
        this.addedToList = added;
    }

    public boolean getAdded()
    {
        return this.addedToList;
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

    public void setPosX(float posX)
    {
        this.posX = posX;
    }

    public float getPosY()
    {
        return posY;
    }

    public void setPosY(float posY)
    {
        this.posY = posY;
    }

    public float getPosZ()
    {
        return posZ;
    }

    public void setPosZ(float posZ)
    {
        this.posZ = posZ;
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
