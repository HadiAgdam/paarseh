package com.hadiagdamapps.paarseh.models;

public class StepRecyclerModel {
    public final int id;
    public final String name;
    public final String description;
    public final boolean locked;
    public final int progress;


    public StepRecyclerModel(int id, String name, String description, boolean locked, int progress) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.locked = locked;
        this.progress = progress;
    }


}
