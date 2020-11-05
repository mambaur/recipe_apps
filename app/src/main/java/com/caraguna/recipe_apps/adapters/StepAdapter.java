package com.caraguna.recipe_apps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.caraguna.recipe_apps.R;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.HolderData> {
    private List<String> lsitData;
    LayoutInflater inflater;
    Context context;

    public StepAdapter(Context context, List<String> lsitData) {
        this.lsitData = lsitData;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_step, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.txtStep.setText(lsitData.get(position));
        if (position%2 == 0){
            holder.linearStep.setBackground(ContextCompat.getDrawable(context, R.color.colorLightGrey3));
        }
    }

    @Override
    public int getItemCount() {
        return lsitData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView txtStep;
        LinearLayout linearStep;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            txtStep = itemView.findViewById(R.id.txtStep);
            linearStep = itemView.findViewById(R.id.linearStep);
        }
    }
}
