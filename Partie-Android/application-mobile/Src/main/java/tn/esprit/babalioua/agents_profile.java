package tn.esprit.babalioua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
public class agents_profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TextView NamePlace;
   // TextView usernamePlace;
    TextView Nom, destination;

    private final String url="http://192.168.211.232/android/getNomPrenom.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents_profile);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile );

        NamePlace=findViewById(R.id.NamePlace);
       // usernamePlace=findViewById(R.id.usernamePlace);
        Nom=findViewById(R.id.Nom);
        destination =findViewById(R.id.destination);

        String id = getSharedPreferences("my_prefs", MODE_PRIVATE).getString("id", "Not found");
        process(id);
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

            case R.id.nav_profile:
                break;

            case R.id.nav_qr:
                Intent intent = new Intent(agents_profile.this, QrCodeScanner.class);
                startActivity(intent);
                break;
            case R.id.nav_dcx:
                logout();
                break;


        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public void process(String value) {

        String qrystring = "?t1=" + value;
        class dbclass extends AsyncTask<String, Void, String> {
            protected void onPostExecute(String data) {
                int pos = data.indexOf("&");
                int pos2 = data.indexOf("|");
                int pos3 = data.indexOf("!");
                int pos4 = data.indexOf("?");

                String name = data.substring(0, pos - 1);
                String username = data.substring(pos + 1, pos2 - 1);
                String des = data.substring(pos4 + 1);
                String nom = data.substring(pos3 + 1, pos4 - 1);

                NamePlace.setText(name);
                //usernamePlace.setText(username);
                Nom.setText(nom);
                destination.setText(des);


            }

            @Override
            protected String doInBackground(String... param) {
                try {
                    URL url = new URL(param[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    return br.readLine();


                } catch (Exception ex) {
                    return ex.getMessage();
                }
            }
        }
        dbclass obj = new dbclass();
        obj.execute(url + qrystring);

    }

    public  void logout(){

        SharedPreferences sp1 = getSharedPreferences("credentials", MODE_PRIVATE);

        if (sp1.contains("id")){
            SharedPreferences.Editor editor1 = sp1.edit();
            editor1.remove("id");
            editor1.putString("msg","Logged out successfully");
            editor1.commit();
            Intent intent = new Intent(agents_profile.this, login_agents.class);
            startActivity(intent);

        }

    }
    }