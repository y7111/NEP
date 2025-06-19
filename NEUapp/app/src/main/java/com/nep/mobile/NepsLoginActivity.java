package com.nep.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.nep.entity.Supervisor;
import com.nep.service.SupervisorService;
import com.nep.service.impl.SupervisorServiceImpl;

public class NepsLoginActivity extends AppCompatActivity {

    private EditText txt_id;
    private EditText txt_password;
    private SupervisorService service;

    // 当前登录成功的公众监督员用户身份，静态变量用于跨Activity共享
    public static Supervisor currentSupervisor; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neps_login);

        txt_id = findViewById(R.id.txt_id);
        txt_password = findViewById(R.id.txt_password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);
        Button btnForgotPassword = findViewById(R.id.btn_forgot_password);

        service = new SupervisorServiceImpl(); //

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
    }

    private void login() {
        boolean isLogin = service.login(txt_id.getText().toString(), txt_password.getText().toString()); //

        if (isLogin) {
            Toast.makeText(NepsLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show(); //
            // 设置当前登录的Supervisor
            currentSupervisor = service.getSupervisorByLoginCode(txt_id.getText().toString()); //

            // 跳转到AQI反馈数据列表界面 (NepsSelectAqiView)
            Intent intent = new Intent(NepsLoginActivity.this, NepsSelectAqiActivity.class);
            startActivity(intent);
            finish(); // 登录成功后关闭当前Activity，防止回退
        } else {
            Toast.makeText(NepsLoginActivity.this, "登录失败: 用户名或密码错误", Toast.LENGTH_SHORT).show(); //
        }
    }

    private void register() {
        Intent intent = new Intent(NepsLoginActivity.this, NepsRegisterActivity.class);
        startActivity(intent);
    }

    private void forgotPassword() {
        Intent intent = new Intent(NepsLoginActivity.this, NepsForgotPasswordPhoneActivity.class);
        startActivity(intent);
    }
}
