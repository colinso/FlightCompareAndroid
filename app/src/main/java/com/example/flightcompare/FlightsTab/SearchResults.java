package com.example.flightcompare.FlightsTab;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flightcompare.Data.CollectionObjects.Airport;
import com.example.flightcompare.Data.CollectionObjects.Flight;
import com.example.flightcompare.Data.JsonObjects.Trip;
import com.example.flightcompare.Data.Singleton;
import com.example.flightcompare.MainActivity;
import com.example.flightcompare.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchResults extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Trip> resultsList;

    private TextView noSearchResultsText;

    MaterialButton searchAgainButton;

    // data members
    String fromAirport;
    String destAirport;
    String departDate;
    String returnDate;
    Boolean roundTrip;

    public SearchResults() {
    }

    public static SearchResults newInstance(String from, String to, String departDate, String returnDate, Boolean roundTrip) {
        SearchResults fragment = new SearchResults();
        Bundle bundle = new Bundle();
        bundle.putString("fromAirport", from);
        bundle.putString("toAirport", to);
        bundle.putString("departDate", departDate);
        bundle.putString("returnDate", returnDate);
        bundle.putBoolean("roundTrip", roundTrip);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            fromAirport = getArguments().getString("fromAirport");
            destAirport = getArguments().getString("toAirport");
            departDate = getArguments().getString("departDate");
            returnDate = getArguments().getString("returnDate");
            roundTrip = getArguments().getBoolean("roundTrip");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flightresults, container, false);

        searchAgainButton = view.findViewById(R.id.search_again_button);
        searchAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchClicked();
            }
        });
        noSearchResultsText = view.findViewById(R.id.textview_no_search_results);
        mRecyclerView = view.findViewById(R.id.flight_results_recycler_view);
        resultsList = new ArrayList<>();

        resultsList = Singleton.getTripsForSearch(fromAirport, destAirport, departDate, returnDate);
        if(resultsList.size() == 0) {
            noSearchResultsText.setVisibility(View.VISIBLE);
        }
        else {
            noSearchResultsText.setVisibility(View.INVISIBLE);
        }

        mLayoutManager = new LinearLayoutManager(this.getContext());
        ((LinearLayoutManager)mLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyFlightResultRecyclerViewAdapter(resultsList);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void onSearchClicked() {
        searchAgainButton.setEnabled(false);
        // clear search data in model
        Singleton.clearSearchData();

        SearchFlights searchFlights = SearchFlights.newInstance();
        ((MainActivity) Objects.requireNonNull(getActivity())).loadFragment(searchFlights);
    }
}
