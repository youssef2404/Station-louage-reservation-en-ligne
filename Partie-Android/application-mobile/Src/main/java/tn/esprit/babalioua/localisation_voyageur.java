package tn.esprit.babalioua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class localisation_voyageur extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation_voyageur);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_loc );

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_reserver:

                Intent intent2 = new Intent(localisation_voyageur.this, Reservation.class);
                startActivity(intent2);
                break;
            case R.id.nav_station:
                Intent intent = new Intent(localisation_voyageur.this, GetLouages.class);
                startActivity(intent);
                break;
            case R.id.nav_loc:
                break;

            case R.id.nav_bienvenue:
                Intent intent3 = new Intent(localisation_voyageur.this, Bienvenue.class);
                startActivity(intent3);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}