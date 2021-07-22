package com.example.wanderrouteroom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.RouteViewHolder> {
    private List<Route> routes = new ArrayList<>();
    private Route route;
    final private RouteItemClickListener mOnClickListener;

    public RouteListAdapter(RouteItemClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteListAdapter.RouteViewHolder holder, int position) {
        Route current = routes.get(position);
        holder.bind(current.getDescription());
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
        notifyDataSetChanged();
    }

    public void setRoute(Route route) {
        this.route = route;
        notifyDataSetChanged();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView routeItemView;

        private RouteViewHolder(View itemView) {
            super(itemView);
            routeItemView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }

        public void bind(String text) {
            routeItemView.setText(text);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(mOnClickListener != null && position != RecyclerView.NO_POSITION) {
                mOnClickListener.onRouteItemClick(routes.get(position));
            }
        }
    }

}




