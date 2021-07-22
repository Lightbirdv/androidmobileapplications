package com.example.wanderrouteroom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PoiListAdapter extends RecyclerView.Adapter<PoiListAdapter.PoiViewHolder> {
    private List<Poi> pois = new ArrayList<>();
    private Poi poi;
    final private PoiItemClickListener mOnClickListener;

    public PoiListAdapter(PoiItemClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public PoiListAdapter.PoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new PoiListAdapter.PoiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoiListAdapter.PoiViewHolder holder, int position) {
        Poi current = pois.get(position);
        holder.bind(current.getPlace());
    }

    @Override
    public int getItemCount() {
        return pois.size();
    }

    public void setPois(List<Poi> pois) {
        this.pois = pois;
        notifyDataSetChanged();
    }

    public void setPoi(Poi poi) {
        this.poi = poi;
        notifyDataSetChanged();
    }

    class PoiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView PoiItemView;

        private PoiViewHolder(View itemView) {
            super(itemView);
            PoiItemView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }

        public void bind(String text) {
            PoiItemView.setText(text);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(mOnClickListener != null && position != RecyclerView.NO_POSITION) {
                mOnClickListener.onPoiItemClick(pois.get(position));
            }
        }
    }
}
