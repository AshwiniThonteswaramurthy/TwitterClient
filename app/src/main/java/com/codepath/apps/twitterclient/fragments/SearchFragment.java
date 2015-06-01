package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchFragment extends ListFragment {

    private String query;

    public static SearchFragment newInstance(String query)
    {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        query = getArguments().getString("query");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        //loading tweets for the first time
        customLoadMoreDataFromApi(0);

        return view;
    }

    public void customLoadMoreDataFromApi(int offset) {
        if (!adapter.isEmpty()) {
            offlineHelper.populateSearchResults(query, adapter.getItem(adapter.getCount() -1).getTid());
        }else{
            offlineHelper.populateSearchResults(query, offset);
        }

    }
}
