package com.example.omkar.workout2.Workout;

/*
Body class stores user's weight and height and provides and abstract interface to outside
classes*/
public class Body {
    private int weight;
    private float height;

    //Constructor
    public Body(int w,float h) {
        weight = w;
        height = h;
    }

    //Getters and setters
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

    //Function to check whether given weight and height are in region using neighbour regression.
    public boolean  isInside(int wt,float ht)
    {
        float L2dist=((ht-this.height)*(ht-this.height))+((wt-this.weight)*(ht-this.height));

        if(Math.sqrt(L2dist)<5){
            return true;
        }
        return false;

    }


}
