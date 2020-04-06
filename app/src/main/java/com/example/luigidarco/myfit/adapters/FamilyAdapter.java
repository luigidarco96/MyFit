package com.example.luigidarco.myfit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.callbacks.FamilyMemberClick;
import com.example.luigidarco.myfit.models.User;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.ViewHolder> {

    private ArrayList<User> dataset;
    private Context context;
    private FamilyMemberClick callback;

    public FamilyAdapter(ArrayList<User> data, Context context, FamilyMemberClick callback) {
        this.context = context;
        this.dataset = data;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_family_list_element, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = dataset.get(position);
        holder.username.setText(user.getUsername());
        holder.itemView.setOnClickListener(itemClick -> {
            callback.onClickItem(user);
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.family_user_username);
        }
    }
}
