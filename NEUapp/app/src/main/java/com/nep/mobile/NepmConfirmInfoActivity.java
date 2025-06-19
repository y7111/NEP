package com.nep.mobile;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nep.entity.AqiFeedback; //
import com.nep.io.FileIO; //

import java.util.ArrayList;
import java.util.List;

public class NepmConfirmInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewConfirmInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nepm_confirm_info);

        recyclerViewConfirmInfo = findViewById(R.id.recycler_view_confirm_info);
        recyclerViewConfirmInfo.setLayoutManager(new LinearLayoutManager(this));
        loadConfirmInfoData();
    }

    private void loadConfirmInfoData() {
        List<AqiFeedback> afList = (List<AqiFeedback>) FileIO.readObject("aqifeedback.txt"); //
        List<AqiFeedback> data = new ArrayList<>();
        if (afList != null) {
            for (AqiFeedback afb : afList) { //
                if (afb.getState() != null && afb.getState().equals("已实测")) { //
                    data.add(afb);
                }
            }
        }
        ConfirmedAqiAdapter adapter = new ConfirmedAqiAdapter(data);
        recyclerViewConfirmInfo.setAdapter(adapter);
    }
}
