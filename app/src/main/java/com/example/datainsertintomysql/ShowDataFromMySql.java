package com.example.datainsertintomysql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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


        String url = "https://maruf5682.000webhostapp.com/apps/view.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            for (int i = 0; i<response.length();i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String phone = jsonObject.getString("phone");
                                String email = jsonObject.getString("email");

                                hashMap = new HashMap<>();
                                hashMap.put("name",name);
                                hashMap.put("phone",phone);
                                hashMap.put("email",email);
                                arrayList.add(hashMap);

                                Toast.makeText(ShowDataFromMySql.this, email, Toast.LENGTH_SHORT).show();
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
            TextView nameText, emailText, phoneText;
            nameText = myView.findViewById(R.id.nameS);
            emailText = myView.findViewById(R.id.email);
            phoneText = myView.findViewById(R.id.phone);

            HashMap<String,String>hashMap1 = arrayList.get(i);
            String name = hashMap1.get("name");
            String phone = hashMap1.get("phone");
            String email = hashMap1.get("email");

            nameText.setText(name);
            phoneText.setText(phone);
            emailText.setText(email);

            return myView;
        }
    }
}