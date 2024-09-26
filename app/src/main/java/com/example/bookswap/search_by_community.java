package com.example.bookswap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class search_by_community extends Fragment {

    private RecyclerView recyclerViewcommunity;
    private CommunityAdapter popularCommunitiesAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@NonNull ViewGroup container,@NonNull
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_by_community, container, false);

        // Intialize RecyclerView
        recyclerViewcommunity = view.findViewById(R.id.community_recyclerview);

        //layout
        recyclerViewcommunity.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));

        List<Community> PopularCommunities = loadpopularCommunities();

        //Adapter
        popularCommunitiesAdapter = new CommunityAdapter(getContext(), PopularCommunities);

        recyclerViewcommunity.setAdapter(popularCommunitiesAdapter);
        return  view;



    }

    private List<Community> loadpopularCommunities() {
        List<Community> Communities = new ArrayList<>();
        Communities.add(new Community("Community 1","https://th.bing.com/th/id/OIP.WFXv7M9CPuUWxE88ib4-zgHaGX?w=190&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Communities.add(new Community("Community 2","https://th.bing.com/th/id/OIP.BxengUBP0jPzfJgIFkJZkQHaNK?w=115&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Communities.add(new Community("Community 3","https://th.bing.com/th/id/OIP.kM0jkZ0f5ee_KPvxwbtnHgHaHa?w=165&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Communities.add(new Community("Community 4","https://th.bing.com/th/id/OIP.LxHEJG96q_5slYQQ5ynmkwHaEc?w=274&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Communities.add(new Community("Community 5","https://th.bing.com/th/id/OIP.LxHEJG96q_5slYQQ5ynmkwHaEc?w=274&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        return  Communities;
    }

}