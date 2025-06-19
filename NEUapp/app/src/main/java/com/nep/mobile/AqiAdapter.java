package com.nep.mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nep.entity.Aqi; //

import java.util.List;

public class AqiAdapter extends RecyclerView.Adapter<AqiAdapter.AqiViewHolder> {

    private List<Aqi> aqiList;

    public AqiAdapter(List<Aqi> aqiList) {
        this.aqiList = aqiList;
    }

    @NonNull
    @Override
    public AqiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aqi, parent, false); // Create item_aqi.xml
        return new AqiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AqiViewHolder holder, int position) {
        Aqi aqi = aqiList.get(position); //
        holder.level.setText(aqi.getLevel()); //
        holder.explain.setText(aqi.getExplain()); //
        holder.impact.setText(aqi.getImpact()); //
    }

    @Override
    public int getItemCount() {
        return aqiList.size();
    }

    public static class AqiViewHolder extends RecyclerView.ViewHolder {
        public TextView level;
        public TextView explain;
        public TextView impact;

        public AqiViewHolder(View itemView) {
            super(itemView);
            level = itemView.findViewById(R.id.text_aqi_level);
            explain = itemView.findViewById(R.id.text_aqi_explain);
            impact = itemView.findViewById(R.id.text_aqi_impact);
        }
    }
}
