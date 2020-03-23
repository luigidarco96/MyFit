package com.example.luigidarco.myfit.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.models.Workout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {

    private ArrayList<Workout> dataset;
    private Context mContext;

    public WorkoutAdapter(ArrayList<Workout> data, Context context) {
        this.dataset = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public WorkoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_workout_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapter.ViewHolder holder, int position) {
        Workout workout = dataset.get(position);
        holder.image.setImageResource(workout.getImageResource());
        holder.title.setText(workout.getTitle());
        holder.time.setText(workout.getTime() + " minutes");
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.workout_image);
            title = itemView.findViewById(R.id.workout_title);
            time = itemView.findViewById(R.id.workout_time);
        }
    }
}
