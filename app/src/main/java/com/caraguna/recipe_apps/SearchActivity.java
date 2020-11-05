package com.caraguna.recipe_apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.caraguna.recipe_apps.adapters.CategoryAdapter;
import com.caraguna.recipe_apps.adapters.SearchAdapter;
import com.caraguna.recipe_apps.models.CategoryModel;
import com.caraguna.recipe_apps.models.ListRecipeModel;
import com.caraguna.recipe_apps.settings.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnClose;

    // RecyclerView Search
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<ListRecipeModel> listData;
    private ListRecipeModel recipeModel;
    private SearchAdapter searchAdapter;

    // RecyclerView Category
    private RecyclerView rvCategory;
    private LinearLayoutManager lmCategory;
    private List<CategoryModel> dataCategory;
    private CategoryModel categoryModel;
    private CategoryAdapter categoryAdapter;

    // Interface
    private EditText eSearch;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnClose = findViewById(R.id.btnClose);
        eSearch = findViewById(R.id.eSearch);
        recyclerView = findViewById(R.id.rvSearch);
        rvCategory = findViewById(R.id.rvCategory);
        progressBar = findViewById(R.id.progressBar);

        eSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        Toast.makeText(SearchActivity.this, eSearch.getText(), Toast.LENGTH_SHORT).show();

                        return true; // consume.
                    }
                }
                return false;
            }
        });

//        eSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                getData(String.valueOf(s));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        getCategory();
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnClose:
                finish();
                break;
        }
    }

    private void getData(String params){
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.baseURLSearch + params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData= new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    if (jsonArray.length()>0){
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject data= jsonArray.getJSONObject(i);
                            recipeModel = new ListRecipeModel();
                            recipeModel.setTitle(data.getString("title"));
                            recipeModel.setKey(data.getString("key"));
                            recipeModel.setThumb(data.getString("thumb"));
                            listData.add(recipeModel);
                        }

                        linearLayoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);

                        searchAdapter = new SearchAdapter(SearchActivity.this, listData);
                        recyclerView.setAdapter(searchAdapter);
                        searchAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SearchActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void getCategory(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.baseURLCategory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataCategory = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        categoryModel = new CategoryModel();
                        categoryModel.setCategory(data.getString("category"));
                        categoryModel.setKey(data.getString("key"));
                        dataCategory.add(categoryModel);
                    }

                    lmCategory = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    rvCategory.setLayoutManager(lmCategory);

                    categoryAdapter = new CategoryAdapter(dataCategory, SearchActivity.this);
                    rvCategory.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(stringRequest);
    }
}
