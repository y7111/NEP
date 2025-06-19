package com.nep.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.nep.service.AdminService; //
import com.nep.service.impl.AdminServiceImpl; //

public class NepmLoginActivity extends AppCompatActivity {

    private EditText txt_id;
    private EditText txt_password;
    private AdminService adminService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nepm_login);

        txt_id = findViewById(R.id.txt_id);
        txt_password = findViewById(R.id.txt_password);
        Button btnLogin = findViewById(R.id.btn_login);

        adminService = new AdminServiceImpl(); //

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        boolean isLogin = adminService.login(txt_id.getText().toString(), txt_password.getText().toString()); //
        if (isLogin) {
            Toast.makeText(NepmLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NepmLoginActivity.this, NepmMainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(NepmLoginActivity.this, "登录失败: 用户名密码错误，请重新输入用户名和密码", Toast.LENGTH_SHORT).show(); //
        }
    }
}
