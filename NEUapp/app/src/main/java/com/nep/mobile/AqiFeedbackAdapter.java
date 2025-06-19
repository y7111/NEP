package com.nep.mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nep.entity.AqiFeedback; //

import java.util.List;

public class AqiFeedbackAdapter extends RecyclerView.Adapter<AqiFeedbackAdapter.AqiFeedbackViewHolder> {

    private List<AqiFeedback> feedbackList;

    public AqiFeedbackAdapter(List<AqiFeedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public AqiFeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aqi_feedback, parent, false); // Create item_aqi_feedback.xml
        return new AqiFeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AqiFeedbackViewHolder holder, int position) {
        AqiFeedback afb = feedbackList.get(position); //
        holder.afId.setText(String.valueOf(afb.getAfId())); //
        holder.proviceName.setText(afb.getProviceName()); //
        holder.cityName.setText(afb.getCityName()); //
        holder.estimateGrade.setText(afb.getEstimateGrade()); //
        holder.date.setText(afb.getDate()); //
        holder.information.setText(afb.getInfomation()); //
        // You might want to display other fields like state, gmName, confirmLevel etc.
        // For simplicity, I'm sticking to the original TableView columns.
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class AqiFeedbackViewHolder extends RecyclerView.ViewHolder {
        public TextView afId;
        public TextView proviceName;
        public TextView cityName;
        public TextView estimateGrade;
        public TextView date;
        public TextView information;

        public AqiFeedbackViewHolder(View itemView) {
            super(itemView);
            afId = itemView.findViewById(R.id.text_feedback_id);
            proviceName = itemView.findViewById(R.id.text_feedback_province);
            cityName = itemView.findViewById(R.id.text_feedback_city);
            estimateGrade = itemView.findViewById(R.id.text_feedback_estimate_grade);
            date = itemView.findViewById(R.id.text_feedback_date);
            information = itemView.findViewById(R.id.text_feedback_info);
        }
    }
}
