package com.example.luigidarco.myfit.fragments;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.adapters.FamilyAdapter;
import com.example.luigidarco.myfit.callbacks.FamilyMemberClick;
import com.example.luigidarco.myfit.callbacks.NetworkCallback;
import com.example.luigidarco.myfit.callbacks.RecyclerItemClick;
import com.example.luigidarco.myfit.managers.NetworkManager;
import com.example.luigidarco.myfit.models.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class FamilyListFragment extends Fragment {

    private final String TAG = "MYFITAPP";

    private ArrayList<User> userList;

    private RecyclerView recyclerView;
    private FamilyAdapter familyAdapter;
    private FloatingActionButton newElement;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_family_list, container, false);

        newElement = view.findViewById(R.id.new_element);
        newElement.setOnClickListener(cview -> {
            AlertDialog dialog = new MaterialAlertDialogBuilder(getActivity())
                    .setTitle("Choose an option")
                    .setView(R.layout.new_family_dialog)
                    .show();

            RelativeLayout bePart = dialog.findViewById(R.id.family_add_be_part);
            RelativeLayout addOne = dialog.findViewById(R.id.family_add_new_member);

            bePart.setOnClickListener(clickView -> {
                dialog.dismiss();
                NavHostFragment.findNavController(getParentFragment())
                        .navigate(R.id.action_nav_family_to_nav_qr_provider);
            });
            addOne.setOnClickListener(clickView -> {
                dialog.dismiss();
                NavHostFragment.findNavController(getParentFragment())
                        .navigate(R.id.action_nav_family_to_nav_qr_scanner);
            });
        });

        recyclerView = view.findViewById(R.id.family_recycler_view);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        userList = new ArrayList<>();

        familyAdapter = new FamilyAdapter(userList, getActivity(), userClick -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", "" + userClick.getId());
            bundle.putString("username", userClick.getUsername());
            NavHostFragment.findNavController(getParentFragment())
                    .navigate(R.id.action_nav_family_to_nav_family_details, bundle);
        });

        recyclerView.setAdapter(familyAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadChildren();
    }

    private void loadChildren() {
        userList.clear();
        String url = getResources().getString(R.string.url_server) + "/users/children";
        NetworkManager.makeGetJsonObjRequest(getActivity(), url, new NetworkCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONArray children = object.getJSONArray("data");
                    for (int i=0; i<children.length(); i++) {
                        JSONObject obj = children.getJSONObject(i);

                        User user = new User(
                                obj.getInt("id"),
                                obj.getString("username"),
                                obj.getString("full_name"),
                                obj.getString("date_of_birth"),
                                obj.getInt("gender")
                        );

                        userList.add(user);
                    }
                    familyAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
