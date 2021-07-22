package com.example.learningcardroomapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LearningCardListAdapter extends RecyclerView.Adapter<LearningCardListAdapter.LearningCardViewHolder> implements Filterable {
    private List<LearningCard> learningCards = new ArrayList<>();
    // copy of all learningCards for filtering options
    private List<LearningCard> learningCardsFull;
    private LearningCard learningCard;
    final private LearningCardItemClickListener mOnClickListener;

    public LearningCardListAdapter(LearningCardItemClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public LearningCardListAdapter.LearningCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new LearningCardListAdapter.LearningCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LearningCardListAdapter.LearningCardViewHolder holder, int position) {
        LearningCard current = learningCards.get(position);
        holder.bind(current.getQuestion(), current.getSubject());
    }

    @Override
    public int getItemCount() {
        return learningCards.size();
    }

    public void setLearningCards(List<LearningCard> learningCards) {
        this.learningCards = learningCards;
        // create copy from learningCards for filtering options
        learningCardsFull = new ArrayList<>(learningCards);
        notifyDataSetChanged();
    }

    // return LearningCard at position
    public LearningCard getLearningCardAt(int position) {
        return learningCards.get(position);
    }

    public void setLearningCard(LearningCard learningCard) {
        this.learningCard = learningCard;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return learningCardFilter;
    }

    private Filter learningCardFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<LearningCard> filteredList = new ArrayList<>();
            // if searchView is empty return everything
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(learningCardsFull);
            } else {
                // else check if constraint is in learningCard item and add that item then to the return list
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (LearningCard item : learningCardsFull) {
                    if (item.getSubject().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            // return the filtered list
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // clear all learningCards and then add the filtered items for the searchView
            learningCards.clear();
            learningCards.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    class LearningCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView LearningCardQuestion, LearningCardSubject;

        private LearningCardViewHolder(View itemView) {
            super(itemView);
            LearningCardQuestion = itemView.findViewById(R.id.textViewQuestions);
            LearningCardSubject = itemView.findViewById(R.id.textViewSubjects);
            itemView.setOnClickListener(this);
        }

        public void bind(String text_question, String text_subject) {
            LearningCardQuestion.setText(text_question);
            LearningCardSubject.setText("Subject: " + text_subject);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(mOnClickListener != null && position != RecyclerView.NO_POSITION) {
                mOnClickListener.onLearningCardItemClick(learningCards.get(position));
            }
        }
    }
}
