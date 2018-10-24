package com.biotecnum.propietariossipe;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * Created by ingluismb on 21/10/17.
 */

public class SignIn extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    //
    private Button logOut, btnFecha, getGPS;
    private String usuario;
    private int year, month, day, consultargps;
    private double latgps, longps;

    // RealTime Database Atributos
    TextView mtvName, tvPasajes;
    private DatabaseReference mDatabaseT, mDatabase;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        logOut = (Button)findViewById(R.id.signOut);
        mtvName = (TextView)findViewById(R.id.tvName);
        tvPasajes = (TextView)findViewById(R.id.tvPasajes);
        btnFecha = (Button) findViewById(R.id.btnFecha);
        getGPS = (Button) findViewById(R.id.getGPS);

        Intent intent= getIntent();
        Bundle b = intent.getExtras();

        if(b!=null) {
            usuario =(String) b.get("datos");
            mtvName.setText("Buseta: " +usuario);
        }


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SignIn.this, SignIn.this, year, month, day);
                datePickerDialog.show();
            }
        });

        getGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child("consultargps").setValue(1);

                Intent i = new Intent(SignIn.this, ConsultarGPS.class);
                i.putExtra("datos", usuario);
                i.putExtra("longps", longps);
                i.putExtra("latgps", latgps);
                startActivity(i);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        mDatabaseT = FirebaseDatabase.getInstance().getReference().child("r/"+usuario+"/"+ year + "/" + month + "/" + day + "/pasajeros/");
        mDatabaseT.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue(Integer.class) != null) {
                    tvPasajes.setText("Fecha: " + day + "/" + (month + 1) + "/" + year +
                            "\n\nCantidad de Pasajeros: " + dataSnapshot.getValue(Integer.class));
                }else{
                    tvPasajes.setText("Fecha: " + day + "/" + (month + 1) + "/" + year +
                            "\n\nNo hay pasajeros resgistrados en la fecha seleccionada");
                }
                mDatabase = FirebaseDatabase.getInstance().getReference().child("busgps/"+usuario+"/");
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        InfoGps infoGps = dataSnapshot.getValue(InfoGps.class);

                        if(infoGps != null){
                            consultargps = infoGps.consultargps;
                            latgps = infoGps.latgps;
                            longps = infoGps.longps;
                            //Toast.makeText(ConsultarGPS.this, latgps+" "+longps, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, final int i, final int i1, final int i2) {
        //tvPasajes.setText("Año: " + i + "\nMes: "+ i1 + "\nDia: " + i2);
        super.onStart();

        mDatabaseT = FirebaseDatabase.getInstance().getReference().child("r/"+usuario+"/"+ i + "/" +i1 + "/" + i2 + "/pasajeros/");
        mDatabaseT.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue(Integer.class) != null) {
                    tvPasajes.setText("Fecha: " + i2 + "/" + (i1 + 1) + "/" + i +
                            "\n\nCantidad de Pasajeros: " + dataSnapshot.getValue(Integer.class));
                }else{
                    tvPasajes.setText("Fecha: " + i2 + "/" + (i1 + 1) + "/" + i +
                            "\n\nNo hay pasajeros resgistrados en la fecha seleccionada");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
