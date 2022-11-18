package tn.esprit.babalioua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Reclamations extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    Button btn_accident, btn_panne, btn_trajet, btn_autre, btn5;
    EditText e_reclamation;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamations);

        /* ---------------- menu -------------*/
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_rec);

        /* ---------------- FIN menu -------------*/

        progressDialog = new ProgressDialog(this);

        e_reclamation = findViewById(R.id.e_reclamation);

        btn_accident = findViewById(R.id.btn1);
        btn_panne = findViewById(R.id.btn2);
        btn_trajet = findViewById(R.id.btn3);
        btn_autre = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);

        btn_accident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accident();
            }
        });

        btn_panne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panne();
            }
        });

        btn_trajet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trajet();
            }
        });

        btn_autre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_autre.setVisibility(View.INVISIBLE);
                btn5.setVisibility(View.VISIBLE);
                e_reclamation.setVisibility(View.VISIBLE);

                btn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autre();
                    }
                });
            }
        });


    }

    private void autre() {
        String txt_reclamation = e_reclamation.getText().toString().trim();

        if (txt_reclamation.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir le champ, SVP !", Toast.LENGTH_LONG).show();
        } else {

            progressDialog.setMessage("Ajout du reclamation...");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.URL_RECLAMATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message")
                                        , Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(getApplicationContext(), "Reclamation ajoutée avec succès", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressDialog.hide();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("reclamation", txt_reclamation);


                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void trajet() {
        progressDialog.setMessage("Ajout du reclamation...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_RECLAMATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message")
                                    , Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getApplicationContext(), "Reclamation ajoutée avec succès", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("reclamation", "Cette Louage a modifiee son trajet");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void panne() {
        progressDialog.setMessage("Ajout du reclamation...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_RECLAMATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message")
                                    , Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getApplicationContext(), "Reclamation ajoutée avec succès", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("reclamation", "Une panne survenu a cette Louage");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void accident() {

        progressDialog.setMessage("Ajout du reclamation...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_RECLAMATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message")
                                    , Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getApplicationContext(), "Reclamation ajoutée avec succès", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("reclamation", "Accident survenu a cette Louage");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /* ------------ fonction Menu ----------*/
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(Reclamations.this, louagistes_profile.class);

        switch (item.getItemId()) {
            case R.id.nav_profile:
                startActivity(intent);
                break;
            case R.id.nav_rec:
                break;

            case R.id.nav_localisation:
                Intent intent3 = new Intent(Reclamations.this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_dcx:
                logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    /* ------------  FIN fonction Menu ----------*/
    public void logout() {

        SharedPreferences sp1 = getSharedPreferences("credentials", MODE_PRIVATE);

        if (sp1.contains("id")) {
            SharedPreferences.Editor editor1 = sp1.edit();
            editor1.remove("id");
            editor1.putString("msg", "Logged out successfully");
            editor1.commit();
            Intent intent = new Intent(Reclamations.this, Bienvenue.class);
            startActivity(intent);

        }
    }
}