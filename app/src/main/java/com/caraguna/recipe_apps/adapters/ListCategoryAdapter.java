package com.caraguna.recipe_apps.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.caraguna.recipe_apps.DetailRecipe;
import com.caraguna.recipe_apps.R;
import com.caraguna.recipe_apps.models.ListRecipeModel;

import java.util.List;

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.HolderData> {
    private List<ListRecipeModel> listData;
    private LayoutInflater inflater;
    private Context context;

    public ListCategoryAdapter(List<ListRecipeModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_category, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.txtTitle.setText(listData.get(position).getTitle());
        holder.txtTimes.setText((listData.get(position).getTimes()));
        holder.txtDifficulty.setText(listData.get(position).getDificulty());
        holder.txtPortion.setText(listData.get(position).getPortion());

        Glide.with(context).load(listData.get(position).getThumb()).into(holder.img);

        final String img = listData.get(position).getThumb();
        final String key = listData.get(position).getKey();
        final String title = listData.get(position).getTitle();
        holder.linear.setOnClickListener(new View.OnClickListener() {
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

    public class HolderData  extends RecyclerView.ViewHolder{
        TextView txtTitle, txtTimes, txtPortion, txtDifficulty;
        ImageView img;
        LinearLayout linear;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            txtTimes = itemView.findViewById(R.id.txtTimes);
            txtPortion = itemView.findViewById(R.id.txtPortion);
            txtDifficulty = itemView.findViewById(R.id.txtDifficulty);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            img = itemView.findViewById(R.id.img);
            linear = itemView.findViewById(R.id.linearList);
        }
    }
}
