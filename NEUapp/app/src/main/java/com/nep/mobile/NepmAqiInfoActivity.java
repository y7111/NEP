package com.nep.mobile;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nep.entity.AqiFeedback; //
import com.nep.io.FileIO; //

import java.util.ArrayList;
import java.util.List;

public class NepmAqiInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAqiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nepm_aqi_info);

        recyclerViewAqiInfo = findViewById(R.id.recycler_view_aqi_info);
        recyclerViewAqiInfo.setLayoutManager(new LinearLayoutManager(this));
        loadAqiInfoData();
    }

    private void loadAqiInfoData() {
        List<AqiFeedback> afList = (List<AqiFeedback>) FileIO.readObject("aqifeedback.txt"); //
        List<AqiFeedback> data = new ArrayList<>();
        if (afList != null) {
            for (AqiFeedback afb : afList) { //
                if (afb.getState() != null && afb.getState().equals("未指派")) { //
                    data.add(afb);
                }
            }
        }
        AqiFeedbackAdapter adapter = new AqiFeedbackAdapter(data);
        recyclerViewAqiInfo.setAdapter(adapter);
    }
}
