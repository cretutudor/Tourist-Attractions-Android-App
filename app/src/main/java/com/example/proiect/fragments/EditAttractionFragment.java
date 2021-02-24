package com.example.proiect.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proiect.Attraction;
import com.example.proiect.adapters.AttractionAdapter;
import com.example.proiect.Database;
import com.example.proiect.Executors;
import com.example.proiect.R;
import com.example.proiect.adapters.RecyclerviewItemClick;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EditAttractionFragment extends Fragment {

    private String selectedLanguage = "eng";
    private AttractionAdapter attractionAdapter;
    private List<Attraction> attractionList;
    private List<Attraction> attractionWishList;
    private Database database;
    private DatabaseReference databaseFirebaseReference;
    private RecyclerView recyclerView;
    private static final String FIREBASE_PATH = "obiective-turistice-romania";

    public EditAttractionFragment(String selectedLanguage, List<Attraction> attractionList, List<Attraction> attractionWishList) {
        this.selectedLanguage = selectedLanguage;
        this.attractionList = attractionList;
        this.attractionWishList = attractionWishList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_attraction, container, false);

        // recycler view initialization
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // adapter initialization
        attractionAdapter = new AttractionAdapter(getContext());
        attractionAdapter.setAttractionList(attractionList);
        recyclerView.setAdapter(attractionAdapter);

        // database initialization
        database = Database.getInstance(getContext());

        // firebase initialization
        databaseFirebaseReference = FirebaseDatabase.getInstance().getReference(FIREBASE_PATH);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int position = viewHolder.getAdapterPosition();
                List<Attraction> adaptorAttractionList = attractionAdapter.getAttractionList();
                String swipedAttractionName = adaptorAttractionList.get(position).getName();
                databaseFirebaseReference.child(swipedAttractionName).removeValue();

                if (selectedLanguage.equals("eng")) {
                    Toast.makeText(getActivity(), R.string.deleted_attraction_toast_eng, Toast.LENGTH_LONG).show();
                }
                else if (selectedLanguage.equals("ro")) {
                    Toast.makeText(getActivity(), R.string.deleted_attraction_toast_ro, Toast.LENGTH_LONG).show();
                }
            }
        }).attachToRecyclerView(recyclerView);

        // recycler view on item click
        RecyclerviewItemClick.addTo(recyclerView).setOnItemClickListener(new RecyclerviewItemClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Attraction clickedAttraction = attractionAdapter.getAttractionList().get(position);
                getActivity().getSupportFragmentManager()
                             .beginTransaction()
                             .replace(R.id.fragment, new AddAttractionFragment(selectedLanguage, "edit", clickedAttraction))
                             .commit();
            }
        });

        // long click to add to wishlist
        RecyclerviewItemClick.addTo(recyclerView).setOnItemLongClickListener(new RecyclerviewItemClick.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                attractionList = attractionAdapter.getAttractionList();
                boolean add = true;
                for (int i = 0; i < attractionWishList.size(); i++) {
                    if (attractionList.get(position).getName().equals(attractionWishList.get(i).getName())) {
                        add = false;
                        break;
                    }
                }
                if (!add) {
                    if (selectedLanguage.equals("eng")) {
                        Toast.makeText(getActivity(), R.string.already_added_toast_eng, Toast.LENGTH_LONG).show();
                    }
                    else if (selectedLanguage.equals("ro")) {
                        Toast.makeText(getActivity(), R.string.already_added_toast_ro, Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    final Attraction attraction = new Attraction(
                            attractionList.get(position).getName(),
                            attractionList.get(position).getCounty(),
                            attractionList.get(position).getCity(),
                            attractionList.get(position).getLatitude(),
                            attractionList.get(position).getLongitude(),
                            attractionList.get(position).getType(),
                            attractionList.get(position).getDescription());
                    Executors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.attractionDAO().insertAttraction(attraction);
                        }
                    });
                    if (selectedLanguage.equals("eng")) {
                        Toast.makeText(getActivity(), R.string.added_attractionWishlist_toast_eng, Toast.LENGTH_LONG).show();
                    }
                    else if (selectedLanguage.equals("ro")) {
                        Toast.makeText(getActivity(), R.string.added_attractionWishlist_toast_ro, Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            }
        });

        return view;
    }
}