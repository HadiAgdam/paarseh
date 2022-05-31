package com.hadiagdamapps.paarseh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hadiagdamapps.paarseh.R;

import java.util.ArrayList;

public class TextRecyclerAdapter extends RecyclerView.Adapter<TextRecyclerAdapter.Holder> {

    final ArrayList<String> list;
    final String[] list2;
    final Context self;
    public ArrayList<TextView> items = new ArrayList<>();

    public TextRecyclerAdapter(ArrayList<String> list, Context self) {
        this.self = self;
        this.list = list;
        this.list2 = null;
    }

    public TextRecyclerAdapter(String[] list, Context self) {
        this.self = self;
        this.list = null;
        this.list2 = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(self).inflate(R.layout.text_recycler_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        items.add(holder.textView);
        String text;
        if (list == null) {
            text = list2[position];
        } else text = list.get(position);
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        if (list == null) return list2.length;
        return list.size();
    }

    public TextView getItem(int index) {
        return items.get(index);
    }

    public ArrayList<TextView> getItems() {
        return items;
    }


    class Holder extends RecyclerView.ViewHolder {
        TextView textView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

}
