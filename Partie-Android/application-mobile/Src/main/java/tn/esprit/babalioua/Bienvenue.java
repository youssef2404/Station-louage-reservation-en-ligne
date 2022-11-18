package tn.esprit.babalioua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Bienvenue extends AppCompatActivity {

    Button btn_v, btn_l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bienvenue);
        btn_v= findViewById(R.id.btn_v);
        btn_l= findViewById(R.id.btn_l);

        btn_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Reservation.class));
            }
        });

        btn_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getApplicationContext(), login_louagistes.class));
            }
        });




    }
}