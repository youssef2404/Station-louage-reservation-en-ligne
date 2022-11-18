package tn.esprit.babalioua;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class QrCodeScanner extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    Button btScan;
    String id, destination;


    private Handler mHandler = new Handler();

    private final String url="http://192.168.211.232/android/verif_ticket.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_qr );

        btScan=findViewById(R.id.bt_scan);

        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator=new IntentIntegrator(
                        QrCodeScanner.this
                );
                intentIntegrator.setPrompt("for flash use volume up key");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.initiateScan();
            }
        });

        mHandler.postDelayed(mToastRunnablle, 1000);

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
                Intent intent = new Intent(QrCodeScanner.this, agents_profile.class);
                startActivity(intent);
                break;

            case R.id.nav_qr:

                break;

            case R.id.nav_dcx:
                logout();
                break;


        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );

        if(intentResult.getContents()!= null){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                    QrCodeScanner.this
            );
            builder.setTitle("Result");
            String qrcontenu=(intentResult.getContents().toString());
            int pos=qrcontenu.indexOf("Z");
             id=qrcontenu.substring(0,pos);
            int pos2=pos+1;
             destination=qrcontenu.substring(pos2);
            process(id,destination);
        }else{
            Toast.makeText(getApplicationContext(),"veuillez scanner un qr code",Toast.LENGTH_SHORT).show();
        }
    }

    public void process(String i,String destin)
    {


        String qrystring="?t1="+i+"&t2="+destin;
        class dbclass extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data)
            {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
                        QrCodeScanner.this
                );
                builder.setMessage(data);
                builder.show();
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


            }
            @Override
            protected String doInBackground(String... param)
            {
                try {
                    URL url=new URL(param[0]);
                    HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    return br.readLine();



                }catch(Exception ex)
                {
                    return ex.getMessage();
                }
            }
        }
        dbclass obj=new dbclass();
        obj.execute(url+qrystring);

    }

    public  void logout(){

        SharedPreferences sp1 = getSharedPreferences("credentials", MODE_PRIVATE);

        if (sp1.contains("id")){
            SharedPreferences.Editor editor1 = sp1.edit();
            editor1.remove("id");
            editor1.putString("msg","Logged out successfully");
            editor1.commit();
            Intent intent = new Intent(QrCodeScanner.this, login_agents.class);
            startActivity(intent);

        }

    }

    private Runnable mToastRunnablle = new Runnable() {
        @Override
        public void run() {

            //onActivityResult();
           // process(id, destination);
            mHandler.postDelayed(this,2000);
        }
    };
}