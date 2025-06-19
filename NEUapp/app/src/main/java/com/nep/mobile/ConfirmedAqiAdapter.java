package com.nep.mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nep.entity.AqiFeedback; //

import java.util.List;

public class ConfirmedAqiAdapter extends RecyclerView.Adapter<ConfirmedAqiAdapter.ConfirmedAqiViewHolder> {

    private List<AqiFeedback> confirmedAqiList;

    public ConfirmedAqiAdapter(List<AqiFeedback> confirmedAqiList) {
        this.confirmedAqiList = confirmedAqiList;
    }

    @NonNull
    @Override
    public ConfirmedAqiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_confirmed_aqi, parent, false); // Create item_confirmed_aqi.xml
        return new ConfirmedAqiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmedAqiViewHolder holder, int position) {
        AqiFeedback afb = confirmedAqiList.get(position); //
        holder.afId.setText(String.valueOf(afb.getAfId())); //
        holder.proviceName.setText(afb.getProviceName()); //
        holder.cityName.setText(afb.getCityName()); //
        holder.estimateGrade.setText(afb.getEstimateGrade()); //
        holder.date.setText(afb.getDate()); //
        holder.afName.setText(afb.getAfName()); //
        holder.so2.setText(String.valueOf(afb.getSo2())); //
        holder.co.setText(String.valueOf(afb.getCo())); //
        holder.pm.setText(String.valueOf(afb.getPm())); //
        holder.confirmLevel.setText(afb.getConfirmLevel()); //
        holder.confirmExplain.setText(afb.getConfirmExplain()); //
        holder.confirmDate.setText(afb.getConfirmDate()); //
        holder.gmName.setText(afb.getGmName()); //
    }

    @Override
    public int getItemCount() {
        return confirmedAqiList.size();
    }

    public static class ConfirmedAqiViewHolder extends RecyclerView.ViewHolder {
        public TextView afId;
        public TextView proviceName;
        public TextView cityName;
        public TextView estimateGrade;
        public TextView date;
        public TextView afName;
        public TextView so2;
        public TextView co;
        public TextView pm;
        public TextView confirmLevel;
        public TextView confirmExplain;
        public TextView confirmDate;
        public TextView gmName;

        public ConfirmedAqiViewHolder(View itemView) {
            super(itemView);
            afId = itemView.findViewById(R.id.text_confirmed_id);
            proviceName = itemView.findViewById(R.id.text_confirmed_province);
            cityName = itemView.findViewById(R.id.text_confirmed_city);
            estimateGrade = itemView.findViewById(R.id.text_confirmed_estimate_grade);
            date = itemView.findViewById(R.id.text_confirmed_date);
            afName = itemView.findViewById(R.id.text_confirmed_af_name);
            so2 = itemView.findViewById(R.id.text_confirmed_so2);
            co = itemView.findViewById(R.id.text_confirmed_co);
            pm = itemView.findViewById(R.id.text_confirmed_pm);
            confirmLevel = itemView.findViewById(R.id.text_confirmed_level);
            confirmExplain = itemView.findViewById(R.id.text_confirmed_explain);
            confirmDate = itemView.findViewById(R.id.text_confirmed_confirm_date);
            gmName = itemView.findViewById(R.id.text_confirmed_gm_name);
        }
    }
}
