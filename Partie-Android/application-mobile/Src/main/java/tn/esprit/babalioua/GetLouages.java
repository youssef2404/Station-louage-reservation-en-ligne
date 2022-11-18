package tn.esprit.babalioua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetLouages extends AppCompatActivity {

    ListView listView;
    ListView listView2;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_louages);

        getJSON("http://192.168.211.232/android/v1/getData.php");
        listView = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView2);
        requestQueue = Volley.newRequestQueue(this);



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
            station[i] = obj.getString("nbLouages");

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

}