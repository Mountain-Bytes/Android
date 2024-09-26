package com.example.bookswap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchByCategory extends Fragment {

    private RecyclerView recyclerViewCategory;
    private CategoryAdapter categoryAdapter;

    public SearchByCategory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_by_category, container, false);

        // Intialize RecyclerView
        recyclerViewCategory = view.findViewById(R.id.search_by_category_result_recyclerview);


        //layout
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        List<Category> PopularCategories = loadCategories();

        //Adapter
        categoryAdapter = new CategoryAdapter(getContext(),PopularCategories);

        recyclerViewCategory.setAdapter(categoryAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerViewCategory.setLayoutManager(gridLayoutManager);
        return  view;
    }

    private List<Category> loadCategories() {
        List<Category> Categories = new ArrayList<>();
        Categories.add(new Category("Science fiction","https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTdl8Gr3Zlx0oxwJ3BYy6jFPRXHg8k7oHwucNhPRpLlNWUm-mxk"));
        Categories.add(new Category("Fantasy","https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTX3mdEmC-e0DveBAy-Kw_tbfU5V0tnztRLUhUOgbL9U4tuV7Cp"));
        Categories.add(new Category("Literary Fiction","https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQr3q_j7PHRXiq2L6Ik2G4xCOPBgbczch-vk3Jf2gkuWRwQbQug"));
        Categories.add(new Category("Thriller","https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRajEtDgBsOLhuSDcQmR6wBGEL8g4nYdhCia0gjZku2kJKZMT9f"));
        return Categories;


    }
}
