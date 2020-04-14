package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luigidarco.myfit.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RobotFragment extends Fragment {

    private final String TAG = "MYFITAPP";

    private ImageView settings;
    private Button forward;
    private Button backward;
    private Button leftward;
    private Button rightward;

    private String robotURL = "http://192.168.43.80";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_robot, container, false);

        settings = view.findViewById(R.id.robot_setting);
        forward = view.findViewById(R.id.forward_button);
        backward = view.findViewById(R.id.backward_button);
        leftward = view.findViewById(R.id.leftward_button);
        rightward = view.findViewById(R.id.rightward_button);

        settings.setOnClickListener(clickView -> {
            EditText editText = new EditText(getContext());
            editText.setText(robotURL);
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Insert pybot ip address")
                    .setView(editText)
                    .setPositiveButton("Save", ((dialogInterface, i) -> {
                        robotURL = editText.getText().toString();
                    }))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
        forward.setOnClickListener(clickView -> handleMovement("forward"));
        backward.setOnClickListener(clickView -> handleMovement("backward"));
        leftward.setOnClickListener(clickView -> handleMovement("leftward"));
        rightward.setOnClickListener(clickView -> handleMovement("rightward"));

        return view;
    }

    private void handleMovement(String movement) {
        String url = robotURL + "/movement/" + movement;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                null,
                error -> Log.e(TAG, error.toString())
        );
        queue.add(request);
    }
}
