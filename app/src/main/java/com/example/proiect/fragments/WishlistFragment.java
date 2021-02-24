package com.example.proiect.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proiect.Attraction;
import com.example.proiect.adapters.AttractionAdapterDetailed;
import com.example.proiect.Database;
import com.example.proiect.Executors;
import com.example.proiect.R;

import java.util.List;

public class WishlistFragment extends Fragment {

    private AttractionAdapterDetailed attractionAdapterDetailed;
    private List<Attraction> attractionWishList;
    private RecyclerView recyclerView;
    private Database database;

    public WishlistFragment(List<Attraction> attractionWishList) {
        this.attractionWishList = attractionWishList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        // recycler view initialization
        recyclerView = view.findViewById(R.id.wishlistRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // adapter initialization
        attractionAdapterDetailed = new AttractionAdapterDetailed(getContext(), attractionWishList);

        recyclerView.setAdapter(attractionAdapterDetailed);

        // local database initialization
        database = Database.getInstance(getContext());

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // on swipe delete from list and from local database
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();
                final List<Attraction> adaptorAttractionList = attractionAdapterDetailed.getAttractionList();
                Executors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.attractionDAO().deleteAttraction(adaptorAttractionList.get(position));
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);

        return view;
    }
}