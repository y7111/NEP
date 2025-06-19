package com.nep.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nep.entity.Aqi; //
import com.nep.entity.AqiFeedback; //
import com.nep.entity.ProvinceCity; //
import com.nep.entity.Supervisor; //
import com.nep.io.FileIO; //
import com.nep.service.AqiFeedbackService; //
import com.nep.service.SupervisorService; //
import com.nep.service.impl.AqiFeedbackServiceImpl; //
import com.nep.service.impl.SupervisorServiceImpl; //
import com.nep.util.CommonUtil; //


import java.util.ArrayList;
import java.util.List;

public class NepsSelectAqiActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAqi;
    private Spinner txt_province;
    private Spinner txt_city;
    private EditText txt_address;
    private Spinner txt_level;
    private EditText txt_information;
    private TextView label_realName;

    private AqiFeedbackService aqiFeedbackService;
    private SupervisorService supervisorService;
    private List<ProvinceCity> provinceCityList;
    private List<Aqi> aqiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neps_select_aqi);

        recyclerViewAqi = findViewById(R.id.recycler_view_aqi);
        txt_province = findViewById(R.id.txt_province);
        txt_city = findViewById(R.id.txt_city);
        txt_address = findViewById(R.id.txt_address);
        txt_level = findViewById(R.id.txt_level);
        txt_information = findViewById(R.id.txt_information);
        label_realName = findViewById(R.id.label_realName);
        Button btnSaveFeedback = findViewById(R.id.btn_save_feedback);
        Button btnFeedbackList = findViewById(R.id.btn_feedback_list);

        aqiFeedbackService = new AqiFeedbackServiceImpl();
        supervisorService = new SupervisorServiceImpl();

        // 获取当前登录的Supervisor信息
        Supervisor currentSupervisor = NepsLoginActivity.currentSupervisor;
        if (currentSupervisor != null) {
            label_realName.setText("欢迎，" + currentSupervisor.getRealName()); //
        } else {
            // 如果没有登录信息，跳转回登录界面
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NepsSelectAqiActivity.this, NepsLoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 初始化AQI列表
        aqiList = (List<Aqi>) FileIO.readObject("aqi.txt"); //
        if (aqiList == null) {
            aqiList = new ArrayList<>();
        }
        recyclerViewAqi.setLayoutManager(new LinearLayoutManager(this));
        AqiAdapter aqiAdapter = new AqiAdapter(aqiList);
        recyclerViewAqi.setAdapter(aqiAdapter);

        // 初始化预估AQI等级 Spinner
        List<String> levels = new ArrayList<>();
        levels.add("预估AQI等级"); // Placeholder
        for (Aqi aqi : aqiList) { //
            levels.add(aqi.getLevel()); //
        }
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, levels);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txt_level.setAdapter(levelAdapter);
        txt_level.setSelection(0); // 默认选中placeholder

        // 初始化省市区域 Spinner
        provinceCityList = (List<ProvinceCity>) FileIO.readObject("province_city.txt"); //
        if (provinceCityList == null) {
            provinceCityList = new ArrayList<>();
        }

        List<String> provinces = new ArrayList<>();
        provinces.add("请选择省区域"); // Placeholder
        for (ProvinceCity pc : provinceCityList) { //
            provinces.add(pc.getProvinceName()); //
        }
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, provinces);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txt_province.setAdapter(provinceAdapter);
        txt_province.setSelection(0);

        // 省市联动
        txt_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Avoid placeholder
                    String selectedProvince = parent.getItemAtPosition(position).toString();
                    List<String> cities = new ArrayList<>();
                    cities.add("请选择市区域"); // Placeholder

                    for (ProvinceCity pc : provinceCityList) { //
                        if (selectedProvince.equals(pc.getProvinceName())) { //
                            cities.addAll(pc.getCityName()); //
                            break;
                        }
                    }
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(NepsSelectAqiActivity.this,
                            android.R.layout.simple_spinner_item, cities);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    txt_city.setAdapter(cityAdapter);
                    txt_city.setSelection(0);
                } else {
                    // 如果选择了placeholder或者回到初始状态，清空城市列表
                    ArrayAdapter<String> emptyCityAdapter = new ArrayAdapter<>(NepsSelectAqiActivity.this,
                            android.R.layout.simple_spinner_item, new String[]{"请选择市区域"});
                    emptyCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    txt_city.setAdapter(emptyCityAdapter);
                    txt_city.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        btnSaveFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFeedBack(currentSupervisor);
            }
        });

        btnFeedbackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedBackList();
            }
        });
    }

    private void saveFeedBack(Supervisor currentSupervisor) {
        String provinceName = txt_province.getSelectedItem().toString();
        String cityName = txt_city.getSelectedItem().toString();
        String address = txt_address.getText().toString().trim();
        String estimateGrade = txt_level.getSelectedItem().toString();
        String information = txt_information.getText().toString().trim();

        if (provinceName.equals("请选择省区域") || cityName.equals("请选择市区域") || address.isEmpty() ||
                estimateGrade.equals("预估AQI等级") || information.isEmpty()) {
            Toast.makeText(this, "请填写所有必填信息", Toast.LENGTH_SHORT).show();
            return;
        }

        AqiFeedback afb = new AqiFeedback(); //
        afb.setAddress(address); //
        afb.setAfName(currentSupervisor.getRealName()); //
        afb.setProviceName(provinceName); //
        afb.setCityName(cityName); //
        afb.setEstimateGrade(estimateGrade); //
        afb.setInfomation(information); //
        afb.setDate(CommonUtil.currentDate()); //
        afb.setState("未指派"); //
        aqiFeedbackService.saveFeedBack(afb); //

        Toast.makeText(this, "您的预估AQI信息提交成功，感谢您的反馈!", Toast.LENGTH_LONG).show(); //
        Intent intent = new Intent(NepsSelectAqiActivity.this, NepsFeedbackListActivity.class);
        startActivity(intent);
        finish();
    }

    private void feedBackList() {
        Intent intent = new Intent(NepsSelectAqiActivity.this, NepsFeedbackListActivity.class);
        startActivity(intent);
    }
}
