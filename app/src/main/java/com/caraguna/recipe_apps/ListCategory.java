package com.caraguna.recipe_apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.caraguna.recipe_apps.adapters.ListCategoryAdapter;
import com.caraguna.recipe_apps.models.ListRecipeModel;
import com.caraguna.recipe_apps.settings.Configuration;
import com.caraguna.recipe_apps.settings.PgDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListCategory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<ListRecipeModel> listData;
    private ListRecipeModel listRecipeModel;
    private String key;
    private ListCategoryAdapter adapter;

    private ProgressDialog progressDialog;

    // Interface
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);

        recyclerView = findViewById(R.id.rvListCategory);
        imgBack = findViewById(R.id.imgBack);

        progressDialog = new ProgressDialog(this);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        key = getIntent().getStringExtra("key");
        getList();
    }

    private void getList(){
        PgDialog.show(progressDialog);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.baseURLDetailCategory+key, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        listRecipeModel = new ListRecipeModel();
                        listRecipeModel.setThumb(data.getString("thumb"));
                        listRecipeModel.setTitle(data.getString("title"));
                        listRecipeModel.setKey(data.getString("key"));
                        listRecipeModel.setTimes(data.getString("times"));
                        listRecipeModel.setPortion(data.getString("portion"));
                        listRecipeModel.setDificulty(data.getString("dificulty"));
                        listData.add(listRecipeModel);
                    }

                    linearLayoutManager = new LinearLayoutManager(ListCategory.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    adapter = new ListCategoryAdapter(listData, ListCategory.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    PgDialog.hide(progressDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                    PgDialog.hide(progressDialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PgDialog.hide(progressDialog);
                error.printStackTrace();
            }
        });
        requestQueue.add(stringRequest);
    }
}
