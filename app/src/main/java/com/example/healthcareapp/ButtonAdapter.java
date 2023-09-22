package com.example.healthcareapp;

// ButtonAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {

    private List<String> originalButtonLabels;
    private List<String> filteredButtonLabels;
    private OnButtonClickListener listener;

    public interface OnButtonClickListener {
        void onButtonClick(String label);
    }
    public ButtonAdapter(List<String> buttonLabels,OnButtonClickListener listener) {
        this.originalButtonLabels = new ArrayList<>(buttonLabels);
        this.filteredButtonLabels = new ArrayList<>(buttonLabels);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbutton_layout, parent, false);
        return new ButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        String label = filteredButtonLabels.get(position);
        holder.button.setText(label);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the onButtonClick method of the listener with the button label
               listener.onButtonClick(label);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredButtonLabels.size();
    }
    public void filter(String searchText) {
        filteredButtonLabels.clear();
        if (searchText.isEmpty()) {
            filteredButtonLabels.addAll(originalButtonLabels);
        } else {
            for (String label : originalButtonLabels) {
                if (label.toLowerCase().contains(searchText.toLowerCase())) {
                    filteredButtonLabels.add(label);
                }
            }
        }
        notifyDataSetChanged();
    }
    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.buttonItem);
        }
    }
}
