package com.example.proiect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proiect.Attraction;
import com.example.proiect.R;
import com.example.proiect.Database;

import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder> {

    private Context context;
    private List<Attraction> attractionList;

    public AttractionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionAdapter.ViewHolder viewHolder, int position) {
        viewHolder.name.setText(attractionList.get(position).getName());
        viewHolder.type.setText(attractionList.get(position).getType());
        viewHolder.county.setText(attractionList.get(position).getCounty());
        viewHolder.city.setText(attractionList.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        if (attractionList != null) {
            return  attractionList.size();
        }
        else {
            return 0;
        }
    }

    public List<Attraction> getAttractionList() {
        return attractionList;
    }

    public void setAttractionList(List<Attraction> attractionList) {
        this.attractionList = attractionList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, type, county, city;
        Database database;

        ViewHolder(@NonNull final View view) {
            super(view);
            database = Database.getInstance(context);
            name = view.findViewById(R.id.textView_attractionName_card);
            type = view.findViewById(R.id.textView_attractionType_card);
            county = view.findViewById(R.id.textView_attractionCounty_card);
            city = view.findViewById(R.id.textView_attractionCity_card);
        }
    }
}