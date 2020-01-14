package com.iqra.dailydairy.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.Event;
import com.iqra.dailydairy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {
    private ArrayList<String> maxDaysList;
    private HashMap<String, Event> events;
    private int currentDate;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvEventTime;
        TextView tvEventName;
        ConstraintLayout bgEvents;

        MyViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvEventDay);
            tvEventTime = itemView.findViewById(R.id.tvEventTime);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            bgEvents = itemView.findViewById(R.id.bgEvents);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventsAdapter(ArrayList<String> _daysList, HashMap<String, Event> _events) {
        maxDaysList = _daysList;
        events = _events;

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd");
        String formattedDate = df.format(c);
        currentDate = Integer.parseInt(formattedDate);
        currentDate--;


    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public EventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.event_list_item, parent, false);
        return new MyViewHolder(listItem);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tvDate.setText(maxDaysList.get(position));
        if (position == currentDate) {
            holder.bgEvents.setBackgroundColor(Color.parseColor("#900C3F"));
        }

        if (events.containsKey(holder.tvDate.getText().toString())) {
            holder.tvEventName.setText(events.get(holder.tvDate.getText().toString()).getName());
            holder.tvEventTime.setText(events.get(holder.tvDate.getText().toString()).getTime());
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return maxDaysList.size();
    }
}