package com.nep.mobile;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nep.dto.AqiLimitDto; //
import com.nep.entity.AqiFeedback; //
import com.nep.entity.GridMember; //
import com.nep.io.FileIO; //
import com.nep.service.AqiFeedbackService; //
import com.nep.service.impl.AqiFeedbackServiceImpl; //
import com.nep.util.CommonUtil; //

import java.util.ArrayList;
import java.util.List;

public class NepgAqiConfirmActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPendingAqi;
    private EditText txt_afId;
    private EditText txt_so2;
    private EditText txt_co;
    private EditText txt_pm;
    private TextView label_so2level;
    private TextView label_so2explain;
    private TextView label_colevel;
    private TextView label_coexplain;
    private TextView label_pmlevel;
    private TextView label_pmexplain;
    private TextView label_confirmlevel;
    private TextView label_confirmexplain;
    private TextView label_realName;

    private AqiFeedbackService aqiFeedbackService;
    private int so2level = 0;
    private int colevel = 0;
    private int pmlevel = 0;
    private AqiLimitDto confirmDto;
    private PendingAqiAdapter pendingAqiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nepg_aqi_confirm);

        recyclerViewPendingAqi = findViewById(R.id.recycler_view_pending_aqi);
        txt_afId = findViewById(R.id.txt_afId);
        txt_so2 = findViewById(R.id.txt_so2);
        txt_co = findViewById(R.id.txt_co);
        txt_pm = findViewById(R.id.txt_pm);
        label_so2level = findViewById(R.id.label_so2level);
        label_so2explain = findViewById(R.id.label_so2explain);
        label_colevel = findViewById(R.id.label_colevel);
        label_coexplain = findViewById(R.id.label_coexplain);
        label_pmlevel = findViewById(R.id.label_pmlevel);
        label_pmexplain = findViewById(R.id.label_pmexplain);
        label_confirmlevel = findViewById(R.id.label_confirmlevel);
        label_confirmexplain = findViewById(R.id.label_confirmexplain);
        label_realName = findViewById(R.id.label_realName);
        Button btnConfirmData = findViewById(R.id.btn_confirm_data);
        Button btnResetFields = findViewById(R.id.btn_reset_fields);

        aqiFeedbackService = new AqiFeedbackServiceImpl(); //

        // 初始化网格员姓名
        GridMember currentGridMember = NepgLoginActivity.currentGridMember;
        if (currentGridMember != null) {
            label_realName.setText("欢迎，" + currentGridMember.getRealName()); //
        } else {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish(); // 返回登录界面
            return;
        }


        // 初始化RecyclerView
        recyclerViewPendingAqi.setLayoutManager(new LinearLayoutManager(this));
        loadPendingAqiData(currentGridMember);


        // 添加编号文本框事件监听
        txt_afId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) { // 当失去焦点时
                    String afIdText = txt_afId.getText().toString();
                    if (!afIdText.isEmpty()) {
                        try {
                            Integer afId = Integer.parseInt(afIdText);
                            boolean found = false;
                            List<AqiFeedback> afList = (List<AqiFeedback>) FileIO.readObject("aqifeedback.txt"); //
                            if (afList != null) {
                                for (AqiFeedback afb : afList) { //
                                    if (afb.getAfId() != null && afb.getAfId().equals(afId) &&
                                            afb.getGmName() != null && afb.getGmName().equals(currentGridMember.getRealName()) &&
                                            afb.getState() != null && afb.getState().equals("已指派")) { //
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            if (!found) {
                                Toast.makeText(NepgAqiConfirmActivity.this, "数据错误: AQI反馈数据编号有误或未指派给您", Toast.LENGTH_SHORT).show(); //
                                txt_afId.setText("");
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(NepgAqiConfirmActivity.this, "数据错误: 无效的反馈编号", Toast.LENGTH_SHORT).show();
                            txt_afId.setText("");
                        }
                    }
                }
            }
        });

        // 浓度输入框的文本变化监听
        txt_so2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateAqiLevels();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        txt_co.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateAqiLevels();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        txt_pm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateAqiLevels();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnConfirmData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmData(currentGridMember);
            }
        });

        btnResetFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    }

    private void loadPendingAqiData(GridMember gridMember) {
        List<AqiFeedback> afList = (List<AqiFeedback>) FileIO.readObject("aqifeedback.txt"); //
        List<AqiFeedback> data = new ArrayList<>();
        if (afList != null) {
            for (AqiFeedback afb : afList) { //
                if (afb.getGmName() != null && afb.getGmName().equals(gridMember.getRealName()) && afb.getState().equals("已指派")) { //
                    data.add(afb);
                }
            }
        }
        pendingAqiAdapter = new PendingAqiAdapter(data);
        recyclerViewPendingAqi.setAdapter(pendingAqiAdapter);
    }

    private void calculateAqiLevels() {
        try {
            double so2Value = txt_so2.getText().toString().isEmpty() ? 0 : Double.parseDouble(txt_so2.getText().toString());
            double coValue = txt_co.getText().toString().isEmpty() ? 0 : Double.parseDouble(txt_co.getText().toString());
            double pmValue = txt_pm.getText().toString().isEmpty() ? 0 : Double.parseDouble(txt_pm.getText().toString());

            AqiLimitDto so2Dto = CommonUtil.so2Limit(so2Value); //
            AqiLimitDto coDto = CommonUtil.coLimit(coValue); //
            AqiLimitDto pmDto = CommonUtil.pmLimit(pmValue); //

            if (so2Dto != null) {
                label_so2level.setText(so2Dto.getLevel()); //
                label_so2level.setTextColor(Color.parseColor(so2Dto.getColor())); //
                label_so2explain.setText(so2Dto.getExplain()); //
                label_so2explain.setBackgroundColor(Color.parseColor(so2Dto.getColor())); //
                so2level = so2Dto.getIntlevel(); //
            } else {
                label_so2level.setText("无");
                label_so2level.setTextColor(Color.BLACK);
                label_so2explain.setText("");
                label_so2explain.setBackgroundColor(Color.TRANSPARENT);
                so2level = 0;
            }

            if (coDto != null) {
                label_colevel.setText(coDto.getLevel()); //
                label_colevel.setTextColor(Color.parseColor(coDto.getColor())); //
                label_coexplain.setText(coDto.getExplain()); //
                label_coexplain.setBackgroundColor(Color.parseColor(coDto.getColor())); //
                colevel = coDto.getIntlevel(); //
            } else {
                label_colevel.setText("无");
                label_colevel.setTextColor(Color.BLACK);
                label_coexplain.setText("");
                label_coexplain.setBackgroundColor(Color.TRANSPARENT);
                colevel = 0;
            }

            if (pmDto != null) {
                label_pmlevel.setText(pmDto.getLevel()); //
                label_pmlevel.setTextColor(Color.parseColor(pmDto.getColor())); //
                label_pmexplain.setText(pmDto.getExplain()); //
                label_pmexplain.setBackgroundColor(Color.parseColor(pmDto.getColor())); //
                pmlevel = pmDto.getIntlevel(); //
            } else {
                label_pmlevel.setText("无");
                label_pmlevel.setTextColor(Color.BLACK);
                label_pmexplain.setText("");
                label_pmexplain.setBackgroundColor(Color.TRANSPARENT);
                pmlevel = 0;
            }

            // 计算最终AQI等级
            confirmDto = CommonUtil.confirmLevel(so2level, colevel, pmlevel); //
            if (confirmDto != null) {
                label_confirmlevel.setText(confirmDto.getLevel()); //
                label_confirmlevel.setTextColor(Color.parseColor(confirmDto.getColor())); //
                label_confirmexplain.setText(confirmDto.getExplain()); //
                label_confirmexplain.setBackgroundColor(Color.parseColor(confirmDto.getColor())); //
            } else {
                label_confirmlevel.setText("无");
                label_confirmlevel.setTextColor(Color.BLACK);
                label_confirmexplain.setText("");
                label_confirmexplain.setBackgroundColor(Color.TRANSPARENT);
            }

        } catch (NumberFormatException e) {
            // 输入无效数字，保持原有状态或显示错误
            Toast.makeText(this, "请输入有效的数字浓度值", Toast.LENGTH_SHORT).show();
            resetAqiLabels(); // 重置标签显示
        }
    }

    private void resetAqiLabels() {
        label_so2level.setText("无");
        label_so2level.setTextColor(Color.BLACK);
        label_so2explain.setText("");
        label_so2explain.setBackgroundColor(Color.TRANSPARENT);

        label_colevel.setText("无");
        label_colevel.setTextColor(Color.BLACK);
        label_coexplain.setText("");
        label_coexplain.setBackgroundColor(Color.TRANSPARENT);

        label_pmlevel.setText("无");
        label_pmlevel.setTextColor(Color.BLACK);
        label_pmexplain.setText("");
        label_pmexplain.setBackgroundColor(Color.TRANSPARENT);

        label_confirmlevel.setText("无");
        label_confirmlevel.setTextColor(Color.BLACK);
        label_confirmexplain.setText("");
        label_confirmexplain.setBackgroundColor(Color.TRANSPARENT);
    }


    private void confirmData(GridMember currentGridMember) {
        String afIdText = txt_afId.getText().toString().trim();
        String so2Text = txt_so2.getText().toString().trim();
        String coText = txt_co.getText().toString().trim();
        String pmText = txt_pm.getText().toString().trim();

        if (afIdText.isEmpty() || so2Text.isEmpty() || coText.isEmpty() || pmText.isEmpty()) {
            Toast.makeText(this, "请填写所有实测数据", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Integer afId = Integer.parseInt(afIdText);
            Double so2 = Double.parseDouble(so2Text);
            Double co = Double.parseDouble(coText);
            Double pm = Double.parseDouble(pmText);

            if (confirmDto == null || confirmDto.getLevel() == null || confirmDto.getExplain() == null) { //
                Toast.makeText(this, "请先输入有效的污染物浓度以计算AQI等级", Toast.LENGTH_SHORT).show();
                return;
            }

            AqiFeedback afb = new AqiFeedback(); //
            afb.setAfId(afId); //
            afb.setState("已实测"); //
            afb.setSo2(so2); //
            afb.setCo(co); //
            afb.setPm(pm); //
            afb.setConfirmDate(CommonUtil.currentDate()); //
            afb.setConfirmLevel(confirmDto.getLevel()); //
            afb.setConfirmExplain(confirmDto.getExplain()); //
            afb.setGmName(currentGridMember.getRealName()); //

            aqiFeedbackService.confirmData(afb); //
            Toast.makeText(this, "污染物实测数据提交成功", Toast.LENGTH_SHORT).show(); //

            // 刷新页面数据表格
            loadPendingAqiData(currentGridMember);
            reset();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的数字浓度值和反馈编号", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 文本框和标签内容重置
     */
    public void reset() {
        txt_afId.setText("");
        txt_so2.setText("");
        txt_co.setText("");
        txt_pm.setText("");
        resetAqiLabels();
    }
}
