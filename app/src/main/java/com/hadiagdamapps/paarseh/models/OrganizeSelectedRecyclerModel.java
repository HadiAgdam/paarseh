package com.hadiagdamapps.paarseh.models;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class OrganizeSelectedRecyclerModel {

    public final int id;
    public final String text;

    public OrganizeSelectedRecyclerModel(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public OrganizeSelectedRecyclerModel(OrganizeRecyclerModel model) {
        this.id = model.id;
        this.text = model.text;
    }

    public abstract void onClick(OrganizeSelectedRecyclerModel self, TextView textView);

}