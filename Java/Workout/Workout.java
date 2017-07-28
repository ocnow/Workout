package com.example.omkar.workout2.Workout;

/*Workout class stores users pushups,situps and repitions*/

public class Workout
{
    private int pushups;
    private int situps;
    private int reps;

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
