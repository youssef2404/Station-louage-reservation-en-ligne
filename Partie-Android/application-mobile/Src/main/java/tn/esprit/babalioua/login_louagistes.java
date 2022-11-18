package tn.esprit.babalioua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class login_louagistes extends AppCompatActivity {

    EditText t1,t2,t3;
    TextView tv;
    private final String url="http://192.168.211.232/android/db_insert_louagistes.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_louagistes);


        t1=(EditText)findViewById(R.id.t1);
        t3=(EditText)findViewById(R.id.t3);
        tv=findViewById(R.id.tv);

        checkedlogoutmsg(tv);

    }

    public void process(View view)
    {

        String n1=t1.getText().toString();
        String n3=t3.getText().toString();

      /*  if (n1.isEmpty() || n3.isEmpty()){
            Toast.makeText(this,"Veuillez remplir les champs, SVP !", Toast.LENGTH_LONG).show();
        }else {

       */

            String qrystring = "?t1=" + n1 + "&t2=" + n3;
            class dbclass extends AsyncTask<String, Void, String> {
                protected void onPostExecute(String data) {
                    t1.setText("");
                    //  t2.setText("");
                    t3.setText("");
                    if (data.matches("[+-]?\\d*(\\.\\d+)?")) {
                        SharedPreferences sp = getSharedPreferences("my_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("id", data.toString());
                        editor.apply();

                        SharedPreferences sp1 = getSharedPreferences("credentials", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sp1.edit();
                        editor1.putString("id", data.toString());
                        editor1.apply();

                        openActivity2();


                    } else {
                      tv.setText("Utilisateur n'existe pas");
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
            dbclass obj = new dbclass();
            obj.execute(url + qrystring);

    }
    public void openActivity2(){
        Intent intent = new Intent(this,louagistes_profile.class);
        startActivity(intent);
    }

    public void checkedlogoutmsg (View view){

        SharedPreferences sp = getSharedPreferences("my_prefs", MODE_PRIVATE);
        if (sp.contains("msg")){
            tv.setText(sp.getString("msg", ""));
            SharedPreferences.Editor ed = sp.edit();
            ed.remove("msg");
            ed.commit();
        }

    }



}