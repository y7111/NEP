package com.nep.mobile;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.nep.entity.AqiFeedback; //
import com.nep.entity.GridMember; //
import com.nep.io.FileIO; //
import com.nep.service.AqiFeedbackService; //
import com.nep.service.impl.AqiFeedbackServiceImpl; //

import java.util.ArrayList;
import java.util.List;

public class NepmAqiAssignActivity extends AppCompatActivity {

    private EditText txt_afId;
    private TextView label_afId;
    private TextView label_afName;
    private TextView label_proviceName;
    private TextView label_cityName;
    private TextView label_address;
    private TextView label_infomation;
    private TextView label_estimateGrade;
    private TextView label_date;
    private Spinner combo_realName;

    private AqiFeedbackService aqiFeedbackService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nepm_aqi_assign);

        txt_afId = findViewById(R.id.txt_afId);
        label_afId = findViewById(R.id.label_afId);
        label_afName = findViewById(R.id.label_afName);
        label_proviceName = findViewById(R.id.label_proviceName);
        label_cityName = findViewById(R.id.label_cityName);
        label_address = findViewById(R.id.label_address);
        label_infomation = findViewById(R.id.label_infomation);
        label_estimateGrade = findViewById(R.id.label_estimateGrade);
        label_date = findViewById(R.id.label_date);
        combo_realName = findViewById(R.id.combo_realName);
        Button btnQueryFeedback = findViewById(R.id.btn_query_feedback);
        Button btnAssignGridMember = findViewById(R.id.btn_assign_grid_member);

        aqiFeedbackService = new AqiFeedbackServiceImpl(); //

        initController(); // 初始化控件显示状态

        // 初始化网格员 Spinner
        List<GridMember> glist = (List<GridMember>) FileIO.readObject("gridmember.txt"); //
        List<String> gmNames = new ArrayList<>();
        gmNames.add("请选择网格员"); // Placeholder
        if (glist != null) {
            for (GridMember gm : glist) { //
                if (gm.getState() != null && gm.getState().equals("工作中")) { //
                    gmNames.add(gm.getRealName()); //
                }
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, gmNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        combo_realName.setAdapter(adapter);
        combo_realName.setSelection(0); // 默认选中placeholder


        btnQueryFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryFeedback();
            }
        });

        btnAssignGridMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignGridMember();
            }
        });
    }

    private void queryFeedback() {
        String afIdText = txt_afId.getText().toString().trim();
        if (afIdText.isEmpty()) {
            Toast.makeText(this, "请输入反馈信息编号", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Integer afId = Integer.parseInt(afIdText);
            List<AqiFeedback> alist = (List<AqiFeedback>) FileIO.readObject("aqifeedback.txt"); //
            boolean found = false;
            if (alist != null) {
                for (AqiFeedback af : alist) { //
                    if (af.getAfId() != null && af.getAfId().equals(afId) && af.getState().equals("未指派")) { //
                        found = true;
                        label_afId.setText(String.valueOf(af.getAfId())); //
                        label_afName.setText(af.getAfName()); //
                        label_address.setText(af.getAddress()); //
                        label_cityName.setText(af.getCityName()); //
                        label_date.setText(af.getDate()); //
                        label_estimateGrade.setText(af.getEstimateGrade()); //
                        label_infomation.setText(af.getInfomation()); //
                        label_proviceName.setText(af.getProviceName()); //
                        break;
                    }
                }
            }

            if (!found) {
                Toast.makeText(this, "查询失败: 未找到当前编号反馈信息或已指派", Toast.LENGTH_SHORT).show(); //
                initController();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的反馈编号", Toast.LENGTH_SHORT).show();
        }
    }

    private void assignGridMember() {
        if (label_afId.getText().toString().equals("无")) {
            Toast.makeText(this, "指派失败: 未找到要指派的反馈信息，请选择要指派的反馈信息", Toast.LENGTH_SHORT).show(); //
            return;
        }
        if (combo_realName.getSelectedItem().toString().equals("请选择网格员")) {
            Toast.makeText(this, "指派失败: 您没有选择要指派的网格员，请选择您要指派的网格员", Toast.LENGTH_SHORT).show(); //
            return;
        }

        String afId = label_afId.getText().toString();
        String realName = combo_realName.getSelectedItem().toString();
        aqiFeedbackService.assignGridMember(afId, realName); //
        Toast.makeText(this, "指派成功: AQI反馈信息指派成功! 请等待网格员实测数据信息", Toast.LENGTH_LONG).show(); //
        initController(); // 指派成功后重置界面
    }

    // 界面控件初始化方法
    private void initController() {
        txt_afId.setText("");
        label_afId.setText("无");
        label_afName.setText("无");
        label_address.setText("无");
        label_cityName.setText("无");
        label_date.setText("无");
        label_estimateGrade.setText("无");
        label_infomation.setText("无");
        label_proviceName.setText("无");
        // Spinner的默认值已经在Adapter中设置
    }
}
