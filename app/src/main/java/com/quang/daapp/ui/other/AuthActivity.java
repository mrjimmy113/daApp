package com.quang.daapp.ui.other;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quang.daapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class AuthActivity extends AppCompatActivity {

    ConstraintLayout loaderLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        loaderLayout = findViewById(R.id.layout_loader);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);


        NavigationUI.setupWithNavController(navView, navController);
    }

    public void activateLoader() {
        loaderLayout.setVisibility(View.VISIBLE);
    }

    public void deactivateLoad() {
        loaderLayout.setVisibility(View.GONE);
    }

}
