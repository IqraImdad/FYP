package com.iqra.dailydairy.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.Event;
import com.iqra.dailydairy.R;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {
    private ArrayList<String> maxDaysList;
    private ArrayList<Event> events;
    private int currentPosition;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView tvDate;
        TextView tvEventTime;
        TextView tvEventName;

        MyViewHolder(ConstraintLayout v) {
            super(v);
            tvDate = v.findViewById(R.id.tvEventDay);
            tvEventTime = v.findViewById(R.id.tvEventTime);
            tvEventName = v.findViewById(R.id.tvEventName);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventsAdapter(ArrayList<String> _daysList, ArrayList<Event> _events) {
        maxDaysList = _daysList;
        events = _events;
        currentPosition = 0;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public EventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(currentPosition  >= maxDaysList.size())
            return;

        for (int i = 0; i < events.size(); i++) {
            if ( events.get(i).getDay().equalsIgnoreCase(maxDaysList.get(currentPosition))) {
                holder.tvEventTime.setText(events.get(i).getTime());
                holder.tvEventName.setText(events.get(i).getName());
            }
        }
        holder.tvDate.setText(maxDaysList.get(position));
        currentPosition++;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return maxDaysList.size();
    }
}