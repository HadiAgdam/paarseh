package com.hadiagdamapps.paarseh.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hadiagdamapps.paarseh.models.OrganizeRecyclerModel;

import java.util.ArrayList;

public class OrganizeRecyclerAdapter extends RecyclerView.Adapter<OrganizeRecyclerAdapter.Holder> {

    public final ArrayList<OrganizeRecyclerModel> list;
    final Context self;

    public OrganizeRecyclerAdapter(Context self, ArrayList<OrganizeRecyclerModel> list) {
        this.self = self;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(new ToggleButton(self));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        OrganizeRecyclerModel model = list.get(position);
        holder.toggle.setTextOn(model.text);
        holder.toggle.setTextOff(model.text);
        holder.toggle.setText(model.text);
        holder.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.onClick(model, holder.toggle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        final ToggleButton toggle;

        public Holder(@NonNull ToggleButton itemView) {
            super(itemView);
            toggle = itemView;
        }
    }

}
