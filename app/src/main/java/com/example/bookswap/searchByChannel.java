package com.example.bookswap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class searchByChannel extends Fragment {

    private  RecyclerView recyclerViewchannel;
    private ChannelAdapter ChannelAdapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_by_channel, container, false);

        //Intialize recycler view
        recyclerViewchannel = view.findViewById(R.id.channel_recyclerview);


        //layout
        recyclerViewchannel.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        List<Community>  PopularChannels = loadpopularchannels();

        //Adapter
         ChannelAdapter= new ChannelAdapter(getContext(),PopularChannels);

         //set Adapters to recycler view
        recyclerViewchannel.setAdapter(ChannelAdapter);
        return view;



        //search button

    }

    private List<Community> loadpopularchannels() {
        List<Community> Channels = new ArrayList<>();
        Channels.add(new Community("Channel 1","https://th.bing.com/th/id/OIP.WFXv7M9CPuUWxE88ib4-zgHaGX?w=190&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Channels.add(new Community("Channel 2","https://th.bing.com/th/id/OIP.BxengUBP0jPzfJgIFkJZkQHaNK?w=115&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Channels.add(new Community("Channel 3","https://th.bing.com/th/id/OIP.kM0jkZ0f5ee_KPvxwbtnHgHaHa?w=165&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Channels.add(new Community("Channel 4","https://th.bing.com/th/id/OIP.LxHEJG96q_5slYQQ5ynmkwHaEc?w=274&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Channels.add(new Community("Channel 6","https://th.bing.com/th/id/OIP.LxHEJG96q_5slYQQ5ynmkwHaEc?w=274&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Channels.add(new Community("Channel 7","https://th.bing.com/th/id/OIP.WFXv7M9CPuUWxE88ib4-zgHaGX?w=190&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Channels.add(new Community("Channel 8","https://th.bing.com/th/id/OIP.BxengUBP0jPzfJgIFkJZkQHaNK?w=115&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Channels.add(new Community("Channel 9","https://th.bing.com/th/id/OIP.kM0jkZ0f5ee_KPvxwbtnHgHaHa?w=165&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Channels.add(new Community("Channel 10","https://th.bing.com/th/id/OIP.LxHEJG96q_5slYQQ5ynmkwHaEc?w=274&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        Channels.add(new Community("Channel 5","https://th.bing.com/th/id/OIP.LxHEJG96q_5slYQQ5ynmkwHaEc?w=274&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"));
        return Channels;
    }
}