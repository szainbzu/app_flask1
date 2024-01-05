package com.example.app_flask1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private Button btnOpen;
    private Button btnGet;
    private EditText edtCat;
    private ListView lstResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstResults = findViewById(R.id.lstBooks);
        btnGet = findViewById(R.id.btnGet);
        btnOpen = findViewById(R.id.btnOpen);
        edtCat = findViewById(R.id.edtCat);
        queue = Volley.newRequestQueue(this);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCat = edtCat.getText().toString().trim();

                if(strCat.isEmpty()){
                    return;
                }

                String url = "http://10.0.2.2:5000/getbook/"+strCat;
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                        null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<String> books = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                books.add( obj.getString("title"));
                            }catch(JSONException exception){
                                Log.d("Error", exception.toString());
                            }
                        }
                        String[] arr = new String[books.size()];
                        arr = books.toArray(arr);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                                android.R.layout.simple_list_item_1, arr);
                        lstResults.setAdapter(adapter);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this, error.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("Error_json", error.toString());
                    }
                });

                queue.add(request);
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });


    }
}