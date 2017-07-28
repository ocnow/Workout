package com.example.omkar.workout2.Workout;

/**
 * Created by Omkar on 6/10/2017.
 */

public class Workout
{
    int pushups;
    int situps;
    int reps;

    public Workout(int pushups,int situps,int reps)
    {
        this.pushups=pushups;
        this.situps=situps;
        this.reps=reps;
    }

    public int getpushups()
    {
        return pushups;
    }

    public int getsitups()
    {
        return situps;
    }

    public int getreps()
    {
        return reps;
    }

    public void setpushups(int push)
    {
        pushups=push;
    }

    public void setsitups(int sit)
    {
        situps=sit;
    }

    public void setreps(int reps)
    {
        this.reps=reps;
    }


}