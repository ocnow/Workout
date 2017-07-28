package com.example.omkar.workout2.Workout;

/**
 * Created by Omkar on 6/10/2017.
 */

public class Body {
    int weight;
    float height;


    public Body(int w,float h) {
        weight = w;
        height = h;
    }

    public int getWeight()
    {
        return weight;
    }

    public float getHeight()
    {
        return height;
    }

    public void setWeight(int w)
    {
        weight=w;
    }

    public void setHeight(float h)
    {
        height=h;
    }


    public boolean  isInside(int wt,float ht)
    {
        float L2dist=((ht-this.height)*(ht-this.height))+((wt-this.weight)*(ht-this.height));

        if(Math.sqrt(L2dist)<5){
            return true;
        }
        return false;

    }


}