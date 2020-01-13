package com.iqra.dailydairy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.Event;
import com.iqra.dailydairy.OnItemClicked;
import com.iqra.dailydairy.R;

import java.util.ArrayList;

public class ChainsEventsAdapter extends RecyclerView.Adapter<ChainsEventsAdapter.MyViewHolder> {

    private ArrayList<Event> events;
    private OnItemClicked mListener;

    public void setOnClickListener(OnItemClicked listener)
    {
        this.mListener = listener;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventTime;
        TextView tvEventName;
        TextView tvEditEvent;

        MyViewHolder(View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventTime = itemView.findViewById(R.id.tvEventTime);
            tvEditEvent = itemView.findViewById(R.id.tvEditEvent);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChainsEventsAdapter(ArrayList<Event> _events) {
        events = _events;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ChainsEventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.chians_event_list_item, parent, false);
        return new MyViewHolder(listItem);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Event currentEvent = events.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tvEventName.setText(currentEvent.getName());
        holder.tvEventTime.setText(currentEvent.getDay()+"-"+currentEvent.getMonth()+"-"+currentEvent.getYear());

        holder.tvEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null)
                {
                    mListener.onItemClicked(position);
                }
            }
        });

        if(position == 0)
        {
            holder.tvEditEvent.setVisibility(View.VISIBLE);
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
        return events.size();
    }
}