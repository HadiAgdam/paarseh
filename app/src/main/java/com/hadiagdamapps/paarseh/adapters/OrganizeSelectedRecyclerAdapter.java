package com.hadiagdamapps.paarseh.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.models.OrganizeSelectedRecyclerModel;

import java.util.ArrayList;

public class OrganizeSelectedRecyclerAdapter extends RecyclerView.Adapter<OrganizeSelectedRecyclerAdapter.Holder> {

    public final ArrayList<OrganizeSelectedRecyclerModel> list;
    final Context self;

    public OrganizeSelectedRecyclerAdapter(ArrayList<OrganizeSelectedRecyclerModel> list, Context self) {
        this.list = list;
        this.self = self;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(new TextView(self));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        OrganizeSelectedRecyclerModel model = list.get(position);
        holder.text.setText(model.text);
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.onClick(model, holder.text);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public final TextView text;

        public Holder(@NonNull TextView itemView) {
            super(itemView);
            text = itemView;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 20, 0);
            text.setLayoutParams(params);
            text.setTextSize(20);
            text.setTextColor(self.getResources().getColor(R.color.black));

        }
    }

}
