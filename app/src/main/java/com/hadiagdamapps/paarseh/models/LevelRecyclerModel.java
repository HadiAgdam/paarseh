package com.hadiagdamapps.paarseh.models;

import androidx.recyclerview.widget.RecyclerView;

public class LevelRecyclerModel {

    public final int id;
    public final String name;
    public final RecyclerView.Adapter adapter;


    public LevelRecyclerModel(int id, String name, RecyclerView.Adapter adapter) {
        this.id = id;
        this.name = name;
        this.adapter = adapter;
    }

}
