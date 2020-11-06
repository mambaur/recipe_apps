package com.caraguna.recipe_apps.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.caraguna.recipe_apps.DetailRecipe;
import com.caraguna.recipe_apps.R;
import com.caraguna.recipe_apps.models.ListRecipeModel;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.HolderData> {
    private List<ListRecipeModel> listData;
    private LayoutInflater inflater;
    private Context context;

    public RecipeListAdapter(Context context, List<ListRecipeModel> listData) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_recipe, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.txtTitle.setText(listData.get(position).getTitle());
        holder.txtPortion.setText(listData.get(position).getPortion());
        holder.txtDificulty.setText(listData.get(position).getDificulty());
        holder.txtTimes.setText(listData.get(position).getTimes());
        Glide.with(context).load(listData.get(position).getThumb()).into(holder.imgView);

        final String img = listData.get(position).getThumb();
        final String key = listData.get(position).getKey();
        final String title = listData.get(position).getTitle();
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailRecipe.class);
                intent.putExtra("key", key);
                intent.putExtra("img", img);
                intent.putExtra("title", title);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        ImageView imgView;
        TextView txtTitle, txtTimes, txtPortion, txtDificulty;
        LinearLayout linearLayout;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.idImg);
            txtTimes = itemView.findViewById(R.id.txtTime);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPortion = itemView.findViewById(R.id.txtPortion);
            txtDificulty = itemView.findViewById(R.id.txtDifficulty);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
