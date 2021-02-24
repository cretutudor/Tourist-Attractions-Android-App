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

import java.util.List;

public class AttractionAdapterDetailed extends RecyclerView.Adapter<AttractionAdapterDetailed.ViewHolder> {

    private Context context;
    private List<Attraction> attractionList;

    public AttractionAdapterDetailed(Context context, List<Attraction> attractionList) {
        this.context = context;
        this.attractionList = attractionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_card_item_detailed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionAdapterDetailed.ViewHolder viewHolder, int position) {
        viewHolder.name.setText(attractionList.get(position).getName());
        viewHolder.type.setText(attractionList.get(position).getType());
        viewHolder.county.setText(attractionList.get(position).getCounty());
        viewHolder.city.setText(attractionList.get(position).getCity());
        viewHolder.description.setText(attractionList.get(position).getDescription());
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
        TextView name, type, county, city, description;

        ViewHolder(@NonNull final View view) {
            super(view);
            name = view.findViewById(R.id.textView_attractionName_card_detailed);
            type = view.findViewById(R.id.textView_attractionType_card_detailed);
            county = view.findViewById(R.id.textView_attractionCounty_card_detailed);
            city = view.findViewById(R.id.textView_attractionCity_card_detailed);
            description = view.findViewById(R.id.textView_attractionDescription_card_detailed);
        }
    }
}