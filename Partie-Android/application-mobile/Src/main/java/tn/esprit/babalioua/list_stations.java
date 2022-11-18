package tn.esprit.babalioua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class list_stations extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    ListView listView;
    ListView listView2;
    RequestQueue requestQueue;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    ImageView img_money;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stations);



        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_station );


        getJSON("http://192.168.211.232/android/v1/getData.php");
        mHandler.postDelayed(mToastRunnablle, 1000);

        listView = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView2);
        img_money = findViewById(R.id.img_money);

        img_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), list_prix.class));
            }
        });


        requestQueue = Volley.newRequestQueue(this);

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
                Intent intent3 = new Intent(list_stations.this, Reservation.class);
                startActivity(intent3);
                break;

            case R.id.nav_station:
                break;

            case R.id.nav_loc:
                Intent intent2 = new Intent(list_stations.this, MapsVoyageur.class);
                startActivity(intent2);
                break;

            case R.id.nav_bienvenue:
                Intent intent = new Intent(list_stations.this, Bienvenue.class);
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    private void loadIntoListView2(String json) throws JSONException {
        //creating a json array from the json string
        JSONArray jsonArray = new JSONArray(json);
        //creating a string array for listview
        String[] station = new String[jsonArray.length()];

        //looping through all the elements in json array
        for (int i = 0; i < jsonArray.length(); i++) {

            //getting json object from the json array
            JSONObject obj = jsonArray.getJSONObject(i);
            //getting the name from the json object and putting it inside string array
            station[i] = obj.getString("zone");

        }


        //the array adapter to load data into list
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, station);

        //attaching adapter to listview
        listView2.setAdapter(arrayAdapter);
    }
    private void loadIntoListView(String json) throws JSONException {
        //creating a json array from the json string
        JSONArray jsonArray = new JSONArray(json);

        //creating a string array for listview
        String[] station = new String[jsonArray.length()];

        //looping through all the elements in json array
        for (int i = 0; i < jsonArray.length(); i++) {

            //getting json object from the json array
            JSONObject obj = jsonArray.getJSONObject(i);
            //getting the name from the json object and putting it inside string array
            station[i] = obj.getString("nbPlace_possible") + " " +"places";

        }
        //the array adapter to load data into list
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, station);

        //attaching adapter to listview
        listView.setAdapter(arrayAdapter);
    }

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                    loadIntoListView2(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {



                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {

                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private Runnable mToastRunnablle = new Runnable() {
        @Override
        public void run() {
            getJSON("http://192.168.211.232/android/v1/getData.php");
            mHandler.postDelayed(this,2000);
        }
    };
}