package com.iqra.dailydairy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.Chain;
import com.iqra.dailydairy.Event;
import com.iqra.dailydairy.OnItemClicked;
import com.iqra.dailydairy.R;

import java.util.ArrayList;

public class MoreItemAdapter extends RecyclerView.Adapter<MoreItemAdapter.MyViewHolder> {

    private ArrayList<Event> events;




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvChainName;
        TextView tvChainItemCounts;
        CardView btnEvents;


        MyViewHolder(View itemView) {
            super(itemView);
            tvChainName = itemView.findViewById(R.id.tvChainName);
            tvChainItemCounts = itemView.findViewById(R.id.tvChainItemCounts);
            btnEvents = itemView.findViewById(R.id.btnEvents);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoreItemAdapter(ArrayList<Event> chains) {
        this.events = chains;

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MoreItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.chain_list_item, parent, false);
        return new MyViewHolder(listItem);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Event currentItem = events.get(position);
        holder.tvChainName.setText(events.get(position).getName() + "\n"+ currentItem.getVenue() );
        String date = currentItem.getDay() + "-" + currentItem.getMonth() + "-" + currentItem.getYear();
        String time = currentItem.getTime();
        holder.tvChainItemCounts.setText(date + "    "+time);
//        holder.tvChainItemCounts.setText(events.get(position).get().size()+ " Events in Chain");
//
//        holder.btnEvents.setOnClickListener(view -> {
//            if(mLisenter != null)
//            {
//                mLisenter.onItemClicked(position);
//            }
//        });


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