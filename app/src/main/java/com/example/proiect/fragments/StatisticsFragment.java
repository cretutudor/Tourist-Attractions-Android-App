package com.example.proiect.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proiect.Attraction;
import com.example.proiect.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsFragment extends Fragment {

    private String selectedLanguage;
    private BarChart barChart;
    private List<Attraction> attractionList;
    private List<Attraction> attractionWishList;
    private List<BarEntry> barEntryList;
    private List<String> labelList;
    private TextView textViewAttractionsNumber, textViewAttractionsWishlistNumber;

    public StatisticsFragment(String selectedLanguage, List<Attraction> attractionList, List<Attraction> attractionWishList) {
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
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        barChart = view.findViewById(R.id.barChart);
        textViewAttractionsNumber = view.findViewById(R.id.textView_attractionsNumber);
        textViewAttractionsWishlistNumber = view.findViewById(R.id.textView_attractionsWishlistNumber);

        String string;
        if (selectedLanguage.equals("eng")) {
            string = getResources().getString(R.string.attractions_number_eng) + " " + attractionList.size();
            textViewAttractionsNumber.setText(string);
            string = getResources().getString(R.string.attractions_wishlist_number_eng) + " " + attractionWishList.size();
            textViewAttractionsWishlistNumber.setText(string);
        }
        else if (selectedLanguage.equals("ro")) {
            string = getResources().getString(R.string.attractions_number_ro) + " " + attractionList.size();
            textViewAttractionsNumber.setText(string);
            string = getResources().getString(R.string.attractions_wishlist_number_ro) + " " + attractionWishList.size();
            textViewAttractionsWishlistNumber.setText(string);
        }

        barEntryList = new ArrayList<>();
        labelList = new ArrayList<>();

        // map
        Map<String, Integer> attractionMap = getMap(attractionList);

        int i = 0;
        // add the map values to the chart
        for (Map.Entry<String, Integer> entry : attractionMap.entrySet()) {
            labelList.add(entry.getKey());
            barEntryList.add(new BarEntry(i, entry.getValue()));
            i++;
        }

        Description description = new Description();
        String label = "";
        if (selectedLanguage.equals("eng")) {
            description.setText("Counties");
            label = "Counties";
        }
        else if (selectedLanguage.equals("ro")) {
            description.setText("Judeţe");
            label = "Judeţe";
        }
        BarDataSet barDataSet = new BarDataSet(barEntryList, label);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.setDescription(description);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelList));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelList.size());
        xAxis.setLabelRotationAngle(270);

        barChart.animateY(2000);
        barChart.invalidate();

        return view;
    }

    // function that creates a map of County, numberOfAttractions pairs
    private Map<String, Integer> getMap(List<Attraction> attractionList) {
        if (attractionList == null || attractionList.isEmpty()) {
            return null;
        }
        Map<String, Integer> map = new HashMap<>();

        for (Attraction attraction : attractionList) {
            if (map.containsKey(attraction.getCounty())) {
                Integer currentValue = map.get(attraction.getCounty());
                Integer newValue = (currentValue != null ? currentValue : 0) + 1;
                map.put(attraction.getCounty(), newValue);
            }
            else {
                map.put(attraction.getCounty(), 1);
            }
        }

        return map;
    }

}