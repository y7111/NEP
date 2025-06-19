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

public class NepsForgotPasswordResetActivity extends AppCompatActivity {

    private EditText txt_newPassword;
    private EditText txt_confirmPassword;
    private Supervisor supervisor;
    private SupervisorService supervisorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neps_forgot_password_reset);

        txt_newPassword = findViewById(R.id.txt_newPassword);
        txt_confirmPassword = findViewById(R.id.txt_confirmPassword);
        Button btnResetPassword = findViewById(R.id.btn_reset_password);
        Button btnBack = findViewById(R.id.btn_back);

        supervisorService = new SupervisorServiceImpl();

        // 从 Intent 中获取 Supervisor 的 loginCode
        String supervisorLoginCode = getIntent().getStringExtra("supervisorLoginCode");
        if (supervisorLoginCode != null) {
            supervisor = supervisorService.getSupervisorByLoginCode(supervisorLoginCode); //
        }

        if (supervisor == null) {
            Toast.makeText(this, "无法加载用户信息，请重试", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void resetPassword() {
        String newPassword = txt_newPassword.getText().toString().trim();
        String confirmPassword = txt_confirmPassword.getText().toString().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.equals(confirmPassword)) { //
            supervisor.setPassword(newPassword); //
            // 注意：这里调用 register 实际上是更新现有用户，因为 loginCode 已经存在
            supervisorService.register(supervisor); //
            Toast.makeText(this, "密码重置成功: 可以使用新密码登录", Toast.LENGTH_LONG).show(); //
            Intent intent = new Intent(NepsForgotPasswordResetActivity.this, NepsLoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "密码不一致: 两次输入的新密码不一致，请重新输入", Toast.LENGTH_SHORT).show(); //
            txt_confirmPassword.setText("");
        }
    }

    private void back() {
        // 返回密保问题界面
        Intent intent = new Intent(NepsForgotPasswordResetActivity.this, NepsForgotPasswordAnswerActivity.class);
        intent.putExtra("supervisorLoginCode", supervisor.getLoginCode()); // 继续传递loginCode
        startActivity(intent);
        finish();
    }
}
