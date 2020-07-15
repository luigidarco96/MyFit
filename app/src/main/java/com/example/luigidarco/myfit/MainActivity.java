package com.example.luigidarco.myfit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.luigidarco.myfit.fragments.ProfileFragment;
import com.example.luigidarco.myfit.managers.StorageManager;
import com.example.luigidarco.myfit.models.User;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MYFITAPP";

    private AppBarConfiguration mAppBarConfiguration;
    private StorageManager spManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spManager = new StorageManager(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        RelativeLayout navLayout = headerView.findViewById(R.id.nav_layout);
        setMargins(navLayout, 0, getStatusBarHeight(), 0, 0);
        TextView username = headerView.findViewById(R.id.navigation_username);
        ImageView imageView = headerView.findViewById(R.id.user_image);
        ImageView editUser = headerView.findViewById(R.id.user_edit);
        editUser.setOnClickListener(onClickUserEdit);

        User currentUser = spManager.getCurrentUser();
        username.setText(currentUser.getUsername());
        if (currentUser.getGender() == 0) {
            imageView.setImageResource(R.drawable.ic_male);
        } else {
            imageView.setImageResource(R.drawable.ic_female);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_robot, R.id.nav_home, R.id.nav_calorie, R.id.nav_workout, R.id.nav_fitness_statistics, R.id.nav_bmi_statistics, R.id.nav_family, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private View.OnClickListener onClickUserEdit = (view) -> {
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_profile);
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawers();
    };
}
