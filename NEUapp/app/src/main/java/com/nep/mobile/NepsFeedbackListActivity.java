package com.nep.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nep.entity.AqiFeedback; //
import com.nep.entity.Supervisor; //
import com.nep.io.FileIO; //

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NepsFeedbackListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFeedback;
    private TextView txt_realName;
    private Supervisor currentSupervisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neps_feedback_list);

        recyclerViewFeedback = findViewById(R.id.recycler_view_feedback);
        txt_realName = findViewById(R.id.txt_realName);
        Button btnBack = findViewById(R.id.btn_back);

        currentSupervisor = NepsLoginActivity.currentSupervisor;
        if (currentSupervisor != null) {
            txt_realName.setText("欢迎，" + currentSupervisor.getRealName()); //
        } else {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NepsFeedbackListActivity.this, NepsLoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 初始化 RecyclerView
        recyclerViewFeedback.setLayoutManager(new LinearLayoutManager(this));
        loadFeedbackData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void loadFeedbackData() {
        List<AqiFeedback> afList = (List<AqiFeedback>) FileIO.readObject("aqifeedback.txt"); //
        if (afList == null) {
            afList = new ArrayList<>();
        }

        List<AqiFeedback> myFeedbackList = new ArrayList<>();
        // 过滤出当前用户提交的反馈，并按时间倒序排列
        // 原JavaFX代码中的注释 if(afb.getAfName().equals(supervisor.getRealName())){ data.add(afb); } 被注释掉了。
        // 这里我假设你可能希望显示所有反馈，或者根据登录用户进行过滤。
        // 如果要过滤，取消下面 if 语句的注释
        for (int i = afList.size() - 1; i >= 0; i--) { // 按照时间排序,有近到远
            AqiFeedback afb = afList.get(i); //
            if (afb.getAfName() != null && currentSupervisor != null && afb.getAfName().equals(currentSupervisor.getRealName())) { //
                myFeedbackList.add(afb);
            }
        }

        AqiFeedbackAdapter adapter = new AqiFeedbackAdapter(myFeedbackList);
        recyclerViewFeedback.setAdapter(adapter);
    }

    private void back() {
        Intent intent = new Intent(NepsFeedbackListActivity.this, NepsSelectAqiActivity.class);
        startActivity(intent);
        finish();
    }
}
