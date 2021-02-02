package com.example.eventtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.eventtest.adapter.EventAdapter;
import com.example.eventtest.viewModel.EventViewModel;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        final EventAdapter eventAdapter = new EventAdapter(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(eventAdapter);


        // get view model from view model provide
        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);

        // add an obserable for live data to return alltask
        eventViewModel.getAllTask().observe(this, eventAdapter::setEventEntitiesList);

//        btnAdd.setOnClickListener( view ->{
//            String Event = txtTask.getText().toString();
//            EventEntity eventEntity = new EventEntity(task);
//            eventViewModel.insert(eventEntity);
//        });
    }
}