package com.example.luigidarco.myfit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.callbacks.DeleteCallback;
import com.example.luigidarco.myfit.models.Food;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private ArrayList<Food> dataset;
    Context mContext;
    DeleteCallback deleteCallback;

    public FoodAdapter(ArrayList<Food> data, Context context, DeleteCallback deleteCallback) {
        this.dataset = data;
        this.mContext = context;
        this.deleteCallback = deleteCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_calorie_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = dataset.get(position);
        try {
            URL url = new URL(food.getImagePath());

            Glide.with(holder.image.getContext()).load(url).into(holder.image);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        holder.name.setText(food.getName());
        holder.calorie.setText(food.getCalorie() + " Kcal");
        holder.trash.setTag(position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView calorie;
        ImageView image;
        ImageView trash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.food_image);
            name = itemView.findViewById(R.id.food_name);
            calorie = itemView.findViewById(R.id.food_calories);
            trash = itemView.findViewById(R.id.food_trash);

            trash.setOnClickListener(view -> {
                deleteCallback.onDelete(Integer.parseInt(trash.getTag().toString()));
            });
        }
    }
}
