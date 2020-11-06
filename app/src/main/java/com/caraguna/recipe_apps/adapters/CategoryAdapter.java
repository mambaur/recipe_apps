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
import com.caraguna.recipe_apps.ListCategory;
import com.caraguna.recipe_apps.R;
import com.caraguna.recipe_apps.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.HolderData> {
    private List<CategoryModel> listData;
    private LayoutInflater inflater;
    private Context context;

    public CategoryAdapter(List<CategoryModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.txtCategory.setText(listData.get(position).getCategory());
        final String key = listData.get(position).getKey();
        final String categoryName = listData.get(position).getCategory();
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListCategory.class);
                intent.putExtra("key", key);
                intent.putExtra("categoryName", categoryName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView txtCategory;
        LinearLayout linearLayout;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            linearLayout = itemView.findViewById(R.id.linearCategory);
        }
    }
}
