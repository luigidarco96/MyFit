package com.example.luigidarco.myfit.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.models.Food;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FoodAdapter extends ArrayAdapter<Food> {

    private ArrayList<Food> dataset;
    Context mContext;

    private static class ViewHolder {
        TextView name;
        TextView calorie;
        ImageView image;
    }

    public FoodAdapter(ArrayList<Food> data, Context context) {
        super(context, R.layout.fragment_calories_element, data);
        this.dataset = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Food food = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fragment_calories_element, parent, false);
            viewHolder.name = convertView.findViewById(R.id.food_name);
            viewHolder.calorie = convertView.findViewById(R.id.food_calories);
            viewHolder.image = convertView.findViewById(R.id.food_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(food.getName());
        viewHolder.calorie.setText("" + food.getCalorie());
        //viewHolder.image.setImageBitmap(food.getImage());

        return convertView;
    }
}
