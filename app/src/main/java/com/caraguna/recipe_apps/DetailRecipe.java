package com.caraguna.recipe_apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.caraguna.recipe_apps.adapters.StepAdapter;
import com.caraguna.recipe_apps.settings.Common;
import com.caraguna.recipe_apps.settings.Configuration;
import com.caraguna.recipe_apps.settings.PgDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailRecipe extends AppCompatActivity implements View.OnClickListener {
    private String key, title, img;

    private RecyclerView rvStep;
    private LinearLayoutManager lmStep;
    private List<String> listStep;
    private StepAdapter stepAdapter;
    private ProgressDialog progressDialog;

    // interface
    private TextView txtTitle, txtDesc, txtIngr, txtStep, txtTime, txtDificulty, txtPortion, txtAuthor, txtDate, txtSelengkapnya;
    private ImageView imgView, btnBack;
    private LinearLayout linearBerbagi;

    // admob
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        imgView = findViewById(R.id.imgRecipe);
        btnBack = findViewById(R.id.btnBack);
        txtTitle = findViewById(R.id.titleBar);
        txtDesc = findViewById(R.id.txtDesc);
        txtIngr = findViewById(R.id.txtIngr);
        txtStep = findViewById(R.id.txtStep);
        txtTime = findViewById(R.id.txtTime);
        txtSelengkapnya = findViewById(R.id.txtSelengkapnya);
        txtDificulty = findViewById(R.id.txtDifficulty);
        txtPortion = findViewById(R.id.txtPortion);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtDate = findViewById(R.id.txtDate);
        rvStep = findViewById(R.id.rvStep);
        linearBerbagi = findViewById(R.id.linearBerbagi);
        adView = findViewById(R.id.adView);

        MobileAds.initialize(this);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        progressDialog = new ProgressDialog(this);

        key = getIntent().getStringExtra("key");
        img = getIntent().getStringExtra("img");
        title = getIntent().getStringExtra("title");

        txtTitle.setText(title);
        Glide.with(this).load(img).into(imgView);

        getDetail();

        btnBack.setOnClickListener(this);
        txtSelengkapnya.setOnClickListener(this);
        linearBerbagi.setOnClickListener(this);
    }

    private void getDetail(){
        PgDialog.show(progressDialog);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.baseURLRecipesDetail+key, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response).getJSONObject("results");

                    String bahan = "";
                    JSONArray ingr = jsonObject.getJSONArray("ingredient");
                    for (int i=0; i<ingr.length(); i++){
                        bahan += ingr.getString(i)+"\n";
                    }

                    JSONArray stepArray = jsonObject.getJSONArray("step");
                    listStep = new ArrayList<>();
                    for (int i=0; i<stepArray.length(); i++){
                        listStep.add(stepArray.getString(i));
                    }

                    // setup recyclerview
                    lmStep = new LinearLayoutManager(DetailRecipe.this, LinearLayoutManager.VERTICAL, false);
                    rvStep.setLayoutManager(lmStep);
                    stepAdapter = new StepAdapter(DetailRecipe.this, listStep);
                    rvStep.setAdapter(stepAdapter);
                    stepAdapter.notifyDataSetChanged();

                    txtIngr.setText(bahan);
                    txtDesc.setText(jsonObject.getString("desc"));
                    txtTime.setText(jsonObject.getString("times"));
                    txtDificulty.setText(jsonObject.getString("dificulty"));
                    txtPortion.setText(jsonObject.getString("servings"));
                    txtAuthor.setText(jsonObject.getJSONObject("author").getString("user"));
                    txtDate.setText(jsonObject.getJSONObject("author").getString("datePublished"));
                    PgDialog.hide(progressDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Common.volleyErrorHandle(DetailRecipe.this, error);
            }
        });
        requestQueue.add(stringRequest);
    }

    private void shareApps(){
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = "\n"+title+"\nDownload aplikasi masak sekarang juga!\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Masakan Indonesia");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Pilih salah satu"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.txtSelengkapnya:
                txtDesc.setEllipsize(null);
                txtDesc.setMaxLines(Integer.MAX_VALUE);
                txtSelengkapnya.setVisibility(View.GONE);
                break;
            case R.id.linearBerbagi:
                shareApps();
                break;
        }
    }
}
