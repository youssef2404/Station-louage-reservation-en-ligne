package tn.esprit.babalioua;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//implements AdapterView.OnItemSelectedListener
public class Reservation extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    ImageView ivOutput;
    Button btnConfirmer;
    Button btnTerminer;
    TextView txt_prix;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    ArrayList<String> station_arriveList = new ArrayList<>();
    ArrayAdapter<String> arriveAdapter;


    RequestQueue requestQueue;
    Spinner spinner2;
    Spinner spinner1, spinner3;

    TextView tvDate;
    DatePickerDialog.OnDateSetListener setListener;

    ProgressDialog progressDialog;

    ListView listView;

    int bitmap_size = 40;
    int max_resolution_image = 800;
    Bitmap bmp, decoded;

    TextInputEditText edit_num;

    private final String url="http://192.168.211.232/android/test.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_reserver);

        progressDialog = new ProgressDialog(this);


        requestQueue = Volley.newRequestQueue(this);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        edit_num = findViewById(R.id.edit_num);


        tvDate = findViewById(R.id.tv_date);
        tvDate.setPaintFlags(tvDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        /* ------------------- get list spinenr ------------------*/
        String url = "http://192.168.211.232/android/station_arrive.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("louages");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String zone = jsonObject.optString("zone");

                        station_arriveList.add(zone);

                        arriveAdapter = new ArrayAdapter<>(Reservation.this,
                                android.R.layout.simple_list_item_1, station_arriveList);
                        arriveAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner2.setAdapter(arriveAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);

        /* ------------------- FIN get list spinenr ------------------*/


        spinner1 = findViewById(R.id.spinner1);
        btnTerminer = findViewById(R.id.btn_ter);

        ivOutput = findViewById(R.id.iv_output);
        btnConfirmer = findViewById(R.id.btn_confirmer);

        /* ----------- date ----------------*/
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Reservation.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });


        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;
                tvDate.setText(date);
            }
        };

        /* ----------- FIN date ----------------*/

        btnConfirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                addTicket();
                process();

                /* ------------- qr-----------------------*/

                String numero = edit_num.getText().toString().trim();

            /*    if (numero.isEmpty()) {
                    btnConfirmer.setVisibility(View.VISIBLE);
                    btnTerminer.setVisibility(View.INVISIBLE);
                } else {

             */

                    btnConfirmer.setVisibility(View.GONE);
                    btnTerminer.setVisibility(View.VISIBLE);

                    btnTerminer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                           /* try {
                                createPdf();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            */

                            startActivity(new Intent(getApplicationContext(), Bienvenue.class));
                        }


                    });

                }
           // }
        });


        //    getJSON("http://192.168.1.17/android/v1/prix.php");
    }


    private void addTicket() {

        String station_depart = spinner1.getSelectedItem().toString().trim();
        String station_arrive = spinner2.getSelectedItem().toString().trim();
        String ville = spinner3.getSelectedItem().toString().trim();
        String date = tvDate.getText().toString().trim();
        String num = edit_num.getText().toString().trim();


       /* if (num.isEmpty()) {
            Toast.makeText(this, "Enter votre numéro de téléphone , SVP!", Toast.LENGTH_LONG).show();
        } else {

        */
            progressDialog.setMessage("Ticket encours d'enregistrement...");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.URL_ADD_TICKET,
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

                            Toast.makeText(getApplicationContext(), "Ticket bien reservée et téléchargée", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressDialog.hide();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("station_depart", station_depart);
                    params.put("station_arrive", station_arrive);
                    params.put("ville", ville);
                    params.put("status", "0");

                    if (date.equals("Choisir Date")) {
                        //LocalDate today = LocalDate.now();
                        SimpleDateFormat today = new SimpleDateFormat("dd-MM-yyyy");

                        Calendar calendar = Calendar.getInstance();
                        Date dateObj = calendar.getTime();
                        String date2 = today.format(dateObj);

                        // String todayDate = today.toString();
                        params.put("dateReservation", date2);
                    } else {
                        params.put("dateReservation", date);
                    }
                    params.put("plateforme", "Mobile");
                    params.put("Num_Tel", num);
                    params.put("Sms", "0");
                    params.put("num_place", "0");
                    params.put("louage", "0");


                    return params;
                }

            };


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

    }

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
        Intent intent = new Intent(Reservation.this, Bienvenue.class);


        switch (item.getItemId()) {

            case R.id.nav_bienvenue:

                startActivity(intent);
                break;

            case R.id.nav_station:
                Intent intent3 = new Intent(Reservation.this, list_stations.class);
                startActivity(intent3);
                break;


            case R.id.nav_loc:
                Intent intent2 = new Intent(Reservation.this, MapsVoyageur.class);
                startActivity(intent2);
                break;

            case R.id.nav_reserver:
                break;


        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public void process() {

        String n1 = edit_num.getText().toString().trim();
        String qrystring = "?t1=" + 20056925;


            class dbclass extends AsyncTask<String, Void, String> {


                @SuppressLint("WrongThread")
                protected void onPostExecute(String data) {

                    //String idTicket = data;

                    /* -------- qr -----------------------------*/
                    String ville_des = spinner2.getSelectedItem().toString().trim();

                    String arrive = data + "Z" + ville_des;

                    MultiFormatWriter writer = new MultiFormatWriter();
                    try {
                        BitMatrix matrix1 = writer.encode(arrive, BarcodeFormat.QR_CODE,
                                350, 350);

                        BarcodeEncoder encoder = new BarcodeEncoder();

                        Bitmap bitmap = encoder.createBitmap(matrix1);

                        ivOutput.setImageBitmap(bitmap);

                        InputMethodManager manager = (InputMethodManager) getSystemService(
                                Context.INPUT_METHOD_SERVICE
                        );
                        manager.hideSoftInputFromWindow(spinner2.getApplicationWindowToken(), 0);

                        /* ------------- FIN creation qr ---------------------*/

                        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                        File file = new File(pdfPath, "myPDF.pdf");
                        try {
                            OutputStream outputStream = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        PdfWriter writer2 = null;
                        try {
                            writer2 = new PdfWriter(String.valueOf(file));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        PdfDocument pdfDocument = new PdfDocument(writer2);
                        Document document = new Document(pdfDocument);

                        pdfDocument.setDefaultPageSize(PageSize.A6);
                        document.setMargins(0, 0, 0, 0);

                        String station = spinner2.getSelectedItem().toString().trim();

                        float[] width = {100f, 100f};
                        Table table = new Table(width);
                        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

                        table.addCell(new Cell().add(new Paragraph("\nStation d'arrive"))).setHorizontalAlignment(HorizontalAlignment.CENTER);
                        table.addCell(new Cell().add(new Paragraph(station)));

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bitmapData = stream.toByteArray();

                        ImageData imageData = ImageDataFactory.create(bitmapData);
                        Image image = new Image(imageData);


                        Paragraph paragraph = new Paragraph("     " + "Smart Beb Alioua" + "\n").setBold().setFontSize(15).setMarginTop(20).setHorizontalAlignment(HorizontalAlignment.CENTER);
                        Paragraph p2 = new Paragraph(String.valueOf(bitmap));


                        document.add(paragraph);
                        document.add(image);
                        document.close();

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }


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



            /* -------------------------------------------------------------------------------------------------- */

            dbclass obj = new dbclass();
            obj.execute(url + qrystring);



    }
}
