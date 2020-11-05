package com.caraguna.recipe_apps.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caraguna.recipe_apps.DetailRecipe;
import com.caraguna.recipe_apps.R;
import com.caraguna.recipe_apps.models.ListRecipeModel;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.HolderData> {
    List<ListRecipeModel> listData;
    LayoutInflater inflater;
    Context context;

    public SearchAdapter(Context context, List<ListRecipeModel> listData) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_search, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.txtSearch.setText(listData.get(position).getTitle());

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

    public class HolderData extends RecyclerView.ViewHolder{
        TextView txtSearch;
        LinearLayout linear;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            txtSearch = itemView.findViewById(R.id.txtSearch);
            linear = itemView.findViewById(R.id.linearSearch);
        }
    }
}
