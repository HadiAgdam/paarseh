package com.hadiagdamapps.paarseh.models;

import android.widget.ToggleButton;

public abstract class OrganizeRecyclerModel {

    public final int id;
    public final String text;

    public OrganizeRecyclerModel(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public abstract void onClick(OrganizeRecyclerModel self, ToggleButton toggle);

}
