package com.hadiagdamapps.paarseh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.models.LevelRecyclerModel;

import java.util.ArrayList;

public class LevelRecyclerAdapter extends RecyclerView.Adapter<LevelRecyclerAdapter.Holder> {

    private final Context self;
    private final ArrayList<LevelRecyclerModel> list;

    public LevelRecyclerAdapter(Context self, ArrayList<LevelRecyclerModel> list) {
        this.self = self;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(self).inflate(R.layout.level_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LevelRecyclerModel item = list.get(position);
        holder.levelText.setText(String.valueOf(position + 1));
        holder.nameText.setText(item.name);
        holder.self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.recycler.setVisibility(holder.recycler.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
        holder.recycler.setLayoutManager(new LinearLayoutManager(self, RecyclerView.VERTICAL, false));
        holder.recycler.setAdapter(item.adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView levelText;
        TextView nameText;
        RecyclerView recycler;
        View self;

        public Holder(@NonNull View itemView) {
            super(itemView);
            levelText = itemView.findViewById(R.id.levelText);
            nameText = itemView.findViewById(R.id.nameText);
            recycler = itemView.findViewById(R.id.stepRecycler);
            self = itemView;
        }
    }

}
