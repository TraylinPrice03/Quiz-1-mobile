package com.example.userapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnFetch;
    ProgressBar progressBar;
    List<User> userList;
    UserAdapter adapter;

    private static final String API_URL = "https://jsonplaceholder.typicode.com/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btnFetch = findViewById(R.id.btnFetch);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        adapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        btnFetch.setOnClickListener(v -> fetchUsers());
    }

    private void fetchUsers() {
        progressBar.setVisibility(View.VISIBLE);
        userList.clear();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                API_URL,
                null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    parseJSON(response);
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to fetch data.", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void parseJSON(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject userObj = response.getJSONObject(i);
                String name = userObj.getString("name");
                String email = userObj.getString("email");
                String phone = userObj.getString("phone");

                userList.add(new User(name, email, phone));
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Error parsing data.", Toast.LENGTH_SHORT).show();
        }
    }
}
