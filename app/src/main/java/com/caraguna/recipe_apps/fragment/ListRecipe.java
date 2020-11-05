package com.caraguna.recipe_apps.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.caraguna.recipe_apps.R;
import com.caraguna.recipe_apps.adapters.RecipeListAdapter;
import com.caraguna.recipe_apps.models.ListRecipeModel;
import com.caraguna.recipe_apps.settings.Configuration;
import com.caraguna.recipe_apps.settings.PgDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListRecipe extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecipeListAdapter recipeListAdapter;
    private List<ListRecipeModel> listData;
    private int pages = 1;
    private ListRecipeModel listRecipeModel;
    private ProgressDialog progressDialog;

    // Endless load recyclerview
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    public ListRecipe() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_recipe, container, false);

        recyclerView = view.findViewById(R.id.rvRecipes);
        progressDialog = new ProgressDialog(view.getContext());

        getListData(view.getContext());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Toast.makeText(getContext(), "You loaded", Toast.LENGTH_SHORT).show();
                            // Do pagination.. i.e. fetch new data

                            loading = true;
                        }
                    }
                }
            }
        });
        return view;
    }

    private void getListData(final Context context){
        PgDialog.show(progressDialog);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.baseURLRecipes + pages, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        listRecipeModel = new ListRecipeModel();
                        listRecipeModel.setTitle(data.getString("title"));
                        listRecipeModel.setThumb(data.getString("thumb"));
                        listRecipeModel.setKey(data.getString("key"));
                        listRecipeModel.setDificulty(data.getString("dificulty"));
                        listRecipeModel.setPortion(data.getString("portion"));
                        listRecipeModel.setTimes(data.getString("times"));
                        listData.add(listRecipeModel);
                    }

                    linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    recipeListAdapter = new RecipeListAdapter(context, listData);
                    recyclerView.setAdapter(recipeListAdapter);
                    recipeListAdapter.notifyDataSetChanged();
                    PgDialog.hide(progressDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
