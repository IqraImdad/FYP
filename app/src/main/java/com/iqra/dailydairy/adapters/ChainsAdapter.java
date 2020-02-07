package com.iqra.dailydairy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.Chain;
import com.iqra.dailydairy.OnItemClickedWithView;
import com.iqra.dailydairy.R;

import java.util.ArrayList;

public class ChainsAdapter extends RecyclerView.Adapter<ChainsAdapter.MyViewHolder> {

    private ArrayList<Chain> chains;
    private OnItemClickedWithView mLisenter;


    public void setOnClickListener(OnItemClickedWithView listener) {
        mLisenter = listener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvChainName;
        TextView tvChainItemCounts;
        TextView btnEdit;
        TextView btnDeleteChain;
        CardView btnEvents;


        MyViewHolder(View itemView) {
            super(itemView);
            tvChainName = itemView.findViewById(R.id.tvChainName);
            tvChainItemCounts = itemView.findViewById(R.id.tvChainItemCounts);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDeleteChain = itemView.findViewById(R.id.btnDeleteChain);
            btnEvents = itemView.findViewById(R.id.btnEvents);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChainsAdapter(ArrayList<Chain> chains) {
        this.chains = chains;

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ChainsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
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
        holder.tvChainName.setText(chains.get(position).getName());
        holder.btnEdit.setVisibility(View.GONE);
//        holder.tvChainItemCounts.setText(chains.get(position).getEvents().size()+ " Events in Chain");

        holder.btnEvents.setOnClickListener(view -> {
            if (mLisenter != null) {
                mLisenter.onItemClicked(holder.btnEvents, position);
            }
        });

        holder.btnDeleteChain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLisenter != null)
                    mLisenter.onItemClicked(holder.btnDeleteChain, position);
            }
        });

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
        return chains.size();
    }
}