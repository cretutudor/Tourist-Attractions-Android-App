package com.example.proiect.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiect.Attraction;
import com.example.proiect.County;
import com.example.proiect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

public class AddAttractionFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    // variable that holds "add" or "edit", depending on where the fragment is used
    private String purpose;

    private String selectedLanguage;
    private Attraction attractionReference;
    private List<Attraction> attractionList;
    private static List<County> countyList;
    private static List<String> cityList;
    private Spinner countySpinner, citySpinner;
    private EditText editTextName, editTextLat, editTextLong, editTextDescription;
    private TextView textViewCounty, textViewCity;
    private FloatingActionButton floatingActionButton;
    private RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7;
    private String selectedRadioButton;
    private ArrayAdapter<County> adapterCountySpinner;
    private ArrayAdapter<String> adapterCitySpinner;
    private DatabaseReference databaseFirebaseReference;
    private static final float MAX_LATITUDE = 48.1500f;
    private static final float MIN_LATITUDE = 43.4000f;
    private static final float MAX_LONGITUDE = 29.4000f;
    private static final float MIN_LONGITUDE = 20.1900f;
    private static final String FIREBASE_PATH = "obiective-turistice-romania";
    private static final String SHARED_PREFERENCES_NAME = "sharedPreferences";


    public AddAttractionFragment(String selectedLanguage, String purpose) {
        this.selectedLanguage = selectedLanguage;
        this.purpose = purpose;
    }

    public AddAttractionFragment(String selectedLanguage, String purpose, List<Attraction> attractionList) {
        this.selectedLanguage = selectedLanguage;
        this.purpose = purpose;
        this.attractionList = attractionList;
    }

    public AddAttractionFragment(String selectedLanguage, String purpose, Attraction attraction) {
        this.selectedLanguage = selectedLanguage;
        this.purpose = purpose;
        this.attractionReference = attraction;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // json
        countyList = new ArrayList<>();
        cityList = new ArrayList<>();
        new JSONTasks().execute();

        // database
        databaseFirebaseReference = FirebaseDatabase.getInstance().getReference(FIREBASE_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_attraction, container, false);

        // county spinner
        countySpinner = view.findViewById(R.id.county_spinner);
        countySpinner.setOnItemSelectedListener(this);

        // city spinner
        citySpinner = view.findViewById(R.id.city_spinner);
        citySpinner.setOnItemSelectedListener(this);

        // form fields
        editTextName = view.findViewById(R.id.editText_name);
        textViewCounty = view.findViewById(R.id.textView_county);
        textViewCity = view.findViewById(R.id.textView_city);
        editTextLat = view.findViewById(R.id.editText_lat);
        editTextLong = view.findViewById(R.id.editText_long);
        editTextDescription = view.findViewById(R.id.editText_description);

        // radio buttons
        rb1 = view.findViewById(R.id.rb1);
        rb2 = view.findViewById(R.id.rb2);
        rb3 = view.findViewById(R.id.rb3);
        rb4 = view.findViewById(R.id.rb4);
        rb5 = view.findViewById(R.id.rb5);
        rb6 = view.findViewById(R.id.rb6);
        rb7 = view.findViewById(R.id.rb7);

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedRadioButton(v);
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedRadioButton(v);
            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedRadioButton(v);
            }
        });

        rb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedRadioButton(v);
            }
        });

        rb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedRadioButton(v);
            }
        });

        rb6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedRadioButton(v);
            }
        });

        rb7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedRadioButton(v);
            }
        });

        // translation with selectedLanguage
        switch (selectedLanguage) {
            case ("ro"):
                editTextName.setHint(R.string.add_attraction_fragment_name_hint_ro);
                textViewCounty.setText(R.string.add_attraction_fragment_county_ro);
                textViewCity.setText(R.string.add_attraction_fragment_city_ro);
                editTextLat.setHint(R.string.add_attraction_fragment_lat_hint_ro);
                editTextLong.setHint(R.string.add_attraction_fragment_long_hint_ro);
                editTextDescription.setHint(R.string.add_attraction_fragment_description_hint_ro);
                rb1.setText(R.string.add_attraction_fragment_rb1_ro);
                rb2.setText(R.string.add_attraction_fragment_rb2_ro);
                rb3.setText(R.string.add_attraction_fragment_rb3_ro);
                rb4.setText(R.string.add_attraction_fragment_rb4_ro);
                rb5.setText(R.string.add_attraction_fragment_rb5_ro);
                rb6.setText(R.string.add_attraction_fragment_rb6_ro);
                rb7.setText(R.string.add_attraction_fragment_rb7_ro);
                break;
            case ("eng"):
                editTextName.setHint(R.string.add_attraction_fragment_name_hint_eng);
                textViewCounty.setText(R.string.add_attraction_fragment_county_eng);
                textViewCity.setText(R.string.add_attraction_fragment_city_eng);
                editTextLat.setHint(R.string.add_attraction_fragment_lat_hint_eng);
                editTextLong.setHint(R.string.add_attraction_fragment_long_hint_eng);
                editTextDescription.setHint(R.string.add_attraction_fragment_description_hint_eng);
                rb1.setText(R.string.add_attraction_fragment_rb1_eng);
                rb2.setText(R.string.add_attraction_fragment_rb2_eng);
                rb3.setText(R.string.add_attraction_fragment_rb3_eng);
                rb4.setText(R.string.add_attraction_fragment_rb4_eng);
                rb5.setText(R.string.add_attraction_fragment_rb5_eng);
                rb6.setText(R.string.add_attraction_fragment_rb6_eng);
                rb7.setText(R.string.add_attraction_fragment_rb7_eng);
                break;
        }

        // floating button
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        // if we're adding an attraction set the onClickListener
        // else delete the floating button and add the save button, used for editing
        if (purpose.equals("add")) {
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        checkForm();
                        final Attraction attraction = new Attraction(
                                editTextName.getText().toString(),
                                countySpinner.getSelectedItem().toString(),
                                citySpinner.getSelectedItem().toString(),
                                Float.parseFloat(editTextLat.getText().toString()),
                                Float.parseFloat(editTextLong.getText().toString()),
                                selectedRadioButton,
                                editTextDescription.getText().toString()
                        );
                        boolean add = true;
                        for (int i = 0; i < attractionList.size(); i++) {
                            if (attraction.getName().equals(attractionList.get(i).getName())) {
                                add = false;
                            }
                        }
                        if (add) {
                            databaseFirebaseReference.child(attraction.getName()).setValue(attraction);
                        }
                        else {
                            if (selectedLanguage.equals("eng")) {
                                throw new Exception("Already registered!");
                            }
                            else if (selectedLanguage.equals("ro")) {
                                throw new Exception("Deja înregistrat!");
                            }
                        }

                        if (selectedLanguage.equals("ro")) {
                            Toast.makeText(getActivity(), R.string.added_attraction_toast_ro, Toast.LENGTH_LONG).show();
                        }
                        else if (selectedLanguage.equals("eng")) {
                            Toast.makeText(getActivity(), R.string.added_attraction_toast_eng, Toast.LENGTH_LONG).show();
                        }

                        // clearing text fields
                        editTextName.setText("");
                        editTextLat.setText("");
                        editTextLong.setText("");
                        editTextDescription.setText("");
                    }
                    catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            // get a test form on long button click
            floatingActionButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                    editTextName.setText(sharedPreferences.getString("name", ""));
                    editTextLat.setText(String.valueOf(sharedPreferences.getFloat("latitude", 0)));
                    editTextLong.setText(String.valueOf(sharedPreferences.getFloat("longitude", 0)));
                    editTextDescription.setText(sharedPreferences.getString("description", ""));
                    return true;
                }
            });
        }
        else if (purpose.equals("edit")) {
            // complete the fields to be edited
            editTextName.setText(attractionReference.getName());
            editTextLat.setText(String.valueOf(attractionReference.getLatitude()));
            editTextLong.setText(String.valueOf(attractionReference.getLongitude()));
            editTextDescription.setText(attractionReference.getDescription());

            // on click for save
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        checkForm();
                        Attraction updatedAttraction = new Attraction(
                                editTextName.getText().toString(),
                                countySpinner.getSelectedItem().toString(),
                                citySpinner.getSelectedItem().toString(),
                                Float.parseFloat(editTextLat.getText().toString()),
                                Float.parseFloat(editTextLong.getText().toString()),
                                selectedRadioButton,
                                editTextDescription.getText().toString()
                        );

                        // delete old one and add the updated version
                        databaseFirebaseReference.child(attractionReference.getName()).removeValue();
                        databaseFirebaseReference.child(updatedAttraction.getName()).setValue(updatedAttraction);

                        if (selectedLanguage.equals("ro")) {
                            Toast.makeText(getActivity(), R.string.modified_attraction_toast_ro, Toast.LENGTH_LONG).show();
                        }
                        else if (selectedLanguage.equals("eng")) {
                            Toast.makeText(getActivity(), R.string.modified_attraction_toast_eng, Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    getActivity().getFragmentManager().popBackStack();
                }
            });
        }
        return view;
    }

    // class used for pulling the JSON file, parsing it and populating the county spinner
    public class JSONTasks extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            countyList.clear();
            String result = null;

            try {
                URL url = new URL("https://raw.githubusercontent.com/cretutudor/RomanianCountiesJSON/main/RomanianCounties.json");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = bufferedReader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }
                else {
                    result = "Error";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject wholeJson = new JSONObject(s);
                JSONArray countiesArray = wholeJson.getJSONArray("counties");

                for(int i = 0; i < countiesArray.length(); i++){
                    JSONObject countyJson = countiesArray.getJSONObject(i);
                    String id = countyJson.getString("id");
                    String name = countyJson.getString("name");
                    String capitalCity;

                    // Bucharest has a different format so we check for id
                    if(id.equals("B")){
                        capitalCity = "";
                    }
                    else {
                        capitalCity = countyJson.getString("capitalCity");
                    }

                    String region = countyJson.getString("region");
                    int population;
                    float area;

                    try {
                        population = Integer.parseInt(countyJson.getString("region"));
                        area = Float.parseFloat(countyJson.getString("region"));
                    }
                    catch(NumberFormatException e) {
                        population = 0;
                        area = 0;
                    }

                    List<String> cities = new ArrayList<>();
                    JSONArray citiesArray;

                    // same goes here
                    if(id.equals("B")) {
                        citiesArray = countyJson.getJSONArray("sectors");
                    }
                    else {
                        citiesArray = countyJson.getJSONArray("cities");
                    }

                    for(int j = 0; j < citiesArray.length(); j++) {
                        JSONObject cityJson = citiesArray.getJSONObject(j);
                        String city = cityJson.getString("name");
                        cities.add(city);
                    }

                    County county = new County(id, name, capitalCity, region, population, area, cities);
                    countyList.add(county);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            // after getting through whole json we can populate the spinners
            // county spinner
            adapterCountySpinner = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countyList);
            adapterCountySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countySpinner.setAdapter(adapterCountySpinner);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        // check if the selected item is from the county spinner so we can populate the city spinner
        if (spinner.getId() == R.id.county_spinner) {
            County selectedCounty = (County) parent.getSelectedItem();
            cityList = selectedCounty.getCities();
            adapterCitySpinner = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityList);
            adapterCitySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            citySpinner.setAdapter(adapterCitySpinner);
        }
        else {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onItemSelectedRadioButton(View view) {
        selectedRadioButton = ((RadioButton) view).getText().toString();
    }

    private void checkForm() throws Exception {
        if (editTextName.getText().toString().equals("") ||
                editTextLat.getText().toString().equals("") ||
                editTextLong.getText().toString().equals("") ||
                editTextDescription.getText().toString().equals("") ||
                selectedRadioButton == null) {
            if (selectedLanguage.equals("eng")) {
                throw new Exception("All fields must be filled!");
            }
            else if (selectedLanguage.equals("ro")) {
                throw new Exception("Toate câmpurile trebuie completate!");
            }
        }
        if (Float.parseFloat(editTextLat.getText().toString()) > MAX_LATITUDE ||
                Float.parseFloat(editTextLat.getText().toString()) < MIN_LATITUDE ||
                Float.parseFloat(editTextLong.getText().toString()) > MAX_LONGITUDE ||
                Float.parseFloat(editTextLong.getText().toString()) < MIN_LONGITUDE) {
            if (selectedLanguage.equals("eng")) {
                throw new Exception("Coordinates are not on romanian territory!");
            }
            else if (selectedLanguage.equals("ro")) {
                throw new Exception("Coordonatele nu se afla pe teritoriul României!");
            }
        }
    }
}