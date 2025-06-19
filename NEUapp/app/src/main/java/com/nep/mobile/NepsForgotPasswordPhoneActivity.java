package com.nep.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.nep.entity.Supervisor; //
import com.nep.service.SupervisorService; //
import com.nep.service.impl.SupervisorServiceImpl; //

public class NepsForgotPasswordPhoneActivity extends AppCompatActivity {

    private EditText txt_phone;
    private SupervisorService supervisorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neps_forgot_password_phone);

        txt_phone = findViewById(R.id.txt_phone);
        Button btnSubmitPhone = findViewById(R.id.btn_submit_phone);
        Button btnBack = findViewById(R.id.btn_back);

        supervisorService = new SupervisorServiceImpl();

        btnSubmitPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPhone();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void submitPhone() {
        String phone = txt_phone.getText().toString().trim();
        if (phone.isEmpty()) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Supervisor supervisor = supervisorService.getSupervisorByLoginCode(phone); //
        if (supervisor != null) {
            // 将找到的 supervisor 对象传递给下一个Activity
            Intent intent = new Intent(NepsForgotPasswordPhoneActivity.this, NepsForgotPasswordAnswerActivity.class);
            intent.putExtra("supervisorLoginCode", supervisor.getLoginCode()); // 传递登录账号，因为Supervisor对象可能很大
            startActivity(intent);
        } else {
            Toast.makeText(this, "未找到用户: 未找到该手机号对应的用户，请检查手机号是否正确", Toast.LENGTH_SHORT).show(); //
        }
    }

    private void back() {
        Intent intent = new Intent(NepsForgotPasswordPhoneActivity.this, NepsLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
