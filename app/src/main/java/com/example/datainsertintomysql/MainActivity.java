package com.example.datainsertintomysql;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity implements ConnectionReceiver.ReceiverListener {

    Button sendBtn,showBtn;
    EditText edtName, edtPhone, edtEmail;
    ProgressBar progressBar;
    String name,email,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendBtn = findViewById(R.id.sendBtn);
        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        progressBar = findViewById(R.id.progressBar);
        showBtn = findViewById(R.id.showBtn);

        // Network validation ===============================
        checkConnection();




//        ===================================================================
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                phone = edtPhone.getText().toString();



                if(name.isEmpty() || email.isEmpty() || phone.isEmpty()){
                    Toast.makeText(MainActivity.this, "Input field is empty", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    sendDataOffline();

                    sendDataOnline();
                }
            }
        });

//        ===================================================================
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShowDataFromMySql.class);
                startActivity(intent);
            }
        });
    }


    // Send data online in MySql
    public void  sendDataOnline(){
        name = edtName.getText().toString();
        email = edtEmail.getText().toString();
        phone = edtPhone.getText().toString();
        String url = "https://maruf5682.000webhostapp.com/apps/data.php?n="+name+"&p="+phone+"&e="+email;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Server Response")
                                .setMessage(response)
                                .show();

                        clearInput();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error server", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    // Send data offline in SQLite
    public void sendDataOffline(){
        DBHelper dbHelper = new DBHelper(this);

        name = edtName.getText().toString();
        email = edtEmail.getText().toString();
        phone = edtPhone.getText().toString();

        Boolean check = dbHelper.insertData(name,phone,email);

        if (check){
            Toast.makeText(this, "offline data save", Toast.LENGTH_SHORT).show();
            clearInput();
        } else Toast.makeText(this, "offline no data save", Toast.LENGTH_SHORT).show();
    }

    // clear input method
    public void clearInput(){
        edtEmail.setText("");
        edtName.setText("");
        edtPhone.setText("");
    }


    // is network available
    private void checkConnection() {

        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        registerReceiver(new ConnectionReceiver(), intentFilter);

        // Initialize listener
        ConnectionReceiver.Listener = this;

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        // display toast
        makeToast(isConnected);

    }

    public void makeToast(boolean isConnected){
        if (isConnected){
            Toast.makeText(this, "INTERNET AVAILABLE", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "NO INTERNET", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        makeToast(isConnected);
    }
}