package com.nep.mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nep.entity.AqiFeedback; //

import java.util.List;

public class PendingAqiAdapter extends RecyclerView.Adapter<PendingAqiAdapter.PendingAqiViewHolder> {

    private List<AqiFeedback> pendingAqiList;

    public PendingAqiAdapter(List<AqiFeedback> pendingAqiList) {
        this.pendingAqiList = pendingAqiList;
    }

    @NonNull
    @Override
    public PendingAqiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pending_aqi, parent, false); // Create item_pending_aqi.xml
        return new PendingAqiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingAqiViewHolder holder, int position) {
        AqiFeedback afb = pendingAqiList.get(position); //
        holder.afId.setText(String.valueOf(afb.getAfId())); //
        holder.afName.setText(afb.getAfName()); //
        holder.date.setText(afb.getDate()); //
        holder.estimateGrade.setText(afb.getEstimateGrade()); //
        holder.proviceName.setText(afb.getProviceName()); //
        holder.cityName.setText(afb.getCityName()); //
        holder.address.setText(afb.getAddress()); //
        holder.information.setText(afb.getInfomation()); //
    }

    @Override
    public int getItemCount() {
        return pendingAqiList.size();
    }

    public void updateData(List<AqiFeedback> newData) {
        this.pendingAqiList = newData;
        notifyDataSetChanged();
    }

    public static class PendingAqiViewHolder extends RecyclerView.ViewHolder {
        public TextView afId;
        public TextView afName;
        public TextView date;
        public TextView estimateGrade;
        public TextView proviceName;
        public TextView cityName;
        public TextView address;
        public TextView information;

        public PendingAqiViewHolder(View itemView) {
            super(itemView);
            afId = itemView.findViewById(R.id.text_pending_id);
            afName = itemView.findViewById(R.id.text_pending_af_name);
            date = itemView.findViewById(R.id.text_pending_date);
            estimateGrade = itemView.findViewById(R.id.text_pending_estimate_grade);
            proviceName = itemView.findViewById(R.id.text_pending_province);
            cityName = itemView.findViewById(R.id.text_pending_city);
            address = itemView.findViewById(R.id.text_pending_address);
            information = itemView.findViewById(R.id.text_pending_info);
        }
    }
}
