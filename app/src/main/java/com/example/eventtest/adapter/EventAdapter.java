package com.example.eventtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventtest.R;
import com.example.eventtest.entity.EventEntity;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final LayoutInflater inflater;
    private List<EventEntity> eventList;

    public EventAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.event_list_items,parent,false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        if(eventList != null){
            EventEntity currentList = eventList.get(position);
            holder.eventName.setText(currentList.getEventname());
        }else{
            holder.eventName.setText("no words");
        }

    }

    public void setEventEntitiesList(List<EventEntity> eventEntitiesList) {
        this.eventList = eventEntitiesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (eventList != null)
            return eventList.size();
       else return 0;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        TextView eventName;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.taskname);
        }
    }

}
