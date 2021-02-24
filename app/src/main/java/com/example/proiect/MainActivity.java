package com.example.proiect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.proiect.fragments.AddAttractionFragment;
import com.example.proiect.fragments.EditAttractionFragment;
import com.example.proiect.fragments.HomeMapsFragment;
import com.example.proiect.fragments.StatisticsFragment;
import com.example.proiect.fragments.WishlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Attraction> attractionList;
    private List<Attraction> attractionWishList;
    private Database database;
    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment;
    private String selectedLanguage = "eng";
    private DatabaseReference databaseReference;
    private static final String MAIN_PURPOSE = "add";
    private static final String FIREBASE_PATH = "obiective-turistice-romania";
    private static final String SHARED_PREFERENCES_NAME = "sharedPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize arrays
        attractionList = new ArrayList<>();
        attractionWishList = new ArrayList<>();

        // initialize databases
        database = Database.getInstance(getApplicationContext());
        databaseReference = FirebaseDatabase.getInstance().getReference(FIREBASE_PATH);

        // get data from the 2 databases
        retrieveFirebaseList();
        retrieveList();

        saveSharedPreferences();

        // bottom navigation view
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeMapsFragment(attractionList)).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectedFragment = null;

            switch(item.getItemId()){
                case R.id.homeMapsFragment:
                    retrieveFirebaseList();
                    selectedFragment = new HomeMapsFragment(attractionList);
                    break;
                case R.id.addAttractionFragment:
                    retrieveFirebaseList();
                    selectedFragment = new AddAttractionFragment(selectedLanguage, MAIN_PURPOSE, attractionList);
                    break;
                case R.id.editAttractionFragment:
                    retrieveFirebaseList();
                    retrieveList();
                    selectedFragment = new EditAttractionFragment(selectedLanguage, attractionList, attractionWishList);
                    break;
                case R.id.wishlistFragment:
                    retrieveFirebaseList();
                    retrieveList();
                    selectedFragment = new WishlistFragment(attractionWishList);
                    break;
                case R.id.statisticsFragment:
                    retrieveFirebaseList();
                    retrieveList();
                    selectedFragment = new StatisticsFragment(selectedLanguage, attractionList, attractionWishList);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();

            return true;
        }
    };

    // top-right options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    // selecting items in the top-right menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int itemId = item.getItemId();

        switch(itemId){
            case R.id.romanian_language:
                selectedLanguage = "ro";
                translate("ro");
                break;
            case R.id.english_language:
                selectedLanguage = "eng";
                translate("eng");
                break;
        }
        return true;
    }

    // function for translation
    private void translate(String languageExtension){
        switch (languageExtension) {
            case ("ro"):
                setTitle(R.string.app_name_ro);
                bottomNavigationView.getMenu().findItem(R.id.homeMapsFragment).setTitle(getResources().getString(R.string.nav_map_title_ro));
                bottomNavigationView.getMenu().findItem(R.id.addAttractionFragment).setTitle(getResources().getString(R.string.nav_addAttraction_title_ro));
                bottomNavigationView.getMenu().findItem(R.id.editAttractionFragment).setTitle(getResources().getString(R.string.nav_editAttraction_title_ro));
                bottomNavigationView.getMenu().findItem(R.id.wishlistFragment).setTitle(getResources().getString(R.string.nav_wishlist_title_ro));
                bottomNavigationView.getMenu().findItem(R.id.statisticsFragment).setTitle(getResources().getString(R.string.nav_statisticsFragment_title_ro));
                break;
            case ("eng"):
                setTitle(R.string.app_name_eng);
                bottomNavigationView.getMenu().findItem(R.id.homeMapsFragment).setTitle(getResources().getString(R.string.nav_map_title_eng));
                bottomNavigationView.getMenu().findItem(R.id.addAttractionFragment).setTitle(getResources().getString(R.string.nav_addAttraction_title_eng));
                bottomNavigationView.getMenu().findItem(R.id.editAttractionFragment).setTitle(getResources().getString(R.string.nav_editAttraction_title_eng));
                bottomNavigationView.getMenu().findItem(R.id.wishlistFragment).setTitle(getResources().getString(R.string.nav_wishlist_title_eng));
                bottomNavigationView.getMenu().findItem(R.id.statisticsFragment).setTitle(getResources().getString(R.string.nav_statisticsFragment_title_eng));
                break;
        }
    }

    private void retrieveFirebaseList() {
        // retrieve data from firebase, add it to the list and populate the recycler view
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attractionList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Attraction attraction = dataSnapshot.getValue(Attraction.class);
                    attractionList.add(attraction);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrieveList() {
        Executors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Attraction> retrievedAttractionList = database.attractionDAO().loadAttractions();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        attractionWishList = retrievedAttractionList;
                    }
                });
            }
        });
    }

    private void saveSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", "Test name");
        editor.putFloat("latitude", 45.9432f);
        editor.putFloat("longitude", 24.9668f);
        editor.putString("description", "Test description");
        editor.commit();
    }
}