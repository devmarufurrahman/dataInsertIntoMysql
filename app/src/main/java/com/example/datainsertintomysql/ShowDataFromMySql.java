package com.example.datainsertintomysql;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowDataFromMySql extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    ArrayList <HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String,String> hashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data_from_my_sql);
        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);

        loadData();

    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View myView = layoutInflater.inflate(R.layout.user_info,null);
            TextView nameText, emailText, phoneText,idText;
            Button updateBtn,deleteBtn;
            idText = myView.findViewById(R.id.id);
            nameText = myView.findViewById(R.id.nameS);
            emailText = myView.findViewById(R.id.email);
            phoneText = myView.findViewById(R.id.phone);
            updateBtn = myView.findViewById(R.id.update);
            deleteBtn = myView.findViewById(R.id.delete);

            HashMap<String,String>hashMap1 = arrayList.get(i);
            String id = hashMap1.get("id");
            String name = hashMap1.get("name");
            String phone = hashMap1.get("phone");
            String email = hashMap1.get("email");

            idText.setText(id);
            nameText.setText(name);
            phoneText.setText(phone);
            emailText.setText(email);

            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog updateDialog = new Dialog(ShowDataFromMySql.this);
                    updateDialog.setContentView(R.layout.update_dialog);
                    updateDialog.setCancelable(true);
                    updateDialog.show();


                    EditText editName = updateDialog.findViewById(R.id.editName);
                    EditText editNumber = updateDialog.findViewById(R.id.editNumber);
                    EditText editEmail = updateDialog.findViewById(R.id.editEmail);
                    Button updateSave = updateDialog.findViewById(R.id.updateSave);

                    editEmail.setText(email);
                    editName.setText(name);
                    editNumber.setText(phone);

                    updateSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           String phone = editNumber.getText().toString();
                           String email = editEmail.getText().toString();
                           String name = editName.getText().toString();
                            String url = "https://maruf5682.000webhostapp.com/apps/update.php?id="+ id +"&name="+ name +"&email="+ email +"&phone="+ phone;

                            progressBar.setVisibility(View.VISIBLE);

                            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ShowDataFromMySql.this, "Input is empty", Toast.LENGTH_SHORT).show();
                            } else {
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                progressBar.setVisibility(View.GONE);
                                                new AlertDialog.Builder(ShowDataFromMySql.this)
                                                        .setTitle("Server Response")
                                                        .setMessage(response)
                                                        .show();
                                                loadData();
                                                updateDialog.dismiss();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });

                                RequestQueue requestQueue = Volley.newRequestQueue(ShowDataFromMySql.this);
                                requestQueue.add(stringRequest);
                            }

                        }
                    });



                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ShowDataFromMySql.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                    String url = "https://maruf5682.000webhostapp.com/apps/delete.php?id="+ id;

                    StringRequest deleteRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    new AlertDialog.Builder(ShowDataFromMySql.this)
                                            .setTitle("Server Response")
                                            .setMessage(response)
                                            .show();
                                    loadData();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ShowDataFromMySql.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(ShowDataFromMySql.this);
                    requestQueue.add(deleteRequest);
                }
            });

            return myView;
        }
    }


    private void loadData(){
        arrayList = new ArrayList<>();

        String url = "https://maruf5682.000webhostapp.com/apps/view.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            for (int i = 0; i<response.length();i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String phone = jsonObject.getString("phone");
                                String email = jsonObject.getString("email");

                                hashMap = new HashMap<>();
                                hashMap.put("id",id);
                                hashMap.put("name",name);
                                hashMap.put("phone",phone);
                                hashMap.put("email",email);
                                arrayList.add(hashMap);


                            }

                            MyAdapter myAdapter = new MyAdapter();
                            listView.setAdapter(myAdapter);
                            Toast.makeText(ShowDataFromMySql.this, "successfully done", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            Log.d("errorCatch", String.valueOf(e));
                            Toast.makeText(ShowDataFromMySql.this, "errorCatch", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", String.valueOf(error));
                Toast.makeText(ShowDataFromMySql.this, "error", Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ShowDataFromMySql.this);
        requestQueue.add(jsonArrayRequest);
    }
}